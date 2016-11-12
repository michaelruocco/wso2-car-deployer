package uk.co.mruoc.wso2;

import org.wso2.carbon.application.mgt.stub.ApplicationAdminStub;
import org.wso2.carbon.authenticator.stub.AuthenticationAdminStub;
import org.wso2.developerstudio.eclipse.carbonserver.base.capp.uploader.CarbonAppUploaderStub;
import uk.co.mruoc.wso2.Authenticator.AuthenticatorBuilder;

import java.io.File;

public class Test {

    public static void main(String[] args) {
        deployTest();
    }

    private static void deployTest() {
        StubFactory stubFactory = createStubFactory();
        Authenticator authenticator = new AuthenticatorBuilder()
                .setHost(stubFactory.getHost())
                .setUsername("admin")
                .setPassword("admin")
                .build();
        AuthenticationAdminStub authenticationAdminStub = stubFactory.createAuthenticationAdminStub();
        authenticator.authenticate(authenticationAdminStub);
        SessionCookieExtractor sessionCookieExtractor = new SessionCookieExtractor();
        String sessionCookie = sessionCookieExtractor.extract(authenticationAdminStub);
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
