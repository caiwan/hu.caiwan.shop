package hu.caiwan.shop.service.dto;

import java.util.Collection;

public class UserProfileDto {

	private UserDto user;
	
	private Collection<String> roles;

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public Collection<String> getRoles() {
		return roles;
	}

	public void setRoles(Collection<String> roles) {
		this.roles = roles;
	}
	
}
