package uk.co.mruoc.wso2;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class UndeployCarFailedExceptionTest {

    @Test
    public void shouldReturnCause() {
        Throwable cause = new Exception("test exception");

        Throwable exception = new UndeployCarFailedException(cause);

        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    public void shouldReturnMessage() {
        String message = "test message";

        Throwable exception = new UndeployCarFailedException(message);

        assertThat(exception.getMessage()).isEqualTo(message);
    }
    
}
