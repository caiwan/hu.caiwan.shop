package hu.caiwan.shop.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.caiwan.shop.events.OnRegistrationCompleteEvent;
import hu.caiwan.shop.persist.model.VerificationToken;
import hu.caiwan.shop.service.UserService;
import hu.caiwan.shop.tasks.SendConfirmEmailTask;

@Service
public class RegistrationListenerWrapper {

	private static Logger LOGGER = LoggerFactory.getLogger(RegistrationListenerWrapper.class);

	@Autowired
	private UserService userService;

	@Autowired
	private SendConfirmEmailTask sendEmailTask;

	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		VerificationToken token = userService.registerVerificationToken(event.getUser());
		if (token != null)
			sendEmailTask.confirmRegistration(event, token);
		else
			throw new NullPointerException("Token can not be null");
	}
}
