package hu.caiwan.shop.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import hu.caiwan.shop.persist.model.Address;
import hu.caiwan.shop.persist.model.Item;
import hu.caiwan.shop.persist.model.Order;
import hu.caiwan.shop.persist.model.OrderItem;

public class OrderToPDF extends PDFGenerator {

	private Order order;

	public OrderToPDF() {
		super();
	}

	public OrderToPDF(Order order) {
		this.order = order;
	}

	protected void generateAddress(Document document, Element rootElement, Address address) {
		Element addrElement = document.createElement("address");
		rootElement.appendChild(addrElement);

		Element countryElement = document.createElement("country");
		countryElement.setTextContent(address.getCountry());
		addrElement.appendChild(countryElement);

		Element cityElement = document.createElement("city");
		cityElement.setTextContent(address.getCity());
		addrElement.appendChild(cityElement);

		Element provinceElement = document.createElement("province");
		provinceElement.setTextContent(address.getProvince());
		addrElement.appendChild(provinceElement);

		Element address1Element = document.createElement("address1");
		address1Element.setTextContent(address.getAddress1());
		addrElement.appendChild(address1Element);

		Element address2Elelemt = document.createElement("address2");
		address2Elelemt.setTextContent(address.getAddress2());
		addrElement.appendChild(address2Elelemt);

		Element postalCodeElement = document.createElement("postalCode");
		postalCodeElement.setTextContent(address.getPostalcode());
		addrElement.appendChild(postalCodeElement);
	}

	protected void generateOrderItemElement(Document document, Element orderItemElement, OrderItem orderItem) {
		Element quantityElement = document.createElement("quantity");
		orderItemElement.appendChild(quantityElement);
		quantityElement.setTextContent(orderItem.getQuantity().toString());

		Element itemElement = document.createElement("item");
		generateItemElement(document, itemElement, orderItem.getItem());
		orderItemElement.appendChild(itemElement);
	}

	protected void generateItemElement(Document document, Element itemElement, Item item) {
		Element nameElement = document.createElement("name");
		nameElement.setTextContent(item.getName());
		itemElement.appendChild(nameElement);

		Element descriptionElement = document.createElement("desctiption");
		itemElement.appendChild(descriptionElement);
		descriptionElement.setTextContent(item.getDescription());

		Element priceElement = document.createElement("price");
		itemElement.appendChild(priceElement);
		priceElement.setTextContent(item.getPrice().toString());

		Element inventoryNumberElement = document.createElement("inventoryNumber");
		itemElement.appendChild(inventoryNumberElement);
		inventoryNumberElement.setTextContent(item.getInventoryNumber());
	}

	@Override
	public Document generateDocument() {
		Document document = null;

		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); 
		df.setTimeZone(tz);

		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			docFactory.setNamespaceAware(true);
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			document = docBuilder.newDocument();
			Element rootElement = document.createElement("order");
			document.appendChild(rootElement);

			Element dateElement = document.createElement("date");
			dateElement.setTextContent(df.format(order.getOrderDate()).toString());
			rootElement.appendChild(dateElement);

			Element billingAddressElement = document.createElement("billing");
			generateAddress(document, billingAddressElement, order.getBillingAddress());
			rootElement.appendChild(billingAddressElement);

			Element shippingAddressElement = document.createElement("shipping");
			generateAddress(document, shippingAddressElement, order.getShippingAddress());
			rootElement.appendChild(shippingAddressElement);

			// Order
			// Element orderListElement = document.createElement("orders");
			// rootElement.appendChild(orderListElement);
			for (OrderItem orderItem : order.getOrderedItems()) {
				Element orderElement = document.createElement("ordered-item");
				generateOrderItemElement(document, orderElement, orderItem);
				rootElement.appendChild(orderElement);
			}

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		}

		return document;
	}

	@Override
	Source getXslt() throws IOException {
		InputStream in;
		URL xsltURL = this.getClass().getResource("/xsl/pdfLayouts/order.xslt");
		in = xsltURL.openStream();

		StreamSource ss = new StreamSource(in);
		return ss;
	}

}
