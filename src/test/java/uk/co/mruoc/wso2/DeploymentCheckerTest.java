package uk.co.mruoc.wso2;

import org.junit.Test;
import org.wso2.carbon.application.mgt.stub.types.carbon.ApplicationMetadata;
import org.wso2.carbon.application.mgt.stub.types.carbon.ArtifactDeploymentStatus;

import java.io.File;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class DeploymentCheckerTest {

    private final File file = new File("");
    private final FileConverter fileConverter = mock(FileConverter.class);
    private final ApplicationMetadata applicationMetadata = mock(ApplicationMetadata.class);

    private final DeploymentChecker deploymentChecker = new DeploymentChecker(fileConverter);

    @Test
    public void shouldReturnFalseIfApplicationMetadataNotFound() {
        given(fileConverter.toApplicationMetadata(file)).willReturn(Optional.empty());

        boolean deployed = deploymentChecker.isDeployed(file);

        assertThat(deployed).isFalse();
    }

    @Test
    public void shouldReturnFalseIfDeploymentStatusIsNotSpecified() {
        ArtifactDeploymentStatus status = createDeploymentStatus(false, "Deployed");
        given(applicationMetadata.getArtifactsDeploymentStatus()).willReturn(new ArtifactDeploymentStatus[] { status });
        given(fileConverter.toApplicationMetadata(file)).willReturn(Optional.ofNullable(applicationMetadata));

        boolean deployed = deploymentChecker.isDeployed(file);

        assertThat(deployed).isFalse();
    }

    @Test
    public void shouldReturnFalseIfDeploymentStatusIsNotDeployed() {
        ArtifactDeploymentStatus status = createDeploymentStatus(true, "Pending");
        given(applicationMetadata.getArtifactsDeploymentStatus()).willReturn(new ArtifactDeploymentStatus[] { status });
        given(fileConverter.toApplicationMetadata(file)).willReturn(Optional.ofNullable(applicationMetadata));

        boolean deployed = deploymentChecker.isDeployed(file);

        assertThat(deployed).isFalse();
    }

    @Test
    public void shouldReturnTrueIfApplicationIsDeployed() {
        ArtifactDeploymentStatus status = createDeploymentStatus(true, "Deployed");
        given(applicationMetadata.getArtifactsDeploymentStatus()).willReturn(new ArtifactDeploymentStatus[] { status });
        given(fileConverter.toApplicationMetadata(file)).willReturn(Optional.ofNullable(applicationMetadata));

        boolean deployed = deploymentChecker.isDeployed(file);

        assertThat(deployed).isTrue();
    }

    @Test
    public void shouldReturnTrueIfOneApplicationStatusIsDeployed() {
        ArtifactDeploymentStatus status1 = createDeploymentStatus(true, "Pending");
        ArtifactDeploymentStatus status2 = createDeploymentStatus(true, "Deployed");
        given(applicationMetadata.getArtifactsDeploymentStatus()).willReturn(new ArtifactDeploymentStatus[] { status1, status2 });
        given(fileConverter.toApplicationMetadata(file)).willReturn(Optional.ofNullable(applicationMetadata));

        boolean deployed = deploymentChecker.isDeployed(file);

        assertThat(deployed).isTrue();
    }

    private ArtifactDeploymentStatus createDeploymentStatus(boolean statusSpecified, String statusName) {
        ArtifactDeploymentStatus status = mock(ArtifactDeploymentStatus.class);
        given(status.isDeploymentStatusSpecified()).willReturn(statusSpecified);
        given(status.getDeploymentStatus()).willReturn(statusName);
        return status;
    }

}
