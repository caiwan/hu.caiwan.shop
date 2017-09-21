package hu.caiwan.shop.service.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hu.caiwan.shop.persist.model.OrderItem;
import hu.caiwan.shop.service.dto.OrderItemDto;

@Component
public class OrderItemMapper extends CustomMapper<OrderItemDto, OrderItem>{

	@Autowired
	private ItemMapper itemMapper;
	
	public OrderItemMapper() {
		super(OrderItemDto.class, OrderItem.class);
	}

	@Override
	public OrderItemDto toDto(OrderItem e) throws RuntimeException {
		OrderItemDto d = new OrderItemDto();
		
		defaultEntity2Dto(e, d);
		
		d.setItem(itemMapper.toDto(e.getItem()));
		
		return d;
	}

	@Override
	public OrderItem fromDto(OrderItemDto d) throws RuntimeException {
		throw new RuntimeException("Not implemented");
	}

	
	
}
