package org.asuki.deltaspike.dto.access;

import java.util.Date;

import lombok.NoArgsConstructor;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.asuki.deltaspike.dto.BusDto;
import org.asuki.deltaspike.dto.LineDto;

@ViewAccessScoped
@NoArgsConstructor
public class BusDtoAccess extends BusDto {

    private static final long serialVersionUID = 1L;

    public BusDtoAccess(LineDto lineDto, Date date) {
        super(lineDto, date);
    }

}
