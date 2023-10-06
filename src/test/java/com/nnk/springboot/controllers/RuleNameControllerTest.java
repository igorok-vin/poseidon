package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.exception.CurvePointNotFoundException;
import com.nnk.springboot.exception.RuleNameNotFoundException;
import com.nnk.springboot.secutity.CustomUserDetailsService;
import com.nnk.springboot.service.RuleNameService;
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
@WebMvcTest(RuleNameController.class)
class RuleNameControllerTest {

    @MockBean
    private RuleNameService ruleNameService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "john", password = "123",roles = "ADMIN")
    void home() throws Exception {
        mockMvc.perform(get("/ruleName/list").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ruleName"))
                .andExpect(view().name("ruleName/list"));
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "ADMIN")
    void addRuleForm() throws Exception {
        mockMvc.perform(get("/ruleName/add").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"));
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "ADMIN")
    public void validateFormWhenFieldsHaveNoErrors() throws Exception {
        mockMvc.perform(post("/ruleName/validate").with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("name","Name")
                        .param("description","Description")
                        .param("json","Json")
                        .param("template","Template")
                        .param("sqlStr","Sql Str")
                        .param("sqlPart","Sql Part"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/ruleName/list"))
                .andExpect(model().attributeHasNoErrors());
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "USER")
    public void validateFormWhenFieldsHaveErrors() throws Exception {
        MvcResult result = mockMvc.perform(post("/ruleName/validate")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("name","")
                        .param("description",""))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"))
                .andExpect(model().attributeHasErrors())
                .andExpect(model().attributeHasFieldErrorCode("ruleName","name","NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("ruleName","description","NotBlank"))
                .andReturn();
        assertThat(result.getResponse().getContentAsString())
                .contains("Name is mandatory","Description is mandatory");
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "ADMIN")
    public void showUpdateForm() throws Exception {
        RuleName ruleName = new RuleName();
            ruleName.setId(1);
            ruleName.setName("Name");
            ruleName.setDescription("Description");
        when(ruleNameService.getRuleNameById(anyInt())).thenReturn(ruleName);
        mockMvc.perform(get("/ruleName/update/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ruleName"))
                .andExpect(view().name("ruleName/update"));
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "USER")
    public void showUpdateFormDoThrowRuleNameNotFoundException()throws Exception {
        when(ruleNameService.getRuleNameById(anyInt()))
                .thenThrow(new RuleNameNotFoundException("Rule name not found"));
        mockMvc.perform(get("/ruleName/update/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/error"));
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "ADMIN")
    public void updateRuleName() throws Exception {
        RuleName ruleName = new RuleName();
            ruleName.setId(1);
            ruleName.setName("Name");
            ruleName.setDescription("Description");
        when(ruleNameService.updateRuleName(any(RuleName.class))).thenReturn(ruleName);
        mockMvc.perform(post("/ruleName/update/1").with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("name","Name")
                        .param("description","Description")
                        .param("json","Json")
                        .param("template","Template")
                        .param("sqlStr","Sql Str")
                        .param("sqlPart","Sql Part"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"))
                .andExpect(view().name("redirect:/ruleName/list"))
                .andExpect(model().attributeHasNoErrors());
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "USER")
    public void updateBidWhenFieldsHaveErrors() throws Exception{
        MvcResult result = mockMvc.perform(post("/ruleName/update/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                .param("name","")
                .param("description",""))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"))
                .andExpect(model().attributeHasErrors())
                .andExpect(model().attributeHasFieldErrorCode("ruleName","name","NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("ruleName","description","NotBlank"))
                .andReturn();
        assertThat(result.getResponse().getContentAsString())
                .contains("Name is mandatory","Description is mandatory");
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "ADMIN")
    public void deleteRuleName() throws Exception {
        mockMvc.perform(get("/ruleName/delete/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/ruleName/list"))
                .andExpect(redirectedUrl("/ruleName/list"));
    }
}