package com.naumen.naumenproject.model;

import javax.persistence.*;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "rent")
public class Rent {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private String title;
    private String houseType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    public Rent() {
    }

    public Rent(String title, String description, String houseType, User user) {
        this.title = title;
        this.description = description;
        this.houseType = houseType;
        this.author = user;
    }

    public String getAuthorName() {
        return author != null ? author.getUsername() : "<Неизвестный пользователь>";
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

}
