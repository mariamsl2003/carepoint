package com.capstone.demo.Controller;

import com.capstone.demo.Model.MemberModel;
import com.capstone.demo.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/users")
public class UsersController {

    @Autowired
    MemberService memberService;

    //showing the sign up page
    @GetMapping("/signup")
    public String SignUp(){
        return "signin";
    }

    //submitting the sign up form
    @PostMapping("/signing")
    public String Singing(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("phoneNumber") long phoneNumber){
        memberService.createMember(username, password, phoneNumber);
        return "redirect:/home";
    }

    //navigating to login page
    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
