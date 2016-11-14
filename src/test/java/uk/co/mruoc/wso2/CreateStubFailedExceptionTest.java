package uk.co.mruoc.wso2;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class CreateStubFailedExceptionTest {

    @Test
    public void shouldReturnCause() {
        Throwable cause = new Exception("test exception");

        Throwable exception = new CreateStubFailedException(cause);

        assertThat(exception.getCause()).isEqualTo(cause);
    }

}
