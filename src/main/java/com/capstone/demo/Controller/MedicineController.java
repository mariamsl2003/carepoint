package com.capstone.demo.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.capstone.demo.Model.MedicineModel;
import com.capstone.demo.Service.MedicineService;

import org.springframework.ui.Model;


@Controller
@RequestMapping("/medicine")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    // get all the medicine (will be tested)
    @GetMapping("/medicines")
    public String getAllMedicine(Model model) {
        Optional<List<MedicineModel>> medicines = medicineService.getMedicineAccepted();
        model.addAttribute("medicines", medicines);
        return "medicine";
    }

    // get the medicine by the name and return to medicine
    @GetMapping("/medicines/search")
    public String getMedicines(Model model, @RequestParam("name") String name) {
        Optional<List<MedicineModel>> medicines = medicineService.findMedicineByName(name);
        if (medicines.isPresent()) {
            model.addAttribute("medicines", medicines);
            return "medicine";
        } else {
            return "no medicine found";
        }
    }

    // continue with the other methods you need

}
