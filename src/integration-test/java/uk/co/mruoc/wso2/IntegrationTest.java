package uk.co.mruoc.wso2;

import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;
import uk.co.mruoc.wso2.Config.ConfigBuilder;

import java.io.File;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class IntegrationTest {

    private static final int PORT = 9443;
    private static final int CONTAINER_START_TIMEOUT = 60000;

    private final File carFile = new File("test/json-validator-mediator-config-local-1.0.0-SNAPSHOT.car");
    private final Wso2ContainerStartupChecker startupChecker = new Wso2ContainerStartupChecker(CONTAINER_START_TIMEOUT);

    @Rule
    public final GenericContainer container = new Wso2Container("michaelruocco/wso2esb:4.9.0")
            .withExposedPorts(PORT)
            .withLogConsumer(startupChecker);

    @Test
    public void deploymentCheckerShouldReturnFalseIfCarIsNotDeployed() {
        startupChecker.waitForContainerToStart();
        RetriableDeploymentChecker deploymentChecker = new RetriableDeploymentChecker(createStubFactory());

        boolean deployed = deploymentChecker.isDeployed(carFile);

        assertThat(deployed).isFalse();
    }

    @Test
    public void shouldDeployCarFile() {
        startupChecker.waitForContainerToStart();

        StubFactory stubFactory = createStubFactory();
        CarDeployer carDeployer = new CarDeployer(stubFactory);
        RetriableDeploymentChecker deploymentChecker = new RetriableDeploymentChecker(stubFactory);

        carDeployer.deploy(carFile);
        boolean deployed = deploymentChecker.isDeployed(carFile);

        assertThat(deployed).isTrue();
    }

    @Test
    public void shouldUndeployCarFile() {
        startupChecker.waitForContainerToStart();

        StubFactory stubFactory = createStubFactory();
        CarDeployer deployer = new CarDeployer(stubFactory);
        RetriableDeploymentChecker deploymentChecker = new RetriableDeploymentChecker(stubFactory);
        deployer.deploy(carFile);
        boolean deployed = deploymentChecker.isDeployed(carFile);
        assertThat(deployed).isTrue();

        CarUndeployer undeployer = new CarUndeployer(stubFactory);
        undeployer.undeploy(carFile);
        boolean undeployed = deploymentChecker.isUndeployed(carFile);
        assertThat(undeployed).isTrue();
    }

    @Test(expected = UndeployCarFailedException.class)
    public void undeployShouldThrowExceptionIfCarIsNotDeployed() {
        startupChecker.waitForContainerToStart();

        StubFactory stubFactory = createStubFactory();
        CarUndeployer undeployer = new CarUndeployer(stubFactory);
        undeployer.undeploy(carFile);
    }

    private StubFactory createStubFactory() {
        Config config = new ConfigBuilder()
                .setUrl(createContainerUrl())
                .setUsername("admin")
                .setPassword("admin")
                .build();

        return new StubFactory(config);
    }

    private String createContainerUrl() {
        return "https://" + container.getContainerIpAddress() + ":" + PORT + "/";
    }

}
