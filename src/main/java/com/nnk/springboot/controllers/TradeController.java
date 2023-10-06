package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.exception.TradeNotFoundException;
import com.nnk.springboot.service.TradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class TradeController {

    private final Logger logger = LoggerFactory.getLogger(TradeController.class);

    @Autowired
    private TradeService tradeService;

    /**
     * GET request. Displaying all trade that exist in database
     *
     * @param model interface that defines a holder for model attributes.
     * @return String "trade/list"
     */
    @RequestMapping("/trade/list")
    public String home(Model model) {
        model.addAttribute("trade",tradeService.getAllTrade());
        logger.info("Controller: GET /trade/list. Displaying list of trade");
        return "trade/list";
    }

    /**
     * GET request. Displaying form for adding new trade to database
     * @return String "trade/add"
     */
    @GetMapping("/trade/add")
    public String addUser(Trade bid) {
        logger.info("Controller: GET /trade/add. Displaying form for adding new trade");
        return "trade/add";
    }

    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result, Model model) {
        if(result.hasErrors()){
            logger.error("Controller: Error. Trade fields have errors");
            return "trade/add";
        }
        tradeService.saveTrade(trade);
        logger.info("Controller: redirect:/trade/list. Trade has been added to the list");
        return "redirect:/trade/list";
    }

    /**
     * GET request. Displaying form for update existing trade in database
     * @param id Trade's ID
     * @param model Model object
     * @return String "trade/update"
     */
    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        try {
            Trade trade = tradeService.getTradeNameById(id);
            model.addAttribute("trade", trade);
        } catch (TradeNotFoundException e) {
            logger.error("Controller: Trade NOT found with id: " + id);
            return "redirect:/error";
        }
        logger.info("Controller: GET /trade/update/{id}. Displaying form for update existing trade");
        return "trade/update";
    }

    /**
     *  POST request. Updating existing trade
     * @param id Trade's ID
     * @param trade Trade object
     * @param result BindingResult for validation
     * @return String "redirect:/trade/list"
     */
    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
                             BindingResult result, Model model) {
        if(result.hasErrors()){
            logger.info("Controller: Error. Trade fields have errors");
            return "trade/update";
        }
        trade.setTradeId(id);
        tradeService.updateTrade(trade);
        logger.info("Controller: Trade has been updated");
        return "redirect:/trade/list";
    }

    /**
     * GET request. Delete existing trade by ID from database
     * @param id Trade's ID
     * @return String "redirect:/trade/list"
     */
    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        tradeService.deleteTrade(id);
        logger.info("Controller: GET /trade/delete/{id}. Trade has been removed");
        return "redirect:/trade/list";
    }
}