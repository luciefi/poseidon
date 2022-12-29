package com.poseidon.poseidon.domain;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;

@Entity
@Data
public class BidList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer bidListId; // tinyint(4) +  PRIMARY KEY

    @NotBlank(message = "account is mandatory")
    @Length(max=30)
    String account;  //VARCHAR(30)

    @NotBlank(message = "type is mandatory")
    @Length(max=30)
    String type; // VARCHAR(30)

    @NotNull(message = "bidQuantity is mandatory")
    @PositiveOrZero
    Double bidQuantity;

    //@NotNull(message = "askQuantity is mandatory")
    @PositiveOrZero
    Double askQuantity;

    // @NotNull(message = "bid is mandatory")
    Double bid;

   // @NotNull(message = "ask is mandatory")
    Double ask;

   // @NotBlank(message = "benchmark is mandatory")
    @Length(max=125)
    String benchmark; // VARCHAR(125)

   // @NotBlank(message = "bidListDate is mandatory")
    Timestamp bidListDate;

   // @NotBlank(message = "commentary is mandatory")
    @Length(max=125)
    String commentary; // VARCHAR(125)

   // @NotBlank(message = "security is mandatory")
    @Length(max=125)
    String security;   // VARCHAR(125)

   // @NotBlank(message = "status is mandatory")
    @Length(max=10)
    String status;  // VARCHAR(10)

    //@NotBlank(message = "trader is mandatory")
    @Length(max=125)
    String trader;  // VARCHAR(125)

   // @NotBlank(message = "book is mandatory")
    @Length(max=125)
    String book;  // VARCHAR(125)

    //@NotBlank(message = "creationName is mandatory")
    @Length(max=125)
    String creationName;  // VARCHAR(125)

    //@NotBlank(message = "creationDate is mandatory")
    Timestamp creationDate;

    //@NotBlank(message = "revisionName is mandatory")
    @Length(max=125)
    String revisionName;  // VARCHAR(125)

    //@NotBlank(message = "revisionDate is mandatory")
    Timestamp revisionDate;

    //@NotBlank(message = "dealName is mandatory")
    @Length(max=125)
    String dealName;  // VARCHAR(125)

    //@NotBlank(message = "dealType is mandatory")
    @Length(max=125)
    String dealType;  // VARCHAR(125)

    //@NotBlank(message = "sourceListId is mandatory")
    @Length(max=125)
    String sourceListId;  // VARCHAR(125)

    //@NotBlank(message = "Side is mandatory")
    @Length(max=125)
    String side;  // VARCHAR(125)

}
