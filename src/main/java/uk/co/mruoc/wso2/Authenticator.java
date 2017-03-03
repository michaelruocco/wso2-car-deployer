package uk.co.mruoc.wso2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.authenticator.stub.AuthenticationAdmin;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;

import java.rmi.RemoteException;

public class Authenticator {

    private static final Logger LOG = LoggerFactory.getLogger(Authenticator.class);

    private final String host;
    private final String username;
    private final String password;

    private Authenticator(AuthenticatorBuilder builder) {
        this.host = builder.host;
        this.username = builder.username;
        this.password = builder.password;
    }

    public boolean authenticate(AuthenticationAdmin authenticationAdmin) {
        try {
            LOG.info("attempting to authenticate against " + host + " with username " + username + " using password");
            return authenticationAdmin.login(username, password, host);
        } catch (RemoteException | LoginAuthenticationExceptionException e) {
            throw new AuthenticationFailedException("authentication against " + host + " with username " + username + " failed", e);
        }
    }

    public static class AuthenticatorBuilder {

        private String host;
        private String username;
        private String password;

        public AuthenticatorBuilder setHost(String host) {
            this.host = host;
            return this;
        }

        public AuthenticatorBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public AuthenticatorBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Authenticator build() {
            return new Authenticator(this);
        }

    }

}
