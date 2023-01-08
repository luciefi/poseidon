package com.poseidon.poseidon.controllers;

import com.poseidon.poseidon.domain.Trade;
import com.poseidon.poseidon.exceptions.TradeNotFoundException;
import com.poseidon.poseidon.services.ITradeService;
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
public class TradeController {

    @Autowired
    private ITradeService service;

    Logger logger = LoggerFactory.getLogger(TradeController.class);

    @RequestMapping("/trade/list")
    public String home(Model model) {
        List<Trade> trades = service.findAll();
        model.addAttribute("trades", trades);
        logger.info("Sending trade list");
        return "trade/list";
    }

    @GetMapping("/trade/add")
    public String addUser(Model model) {
        Trade trade = new Trade();
        model.addAttribute("trade", trade);
        logger.info("Sending new trade form");
        return "trade/add";
    }

    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.info("Cannot create trade : invalid form");
            return "trade/add";
        }
        service.save(trade);
        logger.info("New trade saved");
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Trade trade = service.findById(id);
        model.addAttribute("trade", trade);
        logger.info("Sending update form for trade with id " + id);
        return "trade/update";
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.info("Cannot update trade : invalid form");
            return "trade/update";
        }
        try {
            service.update(trade, id);
            logger.info("Trade with id "+ id +" updated");
        } catch (TradeNotFoundException e) {
            logger.info("Cannot update trade : " + e.getMessage());
            return "trade/update";
        }
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        service.delete(id);
        logger.info("Trade with id "+ id +" deleted");
        return "redirect:/trade/list";
    }
}
