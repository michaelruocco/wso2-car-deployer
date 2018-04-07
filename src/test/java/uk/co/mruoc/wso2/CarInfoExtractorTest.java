package uk.co.mruoc.wso2;

import org.junit.Test;

import java.io.File;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class CarInfoExtractorTest {

    private final CarInfoExtractor extractor = new CarInfoExtractor();

    private final File validCar = new File("test/valid.car");
    private final File noArtifactsXmlCar = new File("test/noArtifactsXml.car");
    private final File nonExistentCar = new File("test/nonExistent.car");
    private final File invalidArtifactsXmlCar = new File("test/invalidArtifactsXml.car");

    @Test
    public void shouldExtractCarInfoFromValidCarFile() {
        CarInfo info = extractor.extract(validCar);

        assertThat(info.getName()).isEqualTo("json-validator-mediator-config-local");
        assertThat(info.getVersion()).isEqualTo("1.0.0-SNAPSHOT");
    }

    @Test
    public void shouldThrowExceptionIfCannotLoadFile() {
        String expectedMessage = "could not extract artifacts.xml from car " + nonExistentCar.getAbsolutePath();

        Throwable thrown = catchThrowable(() -> extractor.extract(nonExistentCar));

        assertThat(thrown)
                .isInstanceOf(InvalidCarException.class)
                .hasMessage(expectedMessage);
    }

    @Test
    public void shouldThrowExceptionIfCarDoesNotHaveArtifactsXml() {
        String expectedMessage = noArtifactsXmlCar.getAbsolutePath() + " does not contain an artifacts.xml file";

        Throwable thrown = catchThrowable(() -> extractor.extract(noArtifactsXmlCar));

        assertThat(thrown)
                .isInstanceOf(InvalidCarException.class)
                .hasMessage(expectedMessage);
    }

    @Test
    public void shouldThrowExceptionIfArtifactsXmlIsInvalid() {
        String expectedMessage = "cannot parse xml: invalidXml";

        Throwable thrown = catchThrowable(() -> extractor.extract(invalidArtifactsXmlCar));

        assertThat(thrown)
                .isInstanceOf(InvalidCarException.class)
                .hasMessage(expectedMessage);
    }

}
