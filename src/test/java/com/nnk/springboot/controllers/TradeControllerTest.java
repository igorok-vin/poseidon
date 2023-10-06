package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.exception.CurvePointNotFoundException;
import com.nnk.springboot.exception.TradeNotFoundException;
import com.nnk.springboot.secutity.CustomUserDetailsService;
import com.nnk.springboot.service.TradeService;
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
@WebMvcTest(TradeController.class)
class TradeControllerTest {
    @MockBean
    private TradeService tradeService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "john", password = "123",roles = "ADMIN")
    public void home() throws Exception {
        mockMvc.perform(get("/trade/list").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("trade"))
                .andExpect(view().name("trade/list"));
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "ADMIN")
    public void addUser() throws Exception {
        mockMvc.perform(get("/trade/add").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"));
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "ADMIN")
    public void validateFormWhenFieldsHaveNoErrors() throws Exception {
        mockMvc.perform(post("/trade/validate").with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("account","Account")
                        .param("type","type")
                        .param("buyQuantity",String.valueOf(2.5)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/trade/list"))
                .andExpect(model().attributeHasNoErrors());
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "USER")
    public void validateFormWhenFieldsHaveErrors() throws Exception {
        MvcResult result = mockMvc.perform(post("/trade/validate")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("account","")
                        .param("type","")
                        .param("buyQuantity",String.valueOf("")))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"))
                .andExpect(model().attributeHasErrors())
                .andExpect(model().attributeHasFieldErrorCode("trade","account","NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("trade","type","NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("trade","buyQuantity","NotNull"))
                .andReturn();
        assertThat(result.getResponse().getContentAsString())
                .contains("Account is mandatory","Type is mandatory","BuyQuantity is mandatory");
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "ADMIN")
    public void showUpdateForm() throws Exception {
        Trade trade = new Trade();
            trade.setTradeId(1);
            trade.setAccount("Account");
            trade.setType("Type");
            trade.setBuyQuantity(2.5);
        when(tradeService.getTradeNameById(anyInt())).thenReturn(trade);
        mockMvc.perform(get("/trade/update/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("trade"))
                .andExpect(view().name("trade/update"));
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "USER")
    public void showUpdateFormDoThrowTradeNotFoundException()throws Exception {
        when(tradeService.getTradeNameById(anyInt()))
                .thenThrow(new TradeNotFoundException("Trade not found"));
        mockMvc.perform(get("/trade/update/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/error"));
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "ADMIN")
    public void updateTrade() throws Exception {
        Trade trade = new Trade();
            trade.setTradeId(1);
            trade.setAccount("Account");
            trade.setType("Type");
            trade.setBuyQuantity(2.5);
        mockMvc.perform(post("/trade/update/1").with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("account","Account")
                        .param("type","type")
                        .param("buyQuantity",String.valueOf(2.5)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/trade/list"))
                .andExpect(model().attributeHasNoErrors());
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "USER")
    public void updateTradeWhenFieldsHaveErrors() throws Exception{
        MvcResult result = mockMvc.perform(post("/trade/update/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("account","")
                        .param("type","")
                        .param("buyQuantity",String.valueOf("")))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attributeHasErrors())
                .andExpect(model().attributeHasFieldErrorCode("trade","account","NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("trade","type","NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("trade","buyQuantity","NotNull"))
                .andReturn();
        assertThat(result.getResponse().getContentAsString())
                .contains("Account is mandatory","Type is mandatory","BuyQuantity is mandatory");
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "ADMIN")
    public void deleteTrade() throws Exception {
        mockMvc.perform(get("/trade/delete/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/trade/list"))
                .andExpect(redirectedUrl("/trade/list"));
    }
}