package hu.caiwan.shop.persist.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import hu.caiwan.shop.persist.type.PaymentMethodType;

@Embeddable
public class OrderStatus {

	private Boolean isOutOfStock;
	private Boolean isCancelled;
	private Boolean isPayed;
	
	@Enumerated(EnumType.ORDINAL)
	private PaymentMethodType paymentMethod;
	
	private Boolean isShipped;
	private Boolean isArrived;

	@Column(length = 4096)
	private String notes;
	
	@Column(length = 4096)
	private String trackingInfo;

	public Boolean getIsOutOfStock() {
		return isOutOfStock;
	}

	public void setIsOutOfStock(Boolean isOutOfStock) {
		this.isOutOfStock = isOutOfStock;
	}

	public Boolean getIsCancelled() {
		return isCancelled;
	}

	public void setIsCancelled(Boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	public Boolean getIsPayed() {
		return isPayed;
	}

	public void setIsPayed(Boolean isPayed) {
		this.isPayed = isPayed;
	}

	public PaymentMethodType getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethodType paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Boolean getIsShipped() {
		return isShipped;
	}

	public void setIsShipped(Boolean isShipped) {
		this.isShipped = isShipped;
	}

	public Boolean getIsArrived() {
		return isArrived;
	}

	public void setIsArrived(Boolean isArrived) {
		this.isArrived = isArrived;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getTrackingInfo() {
		return trackingInfo;
	}

	public void setTrackingInfo(String trackingInfo) {
		this.trackingInfo = trackingInfo;
	}

	

}
