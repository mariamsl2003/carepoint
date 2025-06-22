package com.capstone.demo.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.capstone.demo.Model.MedicalModel;
import com.capstone.demo.Service.MedicalService;

import org.springframework.ui.Model;

@Controller
@RequestMapping("/medical")
public class MedicalController {

    @Autowired
    private MedicalService medicalService;

    // get all the medical (will be tested)
    // we get the accepted one since it is accepted
    @GetMapping("/medicals")
    public String getAllMedical(Model model) {
        Optional<List<MedicalModel>> medical = medicalService.getMedicalAccepted();
        model.addAttribute("medicals", medical);
        return "medical";
    }

    // get the medicine by the name and return to medicine
    @GetMapping("/medicals/search")
    public String getMedicals(Model model, @RequestParam("name") String name) {
        Optional<List<MedicalModel>> medicals = medicalService.findMedicalByName(name);
        if (medicals.isPresent()) {
            model.addAttribute("medicals", medicals);
            return "medical";
        } else {
            return "no medical instrument found";
        }
    }

    // continue with the other methods you need

}
