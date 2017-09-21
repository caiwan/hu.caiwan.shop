package hu.caiwan.shop.service.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import hu.caiwan.shop.persist.model.User;
import hu.caiwan.shop.service.dto.UserDto;

@Component
public class UserMapper implements AbstractMapper<UserDto, User>{

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public final User fromDto(final UserDto dto) {
		final User user = new User();

		user.setId(0L);

		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user.setEmail(dto.getEmail());

		user.setFirstName(dto.getName());

		return user;
	}

	@Override
	public final UserDto toDto(final User user) {
		final UserDto dto = new UserDto();

		dto.setId(user.getId());

		dto.setPassword("");
		dto.setEmail(user.getEmail());

		dto.setName(user.getFirstName());

		return dto;
	}

}
