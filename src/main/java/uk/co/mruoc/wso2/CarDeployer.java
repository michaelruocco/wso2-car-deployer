package uk.co.mruoc.wso2;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.wso2.carbon.application.mgt.stub.ApplicationAdmin;
import org.wso2.developerstudio.eclipse.carbonserver.base.capp.uploader.CarbonAppUploaderStub;

import java.io.File;
import java.rmi.RemoteException;

import static org.wso2.developerstudio.eclipse.carbonserver.base.capp.uploader.CarbonAppUploaderStub.*;

public class CarDeployer {

    private static final Logger LOG = LogManager.getLogger(CarDeployer.class);

    private final UploadedFileItemConverter uploadedFileItemConverter = new UploadedFileItemConverter();

    private final RetriableDeploymentChecker checker;
    private final CarbonAppUploaderStub carbonAppUploader;

    public CarDeployer(StubFactory stubFactory) {
        this(stubFactory.createCarbonAppUploaderStub(), stubFactory.createApplicationAdminStub());
    }

    public CarDeployer(CarbonAppUploaderStub carbonAppUploader, ApplicationAdmin applicationAdmin) {
        this(new RetriableDeploymentChecker(applicationAdmin, 20000), carbonAppUploader);
    }

    public CarDeployer(RetriableDeploymentChecker checker, CarbonAppUploaderStub carbonAppUploader) {
        this.checker = checker;
        this.carbonAppUploader = carbonAppUploader;
    }

    public void deploy(File file) {
        try {
            LOG.info("deploying car " + file.getName());
            UploadedFileItem[] items = uploadedFileItemConverter.toUploadedFileItem(file);
            carbonAppUploader.uploadApp(items);

            if (!checker.isDeployed(file))
                throw new DeployCarFailedException("timed out trying to verify deployment of " + file.getAbsolutePath());
        } catch (RemoteException e) {
            throw new DeployCarFailedException(e);
        }
    }

}