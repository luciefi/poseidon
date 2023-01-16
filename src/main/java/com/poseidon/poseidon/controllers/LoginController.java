package com.poseidon.poseidon.controllers;

import com.poseidon.poseidon.exceptions.UsernameAlreadyExistsException;
import com.poseidon.poseidon.services.IUserService;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @Autowired
    IUserService service;

    Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/login")
    public String login(Model model) {
        String gitHubUrl = "oauth2/authorization/github";
        model.addAttribute("gitHubUrl", gitHubUrl);
        logger.info("Sending login page");
        return "login";
    }


    @GetMapping("/loginSuccess")
    public String getLoginInfo(Model model, OAuth2AuthenticationToken authentication, HttpServletRequest httpServletRequest ) {
        if (service.hasAnAccount(authentication)) {
            return "redirect:/bidList/list";
        }
        logger.info("User does not have account, sending username page");
        String string = "";
        model.addAttribute("string", string);
        String errorMessage = "";
        model.addAttribute("errorMessage",errorMessage);
        return "username-oauth2";
    }

    @PostMapping("/username")
    public String setUsername(@ModelAttribute String string, BindingResult result, Model model, OAuth2AuthenticationToken authentication) {

        try {
            service.saveOAuthUser(string, authentication);
            logger.info("Username saved for OAuth user");
            return "redirect:/bidList/list";

        } catch ( IllegalArgumentException e) {
            logger.info("Cannot save username : " + e.getMessage());
            String errorMessage = e.getMessage();
            model.addAttribute("errorMessage",errorMessage);
            return "username-oauth2";
        }
    }
}
