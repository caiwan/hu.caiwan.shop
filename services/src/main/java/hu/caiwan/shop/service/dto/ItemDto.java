package hu.caiwan.shop.service.dto;

import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.NotEmpty;


@XmlRootElement(name = "item")
public class ItemDto {

	@XmlTransient
	private Long id;
	
	@NotEmpty
	private String name;
	@NotEmpty
	private String description;
	@Min(0)
	private Integer price;
	@Min(0)
	private Integer inStock;
	@Min(0)
	private Integer reservedOrder;
	@Min(0)
	private Integer vat;

	@NotEmpty
	private String inventoryNumber;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getInStock() {
		return inStock;
	}

	public void setInStock(Integer inStock) {
		this.inStock = inStock;
	}

	public Integer getReservedOrder() {
		return reservedOrder;
	}

	public void setReservedOrder(Integer reservedOrder) {
		this.reservedOrder = reservedOrder;
	}

	public String getInventoryNumber() {
		return inventoryNumber;
	}

	public void setInventoryNumber(String inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
	}

	public Integer getVat() {
		return vat;
	}

	public void setVat(Integer vat) {
		this.vat = vat;
	}
	
}
