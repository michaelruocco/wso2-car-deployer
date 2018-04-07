package uk.co.mruoc.wso2;

import org.junit.Test;

import java.net.MalformedURLException;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class UrlConverterTest {

    private final UrlConverter converter = new UrlConverter();

    @Test
    public void shouldConvertToHost() {
        String url = "https://localhost:9443";

        String host = converter.toHost(url);

        assertThat(host).isEqualTo("localhost");
    }

    @Test
    public void shouldThrowExceptionIfUrlIsMalformed() {
        String malformedUrl = "daskdlaskdla";

        Throwable thrown = catchThrowable(() -> converter.toHost(malformedUrl));

        assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class)
                .hasCauseInstanceOf(MalformedURLException.class);
    }

}
