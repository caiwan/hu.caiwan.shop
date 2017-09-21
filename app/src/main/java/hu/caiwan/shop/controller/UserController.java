package hu.caiwan.shop.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hu.caiwan.shop.service.UserService;
import hu.caiwan.shop.service.aspects.access.AdminOnly;
import hu.caiwan.shop.service.dto.UserProfileDto;

@Controller
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(path = { "/" }, method = RequestMethod.GET)
	@ResponseBody
	public Collection<UserProfileDto> getUserInfo() {
		List<UserProfileDto> list = new ArrayList<>();
		list.add(userService.getUserPrivileges());
		return list;
	}

	@AdminOnly
	@RequestMapping(path = { "/{userid}" }, method = RequestMethod.PUT)
	@ResponseBody
	public void updateUserInfo(@RequestParam Integer userid) {
		throw new RuntimeException("Not implemented method");
	}

}
