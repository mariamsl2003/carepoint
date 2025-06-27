package com.capstone.demo.Controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

public class MyErrorController implements ErrorController {

    @RequestMapping("/error")
    public ModelAndView handleError(){
        ModelAndView modelAndView = new ModelAndView("error");
        return modelAndView;
    }


    public String getErrorPath(){
        return "/error";
    }
}
