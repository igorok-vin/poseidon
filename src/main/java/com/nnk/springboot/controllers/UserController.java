package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.exception.UserNameExistInDataBaseException;
import com.nnk.springboot.exception.UserNotFoundException;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@Controller
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
   private UserService userService;

    /**
     *GET request. Displaying login form
     * @return String "/login"
     */
    @GetMapping("/login")
    public String login() {
        logger.info("Controller: GET /login. Displaying login form");
        return "/login";
    }

    /**
     * GET request. Displaying all users that exist in database
     * @param model interface that defines a holder for model attributes.
     * @return String "user/list"
     */
    @RolesAllowed("ADMIN")
    @RequestMapping("/user/list")
    public String home(Model model)
    {
        model.addAttribute("users", userService.findAllUsers());
        logger.info("Controller: GET /user/list. Displaying users list ");
        return "user/list";
    }

    /**
     * GET request. Displaying form for adding new user to database
     * @param user
     * @return String "user/list"
     */
    @RolesAllowed("ADMIN")
    @GetMapping("/user/add")
    public String addUser(User user) {
        logger.info("Controller: GET /user/add. Displaying form for adding new user");
        return "user/add";
    }

    /**
     * POST request. Validation user entry
     * @param user User object
     * @param result BindingResult for validation
     * @param model Model object
     * @return String "redirect:/user/list"
     */
    @RolesAllowed("ADMIN")
    @PostMapping("/user/validate")
    public String validate(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users",userService.findAllUsers());
            logger.error("Controller: Error. User fields have errors");
            return "user/add";
        }
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
            userService.saveUser(user);
        } catch (UserNameExistInDataBaseException e) {
            result.rejectValue("","",e.getMessage());
            model.addAttribute("users", userService.findAllUsers());
            logger.warn("Controller: Warning. User name already exist");
            return "user/add";
        }
        logger.info("Controller: redirect:/user/list. User has been added to the list");
        return "redirect:/user/list";

    }

    /**
     * GET request. Displaying form for update existing user in database
     * @param id User's ID
     * @param model Model object
     * @return String "user/update"
     */
    @RolesAllowed("ADMIN")
    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        try {
            User user  = userService.getUserById(id)/*.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id))*/;
            user.setPassword("");
            model.addAttribute("user", user);
        } catch (UserNotFoundException e) {
            logger.warn("Controller: User NOT found with id: " + id);
            return "redirect:/app/404";
        }
        logger.info("Controller: GET /user/update/{id}. Displaying form for update existing user");
        return "user/update";
    }

    /**
     *  POST request. Updating existing user
     * @param id User's ID
     * @param user User object
     * @param result BindingResult for validation
     * @param model Model object
     * @return String "redirect:/user/list"
     */
    @RolesAllowed("ADMIN")
    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error("Controller: Error. User fields have errors");
            return "user/update";
        }
        try {
            model.addAttribute("users", userService.findAllUsers());
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
            user.setId(id);
            userService.updateUser(user);
        } catch (UserNameExistInDataBaseException e) {
           result.rejectValue("","",e.getMessage());
            model.addAttribute("users", userService.findAllUsers());
            logger.warn("Controller: Warning. User name alredy exist");
            return "user/update";
        }
        logger.info("Controller: User has been updated");
        return "redirect:/user/list";
    }

    /**
     * GET request. Delete existing user by ID from database
     * @param id User's ID
     * @param model Model object
     * @return String "redirect:/user/list"
     */
    @RolesAllowed("ADMIN")
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        /*User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));*/
        userService.deleteUser(id);
        model.addAttribute("users", userService.findAllUsers());
        logger.info("Controller: GET /user/delete/{id}. User has been removed");
        return "redirect:/user/list";
    }
}
