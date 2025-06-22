package com.capstone.demo.Controller;

import com.capstone.demo.Model.MedicalModel;
import com.capstone.demo.Model.MedicineModel;
import com.capstone.demo.Service.MedicalService;
import com.capstone.demo.Service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@PreAuthorize("hasAnyAuthority('PHARMACIST')")
@Controller
@RequestMapping("/pharmacist")
public class PharmacistController {

    //everything for phermacist

    @Autowired
    MedicineService medicineService;

    @Autowired
    MedicalService medicalService;

    //validation of the request
    @GetMapping("/validation")
    public String validation(Model model){
        List<MedicineModel> medicines = medicineService.getMedicinePending();
        List<MedicalModel> medicals = medicalService.getMedicalPending();
        model.addAttribute("items", medicals);
        model.addAttribute("items", medicines);
        return "request_validation";
    }

    //search for requests in the validation
    @GetMapping("/validate_search")
    public String validateSearch(Model model, @RequestParam("name") String name) {
        List<MedicalModel> medicals = medicalService.getMedicalPending();
        List<MedicineModel> medicines = medicineService.getMedicinePending();
        int i = 0;
        while(!medicals.isEmpty() && !medicines.isEmpty()){
            if(medicals.get(i).getName().equals(name)){
                model.addAttribute("item", medicals.get(i));
            }
            if(medicines.get(i).getName().equals(name)){
                model.addAttribute("item", medicines.get(i));
            }

            i++;
        }
        return "redirect:/request_validation";
    }
}
