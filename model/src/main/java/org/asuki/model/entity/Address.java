package org.asuki.model.entity;

import static com.google.common.base.Objects.toStringHelper;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;

@Entity
@Table(name = "address")
@NoArgsConstructor
@Getter
@Setter
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "address_id")
    private int addressId;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "prefecture")
    private String prefecture;

    @Column(name = "city")
    private String city;

    // @formatter:off
    @Override
    public String toString() {
        return toStringHelper(this)
                .add("addressId", addressId)
                .add("zipCode", zipCode)
                .add("prefecture", prefecture)
                .add("city", city)
                .toString();
    }
 // @formatter:on

}