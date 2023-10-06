package com.nnk.springboot.controllers;

import com.nnk.springboot.secutity.CustomUserDetailsService;
import com.nnk.springboot.secutity.SecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@AutoConfigureMockMvc()
@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(username = "john", password = "123",roles = "USER")
    @Test
    public void home() throws Exception{
        mockMvc.perform(get("/")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(view().name("home"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "USER")
    public void adminHome()throws Exception {
        mockMvc.perform(get("/admin/home")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(view().name("redirect:/bidList/list"))
                .andExpect(status().is3xxRedirection());
    }
}