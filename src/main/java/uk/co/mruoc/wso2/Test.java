package uk.co.mruoc.wso2;

import java.io.File;

public class Test {

    public static void main(String[] args) {
        deployTest();
    }

    private static void deployTest() {
        StubFactory stubFactory = createStubFactory();
        CarDeployer deployer = new CarDeployer(stubFactory);
        DeploymentChecker deploymentChecker = new DeploymentChecker(stubFactory);

        File file = new File("test/json-validator-mediator-config-local-1.0.0-SNAPSHOT.car");

        boolean deployed = deploymentChecker.isDeployed(file);
        System.out.println("deployed " + deployed);
        if (!deployed) {
            deployer.deploy(file);
            deployed = deploymentChecker.isDeployed(file);
            System.out.println("deployed " + deployed);
        }

        if (deployed) {
            deployer.undeploy(file);
            deployed = deploymentChecker.isDeployed(file);
            System.out.println("deployed " + deployed);
        }
    }

    private static StubFactory createStubFactory() {
        String serverUrl = "https://192.168.99.100:9444/";

        String sessionCookie = new SessionCookieBuilder()
                .setServerUrl(serverUrl)
                .setUsername("admin")
                .setPassword("admin")
                .build();

        return new DefaultStubFactory(serverUrl, sessionCookie);
    }

}
