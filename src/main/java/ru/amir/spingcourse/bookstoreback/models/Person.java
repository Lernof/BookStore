package ru.amir.spingcourse.bookstoreback.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotNull(message="This filed can't be empty")
    private int id;

    @NotEmpty(message="This filed can't be empty")
    @NotNull
    @Size(max=255, message = "Your full name can't be larger then 255 letters")
    @Column(name = "full_name")
    private String fullName;

    @Column(name = "username")
    @NotEmpty(message="This filed can't be empty")
    @Size(max=255, message = "Your full name can't be larger then 255 letters")
    private String username;

    @NotNull(message="This filed can't be empty")
    @Column(name = "year_of_birth")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date year_of_birth;

    @Column(name = "password")
    @NotEmpty(message="This filed can't be empty")
    private String password;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;

    public Person(String fullName, Date year_of_birth, String username) {
        this.fullName = fullName;
        this.year_of_birth = year_of_birth;
        this.username = username;
    }

    public Person() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFullName(String full_name) {
        this.fullName = full_name;
    }

    public void setYear_of_birth(Date year_of_birth) {
        this.year_of_birth = year_of_birth;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public Date getYear_of_birth() {
        return year_of_birth;
    }

}
