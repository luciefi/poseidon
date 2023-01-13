package com.poseidon.poseidon.controllers;

import com.poseidon.poseidon.domain.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.RolesAllowed;

@Controller
public class HomeController {

    Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping("/")
    public String home(Model model) {
        logger.info("Sending unauthenticated home page");
        return "home";
    }

    @RequestMapping("/admin/home")
    @RolesAllowed("Admin")
    public String adminHome(Model model) {
        logger.info("Sending admin home page");
        return "redirect:/user/list";
    }
}
