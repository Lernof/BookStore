package ru.amir.spingcourse.bookstoreback;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.amir.spingcourse.bookstoreback.models.Book;
import ru.amir.spingcourse.bookstoreback.repositories.BooksRepository;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class BookStoreBackApplicationTests {

    @Autowired
    private BooksRepository booksRepository;

    @Test
    void testCreateAndFindBook() {
        Book newBook = new Book("My New Book", "Author Name", new Date());

        Book savedBook = booksRepository.save(newBook);

        assertNotNull(savedBook.getId(), "Saved book should have a generated ID");

        Book foundBook = booksRepository.findById(savedBook.getId()).orElse(null);

        assertNotNull(foundBook, "Book should be found in DB");
    }
}
