package ru.amir.spingcourse.bookstoreback.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.amir.spingcourse.bookstoreback.models.Book;
import ru.amir.spingcourse.bookstoreback.services.BooksService;
import ru.amir.spingcourse.bookstoreback.services.PeopleService;


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
    public String createBook(@Valid @ModelAttribute("book") Book book, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "books/new";
        }
        booksService.createBook(book);
        return "books/show";
    }

    @GetMapping("/{id}/edit")
    public String editPage(Model model, @PathVariable("id") int id){
        model.addAttribute("book", booksService.findById(id));
        return "books/edit";
    }

    @PatchMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, @Valid @ModelAttribute("book") Book book, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "books/edit";
        }
        booksService.editBook(book, id);
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
}
