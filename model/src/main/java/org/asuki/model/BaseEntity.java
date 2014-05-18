package org.asuki.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

@MappedSuperclass
@Setter
@Getter
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    protected BaseEntity() {
    }

    protected BaseEntity(Integer id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlTransient
    @JsonIgnore
    private Integer id;

}
