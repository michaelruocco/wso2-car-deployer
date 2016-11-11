package uk.co.mruoc.wso2;

import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.OperationContext;
import org.apache.axis2.context.ServiceContext;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.wso2.carbon.authenticator.stub.AuthenticationAdminStub;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;

import java.rmi.RemoteException;

import static org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING;

public class SessionCookieBuilder {

    private static final Logger LOG = LogManager.getLogger(DefaultStubFactory.class);

    private AuthenticationAdminStub stub;
    private String serverUrl;
    private String username;
    private String password;

    public SessionCookieBuilder setStubFactory(StubFactory stubFactory) {
        this.stub = stubFactory.createAuthenticationAdminStub();
        this.serverUrl = stubFactory.getServerUrl();
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
        if (!login())
            throw new CreateSessionCookieFailedException("login against " + serverUrl + " with username " + username + " using password failed");
        return extractSessionCookie();
    }

    private boolean login() {
        try {
            LOG.info("attempting login against " + serverUrl + " with username " + username + " using password");
            return stub.login(username, password, serverUrl);
        } catch (RemoteException | LoginAuthenticationExceptionException e) {
            throw new CreateSessionCookieFailedException(e);
        }
    }

    private String extractSessionCookie() {
        ServiceClient serviceClient = stub._getServiceClient();
        OperationContext operationContext = serviceClient.getLastOperationContext();
        ServiceContext serviceContext = operationContext.getServiceContext();
        return (String) serviceContext.getProperty(COOKIE_STRING);
    }

}
