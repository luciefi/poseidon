package com.poseidon.poseidon.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class AppErrorController implements ErrorController {
    @GetMapping("/error")
    public String error(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorMessage = "Something went wrong.";
        int statusCode = 0;
        if (status != null) {
            statusCode = Integer.parseInt(status.toString());
            if (statusCode == HttpStatus.FORBIDDEN.value()) {
                errorMessage = "You are not authorized for the requested data.";
            }
        }
        model.addAttribute("statusCode", statusCode > 0 ? statusCode : "");
        model.addAttribute("errorMsg", errorMessage);
        return "error-page";
    }
}
