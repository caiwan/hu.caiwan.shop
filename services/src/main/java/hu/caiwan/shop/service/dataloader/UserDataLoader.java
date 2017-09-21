package hu.caiwan.shop.service.dataloader;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import hu.caiwan.shop.persist.dao.RoleRepository;
import hu.caiwan.shop.persist.dao.UserRepository;
import hu.caiwan.shop.persist.model.Role;
import hu.caiwan.shop.persist.model.User;

@Component
@Profile({"dev", "test"})
public class UserDataLoader implements ApplicationListener<ContextRefreshedEvent>{

    private boolean alreadySetup = false;

    @Autowired 
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleDataLoader dataLoader;

	public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        
        dataLoader.onApplicationEvent(event);

        final Role userRole = roleRepository.findByName("ROLE_USER");
        
        final User user = new User();
        
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setPassword(passwordEncoder.encode("test"));
        user.setEmail("test@test.com");
        user.setRoles(Arrays.asList(userRole));
        user.setEnabled(true);
        
        userRepository.save(user);
        
        final Role adminRole = roleRepository.findByName("ROLE_ADMIN");

        final User admin = new User();
        
        admin.setFirstName("Admin");
        admin.setLastName("Admin");
        admin.setPassword(passwordEncoder.encode("test"));
        admin.setEmail("admin@test.com");
        admin.setRoles(Arrays.asList(adminRole));
        admin.setEnabled(true);

        alreadySetup = true;
	}	
	
}
