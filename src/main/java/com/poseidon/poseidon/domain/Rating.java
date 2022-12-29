package com.poseidon.poseidon.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "rating")
@Data
public class Rating {
    // TODO: Map columns in data table RATING with corresponding java fields

    @Id
    Integer id;
    String moodysRating;
    String sandPRating;
    String fitchRating;
    Integer orderNumber;
}
