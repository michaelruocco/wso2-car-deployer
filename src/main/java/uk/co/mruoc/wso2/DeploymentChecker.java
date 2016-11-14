package uk.co.mruoc.wso2;

import org.wso2.carbon.application.mgt.stub.types.carbon.ApplicationMetadata;
import org.wso2.carbon.application.mgt.stub.types.carbon.ArtifactDeploymentStatus;

import java.io.File;
import java.util.Optional;

public class DeploymentChecker {

    private final FileConverter fileConverter;

    public DeploymentChecker(StubFactory stubFactory) {
        this(new FileConverter(stubFactory.createApplicationAdminStub()));
    }

    public DeploymentChecker(FileConverter fileConverter) {
        this.fileConverter = fileConverter;
    }

    public boolean isDeployed(File file) {
        Optional<ApplicationMetadata> metadata = fileConverter.toApplicationMetadata(file);
        return metadata.isPresent() && hasDeploymentStatus(metadata.get(), "Deployed");
    }

    private boolean hasDeploymentStatus(ApplicationMetadata metadata, String requiredStatus) {
        for (ArtifactDeploymentStatus status : metadata.getArtifactsDeploymentStatus()) {
            if (matches(status, requiredStatus))
                return true;
        }
        return false;
    }

    private boolean matches(ArtifactDeploymentStatus status, String requiredStatus) {
        return status.isDeploymentStatusSpecified() && status.getDeploymentStatus().equals(requiredStatus);
    }

}
