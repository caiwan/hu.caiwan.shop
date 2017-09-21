package hu.caiwan.shop.it;

import static org.junit.Assert.assertNotNull;

import static org.junit.Assert.assertNotEquals;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import hu.caiwan.shop.config.MailConfig;
import hu.caiwan.shop.config.MvcConfig;
import hu.caiwan.shop.config.ServiceConfig;
import hu.caiwan.shop.config.TestConfig;
import hu.caiwan.shop.config.TestDatabaseConfig;
import hu.caiwan.shop.config.security.AuthServerConfig;
import hu.caiwan.shop.config.security.OAuthConfig;
import hu.caiwan.shop.config.security.ResourceServerConfig;
import hu.caiwan.shop.pdf.OrderToPDF;
import hu.caiwan.shop.pdf.PDFGenerator;
import hu.caiwan.shop.persist.model.Address;
import hu.caiwan.shop.persist.model.Item;
import hu.caiwan.shop.persist.model.Order;
import hu.caiwan.shop.persist.model.OrderItem;
import hu.caiwan.shop.service.dto.OrderDto;
import hu.caiwan.shop.service.mapper.OrderMapper;

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
public class OrderPDFExportIntegrationTest {

	private Order order;
	 
	@Autowired
	private OrderMapper orderMapper;

	@Before
	public void setup() {
		order = new Order();

		Address addr1 = new Address();
		addr1.setCountry("United States");
		addr1.setProvince("NY");
		addr1.setCity("Merrick");
		addr1.setPostalcode("11566");
		addr1.setAddress1("64 Whitemarsh Drive");

		Address addr2 = new Address();
		addr2.setCountry("United States");
		addr2.setProvince("NC");
		addr2.setCity("Clemmons");
		addr2.setPostalcode("27012");
		addr2.setAddress1("65 Wood Avenue ");

		Item item1 = new Item();
		item1.setName("First item");
		item1.setDescription("Desctipt1");
		item1.setPrice(900);
		item1.setInventoryNumber("ITEM001");

		Item item2 = new Item();
		item2.setName("Second Item");
		item2.setDescription("DESCRIPT2");
		item2.setPrice(1200);
		item2.setInventoryNumber("ITEM002");

		OrderItem orderItem1 = new OrderItem(item1, 3);
		OrderItem orderItem2 = new OrderItem(item2, 5);

		order.addOrderItem(orderItem1);
		order.addOrderItem(orderItem2);

		order.setShippingAddress(addr1);
		order.setBillingAddress(addr2);

	}

	@After
	public void teardown() {
		order = null;
	}

	@Test
	public void given_order_when_export_then_success() throws Exception {
		// Given
		OrderDto orderDto = orderMapper.toDto(order);
		PDFGenerator pdfgen = new OrderToPDF();

		
		// When
		pdfgen.generate(orderDto, this.getClass().getResource("/xsl/pdfLayouts/order.xslt"));
//		pdfgen.generate();

		// Then
		String xmlDoc = pdfgen.getDocument();
		assertNotNull(xmlDoc);
		
//		System.out.println(xmlDoc);

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		pdfgen.writePdf(os);
		byte[] ba = os.toByteArray();

		assertNotNull(ba);
		assertNotEquals(0, ba.length);

		OutputStream fos = new FileOutputStream("/Users/caiwan/lol.pdf");
		fos.write(ba);
		fos.close();
	}
}
