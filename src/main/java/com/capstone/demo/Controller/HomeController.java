package com.capstone.demo.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
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
    public String profile(@RequestParam("member_id") UUID id, @RequestParam(required = false) Boolean edit,
            Model model) {
        MemberModel member = memberService.findById(id);
        model.addAttribute("user", member);
        authenticate(model);
        model.addAttribute("isEditting", edit != null && edit);
        return "profile";
    }

    // profile editting mode
    @PostMapping("/profile/edit")
    public String editProfile(@RequestParam("member_id") UUID id, @RequestParam String username,
            @RequestParam String password, @RequestParam long phoneNumber) {
        memberService.updateMember(id, username, phoneNumber, password);
        return "redirect:/profile?member_id=" + id;
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

    // navigating to volunteer request page to request to be a volunteer
    @GetMapping("/volunteering")
    public String volunteering(@RequestParam("member_id") UUID id, Model model) {
        MemberModel member = memberService.findById(id);
        model.addAttribute("member", member);
        return "volunteer";
    }

    @PostMapping("/volunteer")
    public String volunteer() {
        // make logic to send the form to the admin when admin side time comes
        return "redirect:/profile";
    }

    // navigating to the adding new donation form
    @GetMapping("/donation")
    public String addNewDonation(@RequestParam("member_id") UUID id, Model model) {
        model.addAttribute("id", id);
        return "new_donation";
    }

    @PostMapping("/new-donation")
    public String addDonation(@RequestParam("member_id") UUID id, @RequestParam("type") String type,
            @RequestParam("name") String name, @RequestParam("description") String description,
            @RequestParam("image") MultipartFile image) throws IOException {
        MemberModel member = memberService.findById(id);
        switch (type) {
            case "medicine":
                medicineService.createMedicine(name, description, member, image);
                break;

            case "medical-supplies":
                medicalService.createMedical(name, description, member, image);
                break;
        }

        return "redirect:/profile";
    }

    // go to my_request page:
    @GetMapping("/myrequest")
    public String myRequest(@RequestParam("member_id") UUID id, Model model) {
        MemberModel member = memberService.findById(id);
        List<MedicineModel> medicines = medicineService.getRequestedMedicine(member);
        List<MedicalModel> medicals = medicalService.getRequestedMedical(member);
        model.addAttribute("medicals", medicals);
        model.addAttribute("medicines", medicines);
        return "my_request";
    }

    // search for both medicine and medical
    @GetMapping("/search")
    public String search(Model model, @RequestParam("name") String name) {
        if (medicalService.findMedicalByName(name).isPresent()) {
            Optional<List<MedicalModel>> medical = medicalService.findMedicalByName(name);
            model.addAttribute("med", medical);
        } else {
            Optional<List<MedicineModel>> medicine = medicineService.findMedicineByName(name);
            model.addAttribute("med", medicine);
        }
        return "redirect:/my_request";
    }

    //shows only the medicals in my_request
    @GetMapping("/mymedical")
    public String myMedical(Model model, @RequestParam("member_id") UUID id) {
        MemberModel member = memberService.findById(id);
        List<MedicalModel> medicals = medicalService.getRequestedMedical(member);
        model.addAttribute("medicals", medicals);
        return "redirect:/my_request";
    }

    //shows only the medicines in my_request
    @GetMapping("/mymedicine")
    public String myMedicine(Model model, @RequestParam("member_id") UUID id) {
        MemberModel member = memberService.findById(id);
        List<MedicineModel> medicines = medicineService.getRequestedMedicine(member);
        model.addAttribute("medicines", medicines);
        return "redirect:/my_request";
    }

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
    // still need to have cards info page tht shows us the validation button inside it

}
