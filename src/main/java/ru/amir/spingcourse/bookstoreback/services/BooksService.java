package ru.amir.spingcourse.bookstoreback.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.amir.spingcourse.bookstoreback.models.Book;
import ru.amir.spingcourse.bookstoreback.models.Person;
import ru.amir.spingcourse.bookstoreback.repositories.BooksRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;
    private final PeopleService peopleService;

    @Autowired
    public BooksService(BooksRepository booksRepository, PeopleService peopleService) {
        this.booksRepository = booksRepository;
        this.peopleService = peopleService;
    }

    public List<Book> findAll(){
        return booksRepository.findAll(PageRequest.of(0, 1000)).getContent();
    }

    public List<Book> findAll(int page, int book_per_page, boolean sort_by){
        if(sort_by){
            return booksRepository.findAll(PageRequest.of(page, book_per_page, Sort.by("year"))).getContent();
        }
        else{
            return booksRepository.findAll(PageRequest.of(page, book_per_page)).getContent();
        }
    }

    public Book findById(int id){
        Optional<Book> book = booksRepository.findById(id);
        return book.orElse(null);
    }

    public Book getReferenceById(int id){
        return booksRepository.getReferenceById(id);
    }

    public List<Book> findByNameStartingWith(String startingWith){
        return booksRepository.findByNameStartingWith(startingWith);
    }

    @Transactional
    public void createBook(Book newBook){
        booksRepository.save(newBook);
    }

    @Transactional
    public void releaseBookFromPerson(int id){
        Optional<Book> book = booksRepository.findById(id);
        if(book.isPresent()){
            Book bookWithoutPerson = book.get();
            bookWithoutPerson.setOwner(null);
            bookWithoutPerson.setBorrowed_at(null);
            booksRepository.save(bookWithoutPerson);
        }
     }

     @Transactional
     public void borrow(int person_id, int book_id){
        Optional<Book> book = booksRepository.findById(book_id);
        Person person = peopleService.findById(person_id);
        if(book.isPresent() && person != null){
            Book bookToBorrow = book.get();
            bookToBorrow.setOwner(person);
            LocalDateTime ldt = LocalDateTime.now();
            bookToBorrow.setBorrowed_at(ldt);
        }
     }

    @Transactional
    public void editBook(Book updatedBook, int id){
        Optional<Book> book = booksRepository.findById(id);
        if(book.isPresent()){
            Book newBook = book.get();
            newBook.setId(id);
            newBook.setName(updatedBook.getName());
            newBook.setAuthor(updatedBook.getAuthor());
            newBook.setYear(updatedBook.getYear());
            booksRepository.save(newBook);
        }
    }

    @Transactional
    public void deleteBook(int id){
        booksRepository.deleteById(id);
    }
}
