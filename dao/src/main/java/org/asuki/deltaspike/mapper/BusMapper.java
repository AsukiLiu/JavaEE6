package org.asuki.deltaspike.mapper;

import javax.inject.Inject;

import org.apache.deltaspike.data.api.mapping.SimpleQueryInOutMapperBase;
import org.asuki.deltaspike.dto.BusDto;
import org.asuki.model.entity.deltaspike.Bus;
import org.asuki.model.entity.deltaspike.Line;

public class BusMapper extends SimpleQueryInOutMapperBase<Bus, BusDto> {

    @Inject
    private LineMapper lineMapper;

    @Override
    protected Object getPrimaryKey(BusDto dto) {
        return dto.getId();
    }

    @Override
    protected BusDto toDto(Bus entity) {
        BusDto busDto = new BusDto(lineMapper.toDto(entity.getLine()),
                entity.getDate());
        busDto.setId(entity.getId());

        return busDto;
    }

    @Override
    protected Bus toEntity(Bus entity, BusDto dto) {
        entity.setDate(dto.getDate());
        entity.setLine(lineMapper.toEntity(new Line(), dto.getLineDto()));
        entity.setId(dto.getId());

        return entity;
    }
}
