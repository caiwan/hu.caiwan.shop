package hu.caiwan.shop.service.mapper;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hu.caiwan.shop.persist.model.Privilege;
import hu.caiwan.shop.persist.model.Role;
import hu.caiwan.shop.persist.model.User;
import hu.caiwan.shop.service.dto.UserProfileDto;

@Component
public class UserPrivilegeMapper implements AbstractMapper<UserProfileDto, User>{
	
	@Autowired 
	private UserMapper userMapper;
	
	@Override
	public UserProfileDto toDto(final User user) {
		final UserProfileDto roleDto = new UserProfileDto();
		
		Collection<String> roles = new HashSet<>();
		for(Role role : user.getRoles()){
			for(Privilege privilege : role.getPrivileges()){
				roles.add(privilege.getName());
			}
		}
		
		roleDto.setUser(userMapper.toDto(user));
		
		return roleDto;
	}

	@Override
	public User fromDto(UserProfileDto d) {
		// Nothing to do here.
		return null;
	}
	
}
