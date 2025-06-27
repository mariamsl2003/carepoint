package com.capstone.demo.Controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.capstone.demo.Config.SecurityConfig;
import com.capstone.demo.Config.UserInfoDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    MedicalService medicalService;

    @Autowired
    MedicineService medicineService;

    @Autowired
    MemberService memberService;

    // displaying home page
    @GetMapping("/home")
    public String homePage() {
        //checking the principal type just for the error handling
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        System.out.println("Principal type: " + principal.getClass().getName());

        return "homepage";
    }

    // displaying about page
    @GetMapping("/about")
    public String about() {
        return "about";
    }

    //navigating to the request page to request new medicine or medical
    @PreAuthorize("hasAnyAuthority('MEMBER', 'PHARMACIST')")
    @GetMapping("/request")
    public String request(){
        //need some changes
        return "request";
    }

    // navigating to profile page
    @PreAuthorize("hasAnyAuthority('MEMBER', 'PHARMACIST')")
    @GetMapping("/profile")
    public String profile(@RequestParam(required = false) Boolean edit,
            Model model) {
        Long id = retrieving();
        MemberModel member = memberService.findById(id);
        model.addAttribute("user", member);
        authenticate(model);
        model.addAttribute("isEditting", edit != null && edit);
        return "profile";
    }

    // profile editting mode
    @PreAuthorize("hasAnyAuthority('MEMBER', 'PHARMACIST)")
    @PostMapping("/profile/edit")
    public String editProfile(@RequestParam String username,
            @RequestParam String password, @RequestParam long phoneNumber, @RequestParam String email) {
        Long id = retrieving();
        memberService.updateMember(id, username, email, phoneNumber, password);
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

    //retrieving the id
    private Long retrieving (){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserInfoDetails userDetails = (UserInfoDetails) auth.getPrincipal();
        return userDetails.getMemberId();
    }

    // navigating to volunteer request page to request to be a volunteer
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    @GetMapping("/volunteering")
    public String volunteering(Model model) {
        Long id = retrieving();
        MemberModel member = memberService.findById(id);
        if(member == null){
            logger.info("member is null and passed null");
        }
        model.addAttribute("member", member);
        return "volunteer";
    }

    //volunteer form
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    @PostMapping("/volunteer")
    public String volunteer(@RequestParam("licenseNumber")Long licenseNumber,
                            @RequestParam("currentWork") String currentWork,
                            Model model) {
        Long id = retrieving();
        memberService.updatePending(id, licenseNumber, currentWork);
        model.addAttribute("successfulMessage", "Form Submitted Successfully");
        return "volunteer";
    }

    // navigating to the donation page
    @PreAuthorize("hasAnyAuthority('MEMBER', 'PHARMACIST')")
    @GetMapping("/donation")
    public String addNewDonation(Model model) {
        Long id = retrieving();
        MemberModel member = memberService.findById(id);
        if(member == null){
            logger.info("member is null and pasted null");
        }
        model.addAttribute("member", member);
        return "new_donation";
    }

    //form method
    @PreAuthorize("hasAnyAuthority('MEMBER', 'PHARMACIST')")
    @PostMapping("/new-donation")
    public String addDonation(@RequestParam("name") String name,
                              @RequestParam("quantity") long quantity, @RequestParam("category") String category,
                              @RequestParam("item_image") MultipartFile item_image, @RequestParam("date_image") MultipartFile date_image,
                              Model model) throws IOException {

        Long id = retrieving();
        MemberModel donor = memberService.findById(id);

        if(category.equals("Medicines")){
            MedicineModel medicine = medicineService.createMedicine(name, quantity, item_image, date_image, donor);
        }

        else{
            MedicalModel medical = medicalService.createMedical(name, quantity, item_image, date_image, donor);
        }
        model.addAttribute("successfulMessage", "Form Submitted Successfully");
        return "new_donation";
    }

    // go to my_request page:
    @PreAuthorize("hasAnyAuthority('MEMBER', 'PHARMACIST')")
    @GetMapping("/myrequest")
    public String myRequest(Model model) {
        Long id = retrieving();
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
    public String myMedical(Model model) {
        Long id = retrieving();
        MemberModel member = memberService.findById(id);
        List<MedicalModel> medicals = medicalService.getRequestedMedical(member);
        model.addAttribute("medicals", medicals);
        return "redirect:/my_request";
    }

    //shows only the medicines in my_request
    @PreAuthorize("hasAnyAuthority('MEMBER', 'PHARMACIST')")
    @GetMapping("/mymedicine")
    public String myMedicine(Model model) {
        Long id =retrieving();
        MemberModel member = memberService.findById(id);
        List<MedicineModel> medicines = medicineService.getRequestedMedicine(member);
        model.addAttribute("medicines", medicines);
        return "redirect:/my_request";
    }

}
