package uk.co.mruoc.wso2;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.wso2.carbon.application.mgt.stub.ApplicationAdmin;
import org.wso2.carbon.application.mgt.stub.ApplicationAdminExceptionException;

import java.io.File;
import java.rmi.RemoteException;

public class CarUndeployer {

    private static final Logger LOG = LogManager.getLogger(CarUndeployer.class);

    private final CarInfoExtractor carInfoExtractor = new CarInfoExtractor();

    private final ApplicationAdmin applicationAdmin;

    public CarUndeployer(StubFactory stubFactory) {
        this(stubFactory.createApplicationAdminStub());
    }

    public CarUndeployer(ApplicationAdmin applicationAdmin) {
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