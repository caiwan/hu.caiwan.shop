package hu.caiwan.shop.persist.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.caiwan.shop.persist.model.Address;
import hu.caiwan.shop.persist.model.User;

public interface AddressRepository extends JpaRepository<Address, Long>{

	public List<Address> findByOwner(User owner);
	
}
