package com.nnk.springboot.IT;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.service.RatingService;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"/poseidon_test.sql"})
@DisplayName("Integration Tests")
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RatingControllerTestIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    RatingService ratingService;

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "john", password = "Poseidon2000!",roles = "ADMIN")
    public void validateFormWhenFieldsHaveNoErrorsIT() throws Exception {
        Rating rating = new Rating();
            rating.setId(3);
            rating.setMoodysRating("Moodys rating");
            rating.setSandPRating("Sand P rating");
            rating.setFitchRating("Fitch rating");
            rating.setOrderNumber(2);
        mockMvc.perform(post("/rating/validate").with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("moodysRating",rating.getMoodysRating())
                        .param("sandPRating",rating.getSandPRating())
                        .param("fitchRating",rating.getFitchRating())
                        .param("orderNumber",rating.getOrderNumber().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/rating/list"))
                .andExpect(model().attributeHasNoErrors());
    }

    @Test
    @WithMockUser(username = "john", password = "Poseidon2000!",roles = "ADMIN")
    public void updateRatingIT() throws Exception {
        Rating ratingBeforeUpdate = new Rating();
            ratingBeforeUpdate.setId(3);
            ratingBeforeUpdate.setMoodysRating("Moodys rating");
            ratingBeforeUpdate.setSandPRating("Sand P rating");
            ratingBeforeUpdate.setFitchRating("Fitch rating");
            ratingBeforeUpdate.setOrderNumber(2);
        Rating ratingAfterUpdate = new Rating();
            ratingAfterUpdate.setId(3);
            ratingAfterUpdate.setMoodysRating("Moodys rating");
            ratingAfterUpdate.setSandPRating("Sand P rating");
            ratingAfterUpdate.setFitchRating("Fitch rating");
            ratingAfterUpdate.setOrderNumber(2);

        Rating savedRating = ratingService.saveRating(ratingBeforeUpdate);

        String postUrl = "/rating/update/".concat(String.valueOf(savedRating.getId()));

        mockMvc.perform(post(postUrl).with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("moodysRating",ratingAfterUpdate.getMoodysRating())
                        .param("sandPRating",ratingAfterUpdate.getSandPRating())
                        .param("fitchRating",ratingAfterUpdate.getFitchRating())
                        .param("orderNumber",ratingAfterUpdate.getOrderNumber().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"))
                .andExpect(view().name("redirect:/rating/list"))
                .andExpect(model().attributeHasNoErrors());

        ratingRepository.findById(savedRating.getId()).ifPresent(
                rating -> {assertTrue(
                   rating.getMoodysRating().equals(ratingAfterUpdate.getMoodysRating())
                          && rating.getSandPRating().equals(ratingAfterUpdate.getSandPRating())
                          && rating.getFitchRating().equals(ratingAfterUpdate.getFitchRating())
                           && rating.getOrderNumber().equals(ratingAfterUpdate.getOrderNumber()));}
        );
    }
}