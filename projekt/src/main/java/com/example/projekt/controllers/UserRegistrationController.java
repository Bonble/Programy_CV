package com.example.projekt.controllers;

import com.example.projekt.models.User;
import com.example.projekt.models.UserRegisterDto;
import com.example.projekt.scheduled.StatusStrony;
import com.example.projekt.services.UserService;
import com.example.projekt.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private UserService userService;
    @ModelAttribute("user")
    public UserRegisterDto userRegistrationDto(){
        return new UserRegisterDto();
    }
    @GetMapping
    public String showRegistrationForm() {
        StatusStrony statusStrony = StatusStrony.getInstance();
        if(statusStrony.getMaintenece() || statusStrony.getMainteneceAdmin()){
            return "maintenance";
        }
        return "registration";
    }
    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") UserRegisterDto registrationDto) {
        Optional<User> user = userServiceImpl.getUserByUsername2(registrationDto.getLogin());
        if(user.isEmpty()){
            userService.save(registrationDto);
            return "redirect:/registration?success";
        }
        return "redirect:/registration?fail";
    }
}