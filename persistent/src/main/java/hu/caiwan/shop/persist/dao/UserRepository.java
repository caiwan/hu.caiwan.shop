package hu.caiwan.shop.persist.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.caiwan.shop.persist.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	User findByEmail(String email);
    void delete(User user);
}
