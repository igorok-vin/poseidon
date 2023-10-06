package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exception.BidListNotFoundException;
import com.nnk.springboot.service.BidService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
public class BidListController {

    private final Logger logger = LoggerFactory.getLogger(BidListController.class);
    @Autowired
    BidService bidService;

    /**
     * GET request. Displaying all bidList that exist in database
     *
     * @param model interface that defines a holder for model attributes.
     * @return String "bidList/list"
     */
    @RequestMapping("/bidList/list")
    public String home(Model model) {
        model.addAttribute("bidList", bidService.getAllBidList());
        logger.info("Controller: GET /bidList/list. Displaying list of bidList");
        return "bidList/list";
    }

    /**
     * GET request. Displaying form for adding new bidList to database
     * @param bid BidList object
     * @return String "bidList/add"
     */
    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid, Model model) {
        model.addAttribute("bidList", new BidList());
        logger.info("Controller: GET /bidList/add. Displaying form for adding new bidList");
        return "bidList/add";
    }

    /**
     * POST request. Validation bidList entry
     * @param bid BidList object
     * @param result BindingResult for validation
     * @return String "redirect:/bidList/list"
     */
    @PostMapping("/bidList/validate")
    public String validate(@Valid  BidList bid, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error("Controller: Error. BidList fields have errors");
            return "bidList/add";
        }
        bidService.saveBidList(bid);
        logger.info("Controller: redirect:/bidList/list. BidList has been added to the list");
        return "redirect:/bidList/list";
    }

    /**
     * GET request. Displaying form for update existing bidList in database
     * @param id BidList's ID
     * @param model Model object
     * @return String "bidList/update"
     */
    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        try {
            BidList bidList = bidService.getBidListById(id);
            model.addAttribute("bidList", bidList);
        } catch (BidListNotFoundException e) {
            logger.error("Controller: BidList NOT found with id: " + id);
            return "redirect:/error";
        }
        logger.info("Controller: GET /bidList/update/{id}. Displaying form for update existing bidList");
        return "bidList/update";
    }

    /**
     *  POST request. Updating existing bidList
     * @param id User's ID
     * @param bidList BidList object
     * @param result BindingResult for validation
     * @return String "redirect:/bidList/list"
     */
    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid @ModelAttribute("bidList") BidList bidList,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error("Controller: Error. BidList fields have errors");
            return "bidList/update";
        }
        bidList.setBidListId(id);
        bidService.updateBidList(bidList);
        logger.info("Controller: BidList has been updated");
        return "redirect:/bidList/list";
    }

    /**
     * GET request. Delete existing bidList by ID from database
     * @param id BidList's ID
     * @return String "redirect:/bidList/list"
     */
    @GetMapping("/bidList/delete/{id}")
    public String deleteById(@PathVariable("id") Integer id) {
        bidService.deleteBidList(id);
        logger.info("Controller: GET /bidList/delete/{id}. BidList has been removed");
        return "redirect:/bidList/list";
    }
}
