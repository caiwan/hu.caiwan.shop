package hu.caiwan.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import hu.caiwan.shop.service.AbstractCrudService;
import hu.caiwan.shop.service.ItemService;
import hu.caiwan.shop.service.aspects.access.AdminOnly;
import hu.caiwan.shop.service.dto.ItemDto;

@Controller
@RequestMapping("/api/items")
public class ItemController extends AbstractCrudController<ItemDto, Long>{

	@Autowired
	private ItemService itemService;
	
	@Override
	protected AbstractCrudService<ItemDto, Long> getService() {
		return itemService;
	}

	@Override
	@AdminOnly
	void add(ItemDto dto) {
		super.add(dto);
	}

	@Override
	@AdminOnly
	void update(Long id, ItemDto dto) {
		super.update(id, dto);
	}

	@Override
	@AdminOnly
	void delete(Long id) {
		super.delete(id);
	}

	
	
}
