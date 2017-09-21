package hu.caiwan.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import hu.caiwan.shop.persist.model.Item;
import hu.caiwan.shop.service.dto.ItemDto;
import hu.caiwan.shop.service.mapper.CustomMapper;
import hu.caiwan.utils.Pair;

public class CustomMapperTest extends CustomMapper<ItemDto, Item> {

	public CustomMapperTest() {
		super(ItemDto.class, Item.class);
		// TODO Auto-generated constructor stub
	}

	@Before
	public void setup() {
		createMethodMap(Item.class, ItemDto.class);
	}

	@After
	public void teardown() {
		dto2Entity = null;
		entity2Dto = null;
	}

	@Test
	public void given_entity_when_map_methods_then_success() {
		assertNotNull(dto2Entity);
		assertNotNull(entity2Dto);

		assertNotEquals(0, dto2Entity.size());
		assertNotEquals(0, entity2Dto.size());

		Pattern pGet = Pattern.compile("get(\\w*)");
		Pattern pSet = Pattern.compile("set(\\w*)");

		for (Pair<Method, Method> p : entity2Dto) {
			String e1 = p.getLeft().getName();
			Matcher m1 = pGet.matcher(e1);
			String e2 = p.getRight().getName();
			Matcher m2 = pSet.matcher(e2);

			assertTrue(e1, m1.matches());
			assertTrue(e2, m2.matches());
		}

		for (Pair<Method, Method> p : dto2Entity) {
			String e1 = p.getLeft().getName();
			Matcher m1 = pGet.matcher(e1);
			String e2 = p.getRight().getName();
			Matcher m2 = pSet.matcher(e2);

			assertTrue(e1, m1.matches());
			assertTrue(e2, m2.matches());
		}

	}

	@Test
	public void given_entity_when_map_dto_then_success()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// given
		Item item = new Item();
		item.setId((long) 1);
		item.setDescription("description");
		item.setInStock(999);
		item.setInventoryNumber("ITEM00001");
		item.setName("Item");
		item.setReservedOrder(2);

		// when
		ItemDto itemDto = new ItemDto();
		defaultEntity2Dto(item, itemDto);

		// then
		assertEquals(item.getId(), itemDto.getId());
		assertEquals(item.getInStock(), itemDto.getInStock());
		assertEquals(item.getInventoryNumber(), itemDto.getInventoryNumber());
		assertEquals(item.getName(), itemDto.getName());
		assertEquals(item.getDescription(), itemDto.getDescription());
		assertEquals(item.getReservedOrder(), itemDto.getReservedOrder());

	}

	// do nothing with them
	@Override
	public ItemDto toDto(Item e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Item fromDto(ItemDto d) {
		// TODO Auto-generated method stub
		return null;
	}

}
