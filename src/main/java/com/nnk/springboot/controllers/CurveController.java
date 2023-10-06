package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.exception.CurvePointNotFoundException;
import com.nnk.springboot.service.CurvePointService;
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
public class CurveController {
    private final Logger logger = LoggerFactory.getLogger(CurveController.class);

    @Autowired
    CurvePointService curvePointService;

    /**
     * GET request. Displaying all curvePoint that exist in database
     *
     * @param model interface that defines a holder for model attributes.
     * @return String "curvePoint/list"
     */
    @RequestMapping("/curvePoint/list")
    public String home(Model model) {
        model.addAttribute("curvePointList", curvePointService.getAllCurvePoint());
        logger.info("Controller: GET /curvePoint/list. Displaying list of curvePoint");
        return "curvePoint/list";
    }

    /**
     * GET request. Displaying form for adding new curvePoint to database
     * @param curvePoint CurvePoint object
     * @return String "curvePoint/add"
     */
    @GetMapping("/curvePoint/add")
    public String addBidForm(CurvePoint curvePoint) {
        logger.info("Controller: GET /curvePoint/add. Displaying form for adding new curvePoint");
        return "curvePoint/add";
    }

    /**
     * POST request. Validation curvePoint entry
     * @param curvePoint CurvePoint object
     * @param result BindingResult for validation
     * @return String "redirect:/curvePoint/list"
     */
    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result) {
        if (result.hasErrors()) {
            logger.error("Controller: Error. CurvePoint fields have errors");
            return "curvePoint/add";
        }
        curvePointService.saveCurvePoint(curvePoint);
        logger.info("Controller: redirect:/curvePoint/list. CurvePoint has been added to the list");
        return "redirect:/curvePoint/list";
    }

    /**
     * GET request. Displaying form for update existing curvePoint in database
     * @param id CurvePoint's ID
     * @param model Model object
     * @return String "curvePoint/update"
     */
    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        try {
            CurvePoint curvePoint = curvePointService.getCurvePointById(id);
            model.addAttribute("curvePoint", curvePoint);
        } catch (CurvePointNotFoundException e) {
            logger.error("Controller: CurvePoint NOT found with id: " + id);
            return "redirect:/error";
        }
        logger.info("Controller: GET /curvePoint/update/{id}. Displaying form for update existing curvePoint");
        return "curvePoint/update";
    }

    /**
     *  POST request. Updating existing curvePoint
     * @param id CurvePoint's ID
     * @param curvePoint CurvePoint object
     * @param result BindingResult for validation
     * @return String "redirect:/curvePoint/list"
     */
    @PostMapping("/curvePoint/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint, BindingResult result) {
        if(result.hasErrors()){
            logger.error("Controller: Error. CurvePoint fields have errors");
            return "curvePoint/update";
        }
        curvePoint.setId(id);
        curvePointService.updateCurvePoint(curvePoint);
        logger.info("Controller: CurvePoint has been updated");
        return "redirect:/curvePoint/list";
    }

    /**
     * GET request. Delete existing curvePoint by ID from database
     * @param id CurvePoint's ID
     * @return String "redirect:/curvePoint/list"
     */
    @GetMapping("/curvePoint/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id) {
        curvePointService.deleteCurvePoint(id);
        logger.info("Controller: GET /curvePoint/delete/{id}. CurvePoint has been removed");
        return "redirect:/curvePoint/list";
    }
}
