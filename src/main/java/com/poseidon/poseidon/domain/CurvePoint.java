package com.poseidon.poseidon.domain;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;


@Entity
@Data
public class CurvePoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotNull(message = "curve id is mandatory")
    Integer curveId;

    Timestamp asOfDate;

    @NotNull(message = "term is mandatory")
    Double term;

    @NotNull(message = "value is mandatory")
    Double value;

    Timestamp creationDate;
}
