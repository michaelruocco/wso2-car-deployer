package uk.co.mruoc.wso2;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Stub;
import org.junit.Test;
import org.wso2.carbon.authenticator.stub.AuthenticationAdminStub;

import static org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class SessionCookieConfiguratorTest {

    private static final String COOKIE_VALUE = "cookieValue";

    private final SessionCookieConfigurator configurator = new SessionCookieConfigurator(COOKIE_VALUE);

    @Test
    public void shouldSessionSessionCookieAgainstStub() throws AxisFault {
        Stub stub = new AuthenticationAdminStub("");

        configurator.configure(stub);

        String configuredCookieValue = extractCookieValue(stub);
        assertThat(configuredCookieValue).isEqualTo(COOKIE_VALUE);
    }

    private String extractCookieValue(Stub stub) {
        Options options = extractOptions(stub);
        return (String) options.getProperty(COOKIE_STRING);
    }

    private Options extractOptions(Stub stub) {
        ServiceClient serviceClient = stub._getServiceClient();
        return serviceClient.getOptions();
    }
    
}
