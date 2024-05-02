package ru.amir.spingcourse.bookstoreback.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "book")
public class Book {
    @NotNull(message="This filed can't be empty")
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Size(max=255, message = "author's name can't be larger than 255 signs")
    @NotEmpty(message = "This filed can't be empty")
    @Column(name="author")
    private String author;
    @NotNull(message="This filed can't be empty")
    @NotEmpty(message = "This filed can't be empty")
    @Size(min=3, message = "The name of the book should consist of at least 3 letters")
    @Column(name="name")
    private String name;

    @NotNull(message="This filed can't be empty")
    @Column(name = "year")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date year;

    @Column(name = "borrowed_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime borrowed_at;

    @ManyToOne()
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;

    @Column(name = "image")
    private byte[] image;

    @Column(name = "description")
    @Size(min = 3, message = "Description should be at least 3 letters")
    private String description;
    public Book( String name, String author, Date year) {
        this.author = author;
        this.year = year;
        this.name = name;
    }

    public Book() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public LocalDateTime getBorrowed_at() {
        return borrowed_at;
    }

    public void setBorrowed_at(LocalDateTime borrowed_at) {
        this.borrowed_at = borrowed_at;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person person) {
        this.owner = person;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setYear(Date year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public Date getYear() {
        return year;
    }

    @Transient
    public boolean isOutdated(){
        if(borrowed_at == null){
            return false;
        }
        Duration durr = Duration.between(borrowed_at, LocalDateTime.now());
        long days = durr.toDays();
        return days > 10;
    }
}
