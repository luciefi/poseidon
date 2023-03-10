package com.poseidon.poseidon.controllers;

import com.poseidon.poseidon.domain.CurvePoint;
import com.poseidon.poseidon.exceptions.CurvePointNotFoundException;
import com.poseidon.poseidon.services.ICurvePointService;
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
public class CurveController {

    @Autowired
    private ICurvePointService service;

    Logger logger = LoggerFactory.getLogger(CurveController.class);

    @RequestMapping("/curvePoint/list")
    public String home(Model model) {
        List<CurvePoint> curvePoints = service.findAll();
        model.addAttribute("curvePoints", curvePoints);
        logger.info("Sending curve point list");
        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String addCurvePointForm(Model model) {
        CurvePoint curvePoint = new CurvePoint();
        model.addAttribute("curvePoint", curvePoint);
        logger.info("Sending new curve point form");
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.info("Cannot create curve point : invalid form");
            return "curvePoint/add";
        }
        service.save(curvePoint);
        logger.info("New curve point saved");
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        CurvePoint curvePoint = service.findById(id);
        model.addAttribute("curvePoint", curvePoint);
        logger.info("Sending update form for curve point with id " + id);
        return "curvePoint/update";
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateCurvePoint(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                                   BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.info("Cannot update curve point : invalid form");
            return "curvePoint/update";
        }
        try {
            service.update(curvePoint, id);
            logger.info("Curve Point with id "+ id +" updated");
        } catch (CurvePointNotFoundException e) {
            logger.info("Cannot update curve point : " + e.getMessage());
            return "curvePoint/update";
        }
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurvePoint(@PathVariable("id") Integer id, Model model) {
        service.delete(id);
        logger.info("Curve Point with id "+ id +" deleted");
        return "redirect:/curvePoint/list";
    }
}
