package com.naumen.naumenproject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "rent")
public class Rent {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @NotBlank(message = "Поле не должно быть пустым")
    private String title;
    private String houseType;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "Поле не должно быть пустым")
    private String description;

    private boolean isBooked;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    public Rent(String title, String description, String houseType, User user) {
        this.title = title;
        this.description = description;
        this.houseType = houseType;
        this.author = user;
    }

    public String getAuthorName() {
        return author != null ? author.getUsername() : "<Неизвестный пользователь>";
    }

}
