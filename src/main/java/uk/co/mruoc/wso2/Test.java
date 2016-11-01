package uk.co.mruoc.wso2;

import java.io.File;

public class Test {

    public static void main(String[] args) {
        File file = new File("test/json-validator-mediator-config-local-1.0.0-SNAPSHOT.car");
        CarDeployer deployer = new DefaultCarDeployer("https://192.168.99.100:9444");
        deployer.deploy(file);
    }
}
