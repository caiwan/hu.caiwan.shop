package hu.caiwan.shop.service.dto;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.validator.constraints.NotEmpty;

import hu.caiwan.utils.DateTimeAdapter;

@XmlRootElement(name = "order")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class OrderDto {

	private Integer id;

	@NotNull
	private AddressDto shippingAddress;

	@NotNull
	private AddressDto billingAddress;

	@NotEmpty
	private List<OrderItemDto> orderedItems = new LinkedList<>();

	@NotNull
	private Date orderDate = new Date();

	@XmlTransient
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@XmlElement(name = "shipping")
	public AddressDto getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(AddressDto shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	@XmlElement(name = "billing")
	public AddressDto getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(AddressDto billingAddress) {
		this.billingAddress = billingAddress;
	}

	public List<OrderItemDto> getOrderedItems() {
		return orderedItems;
	}

	@XmlElementWrapper(name = "orderList")
	@XmlElement(name = "orderItem")
	public void setOrderedItems(List<OrderItemDto> orderedItems) {
		this.orderedItems = orderedItems;
	}

	@XmlJavaTypeAdapter(DateTimeAdapter.class)
	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

}
