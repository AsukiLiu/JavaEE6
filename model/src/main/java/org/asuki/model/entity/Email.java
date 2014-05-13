package org.asuki.model.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;

@Entity
@Table(name = "email")
@NoArgsConstructor
@Getter
@Setter
public class Email implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "email_id")
    private int emailId;

    @Column(name = "email_type", nullable = false)
    private int emailType;

    @Column(name = "email_address", nullable = false)
    private String emailAddress;

}
