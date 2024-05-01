package ru.amir.spingcourse.bookstoreback.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String registerPage(){
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam(name = "username") String username,
                               @RequestParam(name = "password") String password,
                               @RequestParam(name = "yearOfBirth") int yearOfBirth){
        Person person = new Person(username, yearOfBirth);
        String encryptedPassword = peopleService.encodePassword(password);
        person.setPassword(encryptedPassword);
        peopleService.createPerson(person);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "auth/login";
    }
}
