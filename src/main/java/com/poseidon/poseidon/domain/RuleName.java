package com.poseidon.poseidon.domain;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
@Data
public class RuleName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotBlank(message = "name is mandatory")
    @Length(max = 125)
    String name;

    @NotBlank(message = "description is mandatory")
    @Length(max = 125)
    String description;

    @NotBlank(message = "json is mandatory")
    @Length(max = 125)
    String json;

    @NotBlank(message = "template is mandatory")
    @Length(max = 512)
    String template;

    @NotBlank(message = "sqlStr is mandatory")
    @Length(max = 125)
    String sqlStr;

    @NotBlank(message = "sqlPart is mandatory")
    @Length(max = 125)
    String sqlPart;
}
