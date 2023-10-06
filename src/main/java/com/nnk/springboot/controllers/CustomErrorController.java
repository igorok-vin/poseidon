package com.nnk.springboot.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

/*error() An incident that could leave the end user with a negative
        experience when using the application. They may contact support.
warn() An incident that could potentially cause a problem, but it's not severe 
       enough to disrupt the normal use of the application.
info()  Major events that occur during normal operations, like when an outside 
        request is received, an external service is called, a business rule 
        (cross-field validation) gets violated, etc.
debug() More details regarding inner workings of the application which would be 
        activated mostly in test environments to investigate some hard to identify issues.
trace() Not very commonly used unless you want to have some very low-level details 
        regarding the data structures, memory dumps, stack traces, etc.
 * */
@Controller
@ResponseBody
public class CustomErrorController implements ErrorController {

    private final Logger logger = LoggerFactory.getLogger(CustomErrorController.class);

    public String getErrorPath() {

        return "/error";
    }

    @GetMapping("/error")
    public ModelAndView handleError(HttpServletResponse response) {
        int code = response.getStatus();
        System.out.println("Error with code " + code + " Happened");
        logger.error("Error with code {} happened", code);
        return new ModelAndView("error");
    }

}
