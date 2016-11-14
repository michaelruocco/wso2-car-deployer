package uk.co.mruoc.wso2;

import org.junit.Test;

import java.net.MalformedURLException;

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static com.googlecode.catchexception.apis.BDDCatchException.when;
import static org.assertj.core.api.BDDAssertions.then;
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

        when(converter).toHost(malformedUrl);

        then(caughtException())
                .isInstanceOf(IllegalArgumentException.class)
                .hasCauseInstanceOf(MalformedURLException.class);
    }

}
