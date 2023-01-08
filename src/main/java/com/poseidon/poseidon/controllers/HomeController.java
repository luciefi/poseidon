package com.poseidon.poseidon.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping("/")
    public String home(Model model) {
        logger.info("Sending user home page");
        return "home";
    }

    @RequestMapping("/admin/home")
    public String adminHome(Model model) {
        logger.info("Sending admin home page");
        return "redirect:/bidList/list";
    }
}
