package hu.caiwan.shop.service.dto;

/**
 * This DTO used to pass generic messages
 */

public class GenericResponse {

	private String message;
	private String error;

	public GenericResponse(String message) {
		super();
		this.message = message;
	}

	public GenericResponse(String message, String error) {
		super();
		this.message = message;
		this.error = error;
	}

	public GenericResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
