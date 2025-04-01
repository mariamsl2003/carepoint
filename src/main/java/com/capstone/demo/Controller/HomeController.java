package com.capstone.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.capstone.demo.Model.MedicalModel;
import com.capstone.demo.Model.MedicineModel;
import com.capstone.demo.Service.MedicalService;
import com.capstone.demo.Service.MedicineService;

@Controller
public class HomeController {

    @Autowired
    MedicalService medicalService;

    @Autowired
    MedicineService medicineService;

    // displaying home page
    @GetMapping("/home")
    public String homePage() {

        return "homepage";
    }

    // displaying about page
    @GetMapping("/about")
    public String about() {
        return "about";
    }

    // returning the random medicals
    @GetMapping("/medical")
    public String medical(Model model) {
        List<MedicalModel> medicals = medicalService.getThreeRandomMedical();
        model.addAttribute("medicals", medicals);
        return "homepage";
    }

    // returning the random medicines
    @GetMapping("/medicine")
    public String medicine(Model model) {
        List<MedicineModel> medicines = medicineService.getThreeRandomMedicine();
        model.addAttribute("medicines", medicines);
        return "homepage";
    }
}
