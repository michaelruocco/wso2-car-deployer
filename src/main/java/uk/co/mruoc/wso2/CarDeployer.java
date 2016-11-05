package uk.co.mruoc.wso2;

import java.io.File;
import java.util.List;

public interface CarDeployer {

    void deployQuietly(File file);

    void deploy(File file);

    void deployIfNotDeployed(File file);

    boolean isDeployed(File file);

    List<String> getAllApplications();

}
