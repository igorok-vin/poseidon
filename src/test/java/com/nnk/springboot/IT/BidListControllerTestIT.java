package com.nnk.springboot.IT;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.service.BidService;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"/poseidon_test.sql"})
@DisplayName("Integration Tests")
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BidListControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BidService bidService;

    @Autowired
    private BidListRepository bidListRepository;

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
    @WithMockUser(username = "john", password = "123",roles = "USER")
    public void validateFormWhenFieldsHaveNoErrorsIT() throws Exception {
        BidList bidList = new BidList();
            bidList.setAccount("account25");
            bidList.setType("type25");
            bidList.setBidQuantity(25.0);

        mockMvc.perform(post("/bidList/validate").with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("account",bidList.getAccount())
                        .param("type",bidList.getType())
                        .param("bidQuantity", bidList.getBidQuantity().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bidList/list"))
                .andExpect(model().attributeHasNoErrors());
    }

    @Test
    @WithMockUser(username = "john", password = "123",roles = "USER")
    public void updateBidIT() throws Exception {
        BidList bidListBeforeUpdate = new BidList();
            bidListBeforeUpdate.setAccount("account7");
            bidListBeforeUpdate.setType("type7");
            bidListBeforeUpdate.setBidQuantity(7.0);
        BidList bidListAfterUpdate = new BidList();
            bidListAfterUpdate.setAccount("account8");
            bidListAfterUpdate.setType("type8");
            bidListAfterUpdate.setBidQuantity(8.0);
        BidList savedbidList = bidService.saveBidList(bidListBeforeUpdate);

        String postUrl = "/bidList/update/".concat(String.valueOf(savedbidList.getBidListId()));

        mockMvc.perform(post(postUrl).with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("account",bidListAfterUpdate.getAccount())
                        .param("type",bidListAfterUpdate.getType())
                        .param("bidQuantity", bidListAfterUpdate.getBidQuantity().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"))
                .andExpect(view().name("redirect:/bidList/list"));

        bidListRepository.findById(savedbidList.getBidListId()).ifPresent(
                bidList -> {assertTrue(bidList.getType().equals(bidListAfterUpdate.getType())&&bidList.getAccount().equals(bidListAfterUpdate.getAccount())&&bidList.getBidQuantity().equals(bidListAfterUpdate.getBidQuantity()));}
        );
    }
}