package uk.co.mruoc.wso2;

import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Stub;
import org.apache.axis2.context.OperationContext;
import org.apache.axis2.context.ServiceContext;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import static org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING;

public class SessionCookieExtractor {

    private static final Logger LOG = LogManager.getLogger(SessionCookieExtractor.class);

    public String extract(Stub stub) {
        ServiceClient serviceClient = stub._getServiceClient();
        OperationContext operationContext = serviceClient.getLastOperationContext();
        ServiceContext serviceContext = operationContext.getServiceContext();
        String cookie = (String) serviceContext.getProperty(COOKIE_STRING);
        LOG.info("extracted session cookie " + cookie + " from stub " + stub);
        return cookie;
    }

}
