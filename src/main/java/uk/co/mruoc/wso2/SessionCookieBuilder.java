package uk.co.mruoc.wso2;

import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.OperationContext;
import org.apache.axis2.context.ServiceContext;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.wso2.carbon.authenticator.stub.AuthenticationAdminStub;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import static org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING;

public class SessionCookieBuilder {

    private static final Logger LOG = LogManager.getLogger(DefaultStubFactory.class);

    private String serverUrl;
    private String username;
    private String password;

    public SessionCookieBuilder setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
        return this;
    }

    public SessionCookieBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public SessionCookieBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public String build() {
        try {
            LOG.info("creating session cookie using server url " + serverUrl);
            AuthenticationAdminStub authenticationStub = buildAuthenticationAdminStub();
            if (!login(authenticationStub))
                throw new CreateSessionCookieFailedException("could not log in to " + serverUrl + " with username " + username + " and password " + password);
            return extractSessionCookie(authenticationStub);
        } catch (RemoteException e) {
            throw new CreateSessionCookieFailedException(e);
        }
    }

    private AuthenticationAdminStub buildAuthenticationAdminStub() throws RemoteException {
        return new AuthenticationAdminStub(serverUrl + "services/AuthenticationAdmin");
    }

    private boolean login(AuthenticationAdminStub authenticationStub) {
        try {
            String host = getHost();
            return authenticationStub.login(username, password, host);
        } catch (RemoteException | LoginAuthenticationExceptionException e) {
            throw new CreateSessionCookieFailedException(e);
        }
    }

    private String getHost() {
        try {
            URL url = new URL(serverUrl);
            return url.getHost();
        } catch (MalformedURLException e) {
            throw new CreateSessionCookieFailedException(e);
        }
    }

    private String extractSessionCookie(AuthenticationAdminStub authenticationStub) {
        ServiceClient serviceClient = authenticationStub._getServiceClient();
        OperationContext operationContext = serviceClient.getLastOperationContext();
        ServiceContext serviceContext = operationContext.getServiceContext();
        return (String) serviceContext.getProperty(COOKIE_STRING);
    }

}
