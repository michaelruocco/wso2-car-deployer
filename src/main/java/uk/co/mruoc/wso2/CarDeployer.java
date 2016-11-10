package uk.co.mruoc.wso2;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.wso2.carbon.application.mgt.stub.ApplicationAdminExceptionException;
import org.wso2.carbon.application.mgt.stub.ApplicationAdminStub;
import org.wso2.developerstudio.eclipse.carbonserver.base.capp.uploader.CarbonAppUploaderStub;

import java.io.File;
import java.rmi.RemoteException;

import static org.wso2.developerstudio.eclipse.carbonserver.base.capp.uploader.CarbonAppUploaderStub.*;

public class CarDeployer {

    private static final Logger LOG = LogManager.getLogger(CarDeployer.class);

    private final UploadedFileItemConverter uploadedFileItemConverter = new UploadedFileItemConverter();
    private final CarInfoExtractor carInfoExtractor = new CarInfoExtractor();

    private final StubFactory stubFactory;

    public CarDeployer(StubFactory stubFactory) {
        this.stubFactory = stubFactory;
    }

    public void deploy(File file) {
        try {
            LOG.info("deploying car " + file.getName());
            CarbonAppUploaderStub carbonAppUploader = stubFactory.buildCarbonAppUploaderStub();
            UploadedFileItem item = uploadedFileItemConverter.toUploadedFileItem(file);
            carbonAppUploader.uploadApp(toArray(item));

            RetriableDeploymentChecker checker = new RetriableDeploymentChecker(stubFactory, 20000);
            if (!checker.isDeployed(file))
                throw new DeployCarFailedException("timed out trying to verify car deployment for " + file.getAbsolutePath());
        } catch (RemoteException e) {
            throw new DeployCarFailedException(e);
        }
    }

    public void undeploy(File file) {
        try {
            LOG.info("undeploying car " + file.getName());
            CarInfo carInfo = extractCarInfo(file);
            ApplicationAdminStub applicationAdmin = stubFactory.buildApplicationAdminStub();
            applicationAdmin.deleteApplication(carInfo.getFullName());

            RetriableDeploymentChecker checker = new RetriableDeploymentChecker(stubFactory, 20000);
            if (!checker.isUndeployed(file))
                throw new UndeployCarFailedException("timed out trying to verify car undeployment for " + file.getAbsolutePath());
        } catch (ApplicationAdminExceptionException | RemoteException e) {
            throw new UndeployCarFailedException(e);
        }
    }

    private CarInfo extractCarInfo(File file) {
        return carInfoExtractor.extract(file);
    }

    private UploadedFileItem[] toArray(UploadedFileItem item) {
        return new UploadedFileItem[] { item };
    }

}