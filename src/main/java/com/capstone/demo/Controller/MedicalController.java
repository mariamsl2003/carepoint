package com.capstone.demo.Controller;

import java.util.UUID;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.capstone.demo.Model.MedicalModel;
import com.capstone.demo.Model.MemberModel;
import com.capstone.demo.Service.MedicalService;
import com.capstone.demo.Service.MemberService;

import org.springframework.ui.Model;

@Controller
@RequestMapping("/medical")
public class MedicalController {

    @Autowired
    private MedicalService medicalService;

    @Autowired
    private MemberService memberService;

    // create medical (test when finish the profile)
    @GetMapping("/add")
    public String addMedcal() {
        return "addMedical";
    }

    @PostMapping("/add")
    public String addMedical(@RequestParam("name") String name, @RequestParam("description") String description,
            @RequestParam("member_id") UUID id) {
        MemberModel member = memberService.findById(id);
        medicalService.createMedical(name, description, member);
        return "addMedical";
    }

    // get all the medical (will be tested)
    // we get the accepted one since it is accepted
    @GetMapping("/medicals")
    public String getAllMedical(Model model) {
        Optional<List<MedicalModel>> medical = medicalService.getMedicalAccepted();
        model.addAttribute("medicals", medical);
        return "medical";
    }

    // upload the image/update it
    @PostMapping("/medicalimg")
    public String uploadImage(@RequestParam("image") MultipartFile image, @RequestParam("id") UUID id) {
        try {
            medicalService.uploadImage(image, id);
            return "image successfully uploaded";
        } catch (Exception e) {
            return "errir uploading the image:" + e.getMessage();
        }
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
