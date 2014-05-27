package org.asuki.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "customConverter")
public class CustomConverter implements Converter {
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {

        return value;
    }

    public String getAsString(FacesContext context, UIComponent component,
            Object value) {

        return value.toString();
    }
}