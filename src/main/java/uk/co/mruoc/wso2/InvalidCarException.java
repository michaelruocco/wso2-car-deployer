package uk.co.mruoc.wso2;

public class InvalidCarException extends RuntimeException {

    public InvalidCarException(String message) {
        super(message);
    }

    public InvalidCarException(String message, Throwable cause) {
        super(message, cause);
    }

}
