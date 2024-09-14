package ru.amir.spingcourse.bookstoreback.Controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import ru.amir.spingcourse.bookstoreback.dao.PersonDAO;
import ru.amir.spingcourse.bookstoreback.models.Book;
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
        model.addAttribute("people", peopleService.findAll());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("role", role);
        return "people/show";
    }

    @GetMapping("/{id}")
    public String showById(Model model, @PathVariable("id") int id){
        model.addAttribute("person", peopleService.findById(id));
        model.addAttribute("books", peopleService.getAllBooks(id));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        PersonDetails threadPerson = (PersonDetails)principal;
        model.addAttribute("role", authentication.getAuthorities().iterator().next().getAuthority());
        model.addAttribute("threadId", threadPerson.getId());
        model.addAttribute("personId", id);
        return "people/index";
    }

    @GetMapping("/new")
    public String newPersonInput(Model model){
        model.addAttribute("person", new Person());
        return "people/new";
    }

    @PostMapping("/new")
    public String createPerson(@Valid @ModelAttribute("person") Person person,
                               @RequestParam("image") MultipartFile image,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "people/new";
        }
        person.setPassword(peopleService.encodePassword(person.getPassword()));
        peopleService.createPerson(person, image);
        return "redirect:/people";
    }


    @GetMapping("/{id}/edit")
    public String editPersonPage(Model model, @PathVariable("id") int id){
        model.addAttribute("person", peopleService.findById(id));
        return "people/edit";
    }

    @PatchMapping("/{id}/edit")
    public String editPerson(@Valid @ModelAttribute("person") Person person,
                             @PathVariable("id") int id,
                             @RequestParam(name = "image") MultipartFile image,
                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "people/edit";
        }
        peopleService.editPerson(person, id, image);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int id){
        peopleService.deletePerson(id);
        return "redirect:/people";
    }
    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
            throws ServletException {

        // Convert multipart object to byte[]
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }
    // Controller method for retrieving the image
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable int id) throws Exception {
        Person person = peopleService.findById(id);
        if(person == null){
            throw new ChangeSetPersister.NotFoundException();
        }
        byte[] image = person.getAvatar();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }
}
