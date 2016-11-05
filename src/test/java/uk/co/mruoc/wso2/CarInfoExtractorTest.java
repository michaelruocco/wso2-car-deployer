package uk.co.mruoc.wso2;

import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class CarInfoExtractorTest {

    private final CarInfoExtractor extractor = new CarInfoExtractor();

    private final File jsonValidatorCar = new File("test/json-validator-mediator-config-local-1.0.0-SNAPSHOT.car");

    @Test
    public void shouldExtractCarInfoFromJsonValidatorCarFile() {
        CarInfo info = extractor.extract(jsonValidatorCar);

        assertThat(info.getName()).isEqualTo("json-validator-mediator-config-local");
        assertThat(info.getVersion()).isEqualTo("1.0.0-SNAPSHOT");
    }

}
