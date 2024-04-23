package ru.amir.spingcourse.bookstoreback.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.amir.spingcourse.bookstoreback.models.Person;
import ru.amir.spingcourse.bookstoreback.repositories.PeopleRepository;
import ru.amir.spingcourse.bookstoreback.security.PersonDetails;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonDetailsService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = peopleRepository.findByFullName(username);
        if(person.isEmpty()){
            throw new UsernameNotFoundException("User Not Found");
        }
        return new PersonDetails(person.get());
    }
}