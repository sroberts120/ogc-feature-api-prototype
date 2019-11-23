package co.uk.ordnancesurvey.exceptions;

/**
 * Custom exception used when there is an incorrect resquest response type. The
 * service being developed only supports application/json.
 * 
 * All other Accepts types requested should return this error.
 * 
 * The error has been built to fit the WFS3 error response specification.
 * 
 * @author sroberts
 *
 */
public class InvalidAcceptsTypeException extends RuntimeException {

	/**
	 * Contructor for invalid Accepts type
	 * 
	 * @param acceptsType
	 */
	public InvalidAcceptsTypeException(String acceptsType) {
		super("Invalid accepts type : " + acceptsType + " : Only Application/json supported");
	}

}
