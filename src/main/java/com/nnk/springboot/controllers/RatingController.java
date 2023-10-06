package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.exception.RatingNotFoundException;
import com.nnk.springboot.service.RatingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class RatingController {

    private final Logger logger = LoggerFactory.getLogger(RatingController.class);

    @Autowired
    RatingService ratingService;

    /**
     * GET request. Displaying all rating that exist in database
     *
     * @param model interface that defines a holder for model attributes.
     * @return String "rating/list"
     */
    @RequestMapping("/rating/list")
    public String home(Model model) {
        model.addAttribute("rating", ratingService.getAllRating());
        logger.info("Controller: GET /rating/list. Displaying list of rating");
        return "rating/list";
    }

    /**
     * GET request. Displaying form for adding new rating to database
     * @param rating Rating object
     * @return String "rating/add"
     */
    @GetMapping("/rating/add")
    public String addRatingForm(Rating rating) {
        logger.info("Controller: GET /rating/add. Displaying form for adding new rating");
        return "rating/add";
    }

    /**
     * POST request. Validation rating entry
     * @param rating Rating object
     * @param result BindingResult for validation
     * @return String "redirect:/rating/list"
     */
    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result, Model model) {
        if(result.hasErrors()) {
            logger.error("Controller: Error. Rating fields have errors");
            return "rating/add";
        }
        ratingService.saveRating(rating);
        logger.info("Controller: redirect:/rating/list. Rating has been added to the list");
        return "redirect:/rating/list";
    }

    /**
     * GET request. Displaying form for update existing rating in database
     * @param id Rating's ID
     * @param model Model object
     * @return String "rating/update"
     */
    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        try {
            Rating rating = ratingService.getRatingById(id);
            model.addAttribute("rating",rating);
        } catch (RatingNotFoundException e) {
            logger.error("Controller: Rating NOT found with id: " + id);
            return "redirect:/error";
        }
        logger.info("Controller: GET /rating/update/{id}. Displaying form for update existing rating");
        return "rating/update";
    }

    /**
     *  POST request. Updating existing rating
     * @param id Rating's ID
     * @param rating Rating object
     * @param result BindingResult for validation
     * @return String "redirect:/rating/list"
     */
    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating, BindingResult result, Model model) {
        if(result.hasErrors()) {
            logger.info("Controller: Error. Rating fields have errors");
            return "rating/update";
        }
        rating.setId(id);
        ratingService.updateRating(rating);
        logger.info("Controller: Rating has been updated");
        return "redirect:/rating/list";
    }

    /**
     * GET request. Delete existing rating by ID from database
     * @param id Rating's ID
     * @return String "redirect:/rating/list"
     */
    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        ratingService.deleteRating(id);
        logger.info("Controller: GET /rating/delete/{id}. Rating has been removed");
        return "redirect:/rating/list";
    }
}
