package hu.caiwan.shop.service.aspects;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hu.caiwan.shop.persist.dao.LogMessageRepository;
import hu.caiwan.shop.persist.model.LogMessage;
import hu.caiwan.shop.persist.type.LogEntryType;
import hu.caiwan.shop.service.UserService;

/**
 * An aspect for every controller to keep track activity of users
 * @author caiwan
 *
 */
@Aspect
@Component
public class ActivityLoggerAspect {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());

	@Autowired
	private LogMessageRepository repository;

	@Autowired
	private UserService userService;
	
	@Autowired 
	private HttpServletRequest request;
	

	@Pointcut("execution (* hu.caiwan.shop.controller.*.*(..))")
	public void logEntry() {
	}

	@After("logEntry()")
	public void logEndpointActivity(JoinPoint joinPoint) {
		String client = userService.getClientHash();
		String message = String.format("Endpoint access %s in mehod %s", request.getRequestURI(), joinPoint.getSignature());

		LOGGER.info(client + " " + message);

		final LogMessage logEntry = new LogMessage(client, LogEntryType.ACCESS, message);
		repository.save(logEntry);
	}
}
