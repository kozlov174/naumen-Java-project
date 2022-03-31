package com.naumen.naumenproject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.annotation.Order;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "messageTable")
@Order
public class Message {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Column(
            name = "messageText",
            columnDefinition = "TEXT",
            length = 200
    )
    @NotBlank(message = "Поле не должно быть пустым")
    private String messageText;

    @Min(value = 1, message = "Поставьте оценку")
    @Max(value = 5, message = "Поставьте оценку")
    private int rating;

    @Column
    private String date;

    @Column
    private boolean isChanged = false;

    @Column
    private boolean isCommented = false;

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

    public boolean isAccessAllowed(Message message, User user) {
        return user.getRoles().contains(Role.ADMIN) || Objects.equals(message.getAuthor().getId(), user.getId());
    }

    public String getAuthorName() {
        return Objects.requireNonNull(author).getFirstName().length() != 0
                || Objects.requireNonNull(author).getLastName().length() != 0
                ? String.format("%s %s", author.getFirstName(), author.getLastName())
                : "<Неизвестный пользователь>";
    }
}
