package com.poseidon.poseidon.controllers;

import com.poseidon.poseidon.domain.RuleName;
import com.poseidon.poseidon.exceptions.RuleNameNotFoundException;
import com.poseidon.poseidon.services.IRuleNameService;
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
public class RuleNameController {

    @Autowired
    private IRuleNameService service;

    Logger logger = LoggerFactory.getLogger(RuleNameController.class);

    @RequestMapping("/ruleName/list")
    public String home(Model model) {
        List<RuleName> ruleNames = service.findAll();
        model.addAttribute("ruleNames", ruleNames);
        logger.info("Sending rule name list");
        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleForm(Model model) {
        RuleName ruleName = new RuleName();
        model.addAttribute("ruleName", ruleName);
        logger.info("Sending new rule name form");

        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.info("Cannot create rule name : invalid form");
            return "ruleName/add";
        }
        service.save(ruleName);
        logger.info("New rule name saved");
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        RuleName ruleName = service.findById(id);
        model.addAttribute("ruleName", ruleName);
        logger.info("Sending update form for rule name with id " + id);
        return "ruleName/update";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.info("Cannot update rule name : invalid form");
            return "ruleName/update";
        }
        try {
            service.update(ruleName, id);
            logger.info("Rule Name with id "+ id +" updated");
        } catch (RuleNameNotFoundException e) {
            logger.info("Cannot update rule name : " + e.getMessage());
            return "ruleName/update";
        }
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        service.delete(id);
        logger.info("Rule Name with id "+ id +" deleted");
        return "redirect:/ruleName/list";
    }
}
