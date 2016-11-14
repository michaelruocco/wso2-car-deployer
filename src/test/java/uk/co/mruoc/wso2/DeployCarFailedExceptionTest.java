package uk.co.mruoc.wso2;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class DeployCarFailedExceptionTest {

    @Test
    public void shouldReturnCause() {
        Throwable cause = new Exception("test exception");

        Throwable exception = new DeployCarFailedException(cause);

        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    public void shouldReturnMessage() {
        String message = "test message";

        Throwable exception = new DeployCarFailedException(message);

        assertThat(exception.getMessage()).isEqualTo(message);
    }
    
}
