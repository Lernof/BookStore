package ru.amir.spingcourse.bookstoreback.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
    private String full_name;

    @NotNull(message="This filed can't be empty")
    @Min(value = 1900, message = "Your date of birth can't be earlyer than 1900")
    @Column(name = "year_of_birth")
    private int year_of_birth;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;

    public Person(String full_name, int year_of_birth) {
        this.full_name = full_name;
        this.year_of_birth = year_of_birth;
    }

    public Person() {
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

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public void setYear_of_birth(int year_of_birth) {
        this.year_of_birth = year_of_birth;
    }

    public int getId() {
        return id;
    }

    public String getFull_name() {
        return full_name;
    }

    public int getYear_of_birth() {
        return year_of_birth;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", year_of_birth=" + year_of_birth +
                ", password='" + password + '\'' +
                '}';
    }
}
