package uk.co.mruoc.wso2;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.wso2.carbon.application.mgt.stub.ApplicationAdmin;
import org.wso2.carbon.application.mgt.stub.ApplicationAdminStub;

import java.io.File;

public class RetriableDeploymentChecker {

    private static final Logger LOG = LogManager.getLogger(RetriableDeploymentChecker.class);

    private static final int DELAY = 1000;
    private static final String LOG_MESSAGE = "checking %s deployed=%s timeout expired=%s slept=%d timeout=%d";

    private final Sleeper sleeper = new Sleeper();
    private final DeploymentChecker deploymentChecker;
    private final int timeout;

    private int slept;

    public RetriableDeploymentChecker(ApplicationAdmin stub, int timeout) {
        this(new DeploymentChecker(new FileConverter(stub)), timeout);
    }

    public RetriableDeploymentChecker(DeploymentChecker deploymentChecker, int timeout) {
        this.deploymentChecker = deploymentChecker;
        this.timeout = timeout;
    }

    public boolean isDeployed(File file) {
        slept = 0;

        while (!deployed(file) && !timeoutExpired())
            sleep();

        return deployed(file);
    }

    public boolean isUndeployed(File file) {
        slept = 0;

        while (deployed(file) && !timeoutExpired())
            sleep();

        return !deployed(file);
    }

    public boolean timeoutExpired() {
        return slept >= timeout;
    }

    private boolean deployed(File file) {
        boolean deployed = deploymentChecker.isDeployed(file);
        LOG.info(buildLogMessage(file, deployed));
        return deployed;
    }

    private void sleep() {
        sleeper.sleep(DELAY);
        slept += DELAY;
    }

    private String buildLogMessage(File file, boolean deployed) {
        return String.format(LOG_MESSAGE, file, deployed, timeoutExpired(), slept, timeout);
    }

}
