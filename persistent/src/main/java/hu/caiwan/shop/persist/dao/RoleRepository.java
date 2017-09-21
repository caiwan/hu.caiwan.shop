package hu.caiwan.shop.persist.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.caiwan.shop.persist.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

    Role findByName(String name);

    void delete(Role role);
	
}
