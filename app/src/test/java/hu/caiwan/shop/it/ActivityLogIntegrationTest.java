package hu.caiwan.shop.it;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.assertj.core.api.AssertDelegateTarget;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import hu.caiwan.shop.config.MailConfig;
import hu.caiwan.shop.config.MvcConfig;
import hu.caiwan.shop.config.ServiceConfig;
import hu.caiwan.shop.config.TestConfig;
import hu.caiwan.shop.config.TestDatabaseConfig;
import hu.caiwan.shop.config.security.AuthServerConfig;
import hu.caiwan.shop.config.security.OAuthConfig;
import hu.caiwan.shop.config.security.ResourceServerConfig;
import hu.caiwan.shop.persist.dao.LogMessageRepository;
import hu.caiwan.shop.persist.model.LogMessage;
import hu.caiwan.shop.test.fixtures.UserFixtures;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class, TestDatabaseConfig.class, MailConfig.class, ServiceConfig.class, MvcConfig.class,
		AuthServerConfig.class, OAuthConfig.class, ResourceServerConfig.class })
@Transactional
@WithMockUser("test@test.com")
public class ActivityLogIntegrationTest {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private UserFixtures userFixtures;

	private MockMvc mvc;

	@Autowired
	private LogMessageRepository logRepository;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
		userFixtures.addDummyUser();
	}

	@Test
	public void given_anEndpoint_when_access_then_logged() throws Exception {
		//@formatter:off
		mvc
		.perform(get("/api/user/"))
    	.andExpect(status().isOk());
		
		assertNotEquals(0, logRepository.count());
		
		LogMessage message = logRepository.findAll().get(0);
		assertNotNull(message);
		
		assertNotEquals(0, message.getClientId().length());
		assertEquals("Endpoint access /api/user/ in mehod Collection hu.caiwan.shop.controller.UserController.getUserInfo()", message.getMessage());
		
		//@formatter:on
	}

}
