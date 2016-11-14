package uk.co.mruoc.wso2;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class WaitFailedExceptionTest {

    @Test
    public void shouldReturnCause() {
        Throwable cause = new Exception("test exception");

        Throwable exception = new WaitFailedException(cause);

        assertThat(exception.getCause()).isEqualTo(cause);
    }

}
