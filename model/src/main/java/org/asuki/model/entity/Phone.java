package org.asuki.model.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.*;

@Entity
@Table(name = "phone")
@NoArgsConstructor
@Getter
@Setter
public class Phone implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "holder_id")
    private int holderId;

    @OneToOne(targetEntity = Employee.class)
    @PrimaryKeyJoinColumn
    private Employee employee;

    @Column(name = "home_phone_number")
    private String homePhoneNumber;

    @Column(name = "mobile_phone_number")
    private String mobilePhoneNumber;

}