package com.capstone.demo.Controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.capstone.demo.Model.MedicalModel;
import com.capstone.demo.Model.MedicineModel;
import com.capstone.demo.Model.MemberModel;
import com.capstone.demo.Service.MedicalService;
import com.capstone.demo.Service.MedicineService;
import com.capstone.demo.Service.MemberService;

@Controller
public class HomeController {

    @Autowired
    MedicalService medicalService;

    @Autowired
    MedicineService medicineService;

    @Autowired
    MemberService memberService;

    // displaying home page
    @GetMapping("/home")
    public String homePage(Model model) {
        authenticate(model);
        return "homepage";
    }

    // displaying about page
    @GetMapping("/about")
    public String about(Model model) {
        authenticate(model);
        return "about";
    }

    // returning the random medicals
    @GetMapping("/medical")
    public String medical(Model model) {
        List<MedicalModel> medicals = medicalService.getThreeRandomMedical();
        model.addAttribute("medicals", medicals);
        authenticate(model);
        return "homepage";
    }

    // returning the random medicines
    @GetMapping("/medicine")
    public String medicine(Model model) {
        List<MedicineModel> medicines = medicineService.getThreeRandomMedicine();
        model.addAttribute("medicines", medicines);
        authenticate(model);
        return "homepage";
    }

    // navigating to profile page
    @GetMapping("/profile")
    public String profile(@RequestParam("member_id") UUID id, Model model) {
        MemberModel member = memberService.findById(id);
        model.addAttribute("user", member);
        authenticate(model);
        return "profile";
    }

    // user authentication
    private void authenticate(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MemberModel member = null;

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            member = memberService.findMemberByUserName(username);
        }

        model.addAttribute("user", member);
        model.addAttribute("header", "fragments/header :: header");
    }

    // upload the image/update it
    @PostMapping("/memberimg")
    public String uploadImage(@RequestParam("image") MultipartFile image, @RequestParam("id") UUID id) {
        try {
            memberService.uploadImage(image, id);
            return "image successfully uploaded";
        } catch (Exception e) {
            return "error uploading the image:" + e.getMessage();
        }
    }
}
