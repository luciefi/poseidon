package com.poseidon.poseidon.controllers;

import com.poseidon.poseidon.domain.BidList;
import com.poseidon.poseidon.exceptions.BidListAlreadyExistsException;
import com.poseidon.poseidon.exceptions.BidListNotFoundException;
import com.poseidon.poseidon.services.IBidService;
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

    @RequestMapping("/bidList/list")
    public String home(Model model) {
        List<BidList> bidLists = service.findAll();
        model.addAttribute("bidLists", bidLists);
        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm(Model model) {
        BidList bidList = new BidList();
        model.addAttribute("bidList", bidList);
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bidList, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "bidList/add";
        }
        try {
            service.save(bidList);
        } catch (BidListAlreadyExistsException e) {
            return "bidList/add";
        }
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        BidList bidList = service.findById(id);
        model.addAttribute("bidList", bidList);
        return "bidList/update";
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "bidList/update/{id}";
        }
        try {
            service.update(bidList, id);
        } catch (BidListNotFoundException e) {
            return "bidList/update/{id}";
        }

        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {

        try {
            service.delete(id);
        }
        catch(BidListNotFoundException e){
            return "bidList/list";
        }
        List<BidList> bidLists = service.findAll();
        model.addAttribute("bidLists", bidLists);
        return "redirect:/bidList/list";
    }
}
