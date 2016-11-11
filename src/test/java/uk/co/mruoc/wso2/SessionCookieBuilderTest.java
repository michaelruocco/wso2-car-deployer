package uk.co.mruoc.wso2;

import org.junit.Test;
import org.wso2.carbon.application.mgt.stub.ApplicationAdminStub;
import org.wso2.carbon.authenticator.stub.*;
import org.wso2.developerstudio.eclipse.carbonserver.base.capp.uploader.CarbonAppUploaderStub;

import java.rmi.RemoteException;

public class SessionCookieBuilderTest {

    @Test
    public void shouldReturnSessionCookie() {

    }

    private static class FakeStubFactory implements StubFactory {

        @Override
        public AuthenticationAdminStub createAuthenticationAdminStub() {
            return null;
        }

        @Override
        public CarbonAppUploaderStub createCarbonAppUploaderStub(String sessionCookie) {
            return null;
        }

        @Override
        public ApplicationAdminStub createApplicationAdminStub(String sessionCookie) {
            return null;
        }

        @Override
        public String getHost() {
            return null;
        }

    }

    private static class FakeAuthenticationAdmin implements AuthenticationAdmin {

        private String username;
        private String password;
        private String url;

        @Override
        public void logout() throws RemoteException, LogoutAuthenticationExceptionException {

        }

        @Override
        public RememberMeData loginWithRememberMeOption(String s, String s1, String s2) throws RemoteException, LoginWithRememberMeOptionAuthenticationExceptionException {
            return null;
        }

        @Override
        public void startloginWithRememberMeOption(String s, String s1, String s2, AuthenticationAdminCallbackHandler authenticationAdminCallbackHandler) throws RemoteException {

        }

        @Override
        public boolean login(String s, String s1, String s2) throws RemoteException, LoginAuthenticationExceptionException {
            return false;
        }

        @Override
        public void startlogin(String s, String s1, String s2, AuthenticationAdminCallbackHandler authenticationAdminCallbackHandler) throws RemoteException {

        }

        @Override
        public String getAuthenticatorName() throws RemoteException {
            return null;
        }

        @Override
        public void startgetAuthenticatorName(AuthenticationAdminCallbackHandler authenticationAdminCallbackHandler) throws RemoteException {

        }

        @Override
        public int getPriority() throws RemoteException {
            return 0;
        }

        @Override
        public void startgetPriority(AuthenticationAdminCallbackHandler authenticationAdminCallbackHandler) throws RemoteException {

        }

        @Override
        public boolean isDisabled() throws RemoteException {
            return false;
        }

        @Override
        public void startisDisabled(AuthenticationAdminCallbackHandler authenticationAdminCallbackHandler) throws RemoteException {

        }

        @Override
        public boolean loginWithRememberMeCookie(String s) throws RemoteException {
            return false;
        }

        @Override
        public void startloginWithRememberMeCookie(String s, AuthenticationAdminCallbackHandler authenticationAdminCallbackHandler) throws RemoteException {

        }
    }

}
