package com.capstone.demo.Controller;

import com.capstone.demo.Config.UserInfoDetails;
import com.capstone.demo.Model.MedicalModel;
import com.capstone.demo.Model.MedicineModel;
import com.capstone.demo.Model.MemberModel;
import com.capstone.demo.Service.MedicalService;
import com.capstone.demo.Service.MedicineService;
import com.capstone.demo.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasAnyAuthority('PHARMACIST')")
@Controller
@RequestMapping("/pharmacist")
public class PharmacistController {

    //everything for pharmacist

    @Autowired
    MedicineService medicineService;

    @Autowired
    MedicalService medicalService;

    @Autowired
    MemberService memberService;

    //retrieving the id
    private Long retrieving (){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserInfoDetails userDetails = (UserInfoDetails) auth.getPrincipal();
        return userDetails.getMemberId();
    }

    //navigating to his profile
    @GetMapping("/profile")
    public String profile(Model model){
        Long id = retrieving();
        MemberModel member = memberService.findById(id);

        List<MedicineModel> medicines = medicineService.getMedicinePending();
        List<MedicalModel> medicals = medicalService.getMedicalPending();

        List<MedicineModel> requestedMedicines = medicineService.requestMedicineRequested();
        List<MedicalModel> requestedMedicals = medicalService.requestMedicalRequested();

        model.addAttribute("member", member);
        model.addAttribute("medicines", medicines);
        model.addAttribute("medicals", medicals);
        model.addAttribute("reqMedicine", requestedMedicines);
        model.addAttribute("reqMedical", requestedMedicals);

        return "pharmacist";
    }

    //edit profile
    @PostMapping("/profile/edit")
    public String editProfile(@RequestParam("id") Long id,
                              @RequestParam("email") String email,
                              @RequestParam("phoneNumber") Long phoneNumber,
                              @RequestParam("password") String password,
                              @RequestParam("address") String address){
        memberService.updateMember(id, email, phoneNumber, password, address);
        return "redirect:/profile";
    }

    //accept medicine donation
    @PostMapping("/medicine/donation/{id}/accept")
    public String acceptMedicineDonation(@PathVariable Long id, Model model){
        medicineService.acceptDonation(id);
        model.addAttribute("successfulMessage", "Donation Updated");
        return "redirect:/pharmacist/profile";
    }

    //reject medicine donation
    @PostMapping("/medicine/donation/{id}/reject")
    public String rejectMedicineDonation(@PathVariable Long id,Model model){
        medicineService.rejectDonation(id);
        model.addAttribute("successfulMessage", "Donation Updated");
        return "redirect:/pharmacist/profile";
    }

    //accept medicine request
    @PostMapping("/medicine/request/{id}/accept")
    public String acceptMedicineRequest(@PathVariable Long id, Model model){
        medicineService.acceptRequest(id);
        model.addAttribute("successfulMessage", "Request Updated");
        return "redirect:/pharmacist/profile";
    }

    //reject medicine request
    @PostMapping("/medicine/request/{id}/reject")
    public String rejectMedicineRequest(@PathVariable Long id, Model model){
        medicineService.rejectRequest(id);
        model.addAttribute("successfulMessage", "Request Updated");
        return "redirect:/pharmacist/profile";
    }

    //accept medical donation
    @PostMapping("/medical/donation/{id}/accept")
    public String acceptMedicalDonation(@PathVariable Long id, Model model){
        medicalService.acceptDonation(id);
        model.addAttribute("successfulMessage", "Donation Updated");
        return "redirect:/pharmacist/profile";
    }

    //reject medical donation
    @PostMapping("/medical/donation/{id}/reject")
    public String rejectMedicalDonation(@PathVariable Long id, Model model){
        medicalService.rejectDonation(id);
        model.addAttribute("successfulMessage", "Donation Updated");
        return "redirect:/pharmacist/profile";
    }

    //accept medical request
    @PostMapping("/medical/request/{id}/accept")
    public String acceptMedicalRequest(@PathVariable Long id, Model model){
        medicalService.acceptRequest(id);
        model.addAttribute("successfulMessage", "Request Updated");
        return "redirect:/pharmacist/profile";
    }

    //reject medical request
    @PostMapping("/medical/request/{id}/reject")
    public String rejectMedicalRequest(@PathVariable Long id, Model model){
        medicalService.rejectRequest(id);
        model.addAttribute("successfulMessage", "Request Updated");
        return "redirect:/pharmacist/profile";
    }
}
