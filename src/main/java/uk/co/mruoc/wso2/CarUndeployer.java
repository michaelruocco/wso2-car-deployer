package uk.co.mruoc.wso2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.application.mgt.stub.ApplicationAdmin;
import org.wso2.carbon.application.mgt.stub.ApplicationAdminExceptionException;

import java.io.File;
import java.rmi.RemoteException;

public class CarUndeployer {

    private static final Logger LOG = LoggerFactory.getLogger(Authenticator.class);

    private final CarInfoExtractor carInfoExtractor;
    private final ApplicationAdmin applicationAdmin;

    public CarUndeployer(StubFactory stubFactory) {
        this(new CarInfoExtractor(), stubFactory.createApplicationAdminStub());
    }

    public CarUndeployer(CarInfoExtractor carInfoExtractor, ApplicationAdmin applicationAdmin) {
        this.carInfoExtractor = carInfoExtractor;
        this.applicationAdmin = applicationAdmin;
    }

    public void undeploy(File file) {
        try {
            LOG.info("undeploying car " + file.getName());
            CarInfo carInfo = carInfoExtractor.extract(file);
            applicationAdmin.deleteApplication(carInfo.getFullName());
        } catch (ApplicationAdminExceptionException | RemoteException e) {
            throw new UndeployCarFailedException(e);
        }
    }

}