package hu.caiwan.shop.test.fixtures;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hu.caiwan.shop.persist.dao.UserRepository;
import hu.caiwan.shop.persist.model.User;
import hu.caiwan.shop.service.dataloader.UserDataLoader;

@Component
public class UserFixtures {

	@Autowired
	UserDataLoader userLoader;
	
	@Autowired
	UserRepository userRepository;
	
	public List<User> addDummyUser(){
		userLoader.onApplicationEvent(null);
		return userRepository.findAll();
	}
	
}
