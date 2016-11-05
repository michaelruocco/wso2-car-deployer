package uk.co.mruoc.wso2;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class CarInfoTest {

    private static final String NAME = "carbon-application-name";
    private static final String VERSION = "1.0.0";

    private final CarInfo info = new CarInfo(NAME, VERSION);

    @Test
    public void shouldReturnName() {
        assertThat(info.getName()).isEqualTo(NAME);
    }

    @Test
    public void shouldReturnVersion() {
        assertThat(info.getVersion()).isEqualTo(VERSION);
    }

    @Test
    public void shouldReturnFullName() {
        assertThat(info.getFullName()).isEqualTo(NAME + "_" + VERSION);
    }

}
