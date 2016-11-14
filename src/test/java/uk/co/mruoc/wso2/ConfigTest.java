package uk.co.mruoc.wso2;

import org.junit.Test;
import uk.co.mruoc.wso2.Config.ConfigBuilder;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ConfigTest {

    @Test
    public void shouldReturnUrl() {
        String url = "http://localhost:9443/";

        Config config = new ConfigBuilder().setUrl(url).build();

        assertThat(config.getUrl()).isEqualTo(url);
    }

    @Test
    public void shouldReturnUsername() {
        String username = "mruoc";

        Config config = new ConfigBuilder().setUsername(username).build();

        assertThat(config.getUsername()).isEqualTo(username);
    }

    @Test
    public void shouldReturnPassword() {
        String password = "Pa$$worD";

        Config config = new ConfigBuilder().setPassword(password).build();

        assertThat(config.getPassword()).isEqualTo(password);
    }

}
