package uk.co.mruoc.wso2;

import org.wso2.carbon.application.mgt.stub.ApplicationAdminStub;
import org.wso2.carbon.application.mgt.stub.types.carbon.ApplicationMetadata;
import org.wso2.carbon.application.mgt.stub.types.carbon.ArtifactDeploymentStatus;

import java.io.File;
import java.util.Optional;

public class DeploymentChecker {

    private final FileConverter fileConverter;

    public DeploymentChecker(ApplicationAdminStub stub) {
        this(new FileConverter(stub));
    }

    public DeploymentChecker(FileConverter fileConverter) {
        this.fileConverter = fileConverter;
    }

    public boolean isDeployed(File file) {
        Optional<ApplicationMetadata> metadata = fileConverter.toApplicationMetadata(file);
        if (!metadata.isPresent())
            return false;
        return hasDeploymentStatus(metadata.get(), "Deployed");
    }

    public boolean hasDeploymentStatus(ApplicationMetadata metadata, String requiredStatus) {
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
