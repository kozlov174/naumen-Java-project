package com.naumen.naumenproject.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;

import java.util.Objects;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Table
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "rent_id")
    private Rent rent;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "author_id")
    private User author;

    @Enumerated(EnumType.STRING)
    private RentStatus status;

    @Column
    @Min(value = 1, message = "Значение должно быть не менее 1")
    private int period;

    public Notification() {
    }

    public Notification(User user, Rent rent, RentStatus rentStatus, User author, int period) {
        sender = user;
        status = rentStatus;
        this.rent = rent;
        this.author = author;
        this.period = period;
    }

    public boolean checkStatus() {
        return getStatus() != RentStatus.REJECTED && getStatus() != RentStatus.APPROVED;
    }

    public boolean isApproved() {
        return getStatus() == RentStatus.APPROVED;
    }

    public boolean isRejected() {
        return getStatus() == RentStatus.REJECTED;
    }

    public boolean isUnderConsideration() {
        return getStatus() == RentStatus.UNDER_CONSIDERATION;
    }

    public boolean isUserNotAuthor(Rent rent, User user) {
        return !Objects.equals(rent.getAuthor().getId(), user.getId());
    }

}
