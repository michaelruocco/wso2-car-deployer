package uk.co.mruoc.wso2;

import uk.co.mruoc.wso2.Config.ConfigBuilder;

import java.io.File;

public class Example {

    public static void main(String[] args) {
        deployTest();
    }

    private static void deployTest() {
        Config config = new ConfigBuilder()
                .setUrl("https://0.0.0.0:9443/")
                .setUsername("admin")
                .setPassword("admin")
                .build();

        StubFactory stubFactory = new StubFactory(config);
        RetriableDeploymentChecker deploymentChecker = new RetriableDeploymentChecker(stubFactory);
        File file = new File("test/json-validator-mediator-config-local-1.0.0-SNAPSHOT.car");

        boolean deployed = deploymentChecker.isDeployed(file);
        System.out.println("deployed " + deployed);
        if (!deployed) {
            CarDeployer deployer = new CarDeployer(stubFactory);
            deployer.deploy(file);
            deployed = deploymentChecker.isDeployed(file);
            System.out.println("deployed " + deployed);
        }

        if (deployed) {
            CarUndeployer undeployer = new CarUndeployer(stubFactory);
            undeployer.undeploy(file);
            deployed = deploymentChecker.isDeployed(file);
            System.out.println("deployed " + deployed);
        }
    }

}
