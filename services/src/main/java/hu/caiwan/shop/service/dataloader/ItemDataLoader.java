package hu.caiwan.shop.service.dataloader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.transaction.annotation.Transactional;

import hu.caiwan.shop.persist.dao.ItemRepository;
import hu.caiwan.shop.persist.model.Item;

public class ItemDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	private boolean alreadySetup = false;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (alreadySetup)
			return;
	
		for (int i=0; i< 5; i++){
			Item item = new Item();
			item.setName(String.format("Item %d", i));
			item.setInventoryNumber(String.format("ITEM%03d",i));
			item.setDescription(String.format("Item detailed description %d", i));
			item.setInStock(100+i*50);
			item.setReservedOrder(0);
			
			itemRepository.save(item);
		}
	}
}
