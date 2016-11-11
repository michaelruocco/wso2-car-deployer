package uk.co.mruoc.wso2;

import org.wso2.carbon.application.mgt.stub.ApplicationAdminStub;
import org.wso2.developerstudio.eclipse.carbonserver.base.capp.uploader.CarbonAppUploaderStub;

import java.io.File;

public class Test {

    public static void main(String[] args) {
        deployTest();
    }

    private static void deployTest() {
        StubFactory stubFactory = createStubFactory();
        SessionCookieBuilder sessionCookieBuilder = new SessionCookieBuilder()
                .setStubFactory(stubFactory)
                .setUsername("admin")
                .setPassword("admin");
        String sessionCookie = sessionCookieBuilder.build();
        CarbonAppUploaderStub carbonAppUploaderStub = stubFactory.createCarbonAppUploaderStub(sessionCookie);
        ApplicationAdminStub stub = stubFactory.createApplicationAdminStub(sessionCookie);
        CarDeployer deployer = new CarDeployer(carbonAppUploaderStub, stub);
        DeploymentChecker deploymentChecker = new DeploymentChecker(stub);

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
        return new DefaultStubFactory(serverUrl);
    }

}
