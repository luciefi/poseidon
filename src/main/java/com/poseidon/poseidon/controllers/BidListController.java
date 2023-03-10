package com.poseidon.poseidon.controllers;

import com.poseidon.poseidon.domain.BidList;
import com.poseidon.poseidon.exceptions.BidListNotFoundException;
import com.poseidon.poseidon.services.IBidService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

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
public class BidListController {

    @Autowired
    private IBidService service;

    Logger logger = LoggerFactory.getLogger(BidListController.class);

    @RequestMapping("/bidList/list")
    public String home(Model model) {
        List<BidList> bidLists = service.findAll();
        model.addAttribute("bidLists", bidLists);
        logger.info("Sending bid list list");

        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm(Model model) {
        BidList bidList = new BidList();
        model.addAttribute("bidList", bidList);
        logger.info("Sending new bid list form");
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bidList, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.info("Cannot create bid list : invalid form");
            return "bidList/add";
        }
        service.save(bidList);
        logger.info("New bid list saved");
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        BidList bidList = service.findById(id);
        model.addAttribute("bidList", bidList);
        logger.info("Sending update form for bid list with id " + id);
        return "bidList/update";
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.info("Cannot update bid list : invalid form");
            return "bidList/update";
        }
        try {
            service.update(bidList, id);
            logger.info("Bid list with id "+ id +" updated");
        } catch (BidListNotFoundException e) {
            logger.info("Cannot update bid list : " + e.getMessage());
            return "bidList/update";
        }

        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        service.delete(id);
        logger.info("Bid list with id "+ id +" deleted");
        return "redirect:/bidList/list";
    }
}
