package com.poseidon.poseidon.controllers;

import com.poseidon.poseidon.domain.User;
import com.poseidon.poseidon.exceptions.NewUserWithEmptyPasswordException;
import com.poseidon.poseidon.exceptions.UserNotFoundException;
import com.poseidon.poseidon.exceptions.UsernameAlreadyExistsException;
import com.poseidon.poseidon.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private IUserService service;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping("/user/list")
    public String home(Model model) {
        List<User> users = service.findAll();
        model.addAttribute("users", users);
        logger.info("Sending user list");
        return "user/list";
    }

    @GetMapping("/user/add")
    public String addUser(User bid) {
        logger.info("Sending new user form");
        return "user/add";
    }

    @PostMapping("/user/validate")
    public String validate(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.info("Cannot create user : invalid form");
            return "user/add";
        }
        try {
            service.save(user);
            logger.info("New user saved");
            return "redirect:/bidList/list";
        } catch (NewUserWithEmptyPasswordException | UsernameAlreadyExistsException e) {
            ObjectError error = new ObjectError("globalError", e.getMessage());
            result.addError(error);
            logger.info("Cannot create user : " + e.getMessage());
            return "user/add";
        }
    }

    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        User user = service.getUserById(id);
        model.addAttribute("user", user);
        logger.info("Sending update form for user with id " + id);
        return "user/update";
    }

    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.info("Cannot update user : invalid form");
            return "user/update";
        }
        try {
            service.update(user, id);
            logger.info("User with id " + id + " updated");
            return "redirect:/user/list";
        } catch (UserNotFoundException | UsernameAlreadyExistsException e) {
            ObjectError error = new ObjectError("globalError", e.getMessage());
            result.addError(error);
            logger.info("Cannot update user : " + e.getMessage());
            return "user/update";
        }
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        service.delete(id);
        logger.info("User with id " + id + " deleted");
        return "redirect:/user/list";
    }
}
