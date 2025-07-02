package com.capstone.demo.Controller;

import com.capstone.demo.Model.MedicalModel;
import com.capstone.demo.Model.MedicineModel;
import com.capstone.demo.Model.MemberModel;
import com.capstone.demo.Service.MedicalService;
import com.capstone.demo.Service.MedicineService;
import com.capstone.demo.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@PreAuthorize("hasAnyAuthority('ADMIN')")
@Controller
@RequestMapping("/admin")

public class AdminController {

    //everything for admin


    @Autowired
    private MedicalService medicalService;

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private MemberService memberService;

    //navigating to admin dashboard
    @GetMapping("/dashboard")
    public String dashboard(Model model){

        System.out.println("In dashboard, memberService: " + memberService);
        System.out.println("In dashboard, medicineService: " + medicineService);
        System.out.println("In dashboard, medicalService: " + medicalService);

        MemberModel admin = new MemberModel("fatima", "fatima55@gmail.com", "Beirut, Lebanon");

        List<MedicineModel> medicines = medicineService.getAllDonation();
        List<MedicalModel> medicals = medicalService.getAllDonation();

        List<MedicineModel> reqMedicines = medicineService.getAllRequests();
        List<MedicalModel> reqMedicals = medicalService.getAllRequests();

        model.addAttribute("admin", admin);
        model.addAttribute("medicines", medicines);
        model.addAttribute("medicals", medicals);
        model.addAttribute("reqMedicines", reqMedicines);
        model.addAttribute("reqMedicals", reqMedicals);
        return "dashboard";
    }

    //navigating to see all the request volunteering
    @GetMapping("/volunteer")
    public String volunteer(Model model){
        MemberModel admin = new MemberModel("fatima", "fatima55@gmail.com", "Beirut, Lebanon");

        List<MemberModel> members = memberService.getPendingMember();

        model.addAttribute("admin", admin);
        model.addAttribute("members", members);
        return "requested_volunteer";
    }

    //if the admin click accept
    @PostMapping("/{id}/accept")
    public String acceptMember(@PathVariable Long id, Model model){
        memberService.updateRole(id);
        model.addAttribute("successfulMessage", "Accepted Volunteer");
        return "redirect:/admin/volunteer";
    }

    //if the admin click rejected
    @PostMapping("/{id}/reject")
    public String rejectMember(@PathVariable Long id, Model model){
        memberService.updateVolunteer(id);
        model.addAttribute("successfulMessage", "Rejected Volunteer");
        return "redirect:/admin/volunteer";
    }

}
