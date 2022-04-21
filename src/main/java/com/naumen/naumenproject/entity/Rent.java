package com.naumen.naumenproject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.text.NumberFormat;
import java.util.List;
import java.util.Objects;

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

    @Column
    @NotBlank(message = "Поле не должно быть пустым")
    private String title;

    @Column
    private String roomsNumber;

    @Column
    private String houseType;

    @Column
    private String rentType;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "Поле не должно быть пустым")
    private String description;

    @Column(name = "price")
    @Min(value = 0, message = "Некорректная цена")
    private Double price;

    @Column
    @NotBlank(message = "Поле не должно быть пустым")
    private String street;

    @Column
    private boolean isBooked;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "renter_id")
    private User currentRenter;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    public Rent(String title, String description, String houseType, User user) {
        this.title = title;
        this.description = description;
        this.houseType = houseType;
        this.author = user;
    }

    public String getAuthorEmail() {
        return author.getEmail();
    }

    public String getRentPrice() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String moneyString = formatter.format(getPrice());
        return getPrice() == 0 ? "Бесплатно" : moneyString;
    }

    public String getAuthorName() {
        return Objects.requireNonNull(author).getFirstName().length() != 0
                && Objects.requireNonNull(author).getLastName().length() != 0
                ? String.format("%s %s", author.getFirstName(), author.getLastName())
                : "<Неизвестный пользователь>";
    }

    public String getProfileTitle(Iterable<Rent> rents) {
        return rents.spliterator().getExactSizeIfKnown() == 0
                ? "Пользователь не не делал публикаций"
                : "Публикации пользователя:";
    }

    public boolean isAccessRestricted(User user) {
        boolean isUserAuthor = user.getId().equals(getAuthor().getId());
        boolean isUserAdmin = user.getRoles().contains(Role.ADMIN);
        return isUserAuthor || isUserAdmin;
    }

    public String getPeriodText() {
        return getRentType().equals("Длительно") ? "месяцев": "дней";
    }

    public boolean isBooked() {
        return getCurrentRenter() != null;
    }

}
