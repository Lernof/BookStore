package ru.amir.spingcourse.bookstoreback.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.amir.spingcourse.bookstoreback.models.Book;
import ru.amir.spingcourse.bookstoreback.models.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    @Query(value = "FROM Book b WHERE b.owner.id = :value")
    List<Book> findAllBooks(@Param("value") int id);
    Optional<Person> findByFullName(String name);
    Optional<Person> findByUsername(String name);
}
