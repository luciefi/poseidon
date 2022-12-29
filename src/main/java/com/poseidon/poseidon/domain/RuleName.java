package com.poseidon.poseidon.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Entity
@Table(name = "rulename")
@Data
public class RuleName {
    // TODO: Map columns in data table RULENAME with corresponding java fields

    @Id
    Integer id;
    String name;
    String description;
    String json;
    String template;
    String sqlStr;
    String sqlPart;
}
