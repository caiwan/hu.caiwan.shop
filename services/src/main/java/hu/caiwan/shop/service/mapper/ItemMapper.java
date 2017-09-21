package hu.caiwan.shop.service.mapper;

import org.springframework.stereotype.Component;

import hu.caiwan.shop.persist.model.Item;
import hu.caiwan.shop.service.dto.ItemDto;

@Component
public class ItemMapper extends CustomMapper<ItemDto, Item> {

	public ItemMapper() {
		super(ItemDto.class, Item.class);
	}

	@Override
	public ItemDto toDto(Item e) {
		ItemDto d = new ItemDto();
		defaultEntity2Dto(e, d);
		return d;
	}

	@Override
	public Item fromDto(ItemDto d) {
		Item e = new Item();
		defaultDto2Entity(d, e);
		return e;
	}

}
