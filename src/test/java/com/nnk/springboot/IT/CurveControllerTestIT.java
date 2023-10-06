package com.nnk.springboot.IT;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.service.CurvePointService;
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
class CurveControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurvePointService curvePointService;

    @Autowired
    private CurvePointRepository curvePointRepository;

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
        CurvePoint curvePoint = new CurvePoint();
            curvePoint.setCurveId(1);
            curvePoint.setTerm(1.5);
            curvePoint.setValue(1.5);
        mockMvc.perform(post("/curvePoint/validate")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("curveId",curvePoint.getCurveId().toString())
                        .param("term",curvePoint.getTerm().toString())
                        .param("value", curvePoint.getValue().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/curvePoint/list"))
                .andExpect(model().attributeHasNoErrors());
    }

    @Test
    @WithMockUser(username = "john", password = "Poseidon2000!",roles = "ADMIN")
    public void updateCurvePointIT() throws Exception {
        CurvePoint curvePointBeforeUpdate = new CurvePoint();
            curvePointBeforeUpdate.setCurveId(7);
            curvePointBeforeUpdate.setTerm(7.5);
            curvePointBeforeUpdate.setValue(7.5);
        CurvePoint curvePointAfterUpdate = new CurvePoint();
            curvePointAfterUpdate.setCurveId(8);
            curvePointAfterUpdate.setTerm(8.5);
            curvePointAfterUpdate.setValue(8.5);

        CurvePoint savedCurvePoint = curvePointService.saveCurvePoint(curvePointBeforeUpdate);

        String postUrl = "/curvePoint/update/".concat(String.valueOf(savedCurvePoint.getId()));

        mockMvc.perform(post(postUrl).with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("curveId",curvePointAfterUpdate.getCurveId().toString())
                        .param("term",curvePointAfterUpdate.getTerm().toString())
                        .param("value", curvePointAfterUpdate.getValue().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"))
                .andExpect(view().name("redirect:/curvePoint/list"));

        curvePointRepository.findById(savedCurvePoint.getId()).ifPresent(
                curvePoint -> {assertTrue(
                   curvePoint.getCurveId().equals(curvePointAfterUpdate.getCurveId())
                        &&curvePoint.getTerm().equals(curvePointAfterUpdate.getTerm())
                        &&curvePoint.getValue().equals(curvePointAfterUpdate.getValue()));}
        );
    }
}