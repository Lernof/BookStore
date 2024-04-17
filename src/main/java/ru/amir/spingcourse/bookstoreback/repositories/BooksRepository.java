package ru.amir.spingcourse.bookstoreback.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.amir.spingcourse.bookstoreback.models.Book;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    @Query("SELECT b FROM Book b WHERE b.name LIKE :namePrefix%")
    List<Book> findByNameStartingWith(@Param("namePrefix") String namePrefix);

}
