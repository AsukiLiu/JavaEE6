package org.asuki.deltaspike.repository;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.mapping.MappingConfig;
import org.asuki.deltaspike.dto.OrderDto;
import org.asuki.deltaspike.mapper.OrderMapper;
import org.asuki.model.entity.deltaspike.Order;

@Repository(forEntity = Order.class)
@MappingConfig(OrderMapper.class)
public interface OrderRepository extends EntityRepository<OrderDto, Long> {

}
