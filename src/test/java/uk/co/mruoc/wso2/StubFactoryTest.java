package uk.co.mruoc.wso2;

import org.junit.Test;
import org.wso2.carbon.application.mgt.stub.ApplicationAdminStub;
import org.wso2.carbon.authenticator.stub.AuthenticationAdminStub;
import org.wso2.developerstudio.eclipse.carbonserver.base.capp.uploader.CarbonAppUploaderStub;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class StubFactoryTest {

    private static final String SERVER_URL = "http://localhost:8080";

    private final StubFactory stubFactory = new StubFactory(SERVER_URL);

    @Test
    public void shouldCreateAuthenticationAdminStub() {
        assertThat(stubFactory.createAuthenticationAdminStub()).isInstanceOf(AuthenticationAdminStub.class);
    }

    @Test
    public void shouldCreateApplicationAdminStub() {
        assertThat(stubFactory.createApplicationAdminStub("")).isInstanceOf(ApplicationAdminStub.class);
    }

    @Test
    public void shouldCreateCarbonAppUploaderStub() {
        assertThat(stubFactory.createCarbonAppUploaderStub("")).isInstanceOf(CarbonAppUploaderStub.class);
    }

    @Test
    public void shouldReturnHost() {
        assertThat(stubFactory.getHost()).isEqualTo("localhost");
    }

}
