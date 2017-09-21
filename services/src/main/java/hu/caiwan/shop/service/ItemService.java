package hu.caiwan.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import hu.caiwan.shop.persist.dao.ItemRepository;
import hu.caiwan.shop.persist.model.Item;
import hu.caiwan.shop.service.dto.ItemDto;
import hu.caiwan.shop.service.mapper.AbstractMapper;
import hu.caiwan.shop.service.mapper.ItemMapper;

@Service
public class ItemService extends GenericCrudService<ItemDto, Item, Long> {

	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired 
	private ItemMapper itemMapper;
	
	@Override
	protected JpaRepository<Item, Long> getRepository() {
		return itemRepository;
	}

	@Override
	protected AbstractMapper<ItemDto, Item> getMapper() {
		return itemMapper;
	}

}
