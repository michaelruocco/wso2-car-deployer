package uk.co.mruoc.wso2;

//import org.apache.axis2.client.Options;
//import org.apache.axis2.client.ServiceClient;
//import org.apache.axis2.client.Stub;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Stub;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.wso2.carbon.application.mgt.stub.ApplicationAdminStub;
import org.wso2.carbon.authenticator.stub.AuthenticationAdminStub;
import org.wso2.developerstudio.eclipse.carbonserver.base.capp.uploader.CarbonAppUploaderStub;

import java.rmi.RemoteException;

import static org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING;

public class DefaultStubFactory implements StubFactory {

    private static final Logger LOG = LogManager.getLogger(DefaultStubFactory.class);

    private final UrlConverter urlConverter = new UrlConverter();

    private final String serverUrl;

    public DefaultStubFactory(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    @Override
    public CarbonAppUploaderStub createCarbonAppUploaderStub(String sessionCookie) {
        try {
            String url = serverUrl + "services/CarbonAppUploader";
            LOG.info("creating carbonAppUploaderStub with url " + url);
            CarbonAppUploaderStub carbonAppUploaderStub = new CarbonAppUploaderStub(url);
            configureStubWithCookie(carbonAppUploaderStub, sessionCookie);
            return carbonAppUploaderStub;
        } catch (RemoteException e) {
            throw new CreateStubException(e);
        }
    }

    @Override
    public ApplicationAdminStub createApplicationAdminStub(String sessionCookie) {
        try {
            String url = serverUrl + "services/ApplicationAdmin";
            LOG.info("creating applicationAdminStub with url " + url);
            ApplicationAdminStub applicationAdminStub = new ApplicationAdminStub(url);
            configureStubWithCookie(applicationAdminStub, sessionCookie);
            return applicationAdminStub;
        } catch (RemoteException e) {
            throw new CreateStubException(e);
        }
    }

    @Override
    public AuthenticationAdminStub createAuthenticationAdminStub() {
        try {
            String url = serverUrl + "services/AuthenticationAdmin";
            LOG.info("creating authenticationAdminStub with url " + url);
            return new AuthenticationAdminStub(url);
        } catch (RemoteException e) {
            throw new CreateStubException(e);
        }
    }

    @Override
    public String getHost() {
        return urlConverter.toHost(serverUrl);
    }

    private void configureStubWithCookie(Stub stub, String sessionCookie) {
        ServiceClient serviceClient = stub._getServiceClient();
        Options options = serviceClient.getOptions();
        options.setManageSession(true);
        options.setProperty(COOKIE_STRING, sessionCookie);
        LOG.info("configured stub " + stub + " with session cookie " + sessionCookie);
    }


}
