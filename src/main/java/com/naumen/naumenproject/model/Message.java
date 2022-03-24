package com.naumen.naumenproject.model;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "messageTable")
public class Message {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Column(
            name = "messageText",
            columnDefinition = "TEXT"
    )
    private String messageText;

    @Min(value = 0)
    @Max(value = 5)
    private int rating;
}
