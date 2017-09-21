package hu.caiwan.shop.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import hu.caiwan.shop.events.OnRegistrationCompleteEvent;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

	private static Logger LOGGER = LoggerFactory.getLogger(RegistrationListener.class.getName());

	public RegistrationListener() {
		LOGGER.info("kljasbdv");
	}

	@Autowired
	RegistrationListenerWrapper wrapper;

	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		wrapper.onApplicationEvent(event);
		LOGGER.info("peniseses");
	}

}
