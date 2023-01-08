package com.poseidon.poseidon.controllers;

import com.poseidon.poseidon.domain.Rating;
import com.poseidon.poseidon.exceptions.RatingNotFoundException;
import com.poseidon.poseidon.services.IRatingService;
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
import java.util.List;

@Controller
public class RatingController {

    @Autowired
    private IRatingService service;

    Logger logger = LoggerFactory.getLogger(RatingController.class);

    @RequestMapping("/rating/list")
    public String home(Model model) {
        List<Rating> ratings = service.findAll();
        model.addAttribute("ratings", ratings);
        logger.info("Sending rating list");
        return "rating/list";
    }

    @GetMapping("/rating/add")
    public String addRatingForm(Model model) {
        Rating rating = new Rating();
        model.addAttribute("rating", rating);
        logger.info("Sending new rating form");
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.info("Cannot create rating : invalid form");
            return "rating/add";
        }
        service.save(rating);
        logger.info("New rating saved");
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Rating rating = service.findById(id);
        model.addAttribute("rating", rating);
        logger.info("Sending update form for rating with id " + id);
        return "rating/update";
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.info("Cannot update rating : invalid form");
            return "rating/update";
        }
        try {
            service.update(rating, id);
            logger.info("Rating with id "+ id +" updated");
        } catch (RatingNotFoundException e) {
            logger.info("Cannot create rating : " + e.getMessage());
            return "rating/update";
        }
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        service.delete(id);
        logger.info("Rating with id "+ id +" deleted");
        return "redirect:/rating/list";
    }
}
