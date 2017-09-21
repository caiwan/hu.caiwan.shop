package hu.caiwan.shop.persist.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.caiwan.shop.persist.model.Privilege;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long>{
	Privilege findByName(String name);

	void delete(Privilege privilege);
}
