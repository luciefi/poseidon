package com.poseidon.poseidon.controllers;

import com.poseidon.poseidon.domain.RuleName;
import com.poseidon.poseidon.exceptions.RuleNameNotFoundException;
import com.poseidon.poseidon.services.IRuleNameService;
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

    @RequestMapping("/ruleName/list")
    public String home(Model model) {
        List<RuleName> ruleNames = service.findAll();
        model.addAttribute("ruleNames", ruleNames);
        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleForm(Model model) {
        RuleName ruleName = new RuleName();
        model.addAttribute("ruleName", ruleName);
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "ruleName/add";
        }
        service.save(ruleName);
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        RuleName ruleName = service.findById(id);
        model.addAttribute("ruleName", ruleName);
        return "ruleName/update";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "ruleName/update";
        }
        try {
            service.update(ruleName, id);
        } catch (RuleNameNotFoundException e) {
            return "ruleName/update";
        }
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        service.delete(id);
        return "redirect:/ruleName/list";
    }
}
