package hu.caiwan.shop.service.mapper;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hu.caiwan.shop.persist.model.Order;
import hu.caiwan.shop.persist.model.OrderItem;
import hu.caiwan.shop.service.dto.OrderDto;
import hu.caiwan.shop.service.dto.OrderItemDto;

@Component
public class OrderMapper extends CustomMapper<OrderDto, Order> {

	@Autowired
	private AddressMapper addressMapper;
	
	@Autowired
	private OrderItemMapper orderItemMapper;
	
	public OrderMapper() {
		super(OrderDto.class, Order.class);
	}

	@Override
	public OrderDto toDto(Order e) {
		OrderDto d = new OrderDto();
		defaultEntity2Dto(e, d);
		
		List<OrderItemDto> oiList = new LinkedList<>();
		for(OrderItem oi : e.getOrderedItems()){
			oiList.add(orderItemMapper.toDto(oi));
		}
		
		d.setOrderedItems(oiList);
		
		d.setBillingAddress(addressMapper.toDto(e.getBillingAddress()));
		d.setShippingAddress(addressMapper.toDto(e.getShippingAddress()));
		
		return d;
	}

	@Override
	public Order fromDto(OrderDto d) {
		throw new RuntimeException("Not implemented");
	}

}
