package com.poseidon.poseidon.domain;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Data
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotBlank(message = "moodys rating is mandatory")
    @Length(max = 125, message = "sandP Rating is too long")
    String moodysRating;


    @NotBlank (message = "sandP rating is mandatory")
    @Length(max = 125, message = "sandP Rating is too long")
    String sandPRating;

    @NotBlank (message = "fitch rating is mandatory")
    @Length(max = 125, message = "sandP Rating is too long")
    String fitchRating;

    @NotNull(message = "order number is mandatory")
    Integer orderNumber;
}
