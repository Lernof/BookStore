package ru.amir.spingcourse.bookstoreback.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.amir.spingcourse.bookstoreback.models.Book;
import ru.amir.spingcourse.bookstoreback.models.Person;
import ru.amir.spingcourse.bookstoreback.repositories.PeopleRepository;


import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService{
    private final BCryptPasswordEncoder passwordEncoder;
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Person> findAll(){
        return peopleRepository.findAll();
    }

    public Person findById(int id){
        Optional<Person> person = peopleRepository.findById(id);
        return person.orElse(null);
    }

    public List<Book> getAllBooks(int id){
        return peopleRepository.findAllBooks(id);
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Transactional
    public void createPerson(Person person){
        peopleRepository.save(person);
    }

    @Transactional
    public void editPerson(Person updatedPerson, int id){
        Optional<Person> person = peopleRepository.findById(id);
        if(person.isPresent()){
            Person newPerson = person.get();
            newPerson.setId(id);
            newPerson.setFullName(updatedPerson.getFullName());
            newPerson.setBooks(updatedPerson.getBooks());
            newPerson.setYear_of_birth(updatedPerson.getYear_of_birth());
            peopleRepository.save(newPerson);
        }
    }

    @Transactional
    public void deletePerson(int id){
        peopleRepository.deleteById(id);
    }
}
