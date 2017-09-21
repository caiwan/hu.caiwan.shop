package hu.caiwan.shop.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import hu.caiwan.shop.events.OnRegistrationCompleteEvent;
import hu.caiwan.shop.persist.model.User;
import hu.caiwan.shop.persist.model.VerificationToken;
import hu.caiwan.shop.service.UserService;

@Service
public class SendConfirmEmailTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(SendConfirmEmailTask.class);

	@Autowired
	private UserService service;

	@Autowired
	private MessageSource messages;

	@Autowired
	private Environment env;

	@Autowired
	private JavaMailSender mailSender;

	@Configurable
	private class EmailSenderWorker implements Runnable {
		
		private SimpleMailMessage message;

		public EmailSenderWorker(SimpleMailMessage message) {
			super();
			this.message = message;
		}

		public void run() {
			mailSender.send(message);
			LOGGER.info("Confirm mail sent to" + message.getTo());
		}
	}

	@Autowired
	private TaskExecutor taskExecutor;

	public void confirmRegistration(final OnRegistrationCompleteEvent event, VerificationToken token) {
		final SimpleMailMessage email = constructEmailMessage(event, event.getUser(), token.getToken());
		taskExecutor.execute(new EmailSenderWorker(email));
	}

	//

	private final SimpleMailMessage constructEmailMessage(final OnRegistrationCompleteEvent event, final User user,
			final String token) {
		if (token == null)
			return null;
		if (token.isEmpty())
			return null;

		final String recipientAddress = user.getEmail();
		final String subject = "Registration Confirmation";
		final String confirmationUrl = /* event.getAppUrl() + */ "/user/registrationConfirm?token=" + token;
		final String message = "Please confirm your registration:"; // messages.getMessage("message.regSucc",
																	// null,
																	// event.getLocale());

		final SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setText(message + " \r\n" + confirmationUrl);
		email.setFrom(env.getProperty("support.email"));

		return email;
	}
}
