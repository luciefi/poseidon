package com.nnk.springboot.domain;

import org.springframework.beans.factory.annotation.Required;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "bidlist")
public class BidList {
    // TODO: Map columns in data table BIDLIST with corresponding java fields

    Integer BidListId; // tinyint(4) +  PRIMARY KEY
    String account;  //VARCHAR(30)
    String type; // VARCHAR(30)
    Double bidQuantity;
    Double askQuantity;
    Double bid;
    Double ask;
    String benchmark; // VARCHAR(125)
    Timestamp bidListDate;
    String commentary; // VARCHAR(125)
    String security;   // VARCHAR(125)
    String status;  // VARCHAR(10)
    String trader;  // VARCHAR(125)
    String book;  // VARCHAR(125)
    String creationName;  // VARCHAR(125)
    Timestamp creationDate;
    String revisionName;  // VARCHAR(125)
    Timestamp revisionDate;
    String dealName;  // VARCHAR(125)
    String dealType;  // VARCHAR(125)
    String sourceListId;  // VARCHAR(125)
    String side;  // VARCHAR(125)
}
