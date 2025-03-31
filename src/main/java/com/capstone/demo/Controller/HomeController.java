package com.capstone.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // displaying home page
    @GetMapping("/home")
    public String homePage() {

        return "homepage";
    }

    // displaying about page
    @GetMapping("/about")
    public String about() {
        return "about";
    }
}
