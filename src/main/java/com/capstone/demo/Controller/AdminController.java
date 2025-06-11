package com.capstone.demo.Controller;

import com.capstone.demo.Enum.RequestResult;
import com.capstone.demo.Enum.Roles;
import com.capstone.demo.Model.MedicalModel;
import com.capstone.demo.Model.MedicineModel;
import com.capstone.demo.Model.MemberModel;
import com.capstone.demo.Service.MedicalService;
import com.capstone.demo.Service.MedicineService;
import com.capstone.demo.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    //everything for admin

    @Autowired
    MedicalService medicalService;

    @Autowired
    MedicineService medicineService;

    @Autowired
    MemberService memberService;

    //navigating to admin dashboard
    @GetMapping("/dashboard")
    private String dashboard(Model model){
        List<MemberModel> members = memberService.getAllMembers();
        long totalCount = members.size();

        //count members of member role
        long memberRole = members.stream().filter(m -> m.getRole() == Roles.MEMEBR).count();

        //count members of pharmacist role
        long pharmacistRole = members.stream().filter(m -> m.getRole() == Roles.PHARMACIST).count();

        //to percentage
        double memberPercentage = totalCount > 0 ? ((double) memberRole / totalCount) *100 :0;
        double pharmacistPercentage = totalCount > 0? ((double) pharmacistRole / totalCount) *100 :0;

        //round and send
        model.addAttribute("memberPercentage", Math.round(memberPercentage));
        model.addAttribute("pharmacistPercentage", Math.round(pharmacistPercentage));
        return "dashboard";
    }

    //navigating to see all medicals and medicines
    @GetMapping("/medi")
    private String medi(Model model){
        //get all medicals and medicines
        List<MedicalModel> medicals = medicalService.getAllMedical();
        List<MedicineModel> medicines = medicineService.getAllMedicine();

        //group each of the medicines and medicals with the requestResult enum
        Map<RequestResult, List<MedicalModel>> medicalGrouped = medicals.stream().collect(Collectors.groupingBy(MedicalModel::getRequestResult));
        Map<RequestResult, List<MedicineModel>> medicineGrouped = medicines.stream().collect(Collectors.groupingBy(MedicineModel::getRequestResult));

        //prepare individual lists
        //for medicals
        List<MedicalModel> medicalPending = medicalGrouped.getOrDefault(RequestResult.PENDING, Collections.emptyList());
        List<MedicalModel> medicalAccepting = medicalGrouped.getOrDefault(RequestResult.ACCEPTED, Collections.emptyList());
        List<MedicalModel> medicalRejecting = medicalGrouped.getOrDefault(RequestResult.REJECTED, Collections.emptyList());

        //for medicines
        List<MedicineModel> medicinePending = medicineGrouped.getOrDefault(RequestResult.PENDING, Collections.emptyList());
        List<MedicineModel> medicineAccepting = medicineGrouped.getOrDefault(RequestResult.ACCEPTED, Collections.emptyList());
        List<MedicineModel> medicineRejecting = medicineGrouped.getOrDefault(RequestResult.REJECTED, Collections.emptyList());

        //passing counts and lists
        //for medicals
        model.addAttribute("pendingMedicalsCount", medicalPending.size());
        model.addAttribute("acceptingMedicalsCount", medicalAccepting.size());
        model.addAttribute("rejectingMedicalsCount", medicalRejecting.size());

        model.addAttribute("pendingMedicals", medicalPending);
        model.addAttribute("acceptingMedicals", medicalAccepting);
        model.addAttribute("rejectingMedicals", medicalRejecting);

        //for medicines
        model.addAttribute("pendingMedicinesCount", medicinePending.size());
        model.addAttribute("acceptingMedicinesCount", medicineAccepting.size());
        model.addAttribute("rejectingMedicinesCount", medicineRejecting.size());

        model.addAttribute("pendingMedicines", medicinePending);
        model.addAttribute("acceptingMedicines", medicineAccepting);
        model.addAttribute("rejectingMedicines", medicineRejecting);

        return "Medi";
    }

    //navigating to see all the request volunteering
    @GetMapping("/volunteer")
    private String volunteer(Model model){
        List<MemberModel> members = memberService.getPendingMember();
        model.addAttribute("members", members);
        return "requested_volunteer";
    }

    //if the admin click accept
    @PostMapping("/{id}/accept")
    public String acceptMember(@PathVariable Long id){
        memberService.updateRole(id);
        return "redirect:/admin/volunteer";
    }

    //if the admin click rejected
    @PostMapping("/{id}/reject")
    public String rejectMember(@PathVariable Long id){
        memberService.updateVolunteer(id);
        return "redirect:/admin/volunteer";
    }


}
