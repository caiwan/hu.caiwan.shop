package hu.caiwan.shop.service.aspects.access;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hu.caiwan.shop.persist.model.User;
import hu.caiwan.shop.persist.type.RoleType;
import hu.caiwan.shop.service.UserService;

/**
 * Filters endpoints by user access levels
 * @author caiwan
 *
 */

@Aspect
@Component
public class SecurityAccessFilter {
	
	@Autowired
	private UserService userService;

	@Pointcut("execution (* hu.caiwan.shop.controller.*.*(..))")
	public void endpoints() {
	}
	
	@Pointcut("@annotation(hu.caiwan.shop.service.aspects.access.AdminOnly)")
	public void adminAnnotation(){
	}
	
	@Before("endpoints() && adminAnnotation()")
	public void decorateAdminOnlyAccess(){
		final User user = userService.getCurrentUser();
		if(user == null)
			throw new RuntimeException("Access for admin only!");
		
		if (! user.getRoles().contains(RoleType.ROLE_ADMIN.name()))
			throw new RuntimeException("Access for admin only!");
	}
	
}
