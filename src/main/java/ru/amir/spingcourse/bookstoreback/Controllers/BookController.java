package ru.amir.spingcourse.bookstoreback.Controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import ru.amir.spingcourse.bookstoreback.models.Book;
import ru.amir.spingcourse.bookstoreback.services.BooksService;
import ru.amir.spingcourse.bookstoreback.services.PeopleService;

import java.io.IOException;


@Controller
@RequestMapping("/books")
public class BookController {
    private final BooksService booksService;
    private final PeopleService peopleService;

    @Autowired
    public BookController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String showBooks(Model model,
                            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                            @RequestParam(value = "book_per_page", required = false, defaultValue = "1000") int book_per_page,
                            @RequestParam(value = "sort_by", required = false, defaultValue = "false")boolean sort_by){
        model.addAttribute("books", booksService.findAll(page, book_per_page, sort_by));
        return "books/show";
    }

    @GetMapping("/{id}")
    public String showById(Model model, @PathVariable("id") int id){
        Book book = booksService.findById(id);
        model.addAttribute("book", book);
        model.addAttribute("person", book.getOwner());
        model.addAttribute("people", peopleService.findAll());
        return "books/index";
    }

    @GetMapping("/new")
    public String newBook(Model model){
        model.addAttribute("book", new Book());
        return "books/new";
    }

    @PostMapping("/new")
    public String createBook(@Valid @ModelAttribute("book") Book book,
                             @RequestParam("image")MultipartFile image,
                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "books/new";
        }
        booksService.createBook(book, image);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editPage(Model model, @PathVariable("id") int id){
        model.addAttribute("book", booksService.findById(id));
        return "books/edit";
    }

    @PatchMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id,
                           @Valid @ModelAttribute("book") Book book,
                           @RequestParam(value = "image") MultipartFile image,
                           BindingResult bindingResult) throws IOException {
        if(bindingResult.hasErrors()){
            return "books/edit";
        }
        System.out.println(image.getBytes());
        System.out.println(book.getImage());
        booksService.editBook(book, id, image);
        return "redirect:/books";
    }

    @PatchMapping("release")
    public String release(@RequestParam("book_id") int id){
        booksService.releaseBookFromPerson(id);
        return "redirect:/books";
    }

    @PatchMapping("/borrow")
    public String borrowBook(@RequestParam("selected_person") int person_id, @RequestParam("book_id") int book_id){
        booksService.borrow(person_id, book_id);
        return "redirect:/books";
    }

    @DeleteMapping("/delete")
    public String deleteBook(@RequestParam("book_id") int id){
        booksService.deleteBook(id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String searchBook(Model model,@RequestParam(value = "book_name", required = false, defaultValue = "#$%^&*") String bookName){
        model.addAttribute("books", booksService.findByNameStartingWith(bookName));
        return "books/search";
    }

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
            throws ServletException {

        // Convert multipart object to byte[]
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }
    // Controller method for retrieving the image
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) throws Exception {
        Book book = booksService.findById(id);
        if(book == null){
            throw new ChangeSetPersister.NotFoundException();
        }
        byte[] image = book.getImage();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }
}
