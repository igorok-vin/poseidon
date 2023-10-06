package com.nnk.springboot.IT;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.service.RuleNameService;
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
class RuleNameControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RuleNameService ruleNameService;

    @Autowired
    private RuleNameRepository ruleNameRepository;

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
        RuleName ruleName = new RuleName();
        ruleName.setName("Name");
        ruleName.setDescription("Description");
        mockMvc.perform(post("/ruleName/validate").with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("name", ruleName.getName())
                        .param("description", ruleName.getDescription()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/ruleName/list"))
                .andExpect(model().attributeHasNoErrors());
    }

    @Test
    @WithMockUser(username = "john", password = "Poseidon2000!",roles = "ADMIN")
    public void updateRuleNameIT() throws Exception {
        RuleName ruleNameBeforeUpdate = new RuleName();
            ruleNameBeforeUpdate.setName("Name1");
            ruleNameBeforeUpdate.setDescription("Description1");
        RuleName ruleNameAfterUpdate = new RuleName();
            ruleNameAfterUpdate.setName("Name2");
            ruleNameAfterUpdate.setDescription("Description2");

        RuleName  savedRuleName = ruleNameService.saveRuleName(ruleNameBeforeUpdate);

        String postUrl = "/ruleName/update/".concat(String.valueOf(savedRuleName.getId()));
        mockMvc.perform(post(postUrl).with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("name", ruleNameAfterUpdate.getName())
                        .param("description", ruleNameAfterUpdate.getDescription()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/ruleName/list"))
                .andExpect(model().attributeHasNoErrors());

        ruleNameRepository.findById(savedRuleName.getId()).ifPresent(
                ruleName -> {assertTrue(
                        ruleName.getName().equals(ruleNameAfterUpdate.getName())
                                &&ruleName.getDescription().equals(ruleNameAfterUpdate.getDescription()))
                ;}
        );
    }
}