package uk.co.mruoc.wso2;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.wso2.carbon.application.mgt.stub.ApplicationAdminExceptionException;
import org.wso2.carbon.application.mgt.stub.ApplicationAdminStub;
import org.wso2.developerstudio.eclipse.carbonserver.base.capp.uploader.CarbonAppUploaderStub;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

import static org.wso2.developerstudio.eclipse.carbonserver.base.capp.uploader.CarbonAppUploaderStub.*;

public class DefaultCarDeployer implements CarDeployer {

    private static final Logger LOG = LogManager.getLogger(DefaultCarDeployer.class);

    private final CarInfoExtractor carInfoExtractor = new CarInfoExtractor();
    private final UploadedFileItemConverter uploadedFileItemConverter = new UploadedFileItemConverter();
    private final StubFactory stubFactory;

    public DefaultCarDeployer(StubFactory stubFactory) {
        this.stubFactory = stubFactory;
    }

    @Override
    public void deployQuietly(File file) {
        deployCApp(file);
    }

    @Override
    public void deploy(File file) {
        deployCApp(file);
        if (!isDeployed(file))
            throw new DeployCarFailedException("failed to deploy " + file.getAbsolutePath());
    }

    @Override
    public void deployIfNotDeployed(File file) {
        if (isDeployed(file)) {
            LOG.info(file.getAbsolutePath() + " is already deployed, not redeploying");
            return;
        }
        deploy(file);
    }

    @Override
    public boolean isDeployed(File file) {
        CarInfo carInfo = extractCarInfo(file);
        List<String> deployedApplications = getAllApplications();
        return deployedApplications.contains(carInfo.getFullName());
    }

    @Override
    public List<String> getAllApplications() {
        try {
            ApplicationAdminStub applicationAdminStub = stubFactory.buildApplicationAdminStub();
            return Arrays.asList(applicationAdminStub.listAllApplications());
        } catch (ApplicationAdminExceptionException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private CarInfo extractCarInfo(File file) {
        return carInfoExtractor.extract(file);
    }

    private void deployCApp(File file) {
        try {
            CarbonAppUploaderStub carbonAppUploaderStub = stubFactory.buildCarbonAppUploaderStub();
            UploadedFileItem uploadedFileItem = uploadedFileItemConverter.toUploadedFileItem(file);
            UploadedFileItem[] fileItems = new UploadedFileItem[]{uploadedFileItem};
            LOG.info("deploying car " + file.getName());
            carbonAppUploaderStub.uploadApp(fileItems);
        } catch (RemoteException | MalformedURLException e) {
            throw new DeployCarFailedException(e);
        }
    }

}