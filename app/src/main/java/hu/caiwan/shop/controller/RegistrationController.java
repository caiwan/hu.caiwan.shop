package hu.caiwan.shop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import hu.caiwan.shop.events.OnRegistrationCompleteEvent;
import hu.caiwan.shop.exceptions.VerifyTokenException;
import hu.caiwan.shop.persist.model.User;
import hu.caiwan.shop.service.UserService;
import hu.caiwan.shop.service.dto.GenericResponse;
import hu.caiwan.shop.service.dto.UserDto;

@Controller
@RequestMapping(path = { "" })
public class RegistrationController {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserService userService;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@RequestMapping(value = { "/signup/", "/register/" }, method = RequestMethod.POST)
	@ResponseBody
	public GenericResponse registerUser(@RequestBody @Valid final UserDto accountDto,
			final HttpServletRequest request) {
		// TODO
		// final String response = request.getParameter("g-recaptcha-response");
		// captchaService.processResponse(response);

		LOGGER.info("Registering user account with information: {}", accountDto);

		final User user = userService.registerUser(accountDto);
		eventPublisher.publishEvent(new OnRegistrationCompleteEvent(this, user, request.getLocale()));
		return new GenericResponse("success");
	}

	@RequestMapping(value = "/validate/{token}", method = RequestMethod.GET)
	@ResponseBody
	public void valudate(@PathVariable String token, HttpServletResponse httpServletResponse) {
		try {
			userService.acceptVerificationToken(token);
			httpServletResponse.setHeader("Location", "index.html#success");
		} catch (VerifyTokenException ex) {
			httpServletResponse.setHeader("Location", "index.html#error");
		}
	}
}
