package uk.co.mruoc.wso2;

public class AuthenticationFailedException extends RuntimeException {

    public AuthenticationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

}
