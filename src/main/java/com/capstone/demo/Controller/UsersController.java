package com.capstone.demo.Controller;

import com.capstone.demo.Config.UserInfoDetails;
import com.capstone.demo.Config.UserInfoDetailsService;
import com.capstone.demo.Model.MemberModel;
import com.capstone.demo.Service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/users")
@Controller
public class UsersController {

    //logging in and signing in

    @Autowired
    MemberService memberService;
    @Autowired
    private UserInfoDetailsService userInfoDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    //showing the sign up page
    @GetMapping("/signup")
    public String SignUp(){
        return "signin";
    }

    //submitting the sign up form
    @PostMapping("/signing")
    public String Singing(@RequestParam("username") String username,
                          @RequestParam("email") String email,
                          @RequestParam("password") String password,
                          HttpServletRequest request){
        //creation of the user
        memberService.createMember(username, email, password);

        //logging after register automatically
        UserDetails userDetails = userInfoDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        password,
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.getSession().setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );
        return "redirect:/home";
    }

    //navigating to login page
    @GetMapping("/login")
    public String login(){
        System.out.println("In login, memberService: " + memberService);
        logger.info("Accessing login page");
        return "login";
    }

    //navigating to forget password
    @GetMapping("/pass")
    public String forgetPassword(){
        return "forget-password";
    }

    @PostMapping("/pass/sending")
    public String dealingForgetPassword(@RequestParam("email") String email){

        return "";
    }
}
