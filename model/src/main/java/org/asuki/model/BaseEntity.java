package org.asuki.model;

import static org.asuki.common.javase.DateUtil.createNow;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.asuki.model.jaxb.IsoDateAdapter;

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

    @Column(name = "created_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(IsoDateAdapter.class)
    // @XmlTransient
    @JsonIgnore
    private Date createdTime;

    @Column(name = "updated_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(IsoDateAdapter.class)
    // @XmlTransient
    @JsonIgnore
    private Date updatedTime;

    @PrePersist
    private void prePersist() {
        setCreatedTime(new Date(createNow()));
        setUpdatedTime(getCreatedTime());
    }

    @PreUpdate
    private void preUpdate() {
        setUpdatedTime(new Date(createNow()));
    }

}
