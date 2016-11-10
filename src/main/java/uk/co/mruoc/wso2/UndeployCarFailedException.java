package uk.co.mruoc.wso2;

public class UndeployCarFailedException extends RuntimeException {

    public UndeployCarFailedException(String message) {
        super(message);
    }

    public UndeployCarFailedException(Throwable cause) {
        super(cause);
    }

}
