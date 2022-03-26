package com.naumen.naumenproject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "messageTable")
public class Message {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Column(
            name = "messageText",
            columnDefinition = "TEXT"
    )
    @NotBlank(message = "Поле не должно быть пустым")
    private String messageText;

    @Min(value = 1, message = "Поставьте оценку")
    @Max(value = 5, message = "Поставьте оценку")
    private int rating;
    private String date;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "rent_id")
    private Rent rent;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    public Message(String messageText, int rating, User user, Rent rent, String date) {
        this.messageText = messageText;
        this.rating = rating;
        this.author = user;
        this.rent = rent;
        this.date = date;
    }

    public String getAuthorName() {
        return author != null ? author.getUsername() : "<Неизвестный пользователь>";
    }
}
