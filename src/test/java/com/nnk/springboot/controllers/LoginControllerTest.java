package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.secutity.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "john", password = "123",roles = "ADMIN")
    public void getAllUserArticles()throws Exception {
        List<User> userList = new ArrayList<>();
        User user1 = new User();
            user1.setId(1);
            user1.setFullname("fullName1");
            user1.setUsername("userName1");
            user1.setPassword("password1");
            user1.setRole("ADMIN");
        User user2 = new User();
            user2.setId(2);
            user2.setFullname("fullName2");
            user2.setUsername("userName2");
            user2.setPassword("password2");
            user2.setRole("USER");
        userList.add(user1);
        userList.add(user2);
        when(userRepository.findAll()).thenReturn(userList);
        mockMvc.perform(get("/app/secure/article-details")
                     .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andExpect(model().attribute("users",hasItem(hasProperty("id",is(1)))))
        .andExpect(model().attribute("users",hasItem(hasProperty("username",is("userName1")))))
         .andExpect(model().attribute("users",hasItem(hasProperty("fullname",is("fullName1")))))
         .andExpect(model().attribute("users",hasItem(hasProperty("role",is("ADMIN"))))).andExpect(model().attribute("users",hasItem(hasProperty("username",is("userName2")))));

    }

    @Test
    public  void error() throws Exception{
        mockMvc.perform(get("/app/error")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("403"))
                .andExpect(model().attribute("errorMsg", "You are not authorized for the requested data."));
    }
}