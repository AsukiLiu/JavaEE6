//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.07.07 at 11:49:47 PM JST 
//


package org.asuki.model.jackson.generated;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Vegetable.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Vegetable">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Carrot"/>
 *     &lt;enumeration value="Squash"/>
 *     &lt;enumeration value="Spinach"/>
 *     &lt;enumeration value="Celery"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Vegetable")
@XmlEnum
public enum Vegetable {

    @XmlEnumValue("Carrot")
    CARROT("Carrot"),
    @XmlEnumValue("Squash")
    SQUASH("Squash"),
    @XmlEnumValue("Spinach")
    SPINACH("Spinach"),
    @XmlEnumValue("Celery")
    CELERY("Celery");
    private final String value;

    Vegetable(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Vegetable fromValue(String v) {
        for (Vegetable c: Vegetable.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
