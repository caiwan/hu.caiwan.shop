/**
 * Email settings 
 */
package hu.caiwan.shop.config;

import java.util.Properties;

import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
	
	@Profile({"dev", "live"})	
	class MaliLiveConfig{
		private final Logger LOGGER = LoggerFactory.getLogger(getClass());
		
		@Autowired
		private Environment env;
	
		@Bean
		public JavaMailSender javaMailSender() {
			final JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
	
			try {
				mailSenderImpl.setHost(env.getRequiredProperty("smtp.host"));
				mailSenderImpl.setPort(env.getRequiredProperty("smtp.port", Integer.class));
				mailSenderImpl.setProtocol(env.getRequiredProperty("smtp.protocol"));
				mailSenderImpl.setUsername(env.getRequiredProperty("smtp.username"));
				mailSenderImpl.setPassword(env.getRequiredProperty("smtp.password"));
				
				Properties props = new Properties();
				props.setProperty("mail.smtps.auth", "true");
				props.setProperty("mail.smtp.starttls.enable", "true");
				props.setProperty("mail.transport.protocol", "smtps");
				
				
				props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");   
				props.setProperty("mail.smtp.socketFactory.fallback", "false");   
				props.setProperty("mail.smtp.port", "465");   
				props.setProperty("mail.smtp.socketFactory.port", "465"); 
				
				mailSenderImpl.setJavaMailProperties(props);
				
				mailSenderImpl.setDefaultEncoding("UTF-8");
				
			} catch (IllegalStateException ise) {
				LOGGER.error("Could not resolve email.properties.  See email.properties.sample");
				throw ise;
			} catch (NullPointerException ex) {
				LOGGER.error("Got some NPE. Screaming.");
				throw ex;
			}
			final Properties javaMailProps = new Properties();
			javaMailProps.put("mail.smtp.auth", true);
			javaMailProps.put("mail.smtp.starttls.enable", true);
			mailSenderImpl.setJavaMailProperties(javaMailProps);
	
			return mailSenderImpl;
		}
	}
	
	@Configuration
	@Profile("test")	
	class MailDevConfig{
		@Bean
		public JavaMailSender javaMailSender() {
			return Mockito.mock(JavaMailSender.class);
		}
	}
}
