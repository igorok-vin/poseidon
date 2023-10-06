package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.exception.UserNameExistInDataBaseException;
import com.nnk.springboot.exception.UserNotFoundException;
import com.nnk.springboot.secutity.CustomUserDetailsService;
import com.nnk.springboot.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "john", password = "123",roles = "ADMIN")
    public void home() throws Exception {
        mockMvc.perform(get("/user/list")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attributeExists("users"));
    }

    @WithMockUser(username = "john", password = "123",roles = "ADMIN")
    @Test
    public void addUser()throws Exception {
        mockMvc.perform(get("/user/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().attributeDoesNotExist())
        ;
    }

    @WithMockUser(username = "john", password = "123",roles = "ADMIN")
    @Test
    public void validate()throws Exception {
        User user = new User();
        user.setId(1);
        user.setUsername("Username");
        user.setPassword("User2000!");
        user.setFullname("Fullname");
        user.setRole("USER");
        when(passwordEncoder.encode(any())).thenReturn(user.getPassword());
        mockMvc.perform(post("/user/validate")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("username", "Username")
                        .param("password", "User2000!")
                        .param("fullname", "Fullname")
                        .param("role", "USER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/list"))
                .andExpect(redirectedUrl("/user/list"))
                .andExpect(model().attributeHasNoErrors())
                .andExpect(model().attributeDoesNotExist());
    }

    @WithMockUser(username = "john", password = "123",roles = "ADMIN")
    @Test
    public void validateWhenFieldsHaveErrors()throws Exception {
       MvcResult result= mockMvc.perform(post("/user/validate")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("username", "")
                        .param("password", "")
                        .param("fullname", "")
                        .param("role", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().attributeHasErrors())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeHasFieldErrorCode("user","username","NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("user","password","PasswordValidation"))
                .andExpect(model().attributeHasFieldErrorCode("user","fullname","NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("user","role","NotBlank"))
               .andReturn();
       assertThat(result.getResponse().getContentAsString()).contains("Username is mandatory","FullName is mandatory","Role is mandatory");
    }

    @WithMockUser(username = "john", password = "123",roles = "ADMIN")
    @Test
    public void validateDoThrowUserNameExistInDataBaseException()throws Exception {
        User user = new User();
            user.setId(1);
            user.setUsername("Username");
            user.setPassword("User2000!");
            user.setFullname("Fullname");
            user.setRole("USER");
        when(passwordEncoder.encode(any())).thenReturn(user.getPassword());
        when(userService.saveUser(any(User.class))).thenThrow(new UserNameExistInDataBaseException("This user name already exist. User name must be unique"));
        mockMvc.perform(post("/user/validate")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("username", "Username")
                        .param("password", "User2000!")
                        .param("fullname", "Fullname")
                        .param("role", "USER"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void showUpdateForm() throws Exception{
        User user = new User();
            user.setId(1);
            user.setUsername("Username");
            user.setPassword("User2000!");
            user.setFullname("Fullname");
            user.setRole("USER");
        when(userService.getUserById(anyInt())).thenReturn(user);
        mockMvc.perform(get("/user/update/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().attributeDoesNotExist());
    }

    @Test
    public void showUpdateFormDoThrowUserNotFoundException() throws Exception{
        when(userService.getUserById(anyInt())).thenThrow(new UserNotFoundException("User not found"));
        mockMvc.perform(get("/user/update/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/app/404"));
    }

    @Test
    public void updateUser()throws Exception{
        User user = new User();
        user.setId(1);
        user.setUsername("Username");
        user.setPassword("User2000!");
        user.setFullname("Fullname");
        user.setRole("USER");
        when(passwordEncoder.encode(any())).thenReturn(user.getPassword());
        mockMvc.perform(post("/user/update/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("username", "Username")
                        .param("password", "User2000!")
                        .param("fullname", "Fullname")
                        .param("role", "USER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/list"))
                .andExpect(redirectedUrl("/user/list"))
                .andExpect(model().attributeHasNoErrors())
                .andExpect(model().attributeDoesNotExist());
    }

    @WithMockUser(username = "john", password = "123",roles = "ADMIN")
    @Test
    public void updateUserWhenFieldsHaveErrors()throws Exception {
        MvcResult result= mockMvc.perform(post("/user/update/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("username", "")
                        .param("password", "")
                        .param("fullname", "")
                        .param("role", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().attributeHasErrors())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeHasFieldErrorCode("user","username","NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("user","password","PasswordValidation"))

                .andExpect(model().attributeHasFieldErrorCode("user","fullname","NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("user","role","NotBlank"))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).contains("Username is mandatory","FullName is mandatory");
    }

    @WithMockUser(username = "john", password = "123",roles = "ADMIN")
    @Test
    public void updateUserDoThrowUserNameExistInDataBaseException()throws Exception {
        User user = new User();
        user.setId(1);
        user.setUsername("Username");
        user.setPassword("User2000!");
        user.setFullname("Fullname");
        user.setRole("USER");
        when(passwordEncoder.encode(any())).thenReturn(user.getPassword());
        when(userService.updateUser(any(User.class))).thenThrow(new UserNameExistInDataBaseException("This user name already exist. User name must be unique"));
        mockMvc.perform(post("/user/update/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("username", "Username")
                        .param("password", "User2000!")
                        .param("fullname", "Fullname")
                        .param("role", "USER"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void deleteUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/delete/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"))
                .andExpect(view().name("redirect:/user/list"))
                .andExpect(model().attributeDoesNotExist());
    }
}