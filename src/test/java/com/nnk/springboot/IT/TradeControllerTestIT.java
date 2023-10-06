package com.nnk.springboot.IT;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.service.TradeService;
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
class TradeControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private TradeRepository tradeRepository;

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
    public void validateWhenFieldsHaveNoErrorsIT() throws Exception {
        Trade trade = new Trade();
        trade.setAccount("Account");
        trade.setType("Type");
        trade.setBuyQuantity(2.5);
        mockMvc.perform(post("/trade/validate").with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("account",trade.getAccount())
                        .param("type",trade.getType())
                        .param("buyQuantity",trade.getBuyQuantity().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/trade/list"))
                .andExpect(model().attributeHasNoErrors());
    }

    @Test
    @WithMockUser(username = "john", password = "Poseidon2000!",roles = "ADMIN")
    public void updateTradeIT() throws Exception {
        Trade tradeBeforeUpdate = new Trade();
            tradeBeforeUpdate.setAccount("Account5");
            tradeBeforeUpdate.setType("Type5");
            tradeBeforeUpdate.setBuyQuantity(5.5);
        Trade tradeAfterUpdate = new Trade();
            tradeAfterUpdate.setAccount("Account7");
            tradeAfterUpdate.setType("Type7");
            tradeAfterUpdate.setBuyQuantity(7.7);

        Trade savedTrade = tradeService.saveTrade(tradeBeforeUpdate);

        String postUrl = "/trade/update/".concat(String.valueOf(savedTrade.getTradeId()));

        mockMvc.perform(post(postUrl).with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("account",tradeAfterUpdate.getAccount())
                        .param("type",tradeAfterUpdate.getType())
                        .param("buyQuantity",tradeAfterUpdate.getBuyQuantity().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/trade/list"))
                .andExpect(model().attributeHasNoErrors());

        tradeRepository.findById(savedTrade.getTradeId()).ifPresent(
                trade -> {assertTrue(
                        trade.getAccount().equals(tradeAfterUpdate.getAccount())
                                &&trade.getType().equals(tradeAfterUpdate.getType())
                                &&trade.getBuyQuantity().equals(tradeAfterUpdate.getBuyQuantity()));}
        );
    }
}