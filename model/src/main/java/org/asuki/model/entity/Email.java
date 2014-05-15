package org.asuki.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.asuki.model.BaseEntity;

import lombok.*;

@Entity
@Table(name = "email")
@NoArgsConstructor
@Getter
@Setter
public class Email extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "email_type", nullable = false)
    private int emailType;

    @Column(name = "email_address", nullable = false)
    private String emailAddress;

}
