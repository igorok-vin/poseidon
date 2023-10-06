package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.exception.CurvePointNotFoundException;
import com.nnk.springboot.secutity.CustomUserDetailsService;
import com.nnk.springboot.service.CurvePointService;
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
@WebMvcTest(CurveController.class)
class CurveControllerTest {

    @MockBean
    private CurvePointService curvePointService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "john", password = "123",roles = "ADMIN")
    public void home() throws Exception {
        mockMvc.perform(get("/curvePoint/list").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("curvePointList"))
                .andExpect(view().name("curvePoint/list"));
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "USER")
    public void addBidForm() throws Exception {
        mockMvc.perform(get("/curvePoint/add").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"));
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "USER")
    public void validateFormWhenFieldsHaveNoErrors()throws Exception {
        mockMvc.perform(post("/curvePoint/validate").with(SecurityMockMvcRequestPostProcessors.csrf())
                            .param("curveId",String.valueOf(1))
                            .param("term",String.valueOf(2.5))
                            .param("value",String.valueOf(4.5)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/curvePoint/list"))
                .andExpect(model().attributeHasNoErrors());
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "USER")
    public void validateFormWhenFieldsHaveErrors() throws Exception {
        MvcResult result = mockMvc.perform(post("/curvePoint/validate")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("curveId",String.valueOf(""))
                        .param("term",String.valueOf(""))
                        .param("value",String.valueOf("")))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"))
                .andExpect(model().attributeHasErrors())
                .andExpect(model().attributeHasFieldErrorCode("curvePoint","curveId","NotNull"))
                .andExpect(model().attributeHasFieldErrorCode("curvePoint","term","NotNull"))
                .andExpect(model().attributeHasFieldErrorCode("curvePoint","value","NotNull"))
                .andReturn();
        assertThat(result.getResponse().getContentAsString())
                .contains("Curve point ID is mandatory","Term is mandatory","Value is mandatory");
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "USER")
    public void showUpdateForm()throws Exception {
        CurvePoint curvePoint = new CurvePoint();
            curvePoint.setId(1);
            curvePoint.setCurveId(2);
            curvePoint.setTerm(3.5);
            curvePoint.setValue(4.5);
        when(curvePointService.getCurvePointById(anyInt())).thenReturn(curvePoint);
        mockMvc.perform(get("/curvePoint/update/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("curvePoint"))
                .andExpect(view().name("curvePoint/update"));
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "USER")
    public void showUpdateFormDoThrowBidListNotFoundException()throws Exception {
        when(curvePointService.getCurvePointById(anyInt()))
                .thenThrow(new CurvePointNotFoundException("Curve point not found"));
        mockMvc.perform(get("/curvePoint/update/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/error"));
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "USER")
    public void updateBid() throws Exception{
        CurvePoint curvePoint = new CurvePoint();
            curvePoint.setId(1);
            curvePoint.setCurveId(2);
            curvePoint.setTerm(3.5);
            curvePoint.setValue(4.5);
        when(curvePointService.updateCurvePoint(any(CurvePoint.class))).thenReturn(curvePoint);
        mockMvc.perform(post("/curvePoint/update/1")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("curveId", String.valueOf("5"))
                        .param("term", String.valueOf("6.0"))
                        .param("value", String.valueOf("7.0")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"))
                .andExpect(view().name("redirect:/curvePoint/list"));
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "USER")
    public void updateBidListWhenFieldsHaveErrors() throws Exception{
        MvcResult result = mockMvc.perform(post("/curvePoint/update/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("curveId",String.valueOf(""))
                        .param("term",String.valueOf(""))
                        .param("value",String.valueOf("")))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(model().attributeHasErrors())
                .andExpect(model().attributeHasFieldErrorCode("curvePoint","curveId","NotNull"))
                .andExpect(model().attributeHasFieldErrorCode("curvePoint","term","NotNull"))
                .andExpect(model().attributeHasFieldErrorCode("curvePoint","value","NotNull"))
                .andReturn();
        assertThat(result.getResponse().getContentAsString())
                .contains("Curve point ID is mandatory","Term is mandatory","Value is mandatory");
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "USER")
    public void deleteBidList()throws Exception {
        mockMvc.perform(get("/curvePoint/delete/1")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/curvePoint/list"))
                .andExpect(redirectedUrl("/curvePoint/list"));
    }
}