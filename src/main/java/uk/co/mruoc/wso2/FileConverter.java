package uk.co.mruoc.wso2;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.wso2.carbon.application.mgt.stub.ApplicationAdmin;
import org.wso2.carbon.application.mgt.stub.ApplicationAdminExceptionException;
import org.wso2.carbon.application.mgt.stub.types.carbon.ApplicationMetadata;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Optional;

public class FileConverter {

    private static final Logger LOG = LogManager.getLogger(DeploymentChecker.class);

    private final CarInfoExtractor carInfoExtractor;
    private final ApplicationAdmin stub;

    public FileConverter(ApplicationAdmin stub) {
        this(new CarInfoExtractor(), stub);
    }

    public FileConverter(CarInfoExtractor carInfoExtractor, ApplicationAdmin stub) {
        this.carInfoExtractor = carInfoExtractor;
        this.stub = stub;
    }

    public Optional<ApplicationMetadata> toApplicationMetadata(File file) {
        try {
            CarInfo carInfo = carInfoExtractor.extract(file);
            return Optional.ofNullable(stub.getAppData(carInfo.getFullName()));
        } catch (ApplicationAdminExceptionException | RemoteException e) {
            LOG.debug("could not get application metadata from " + file.getAbsolutePath(), e);
            return Optional.empty();
        }
    }

}
