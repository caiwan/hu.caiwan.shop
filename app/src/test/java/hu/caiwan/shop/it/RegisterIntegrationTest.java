package hu.caiwan.shop.it;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import hu.caiwan.shop.config.MailConfig;
import hu.caiwan.shop.config.MvcConfig;
import hu.caiwan.shop.config.ServiceConfig;
import hu.caiwan.shop.config.TestConfig;
import hu.caiwan.shop.config.TestDatabaseConfig;
import hu.caiwan.shop.config.security.AuthServerConfig;
import hu.caiwan.shop.config.security.OAuthConfig;
import hu.caiwan.shop.config.security.ResourceServerConfig;
import hu.caiwan.shop.events.OnRegistrationCompleteEvent;
import hu.caiwan.shop.listeners.RegistrationListenerWrapper;
import hu.caiwan.shop.persist.dao.VerificationTokenRepository;
import hu.caiwan.shop.persist.model.User;
import hu.caiwan.shop.persist.model.VerificationToken;
import hu.caiwan.shop.service.UserService;
import hu.caiwan.shop.service.dto.UserDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { 
		TestConfig.class, TestDatabaseConfig.class, 
		ServiceConfig.class, MailConfig.class,
		MvcConfig.class,
		AuthServerConfig.class, OAuthConfig.class, ResourceServerConfig.class 
	})
@ActiveProfiles("test")
@Transactional
public class RegisterIntegrationTest {
	
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    private UserService userService;

    @Autowired 
    private VerificationTokenRepository tokenRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    private MockMvc mockMvc;
    
    @Autowired
    private RegistrationListenerWrapper registrationListener;
	
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
    
	protected String json(Object o) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }
    
	@Before
	public void setup() {
		
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
//        		.apply(OAuth2ContextSetup.standard(clientHolder))
        		.build();

        User user = new User();
        user.setEmail(UUID.randomUUID().toString() + "@example.com");
        user.setPassword(UUID.randomUUID().toString());
        user.setFirstName("First");
        user.setLastName("Last");

        entityManager.persist(user);

        /*
            flush managed entities to the database to populate identifier field
         */
        entityManager.flush();
        entityManager.clear();
	}

	@Test
	public void given_noUser_whenRegister_then_success() throws Exception {
		String username = UUID.randomUUID().toString();
		String password = UUID.randomUUID().toString();
		String email = UUID.randomUUID().toString() + "@xxx.com";
		
		UserDto user = new UserDto(0L, username, password, password, email);
		
		//@formatter:off
		ResultActions resultActions = this.mockMvc.perform(
			post("/signup/").content(json(user)).contentType(contentType)
		);
		
		resultActions.andExpect(jsonPath("$.message", is("success")));
		//@formatter:on
	}
	
	/**
	 * Email confirm tokens
	 */
	
    @Test
    public void given_registredUser_when_generateToken_then_success() throws Exception {
    	// given 
		String username = UUID.randomUUID().toString();
		String password = UUID.randomUUID().toString();
		String email = UUID.randomUUID().toString() + "@xxx.com";
		
		UserDto dto = new UserDto(0L, username, password, password, email);
		User user = userService.registerUser(dto);
		OnRegistrationCompleteEvent event = new OnRegistrationCompleteEvent(this, user, Locale.ENGLISH);

		//when
		registrationListener.onApplicationEvent(event);
		
		//then 
		VerificationToken token = tokenRepository.findByUser(user);
		
		Assert.assertNotNull(token);
		Assert.assertEquals(user, token.getUser());
		Assert.assertNotNull(token.getToken());
		Assert.assertFalse(token.getToken().isEmpty());
    }

    @Test
    public void given_registredUser_when_generateDuplicateTokenForSameUser_then_fail() throws Exception {
    	// given 
		String username = UUID.randomUUID().toString();
		String password = UUID.randomUUID().toString();
		String email = UUID.randomUUID().toString() + "@xxx.com";
		
		UserDto dto = new UserDto(0L, username, password, password, email);
		User user = userService.registerUser(dto);
		
		OnRegistrationCompleteEvent event0 = new OnRegistrationCompleteEvent(this, user, Locale.ENGLISH);
		OnRegistrationCompleteEvent event1 = new OnRegistrationCompleteEvent(this, user, Locale.ENGLISH);

		//when
		registrationListener.onApplicationEvent(event0);
		try {
			registrationListener.onApplicationEvent(event1);
			Assert.fail();
		} catch(Exception e){
			// then
			Assert.assertNotNull(e);
		}
    }
    
    /**
     * Confirm and accept email tokens 
     * @throws Exception 
     */
    
    @Test 
    public void given_validConfirmationToken_when_accept_then_success() throws Exception{
    	// given 
		String username = UUID.randomUUID().toString();
		String password = UUID.randomUUID().toString();
		String email = UUID.randomUUID().toString() + "@xxx.com";
		
		UserDto dto = new UserDto(0L, username, password, password, email);
		User user = userService.registerUser(dto);
		
		OnRegistrationCompleteEvent event1 = new OnRegistrationCompleteEvent(this, user, Locale.ENGLISH);
		registrationListener.onApplicationEvent(event1);
		
		VerificationToken token = tokenRepository.findByUser(user);
		
		String validUUID = token.getToken();
		
		// when 

		// @formatter:off
		this.mockMvc.perform(
			get("/validate/" + validUUID)
		)
		.andExpect(
			redirectedUrl("index.html#success")
		);
		// @formatter:on
		
//		resultActions.andExpect(ok());
    }
    
    
    @Test 
    public void given_invalidConfirmationToken_when_accept_then_fail() throws Exception{
    	// given 
		String invalidUUID = UUID.randomUUID().toString();
		
		// when 
		
		// @formatter:off
		this.mockMvc.perform(
			get("/validate/" + invalidUUID)
		)
		//then
		.andExpect(
			redirectedUrl("index.html#error")
		);
		// @formatter:on
		
		// check DB 
    }
}
