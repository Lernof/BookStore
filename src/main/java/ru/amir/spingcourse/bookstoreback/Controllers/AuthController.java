package ru.amir.spingcourse.bookstoreback.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.amir.spingcourse.bookstoreback.models.Person;
import ru.amir.spingcourse.bookstoreback.services.PeopleService;

@Controller
@RequestMapping("/auth")
public class AuthController{

    private final PeopleService peopleService;

    @Autowired
    public AuthController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping("/register")
    public String registerPage(Model model){
        model.addAttribute("person", new Person());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("person") Person newPerson,
                               BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "auth/register";
        }
        newPerson.setPassword(peopleService.encodePassword(newPerson.getPassword()));
        peopleService.createPerson(newPerson);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "auth/login";
    }

    @GetMapping("/logout")
    public String showLogoutPage() {
        return "auth/logout";
    }
}
