package uk.co.mruoc.wso2;

import java.io.File;

public class Test {

    public static void main(String[] args) {
        String serverUrl = "https://192.168.99.100:9444/";
        String username = "admin";
        String password = "admin";

        //File file = new File("test/json-validator-mediator-config-local-1.0.0-SNAPSHOT.car");
        CarDeployer deployer = new DefaultCarDeployer(serverUrl, username, password);
        //deployer.deploy(file);

        deployer.isDeployed("json-validator-mediator-config-local_1.0.0-SNAPSHOT");
    }
}
