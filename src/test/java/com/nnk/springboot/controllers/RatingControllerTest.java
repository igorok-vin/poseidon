package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.exception.BidListNotFoundException;
import com.nnk.springboot.exception.RatingNotFoundException;
import com.nnk.springboot.secutity.CustomUserDetailsService;
import com.nnk.springboot.service.RatingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(RatingController.class)
class RatingControllerTest {

    @MockBean
    private RatingService ratingService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "john", password = "123",roles = "ADMIN")
    public void home()throws Exception {
        mockMvc.perform(get("/rating/list").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("rating"))
                .andExpect(view().name("rating/list"));
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "USER")
    public void addRatingForm()throws Exception {
        mockMvc.perform(get("/rating/add").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("rating"))
                .andExpect(view().name("rating/add"));
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "ADMIN")
    public void validateFormWhenFieldsHaveNoErrors()throws Exception {
        mockMvc.perform(post("/rating/validate").with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("moodysRating","Moodys Rating")
                        .param("sandPRating","Sand P Rating")
                        .param("fitchRating","Fitch Rating")
                        .param("orderNumber", String.valueOf(2)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/rating/list"))
                .andExpect(model().attributeHasNoErrors());
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "USER")
    public void validateFormWhenFieldsHaveErrors() throws Exception {
        MvcResult result = mockMvc.perform(post("/rating/validate").with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("moodysRating","")
                        .param("sandPRating","")
                        .param("fitchRating","")
                        .param("orderNumber", String.valueOf("")))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"))
                .andExpect(model().attributeHasErrors())
                .andExpect(model().attributeHasFieldErrorCode("rating","moodysRating","NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("rating","sandPRating","NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("rating","fitchRating","NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("rating","orderNumber","NotNull"))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).contains("Moodys Rating is mandatory","Sand P is mandatory","Fitch Rating is mandatory","Order Number is mandatory");
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "USER")
    public void showUpdateForm() throws Exception {
        Rating rating = new Rating();
                rating.setId(3);
                rating.setMoodysRating("Moodys rating");
                rating.setSandPRating("Sand P rating");
                rating.setFitchRating("Fitch rating");
                rating.setOrderNumber(2);
        when(ratingService.getRatingById(anyInt())).thenReturn(rating);
        mockMvc.perform(get("/rating/update/3").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("rating"))
                .andExpect(view().name("rating/update"));
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "USER")
    public void showUpdateFormDoThrowRatingNotFoundException() throws Exception {
        when(ratingService.getRatingById(anyInt())).thenThrow(new RatingNotFoundException(" Rating not found"));

        mockMvc.perform(get("/rating/update/1").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/error"));
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "ADMIN")
    public void updateRating()throws Exception {
        Rating rating = new Rating();
        rating.setId(3);
        rating.setMoodysRating("Moodys rating");
        rating.setSandPRating("Sand P rating");
        rating.setFitchRating("Fitch rating");
        rating.setOrderNumber(2);
        when(ratingService.updateRating(any(Rating.class))).thenReturn(rating);
    mockMvc.perform(post("/rating/update/3").with(SecurityMockMvcRequestPostProcessors.csrf())
                .param("moodysRating","Moodys")
                .param("sandPRating","Sand P")
                .param("fitchRating","Fitch")
                .param("orderNumber", String.valueOf("5")))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/rating/list"))
            .andExpect(view().name("redirect:/rating/list"));
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "ADMIN")
    public void updateRatingWhenFieldsHaveErrors()throws Exception {

        MvcResult result = mockMvc.perform(post("/rating/update/3").with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("moodysRating","")
                        .param("sandPRating","")
                        .param("fitchRating","")
                        .param("orderNumber", String.valueOf("")))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"))
                .andExpect(model().attributeHasErrors())
                .andExpect(model().attributeHasFieldErrorCode("rating","moodysRating","NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("rating","sandPRating","NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("rating","fitchRating","NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("rating","orderNumber","NotNull"))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).contains("Moodys Rating is mandatory","Sand P is mandatory","Fitch Rating is mandatory","Order Number is mandatory");
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "ADMIN")
    public void deleteRating()throws Exception {
        mockMvc.perform(get("/rating/delete/1").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/rating/list"))
                .andExpect(redirectedUrl("/rating/list"));
    }
}