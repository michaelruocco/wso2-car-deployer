package uk.co.mruoc.wso2;

import org.wso2.carbon.application.mgt.stub.ApplicationAdminStub;
import org.wso2.carbon.authenticator.stub.AuthenticationAdminStub;
import org.wso2.developerstudio.eclipse.carbonserver.base.capp.uploader.CarbonAppUploaderStub;

import java.rmi.RemoteException;

public interface StubFactory {

    CarbonAppUploaderStub createCarbonAppUploaderStub();

    ApplicationAdminStub createApplicationAdminStub();

    AuthenticationAdminStub createAuthenticationAdminStub();

    String getServerUrl();

}
