package ru.amir.spingcourse.bookstoreback.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.amir.spingcourse.bookstoreback.config.SecurityConfig;
import ru.amir.spingcourse.bookstoreback.dao.PersonDAO;
import ru.amir.spingcourse.bookstoreback.models.Person;
import ru.amir.spingcourse.bookstoreback.security.PersonDetails;
import ru.amir.spingcourse.bookstoreback.services.PeopleService;

@Controller
@RequestMapping("/people")
public class PersonController {

    private final PersonDAO personDAO;
    private final PeopleService peopleService;

    @Autowired
    public PersonController(PersonDAO personDAO, PeopleService peopleService) {
        this.personDAO = personDAO;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String showPeople(Model model){
//        model.addAttribute("people", personDAO.getAllPeople());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        System.out.println(personDetails.getPerson());
        model.addAttribute("people", peopleService.findAll());
        return "people/show";
    }

    @GetMapping("/{id}")
    public String showById(Model model, @PathVariable("id") int id){
        model.addAttribute("person", peopleService.findById(id));
        model.addAttribute("books", peopleService.getAllBooks(id));
        return "people/index";
    }

    @GetMapping("/new")
    public String newPersonInput(Model model){
        model.addAttribute("person", new Person());
        return "people/new";
    }

    @PostMapping("/new")
    public String createPerson(@Valid @ModelAttribute("person") Person person, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "people/new";
        }
        peopleService.createPerson(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String editPersonPage(Model model, @PathVariable("id") int id){
        model.addAttribute("person", peopleService.findById(id));
        return "people/edit";
    }

    @PatchMapping("/{id}/edit")
    public String editPerson(@Valid @ModelAttribute("person") Person person, @PathVariable("id") int id,
                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "people/edit";
        }
        peopleService.editPerson(person, id);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int id){
        peopleService.deletePerson(id);
        return "redirect:/people";
    }
}
