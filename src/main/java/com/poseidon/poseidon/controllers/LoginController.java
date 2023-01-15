package com.poseidon.poseidon.controllers;

import com.poseidon.poseidon.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String login(Model model) {
        String gitHubUrl = "oauth2/authorization/github";
        model.addAttribute("gitHubUrl", gitHubUrl);
        return "login";
    }

}
