package uk.co.mruoc.wso2;

import org.junit.Test;
import org.wso2.carbon.application.mgt.stub.ApplicationAdmin;
import org.wso2.carbon.application.mgt.stub.ApplicationAdminExceptionException;
import org.wso2.developerstudio.eclipse.carbonserver.base.capp.uploader.CarbonAppUploaderStub;
import org.wso2.developerstudio.eclipse.carbonserver.base.capp.uploader.CarbonAppUploaderStub.UploadedFileItem;

import java.io.File;
import java.rmi.RemoteException;

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static com.googlecode.catchexception.apis.BDDCatchException.when;
import static org.assertj.core.api.Java6BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CarUndeployerTest {

    private static final String FULL_NAME = "fullName";

    private final File file = new File("");
    private final CarInfo carInfo = mock(CarInfo.class);
    private final CarInfoExtractor extractor = mock(CarInfoExtractor.class);
    private final ApplicationAdmin applicationAdmin = mock(ApplicationAdmin.class);

    private final CarUndeployer undeployer = new CarUndeployer(extractor, applicationAdmin);

    @Test
    public void shouldDeployCarbonApp() throws RemoteException, ApplicationAdminExceptionException {
        given(extractor.extract(file)).willReturn(carInfo);
        given(carInfo.getFullName()).willReturn(FULL_NAME);

        undeployer.undeploy(file);

        verify(applicationAdmin).deleteApplication(FULL_NAME);
    }

    @Test
    public void shouldThrowExceptionIfUndeployFailsWithRemoteException() throws RemoteException, ApplicationAdminExceptionException {
        given(extractor.extract(file)).willReturn(carInfo);
        given(carInfo.getFullName()).willReturn(FULL_NAME);
        willThrow(new RemoteException()).given(applicationAdmin).deleteApplication(FULL_NAME);

        when(undeployer).undeploy(file);

        then(caughtException())
                .isInstanceOf(UndeployCarFailedException.class)
                .hasCauseInstanceOf(RemoteException.class);
    }

    @Test
    public void shouldThrowExceptionIfUndeployFailsWithApplicationAdminExceptionException() throws RemoteException, ApplicationAdminExceptionException {
        given(extractor.extract(file)).willReturn(carInfo);
        given(carInfo.getFullName()).willReturn(FULL_NAME);
        willThrow(new ApplicationAdminExceptionException()).given(applicationAdmin).deleteApplication(FULL_NAME);

        when(undeployer).undeploy(file);

        then(caughtException())
                .isInstanceOf(UndeployCarFailedException.class)
                .hasCauseInstanceOf(ApplicationAdminExceptionException.class);
    }

}
