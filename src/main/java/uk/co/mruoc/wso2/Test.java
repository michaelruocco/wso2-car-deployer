package uk.co.mruoc.wso2;

import java.io.File;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        String serverUrl = "https://192.168.99.100:9444/";

        String sessionCookie = new SessionCookieBuilder()
                .setServerUrl(serverUrl)
                .setUsername("admin")
                .setPassword("admin")
                .build();

        StubFactory stubFactory = new StubFactory(serverUrl, sessionCookie);
        CarDeployer deployer = new DefaultCarDeployer(stubFactory);

        //List<String> faultyApplications = deployer.getAllFaultyApplications();
        //faultyApplications.forEach(a -> System.out.println("faulty " + a));

        //List<String> applications = deployer.getAllApplications();
        //applications.forEach(a -> System.out.println("applications " + a));

        File jsonValidator = new File("test/json-validator-mediator-config-local-1.0.0-SNAPSHOT.car");
        File sharedResources = new File("test/shared-resources-0.0.13.car");

        deployer.deployIfNotDeployed(jsonValidator);
        deployer.deployIfNotDeployed(sharedResources);

        //deployer.isDeployed("json-validator-mediator-config-local_1.0.0-SNAPSHOT");
        //System.out.println("deployed=" + deployer.isDeployed("shared-resources-0.0.13.car"));
        System.out.println("deployed=" + deployer.isDeployed(jsonValidator));
        System.out.println("deployed=" + deployer.isDeployed(sharedResources));
    }
}
