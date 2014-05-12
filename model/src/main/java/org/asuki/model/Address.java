package org.asuki.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;

@Entity
@Table(name = "ADDRESS")
@NoArgsConstructor
@Getter
@Setter
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ADDRESS_ID")
    private int addressId;

    @Column(name = "ZIP_CODE")
    private String zipCode;

    @Column(name = "PREFECTURE")
    private String prefecture;

    @Column(name = "CITY")
    private String city;

}