package uk.co.mruoc.wso2;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.wso2.carbon.application.mgt.stub.ApplicationAdminExceptionException;
import org.wso2.carbon.application.mgt.stub.ApplicationAdminStub;
import org.wso2.carbon.application.mgt.stub.types.carbon.ApplicationMetadata;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Optional;

public class FileConverter {

    private static final Logger LOG = LogManager.getLogger(DeploymentChecker.class);

    private final CarInfoExtractor carInfoExtractor = new CarInfoExtractor();
    private final StubFactory stubFactory;

    public FileConverter(StubFactory stubFactory) {
        this.stubFactory = stubFactory;
    }

    public Optional<ApplicationMetadata> toApplicationMetadata(File file) {
        try {
            CarInfo carInfo = carInfoExtractor.extract(file);
            ApplicationAdminStub applicationAdminStub = stubFactory.buildApplicationAdminStub();
            return Optional.ofNullable(applicationAdminStub.getAppData(carInfo.getFullName()));
        } catch (ApplicationAdminExceptionException | RemoteException e) {
            LOG.debug("could not get application metadata " + file.getAbsolutePath(), e);
            return Optional.empty();
        }
    }

}
