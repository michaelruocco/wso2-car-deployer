package uk.co.mruoc.wso2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.application.mgt.stub.ApplicationAdmin;

import java.io.File;

public class RetriableDeploymentChecker {

    private static final Logger LOG = LoggerFactory.getLogger(Authenticator.class);

    private static final int DEFAULT_TIMEOUT = 20000;
    private static final int DELAY = 1000;
    private static final String LOG_MESSAGE = "checking %s deployed=%s timeout expired=%s slept=%d timeout=%d";

    private final Sleeper sleeper = new Sleeper();
    private final DeploymentChecker deploymentChecker;
    private final int timeout;

    private int slept;

    public RetriableDeploymentChecker(StubFactory stubFactory) {
        this(stubFactory.createApplicationAdminStub(), DEFAULT_TIMEOUT);
    }

    public RetriableDeploymentChecker(StubFactory stubFactory, int timeout) {
        this(stubFactory.createApplicationAdminStub(), timeout);
    }

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
