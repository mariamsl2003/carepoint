package com.capstone.demo.Controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.capstone.demo.Config.SecurityConfig;
import com.capstone.demo.Config.UserInfoDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    private ObjectMapper objectMapper;

    @Autowired
    MedicineService medicineService;

    @Autowired
    MemberService memberService;

    // displaying home page
    @GetMapping("/home")
    public String homePage(Model model, @AuthenticationPrincipal UserInfoDetails userDetails) {
        //checking the principal type just for the error handling
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        System.out.println("Principal type: " + principal.getClass().getName());

        System.out.println("In home, memberService: " + memberService);
        System.out.println("In home, medicineService: " + medicineService);
        System.out.println("In home, medicalService: " + medicalService);

        //making sure the user is authenticated
        MemberModel member = null;

        if(userDetails != null){
            member = memberService.findUsingEmail(userDetails.getUsername());
        }

        System.out.println("member: " + member);

        model.addAttribute("member", member);

        return "homepage";
    }

    // displaying about page
    @GetMapping("/about")
    public String about(Model model, @AuthenticationPrincipal UserInfoDetails userDetails) {
        //making sure the user is authenticated
        MemberModel member = null;

        if(userDetails != null){
            member = memberService.findUsingEmail(userDetails.getUsername());
        }

        System.out.println("member: " + member);

        model.addAttribute("member", member);
        return "about";
    }

    //navigating to the request page to request new medicine or medical
    @PreAuthorize("hasAnyAuthority('MEMBER', 'PHARMACIST')")
    @GetMapping("/request")
    public String request(Model model){
        Long id = retrieving();
        MemberModel member = memberService.findById(id);

        List<MedicalModel> medicals = medicalService.getAcceptedDonation();
        List<MedicineModel> medicine = medicineService.getAcceptedDonation();

        System.out.println("medicals: " + medicals);
        System.out.println("medicines: " + medicine);

        model.addAttribute("member", member);
        model.addAttribute("medicines", medicine);
        model.addAttribute("medicals", medicals);
        return "request";
    }

    @PreAuthorize("hasAnyAuthority('MEMBER', 'PHARMACIST')")
    @PostMapping("/request/form")
    public String requestForm(@RequestParam("id") Long id, @RequestParam("quantity") Long quantity,
                              @RequestParam("prescript") MultipartFile prescript, @RequestParam("category") String category) throws IOException {

        Long member_id = retrieving();
        MemberModel member = memberService.findById(member_id);

        if(category.equals("tools")){
            medicalService.requesting(id, quantity, prescript, member);
        }
        else{
            medicineService.requesting(id, quantity, prescript, member);
        }

        return "redirect:/request";

    }

    // navigating to profile page
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    @GetMapping("/profile")
    public String profile(Model model) {
        Long id = retrieving();
        MemberModel member = memberService.findById(id);

        Long MedicineCount = medicineService.getCountByDonor(id);
        Long MedicalCount = medicalService.getCountByDonor(id);
        Long count = MedicalCount + MedicineCount;

        Long MedicineReqCount = medicineService.getCountByRequester(id);
        Long MedicalReqCount = medicalService.getCountByRequester(id);
        Long reqCount = MedicalReqCount + MedicineReqCount;


        model.addAttribute("member", member);
        model.addAttribute("count", count);
        model.addAttribute("reqCount", reqCount);

        return "profile";
    }

    //edit profile
    @PreAuthorize("hasAnyAuthenticate('MEMBER')")
    @PostMapping("/profile/edit")
    public String editProfile(@RequestParam("email") String email,
                              @RequestParam("phoneNumber") Long phoneNumber,
                              @RequestParam("password") String password,
                              @RequestParam("address") String address,
                              Model model){
        Long id = retrieving();
        memberService.updateMember(id, email, phoneNumber, password, address);
        model.addAttribute("successMessage", "Profile Updated");
        return "redirect:/profile";
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
                              @RequestParam("description") String description,Model model) throws IOException {

        Long id = retrieving();
        MemberModel donor = memberService.findById(id);

        if(category.equals("Medicines")){
            MedicineModel medicine = medicineService.createMedicine(name, quantity, item_image, date_image,description, donor);
        }

        else{
            MedicalModel medical = medicalService.createMedical(name, quantity, item_image, date_image, description, donor);
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
        List<MedicineModel> medicines = medicineService.myRequests(id);
        List<MedicalModel> medicals = medicalService.myRequests(id);

        System.out.println("in myrequest member.role: "+ member.getRole());

        model.addAttribute("role", member.getRole());
        model.addAttribute("member", member);
        model.addAttribute("medicals", medicals);
        model.addAttribute("medicines", medicines);
        return "my_request";
    }

    //navigate to my donation
    @PreAuthorize("hasAnyAuthority('MEMBER', 'PHARMACIST')")
    @GetMapping("/mydonation")
    public String myDonation(Model model) {
        Long id =retrieving();
        MemberModel member = memberService.findById(id);
        List<MedicineModel> medicines = medicineService.myDonations(id);
        List<MedicalModel> medicals = medicalService.myDonations(id);

        System.out.println("in mydonation member.role: " + member.getRole());

        model.addAttribute("role", member.getRole());
        model.addAttribute("member", member);
        model.addAttribute("medicines", medicines);
        model.addAttribute("medicals", medicals);
        return "my_donation";
    }

    //delete medicine in my donation
    @PreAuthorize("hasAnyAuthority('MEMBER', 'PHARMACIST')")
    @PostMapping("/{id}/medicineRemove")
    public String medicineRemove(@PathVariable Long id){
        medicineService.removeMedicine(id);
        return "redirect:/mydonation";
    }

    //delete medical in my donation
    @PreAuthorize("hasAnyAuthority('MEMBER', 'PHARMACIST')")
    @PostMapping("/{id}/medicalRemove")
    public String medicalRemove(@PathVariable Long id){
        medicalService.removeMedicalById(id);
        return "redirect:/mydonation";
    }

    //delete medicine in my request
    @PreAuthorize("hasAnyAuthority('MEMBER', 'PHARMACIST')")
    @PostMapping("/{id}/reqMedicineRemove")
    public String reqMedicineRemove(@PathVariable Long id){
        medicineService.removeRequestingMedicine(id);
        return "redirect:/myrequest";
    }

    //delete medical in my request
    @PreAuthorize("hasAnyAuthority('MEMBER', 'PHARMACIST')")
    @PostMapping("/{id}/reqMedicalRemove")
    public String reqMedicalRemove(@PathVariable Long id){
        medicalService.removeRequestingMedical(id);
        return "redirect:/myrequest";
    }

}
