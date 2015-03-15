package org.asuki.deltaspike.dto.access;

import java.util.List;

import javax.inject.Named;

import lombok.NoArgsConstructor;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.asuki.deltaspike.dto.SeatsDto;

@Named
@ViewAccessScoped
@NoArgsConstructor
public class SeatsDtoAccess extends SeatsDto {

    private static final long serialVersionUID = 1L;

    public SeatsDtoAccess(List<String> chosenSeats) {
        super(chosenSeats);
    }

}
