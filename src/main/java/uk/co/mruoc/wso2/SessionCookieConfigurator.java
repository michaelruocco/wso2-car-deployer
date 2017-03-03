package uk.co.mruoc.wso2;

import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Stub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING;

public class SessionCookieConfigurator {

    private static final Logger LOG = LoggerFactory.getLogger(Authenticator.class);

    private final String cookie;

    public SessionCookieConfigurator(String cookie) {
        this.cookie = cookie;
    }

    public void configure(Stub stub) {
        ServiceClient serviceClient = stub._getServiceClient();
        Options options = serviceClient.getOptions();
        options.setManageSession(true);
        options.setProperty(COOKIE_STRING, cookie);
        LOG.info("configured stub " + stub + " with session cookie " + cookie);
    }

}
