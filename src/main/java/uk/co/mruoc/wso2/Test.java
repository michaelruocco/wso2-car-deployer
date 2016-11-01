package uk.co.mruoc.wso2;

import java.io.File;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        String serverUrl = "https://192.168.99.100:9444/";
        String username = "admin";
        String password = "admin";

        CarDeployer deployer = new DefaultCarDeployer(serverUrl, username, password);

        List<String> applications = deployer.getAllApplications();
        applications.forEach(System.out::println);

        //File file = new File("test/json-validator-mediator-config-local-1.0.0-SNAPSHOT.car");

        //deployer.deploy(file);

        deployer.isDeployed("json-validator-mediator-config-local_1.0.0-SNAPSHOT");
    }
}
