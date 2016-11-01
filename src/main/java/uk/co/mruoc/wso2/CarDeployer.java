package uk.co.mruoc.wso2;

import java.io.File;
import java.util.List;

public interface CarDeployer {

    void deploy(File file);

    boolean isDeployed(String applicationName);

    List<String> getAllApplications();

}
