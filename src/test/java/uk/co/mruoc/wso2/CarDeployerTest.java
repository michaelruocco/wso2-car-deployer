package uk.co.mruoc.wso2;

import org.junit.Test;
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

public class CarDeployerTest {

    private final File file = new File("");
    private final UploadedFileItemConverter converter = mock(UploadedFileItemConverter.class);
    private final CarbonAppUploaderStub stub = mock(CarbonAppUploaderStub.class);
    private final UploadedFileItem[] items = new UploadedFileItem[0];
    private final CarDeployer deployer = new CarDeployer(converter, stub);

    @Test
    public void shouldDeployCarbonApp() throws RemoteException {
        given(converter.toUploadedFileItem(file)).willReturn(items);

        deployer.deploy(file);

        verify(stub).uploadApp(items);
    }

    @Test
    public void shouldThrowExceptionIfDeploymentFails() throws RemoteException {
        given(converter.toUploadedFileItem(file)).willReturn(items);
        willThrow(new RemoteException()).given(stub).uploadApp(items);

        when(deployer).deploy(file);

        then(caughtException())
                .isInstanceOf(DeployCarFailedException.class)
                .hasCauseInstanceOf(RemoteException.class);
    }

}
