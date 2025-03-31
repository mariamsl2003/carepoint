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

import com.capstone.demo.Model.MedicineModel;
import com.capstone.demo.Model.MemberModel;
import com.capstone.demo.Repository.MemberRepository;
import com.capstone.demo.Service.MedicineService;

import org.springframework.ui.Model;

@Controller
@RequestMapping("/medicine")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private MemberRepository memberRepository;

    // create medicine (test when finish the profile)
    @GetMapping("/add")
    public String addMedicine() {
        return "addMedicine";
    }

    @PostMapping("/add")
    public String addMedicine(@RequestParam("name") String name, @RequestParam("description") String description,
            @RequestParam("member_id") UUID id) {
        MemberModel member = memberRepository.findMemberById(id);
        medicineService.createMedicine(name, description, member);
        return "addMedicine";
    }

    // get all the medicine (will be tested)
    @GetMapping("/medicines")
    public String getAllMedicine(Model model) {
        List<MedicineModel> medicines = medicineService.getAllMedicine();
        model.addAttribute("medicines", medicines);
        return "medicine";
    }

    // upload the image/update it
    @PostMapping("/medicineimg")
    public String uploadImage(@RequestParam("image") MultipartFile image, @RequestParam("id") UUID id) {
        try {
            medicineService.uploadImage(image, id);
            return "image successfully uploaded";
        } catch (Exception e) {
            return "errir uploading the image:" + e.getMessage();
        }
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
