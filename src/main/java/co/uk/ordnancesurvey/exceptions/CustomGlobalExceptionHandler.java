package co.uk.ordnancesurvey.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class should intercept excpetions and set the correct response codes
 * 
 * By default responses thrown by spring at 500's when there is possiblity for
 * multiple codes that would be valid returns
 *
 */
@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

	
	/**
	 * Let Spring BasicErrorController handle the exception, we just override the status code
	 * @param response
	 * @throws IOException
	 */
	@ExceptionHandler(InvalidAcceptsTypeException.class)
	public void springHandleNotFound(HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

}