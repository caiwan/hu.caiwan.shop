package hu.caiwan.shop.persist.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.caiwan.shop.persist.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long>{

}
