package hu.caiwan.shop.events;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import hu.caiwan.shop.persist.model.User;

public class OnRegistrationCompleteEvent extends ApplicationEvent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4895847420225794398L;
	private final User user;
	private final Locale locale;

	public OnRegistrationCompleteEvent(Object source, User user, Locale locale) {
		super(source);
		this.user = user;
		this.locale = locale;
	}

	public User getUser() {
		return user;
	}

	public Locale getLocale() {
		return locale;
	}

}
