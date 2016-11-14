package uk.co.mruoc.wso2;

import org.apache.axis2.client.Stub;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.wso2.carbon.application.mgt.stub.ApplicationAdminStub;
import org.wso2.carbon.authenticator.stub.AuthenticationAdminStub;
import org.wso2.developerstudio.eclipse.carbonserver.base.capp.uploader.CarbonAppUploaderStub;
import uk.co.mruoc.wso2.Authenticator.AuthenticatorBuilder;

import java.rmi.RemoteException;

public class StubFactory {

    private static final Logger LOG = LogManager.getLogger(StubFactory.class);

    private final UrlConverter urlConverter = new UrlConverter();
    private final SessionCookieExtractor sessionCookieExtractor = new SessionCookieExtractor();

    private final Authenticator authenticator;
    private final String serverUrl;
    private final AuthenticationAdminStub authenticationAdminStub;
    private String sessionCookie;

    public StubFactory(Config config) {
        this.serverUrl = config.getUrl();
        this.authenticator = new AuthenticatorBuilder()
                .setHost(urlConverter.toHost(serverUrl))
                .setUsername(config.getUsername())
                .setPassword(config.getPassword())
                .build();
        this.authenticationAdminStub = createAuthenticationAdminStub();
        this.sessionCookie = generateSessionCookie();
    }

    public CarbonAppUploaderStub createCarbonAppUploaderStub() {
        try {
            String url = serverUrl + "services/CarbonAppUploader";
            LOG.info("creating carbonAppUploaderStub with url " + url);
            CarbonAppUploaderStub carbonAppUploaderStub = new CarbonAppUploaderStub(url);
            configureWithSessionCookie(carbonAppUploaderStub, sessionCookie);
            return carbonAppUploaderStub;
        } catch (RemoteException e) {
            throw new CreateStubFailedException(e);
        }
    }

    public ApplicationAdminStub createApplicationAdminStub() {
        try {
            String url = serverUrl + "services/ApplicationAdmin";
            LOG.info("creating applicationAdminStub with url " + url);
            ApplicationAdminStub applicationAdminStub = new ApplicationAdminStub(url);
            configureWithSessionCookie(applicationAdminStub, sessionCookie);
            return applicationAdminStub;
        } catch (RemoteException e) {
            throw new CreateStubFailedException(e);
        }
    }

    private String generateSessionCookie() {
        authenticator.authenticate(authenticationAdminStub);
        return sessionCookieExtractor.extract(authenticationAdminStub);
    }

    private AuthenticationAdminStub createAuthenticationAdminStub() {
        try {
            String url = serverUrl + "services/AuthenticationAdmin";
            LOG.info("creating authenticationAdminStub with url " + url);
            return new AuthenticationAdminStub(url);
        } catch (RemoteException e) {
            throw new CreateStubFailedException(e);
        }
    }

    private void configureWithSessionCookie(Stub stub, String cookie) {
        SessionCookieConfigurator configurator = new SessionCookieConfigurator(cookie);
        configurator.configure(stub);
    }

}
