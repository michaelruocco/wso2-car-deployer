package uk.co.mruoc.wso2;

public class DeployCarFailedException extends RuntimeException {

    public DeployCarFailedException(String message) {
        super(message);
    }

    public DeployCarFailedException(Throwable cause) {
        super(cause);
    }

}
