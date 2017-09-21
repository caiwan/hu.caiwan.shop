package hu.caiwan.shop.service.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.caiwan.shop.persist.dao.UserRepository;
import hu.caiwan.shop.persist.model.Privilege;
import hu.caiwan.shop.persist.model.Role;
import hu.caiwan.shop.persist.model.User;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		LOGGER.info("Login attempt: " + email);
		try {
        	
            final User user = userRepository.findByEmail(email);
            
            if (user == null) {
                throw new UsernameNotFoundException("No user found with username: " + email);
            }
            
//            LOGGER.info("Found user: " + user.getId());
            
            //@formatter:off
            return new org.springframework.security.core.userdetails.User(
            		user.getEmail(),                 // String username,       
					user.getPassword(),              // String password, 
            		user.getEnabled(),               // boolean enabled, 
            		true,                            // boolean accountNonExpired, 
            		true,                            // boolean credentialsNonExpired, 
            		!user.getLocked(),             // boolean accountNonLocked, 
            		getAuthorities(user.getRoles())  // Collection<? extends GrantedAuthority> authorities
            	);
            //@formatter:off
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
	}

    public final Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private final List<String> getPrivileges(final Collection<Role> roles) {
        final List<String> privileges = new ArrayList<String>();
        final List<Privilege> collection = new ArrayList<Privilege>();
        for (final Role role : roles) {
            collection.addAll(role.getPrivileges());
        }
        for (final Privilege item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    private final List<GrantedAuthority> getGrantedAuthorities(final List<String> privileges) {
        final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (final String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
	
}
