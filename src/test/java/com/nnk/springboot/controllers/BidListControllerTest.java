package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exception.BidListNotFoundException;
import com.nnk.springboot.secutity.CustomUserDetailsService;
import com.nnk.springboot.service.BidService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(BidListController.class)
class BidListControllerTest {

    @MockBean
    private BidService bidService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "john", password = "123",roles = "ADMIN")
    public void home() throws Exception {
        mockMvc.perform(get("/bidList/list").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bidList"))
                .andExpect(view().name("bidList/list"));
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "USER")
    public void addBidForm() throws Exception {
        mockMvc.perform(get("/bidList/add").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bidList"))
                .andExpect(view().name("bidList/add"));;
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "USER")
    public void validateFormWhenFieldsHaveNoErrors() throws Exception {
        mockMvc.perform(post("/bidList/validate").with(SecurityMockMvcRequestPostProcessors.csrf())
                    .param("account","Account")
                    .param("type","Type")
                    .param("bidQuantity", String.valueOf(2.0)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bidList/list"))
                .andExpect(model().attributeHasNoErrors());
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "USER")
    public void validateFormWhenFieldsHaveErrors() throws Exception {
        MvcResult result = mockMvc.perform(post("/bidList/validate").with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("account","")
                        .param("type","")
                        .param("bidQuantity", String.valueOf("")))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"))
                .andExpect(model().attributeHasErrors())
                .andExpect(model().attributeHasFieldErrorCode("bidList","account","NotBlank")).andReturn();
        assertThat(result.getResponse().getContentAsString()).contains("Account is mandatory","Type is mandatory","Bid quantity is mandatory");
    }


    @Test
    @WithMockUser(username = "john", password = "123",roles = "USER")
    public void showUpdateForm() throws Exception {
        BidList bidList = new BidList();
            bidList.setBidListId(1);
            bidList.setAccount("account");
            bidList.setType("type");
            bidList.setBidQuantity(2.0);
        when(bidService.getBidListById(anyInt())).thenReturn(bidList);
        mockMvc.perform(get("/bidList/update/1").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bidList"))
                .andExpect(view().name("bidList/update"));
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "USER")
    public void showUpdateFormDoThrowBidListNotFounfException() throws Exception {
        when(bidService.getBidListById(anyInt())).thenThrow(new BidListNotFoundException("BidList not found"));

        mockMvc.perform(get("/bidList/update/1").with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("account","Account")
                        .param("type","Type")
                        .param("bidQuantity", String.valueOf(2.0)))
                .andExpect(status().is3xxRedirection())

                .andExpect(view().name("redirect:/error"));
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "USER")
    public void updateBid() throws Exception {
        BidList bidList = new BidList();
            bidList.setBidListId(1);
            bidList.setAccount("account");
            bidList.setType("type");
            bidList.setBidQuantity(2.0);
        when(bidService.updateBidList(any(BidList.class))).thenReturn(bidList);

        mockMvc.perform(post("/bidList/update/1").with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("account","Account")
                        .param("type","Type")
                        .param("bidQuantity", String.valueOf(2.0)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"))
                .andExpect(view().name("redirect:/bidList/list"));
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "USER")
    public void updateBidWhenFieldsHaveErrors() throws Exception {
        MvcResult result = mockMvc.perform(post("/bidList/update/1").with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("account","")
                        .param("type","")
                        .param("bidQuantity", String.valueOf("")))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"))
                .andExpect(model().attributeHasErrors())
                .andExpect(model().attributeHasFieldErrorCode("bidList","account","NotBlank")).andReturn();
        assertThat(result.getResponse().getContentAsString()).contains("Account is mandatory","Type is mandatory","Bid quantity is mandatory");
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "USER")
    public void deleteById() throws Exception {
        mockMvc.perform(get("/bidList/delete/1").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bidList/list"))
                .andExpect(redirectedUrl("/bidList/list"));
    }
}