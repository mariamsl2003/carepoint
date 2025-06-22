package com.capstone.demo.Controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

import javax.swing.text.html.Option;


@Controller
public class HomeController {

    //for member

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
    @PreAuthorize("hasAnyAuthority('MEMBER', 'PHARMACIST')")
    @GetMapping("/profile")
    public String profile(@RequestParam("member_id") Long id, @RequestParam(required = false) Boolean edit,
            Model model) {
        MemberModel member = memberService.findById(id);
        model.addAttribute("user", member);
        authenticate(model);
        model.addAttribute("isEditting", edit != null && edit);
        return "profile";
    }

    // profile editting mode
    @PreAuthorize("hasAnyAuthority('MEMBER', 'PHARMACIST)")
    @PostMapping("/profile/edit")
    public String editProfile(@RequestParam("member_id") Long id, @RequestParam String username,
            @RequestParam String password, @RequestParam long phoneNumber) {
        memberService.updateMember(id, username, phoneNumber, password);
        return "redirect:/profile?member_id=" + id;
    }

    // user authentication
    private void authenticate(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<MemberModel> member = null;

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            member = memberService.findMemberByUserName(username);
        }

        model.addAttribute("user", member);
        model.addAttribute("header", "fragments/header :: header");
    }

    // upload the image/update it
    @PreAuthorize("hasAnyAuthority('MEMBER', 'PHARMACIST')")
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
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    @GetMapping("/volunteering")
    public String volunteering(@RequestParam("member_id") Long id, Model model) {
        MemberModel member = memberService.findById(id);
        model.addAttribute("member", member);
        return "volunteer";
    }

    //volunteer form
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    @PostMapping("/{id}/volunteer")
    public String volunteer(@PathVariable Long id, @RequestParam("certificant_date")Date certificantDate, @RequestParam("syndicate_id") String syndicateId) {
        memberService.updatePending(id, certificantDate, syndicateId);
        return "redirect:/profile";
    }

    // navigating to the adding new donation form
    @PreAuthorize("hasAnyAuthority('MEMBER', 'PHARMACIST')")
    @GetMapping("/donation")
    public String addNewDonation(@RequestParam("member_id") Long id, Model model) {
        model.addAttribute("id", id);
        return "new_donation";
    }

    //form method
    @PreAuthorize("hasAnyAuthority('MEMBER', 'PHARMACIST')")
    @PostMapping("/new-donation")
    public String addDonation(@RequestParam("member_id") Long id, @RequestParam("type") String type,
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
    @PreAuthorize("hasAnyAuthority('MEMBER', 'PHARMACIST')")
    @GetMapping("/myrequest")
    public String myRequest(@RequestParam("member_id") Long id, Model model) {
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
    @PreAuthorize("hasAnyAuthority('MEMBER', 'PHARMACIST')")
    @GetMapping("/mymedical")
    public String myMedical(Model model, @RequestParam("member_id") Long id) {
        MemberModel member = memberService.findById(id);
        List<MedicalModel> medicals = medicalService.getRequestedMedical(member);
        model.addAttribute("medicals", medicals);
        return "redirect:/my_request";
    }

    //shows only the medicines in my_request
    @PreAuthorize("hasAnyAuthority('MEMBER', 'PHARMACIST')")
    @GetMapping("/mymedicine")
    public String myMedicine(Model model, @RequestParam("member_id") Long id) {
        MemberModel member = memberService.findById(id);
        List<MedicineModel> medicines = medicineService.getRequestedMedicine(member);
        model.addAttribute("medicines", medicines);
        return "redirect:/my_request";
    }


    // still need to have cards info page that shows us the validation button inside it

    @GetMapping("/cards")
    public String cardsInfo(@RequestParam("member_id") Long id, Model model){
        if (medicalService.getMedicalById(id).isPresent()) {
            Optional<MedicalModel> medical = medicalService.getMedicalById(id);
            model.addAttribute("med", medical);
        } else {
            Optional<MedicineModel> medicine = medicineService.getMedicineById(id);
            model.addAttribute("med", medicine);
        }

        return "cards_info";
    }

}
