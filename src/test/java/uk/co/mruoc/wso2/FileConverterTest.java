package uk.co.mruoc.wso2;

import org.junit.Test;
import org.wso2.carbon.application.mgt.stub.ApplicationAdmin;
import org.wso2.carbon.application.mgt.stub.ApplicationAdminExceptionException;
import org.wso2.carbon.application.mgt.stub.types.carbon.ApplicationMetadata;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class FileConverterTest {

    private static final String FULL_NAME = "fullName";

    private final File file = new File("");
    private final CarInfo carInfo = mock(CarInfo.class);
    private final CarInfoExtractor extractor = mock(CarInfoExtractor.class);
    private final ApplicationAdmin stub = mock(ApplicationAdmin.class);
    private final ApplicationMetadata metadata = mock(ApplicationMetadata.class);

    private final FileConverter converter = new FileConverter(extractor, stub);

    @Test
    public void shouldReturnApplicationMetadata() throws ApplicationAdminExceptionException, RemoteException {
        given(carInfo.getFullName()).willReturn(FULL_NAME);
        given(extractor.extract(file)).willReturn(carInfo);
        given(stub.getAppData(FULL_NAME)).willReturn(metadata);

        Optional<ApplicationMetadata> returnedMetadata = converter.toApplicationMetadata(file);

        assertThat(returnedMetadata.isPresent()).isTrue();
        assertThat(returnedMetadata.get()).isEqualTo(metadata);
    }

    @Test
    public void shouldReturnEmptyApplicationMetadataIfGetMetadataFailsWithRemoteException() throws ApplicationAdminExceptionException, RemoteException {
        given(carInfo.getFullName()).willReturn(FULL_NAME);
        given(extractor.extract(file)).willReturn(carInfo);
        given(stub.getAppData(FULL_NAME)).willThrow(new RemoteException());

        Optional<ApplicationMetadata> returnedMetadata = converter.toApplicationMetadata(file);

        assertThat(returnedMetadata.isPresent()).isFalse();
    }

    @Test
    public void shouldReturnEmptyApplicationMetadataIfGetMetadataFailsWithApplicationAdminExceptionException() throws ApplicationAdminExceptionException, RemoteException {
        given(carInfo.getFullName()).willReturn(FULL_NAME);
        given(extractor.extract(file)).willReturn(carInfo);
        given(stub.getAppData(FULL_NAME)).willThrow(new ApplicationAdminExceptionException());

        Optional<ApplicationMetadata> returnedMetadata = converter.toApplicationMetadata(file);

        assertThat(returnedMetadata.isPresent()).isFalse();
    }

}
