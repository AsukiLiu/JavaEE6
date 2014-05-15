package org.asuki.model.entity;

import static com.google.common.base.Objects.toStringHelper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.asuki.model.BaseEntity;

import lombok.*;

@Entity
@Table(name = "address")
@NoArgsConstructor
@Getter
@Setter
public class Address extends BaseEntity {

    private static final long serialVersionUID = 1L;

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
                .add("zipCode", zipCode)
                .add("prefecture", prefecture)
                .add("city", city)
                .toString();
    }
    // @formatter:on

}