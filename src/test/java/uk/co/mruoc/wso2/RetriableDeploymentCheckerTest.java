package uk.co.mruoc.wso2;

import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class RetriableDeploymentCheckerTest {

    private static final int TIMEOUT = 1000;

    private final File file = new File("");
    private final DeploymentChecker deploymentChecker = mock(DeploymentChecker.class);

    private final RetriableDeploymentChecker retriableDeploymentChecker = new RetriableDeploymentChecker(deploymentChecker, TIMEOUT);

    @Test
    public void shouldReturnFalseIfNotDeployed() {
        given(deploymentChecker.isDeployed(file)).willReturn(false, false);

        boolean deployed = retriableDeploymentChecker.isDeployed(file);

        assertThat(deployed).isFalse();
    }

    @Test
    public void shouldReturnTrueIfDeployed() {
        given(deploymentChecker.isDeployed(file)).willReturn(false, true);

        boolean deployed = retriableDeploymentChecker.isDeployed(file);

        assertThat(deployed).isTrue();
    }

    @Test
    public void shouldReturnFalseIfNotUndeployed() {
        given(deploymentChecker.isDeployed(file)).willReturn(true, true);

        boolean deployed = retriableDeploymentChecker.isUndeployed(file);

        assertThat(deployed).isFalse();
    }

    @Test
    public void shouldReturnTrueIfUndeployed() {
        given(deploymentChecker.isDeployed(file)).willReturn(true, false);

        boolean deployed = retriableDeploymentChecker.isUndeployed(file);

        assertThat(deployed).isTrue();
    }

}
