package org.asuki.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.asuki.model.BaseEntity;

import lombok.*;
import lombok.experimental.Builder;

@Entity
@Table(name = "email")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Email extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "email_address", nullable = false)
    private String emailAddress;

}
