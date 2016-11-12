package uk.co.mruoc.wso2;

import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Stub;
import org.apache.axis2.context.OperationContext;
import org.apache.axis2.context.ServiceContext;
import org.junit.Before;
import org.junit.Test;

import static org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class SessionCookieExtractorTest {

    private static final String SESSION_COOKIE = "abc123def";

    private final Stub stub = mock(Stub.class);
    private final ServiceClient serviceClient = mock(ServiceClient.class);
    private final OperationContext operationContext = mock(OperationContext.class);
    private final ServiceContext serviceContext = mock(ServiceContext.class);

    private final SessionCookieExtractor extractor = new SessionCookieExtractor();

    @Before
    public void setUp() {
        given(stub._getServiceClient()).willReturn(serviceClient);
        given(serviceClient.getLastOperationContext()).willReturn(operationContext);
        given(operationContext.getServiceContext()).willReturn(serviceContext);
    }

    @Test
    public void shouldExtractSessionCookie() {
        given(serviceContext.getProperty(COOKIE_STRING)).willReturn(SESSION_COOKIE);

        String cookie = extractor.extract(stub);

        assertThat(cookie).isEqualTo(SESSION_COOKIE);
    }

}
