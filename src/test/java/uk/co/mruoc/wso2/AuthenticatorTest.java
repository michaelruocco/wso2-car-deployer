package uk.co.mruoc.wso2;

import org.junit.Test;
import org.wso2.carbon.authenticator.stub.AuthenticationAdminStub;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import uk.co.mruoc.wso2.Authenticator.AuthenticatorBuilder;

import java.rmi.RemoteException;

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static com.googlecode.catchexception.apis.BDDCatchException.when;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class AuthenticatorTest {

    private static final String USERNAME = "mruoc";
    private static final String PASSWORD = "Pa$$word";
    private static final String HOST = "localhost";

    private final AuthenticationAdminStub stub = mock(AuthenticationAdminStub.class);

    private final Authenticator authenticator = new AuthenticatorBuilder()
            .setUsername(USERNAME)
            .setPassword(PASSWORD)
            .setHost(HOST)
            .build();

    @Test
    public void shouldReturnTrueIfAuthenticationSuccessful() throws RemoteException, LoginAuthenticationExceptionException {
        given(stub.login(USERNAME, PASSWORD, HOST)).willReturn(true);

        boolean result = authenticator.authenticate(stub);

        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseIfAuthenticationUnsuccessful() throws RemoteException, LoginAuthenticationExceptionException {
        given(stub.login(USERNAME, PASSWORD, HOST)).willReturn(false);

        boolean result = authenticator.authenticate(stub);

        assertThat(result).isFalse();
    }

    @Test
    public void shouldThrowExceptionIfAuthenticationFailsWithRemoteException() throws RemoteException, LoginAuthenticationExceptionException {
        given(stub.login(USERNAME, PASSWORD, HOST)).willThrow(new RemoteException());

        when(authenticator).authenticate(stub);

        then(caughtException())
                .isInstanceOf(AuthenticationFailedException.class)
                .hasCauseInstanceOf(RemoteException.class)
                .hasMessage(expectedExceptionMessage());
    }

    @Test
    public void shouldThrowExceptionIfAuthenticationFailsWithLoginException() throws RemoteException, LoginAuthenticationExceptionException {
        given(stub.login(USERNAME, PASSWORD, HOST)).willThrow(new LoginAuthenticationExceptionException());

        when(authenticator).authenticate(stub);

        then(caughtException())
                .isInstanceOf(AuthenticationFailedException.class)
                .hasCauseInstanceOf(LoginAuthenticationExceptionException.class)
                .hasMessage(expectedExceptionMessage());
    }

    private String expectedExceptionMessage() {
        return String.format("authentication against %s with username %s failed", HOST, USERNAME);
    }

}
