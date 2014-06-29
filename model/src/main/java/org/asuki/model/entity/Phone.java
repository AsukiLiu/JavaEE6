package org.asuki.model.entity;

import static com.google.common.base.Objects.toStringHelper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.asuki.model.BaseEntity;

import lombok.*;

@Entity
@Table(name = "phone")
@NoArgsConstructor
@Getter
@Setter
public class Phone extends BaseEntity {

    private static final long serialVersionUID = 1L;

    // Two way
    @OneToOne
    @PrimaryKeyJoinColumn
    private Employee employee;

    @Column(name = "home_phone_number")
    private String homePhoneNumber;

    @Column(name = "mobile_phone_number")
    private String mobilePhoneNumber;

    // @formatter:off
    @Override
    public String toString() {
        return toStringHelper(this)
                .add("homePhoneNumber", homePhoneNumber)
                .add("mobilePhoneNumber", mobilePhoneNumber)
                .toString();
    }
    // @formatter:on

}