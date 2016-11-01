package uk.co.mruoc.wso2;

import java.io.File;

public interface CarDeployer {

    void deploy(File file);

    boolean isDeployed(String applicationName);

}
