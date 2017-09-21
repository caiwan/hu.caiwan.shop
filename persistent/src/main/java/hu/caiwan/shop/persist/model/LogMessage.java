package hu.caiwan.shop.persist.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import hu.caiwan.shop.persist.type.LogEntryType;

/**
 * Keeps an audit log for every acton has taken
 * @author caiwan
 */

@Entity
public class LogMessage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date occurDate = new Date();
	
	private String clientId;
	
	@Enumerated(EnumType.ORDINAL)
	private LogEntryType type;
	
	@Lob
	@Column(length=10000)
	private String message;

	
	public LogMessage() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public LogMessage(String clientId, LogEntryType type, String message) {
		super();
		this.clientId = clientId;
		this.type = type;
		this.message = message;
		this.occurDate = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getOccurDate() {
		return occurDate;
	}

	public void setOccurDate(Date occurDate) {
		this.occurDate = occurDate;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public LogEntryType getType() {
		return type;
	}

	public void setType(LogEntryType type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
