package co.uk.ordnancesurvey.exceptions;

public class InvalidAcceptsTypeException extends RuntimeException {

    public InvalidAcceptsTypeException(String acceptsType) {
        super("Invalid accepts type : " + acceptsType + " : Only Application/json supported");
    }

}
