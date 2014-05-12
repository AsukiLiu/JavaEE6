package org.asuki.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.*;

@Entity
@Table(name = "PHONE")
@NoArgsConstructor
@Getter
@Setter
public class Phone implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "HOLDER_ID")
    private int holderId;

    @OneToOne(targetEntity = Employee.class)
    @PrimaryKeyJoinColumn
    private Employee employee;

    @Column(name = "HOME_PHONE_NUMBER")
    private String homePhoneNumber;

    @Column(name = "MOBILE_PHONE_NUMBER")
    private String mobilePhoneNumber;

}