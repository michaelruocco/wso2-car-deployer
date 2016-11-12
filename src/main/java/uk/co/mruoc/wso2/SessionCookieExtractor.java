package uk.co.mruoc.wso2;

import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Stub;
import org.apache.axis2.context.OperationContext;
import org.apache.axis2.context.ServiceContext;

import static org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING;

public class SessionCookieExtractor {

    public String extract(Stub stub) {
        ServiceClient serviceClient = stub._getServiceClient();
        OperationContext operationContext = serviceClient.getLastOperationContext();
        ServiceContext serviceContext = operationContext.getServiceContext();
        return (String) serviceContext.getProperty(COOKIE_STRING);
    }

}
