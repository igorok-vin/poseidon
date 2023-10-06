package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.exception.RuleNameNotFoundException;
import com.nnk.springboot.service.RuleNameService;
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
public class RuleNameController {

    private final Logger logger = LoggerFactory.getLogger(RuleNameController.class);

    @Autowired
    private RuleNameService ruleNameService;

    /**
     * GET request. Displaying all ruleName that exist in database
     *
     * @param model interface that defines a holder for model attributes.
     * @return String "ruleName/list"
     */
    @RequestMapping("/ruleName/list")
    public String home(Model model) {
        model.addAttribute("ruleName",ruleNameService.getAllruleName());
        logger.info("Controller: GET /ruleName/list. Displaying list of ruleName");
        return "ruleName/list";
    }

    /**
     * GET request. Displaying form for adding new ruleName to database
     * @return String "ruleName/add"
     */
    @GetMapping("/ruleName/add")
    public String addRuleForm(RuleName ruleName) {
        logger.info("Controller: GET /ruleName/add. Displaying form for adding new ruleName");
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result) {
        if(result.hasErrors()){
            logger.error("Controller: Error. RuleName fields have errors");
            return "ruleName/add";
        }
        ruleNameService.saveRuleName(ruleName);
        logger.info("Controller: redirect:/ruleName/list. RuleName has been added to the list");
        return "redirect:/ruleName/list";
    }

    /**
     * GET request. Displaying form for update existing ruleName in database
     * @param id RuleName's ID
     * @param model Model object
     * @return String "ruleName/update"
     */
    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        try {
            RuleName ruleName = ruleNameService.getRuleNameById(id);
            model.addAttribute("ruleName", ruleName);
        } catch (RuleNameNotFoundException e) {
            logger.error("Controller: RuleName NOT found with id: " + id);
            return "redirect:/error";
        }
        logger.info("Controller: GET /ruleName/update/{id}. Displaying form for update existing ruleName");
        return "ruleName/update";
    }

    /**
     *  POST request. Updating existing ruleName
     * @param id RuleName's ID
     * @param ruleName RuleName object
     * @param result BindingResult for validation
     * @return String "redirect:/ruleName/list"
     */
    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName, BindingResult result) {
        if(result.hasErrors()){
            logger.info("Controller: Error. RuleName fields have errors");
            return "ruleName/update";
        }
        ruleName.setId(id);
        ruleNameService.updateRuleName(ruleName);
        logger.info("Controller: RuleName has been updated");
        return "redirect:/ruleName/list";
    }

    /**
     * GET request. Delete existing ruleName by ID from database
     * @param id RuleName's ID
     * @return String "redirect:/ruleName/list"
     */
    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id) {
       ruleNameService.deleteRuleName(id);
        logger.info("Controller: GET /ruleName/delete/{id}. RuleName has been removed");
        return "redirect:/ruleName/list";
    }
}