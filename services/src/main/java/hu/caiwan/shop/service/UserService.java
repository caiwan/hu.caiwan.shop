package hu.caiwan.shop.service;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import hu.caiwan.shop.exceptions.VerifyTokenException;
import hu.caiwan.shop.persist.dao.RoleRepository;
import hu.caiwan.shop.persist.dao.UserRepository;
import hu.caiwan.shop.persist.dao.VerificationTokenRepository;
import hu.caiwan.shop.persist.model.Role;
import hu.caiwan.shop.persist.model.User;
import hu.caiwan.shop.persist.model.VerificationToken;
import hu.caiwan.shop.persist.type.RoleType;
import hu.caiwan.shop.service.dto.UserDto;
import hu.caiwan.shop.service.dto.UserProfileDto;
import hu.caiwan.shop.service.mapper.UserMapper;
import hu.caiwan.shop.service.mapper.UserPrivilegeMapper;
import hu.caiwan.utils.Hash;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepositopry;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private VerificationTokenRepository tokenRepository;
	
	@Autowired 
	private UserMapper userMapper;
	
	@Autowired
	private UserPrivilegeMapper privilegeMapper; 
		
	/******************************************************************************
	 * API calls 
	 ******************************************************************************/
	
	/**
	 * Creates a new user and persist to database
	 * @param dto user dto object
	 * @return new user entity
	 */
	public User registerUser(UserDto dto){
		if (isEmailExist(dto.getEmail())){
			// TODO exception
			throw new RuntimeException("User with such email is already exists");
		}
		
		final User user = userMapper.fromDto(dto);
		final Role role = roleRepository.findByName(RoleType.ROLE_USER.name()); 
		user.setRoles(Arrays.asList(role));
		
		user.setEnabled(false);
		user.setLocked(false);
		
		return userRepositopry.save(user);
	}  
	
	/**
	 * Creates a new verification token for user and persist it  
	 * @return new verification token entity
	 */
	public VerificationToken registerVerificationToken(User user){
		final VerificationToken dupeToken = tokenRepository.findByUser(user); 
		if (dupeToken != null)
			throw new RuntimeException("Duplicated token");
		
		final VerificationToken token = new VerificationToken();
		token.setValid(true);
		token.setUser(user);
		token.updateToken(UUID.randomUUID().toString());
		
		tokenRepository.save(token);
		
		return token;
	}
	
	public void acceptVerificationToken(String tokenString){
		final VerificationToken token = tokenRepository.findByToken(tokenString);
		if (token == null)
			throw new VerifyTokenException("No such token");
		
		if (!token.getValid())
			throw new VerifyTokenException("Invlalid token");
		
		if (!token.getExpiration().after(new Date()))
			throw new VerifyTokenException("Token expired");
		
		final User user = token.getUser();

		if (user.getLocked())
			throw new VerifyTokenException("User already locked");
		
		if (user.getEnabled())
			throw new VerifyTokenException("User already enabled");
		
		token.setValid(false);
		user.setEnabled(true);
		
		tokenRepository.save(token);
		userRepositopry.save(user);
	}
	
	/**
	 * @return priviliges and roles assigned to the current user 
	 */
	public UserProfileDto getUserPrivileges(){
		final User user = userRepositopry.findByEmail(getCurrenentUserName());
		return privilegeMapper.toDto(user);
	}
	
	/******************************************************************************
	 * Non-API calls  
	 ******************************************************************************/
	
	/**
	 * 
	 * @return
	 */
	private String getCurrenentUserName(){
	      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	      if (auth == null)
	    	  return null;
	      String name = auth.getName(); //get logged in username
	      return name;
	}
	
	/**
	 * @return database entity of currently logged in user
	 */
	public User getCurrentUser(){
		final String name = getCurrenentUserName();
		if (name == null) 
			return null;
		final User user = userRepositopry.findByEmail(name);
		return user;
	}
	
	/**
	 * Check of existing email
	 * @param email
	 * @return
	 */
	private boolean isEmailExist(final String email) {
		final User user = userRepositopry.findByEmail(email);
		if (user != null) {
			return true;
		}
		return false;
	}
	
	private @Autowired HttpServletRequest request;
	
	/**
	 * Gets the client IP address
	 * @return
	 */
	public String getClientIP(){
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null)
			return request.getRemoteHost();
		else if (ip.isEmpty())
			return request.getRemoteHost();
		return ip;
	}
	
	/**
	 * Hash of client ID, to identify user without having an exact user information
	 * - ip hash when not registered
	 * - user id when registered
	 * @return salted hash of client id
	 */
	public String getClientHash(){
		String hash;
		final User user = getCurrentUser();
		if (user == null)
			hash = getClientIP();
		else 
			hash = user.getEmail() + user.getId().toString();
		
		// TODO: move this decent hash out to a conf. file
		return Hash.hashSha512(hash, "q69dnTKDNqONmflxOEzwAYj7tSoL2dpLKrR9kv10QY9de54tdEFCr78rryzDOTPKfYKVFCh6DIFAZxbpJm8WP6AJV81Hm67kjOaK", 1, 256);
	}

	
}
