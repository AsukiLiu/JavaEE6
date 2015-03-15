package org.asuki.deltaspike.dto.access;

import javax.inject.Named;

import lombok.NoArgsConstructor;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.asuki.deltaspike.dto.LineDto;

@Named
@ViewAccessScoped
@NoArgsConstructor
public class LineDtoAccess extends LineDto {

    private static final long serialVersionUID = 1L;

    public LineDtoAccess(String departure, String arrival, int price) {
        super(departure, arrival, price);
    }

}
