package org.asuki.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;

@Entity
@Table(name = "EMAIL")
@NoArgsConstructor
@Getter
@Setter
public class Email implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "EMAIL_ID")
    private int emailId;

    @Column(name = "EMAIL_TYPE", nullable = false)
    private int emailType;

    @Column(name = "EMAIL_ADDRESS", nullable = false)
    private String emailAddress;

}
