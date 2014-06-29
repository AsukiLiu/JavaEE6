package org.asuki.model.entity;

import static com.google.common.base.Objects.toStringHelper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.NotBlank;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.asuki.model.BaseEntity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.*;
import lombok.experimental.Builder;

@Entity
@Table(name = "address")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@XmlRootElement(name = "address")
@JsonIgnoreProperties(ignoreUnknown = true)
// @JsonPropertyOrder({ "city", "zipCode", "prefecture" })
public class Address extends BaseEntity {

    private static final long serialVersionUID = 1L;

    // 全角スペースはOK
    @Column(name = "zip_code")
    @XmlElement
    @JsonProperty
    @NotBlank
    @Pattern(regexp = "[0-9]{3}-[0-9]{4}")
    private String zipCode;

    @Column(name = "prefecture")
    @XmlElement
    @JsonProperty
    private String prefecture;

    @Column(name = "city")
    @XmlAttribute
    @JsonProperty
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