package hu.caiwan.shop.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import hu.caiwan.shop.service.dto.GenericResponse;
//import hu.caiwan.utils.MessageSource;

@ControllerAdvice
public class ExceptionHandlerController {

	// @Autowired
	// protected MessageSource messageSource;

	@ExceptionHandler
	@ResponseBody
	public ResponseEntity<Collection<GenericResponse>> handleValidationError(
			final MethodArgumentNotValidException exception, final HttpServletRequest request) 
	{
		final List<GenericResponse> response = new ArrayList<>();

		final BindingResult result = exception.getBindingResult();
		for (final FieldError e : result.getFieldErrors()) {
			response.add(new GenericResponse(e.getField(), e.getDefaultMessage()));
		}
		return new ResponseEntity<Collection<GenericResponse>>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	@ResponseBody
	public ResponseEntity<Collection<GenericResponse>> handleError(final HttpServletRequest req,
			final Exception exception) {
	
		exception.printStackTrace();
		
		final List<GenericResponse> response = new ArrayList<>();
		response.add(new GenericResponse(exception.getClass().getName(), exception.getMessage()));
		return new ResponseEntity<Collection<GenericResponse>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
