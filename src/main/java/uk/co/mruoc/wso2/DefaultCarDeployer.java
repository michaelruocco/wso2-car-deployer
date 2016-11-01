package uk.co.mruoc.wso2;

import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.wso2.carbon.application.mgt.stub.ApplicationAdminExceptionException;
import org.wso2.carbon.application.mgt.stub.ApplicationAdminStub;
import org.wso2.carbon.application.mgt.stub.types.carbon.ApplicationMetadata;
import org.wso2.carbon.application.mgt.stub.types.carbon.ArtifactDeploymentStatus;
import org.wso2.carbon.authenticator.stub.AuthenticationAdminStub;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.developerstudio.eclipse.carbonserver.base.capp.uploader.CarbonAppUploaderStub;

import javax.activation.DataHandler;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

import org.apache.axis2.client.Stub;

import static org.wso2.developerstudio.eclipse.carbonserver.base.capp.uploader.CarbonAppUploaderStub.*;

public class DefaultCarDeployer implements CarDeployer {

    private static final Logger LOG = LogManager.getLogger(DefaultCarDeployer.class);

    private final String serverUrl;
    private final String username;
    private final String password;

    public DefaultCarDeployer(String serverUrl, String username, String password) {
        this.serverUrl = serverUrl;
        this.username = username;
        this.password = password;
    }

    public void deploy(File file) {
        try {
            deployCApp(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isDeployed(String applicationName) {
        try {
            ApplicationAdminStub applicationAdminStub = new ApplicationAdminStub(serverUrl + "services/ApplicationAdmin");
            configureStub(applicationAdminStub);
            ApplicationMetadata metadata = applicationAdminStub.getAppData(applicationName);
            for (ArtifactDeploymentStatus status : metadata.getArtifactsDeploymentStatus()) {
                System.out.println(status.getArtifactName() + " " + status.getDeploymentStatus());
            }
            return true;
        } catch (ApplicationAdminExceptionException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getAllApplications() {
        try {
            ApplicationAdminStub applicationAdminStub = new ApplicationAdminStub(serverUrl + "services/ApplicationAdmin");
            configureStub(applicationAdminStub);
            return Arrays.asList(applicationAdminStub.listAllApplications());
        } catch (ApplicationAdminExceptionException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void deployCApp(File carFile) throws Exception {
        CarbonAppUploaderStub carbonAppUploaderStub = getCarbonAppUploaderStub();
        UploadedFileItem uploadedFileItem = new UploadedFileItem();
        DataHandler param = new DataHandler(carFile.toURI().toURL());
        uploadedFileItem.setDataHandler(param);
        uploadedFileItem.setFileName(carFile.getName());
        uploadedFileItem.setFileType("jar");
        UploadedFileItem[] fileItems = new UploadedFileItem[]{uploadedFileItem};
        LOG.info("uploading " + carFile.getName() + " to " + serverUrl + "...");
        carbonAppUploaderStub.uploadApp(fileItems);
    }

    private CarbonAppUploaderStub getCarbonAppUploaderStub() throws RemoteException {
        CarbonAppUploaderStub carbonAppUploaderStub = new CarbonAppUploaderStub(serverUrl + "services/CarbonAppUploader");
        configureStub(carbonAppUploaderStub);
        return carbonAppUploaderStub;
    }

    private String createSessionCookie() {
        try {
            LOG.info("creating session cookie using server url " + serverUrl);
            URL url = new URL(serverUrl);
            AuthenticationAdminStub authenticationStub = new AuthenticationAdminStub(serverUrl + "services/AuthenticationAdmin");
            authenticationStub._getServiceClient().getOptions().setManageSession(true);
            if (!authenticationStub.login(username, password, url.getHost()))
                throw new RuntimeException("could not log in to " + url.toExternalForm() + " with username " + username + " and password " + password);
            ServiceContext serviceContext = authenticationStub._getServiceClient().getLastOperationContext().getServiceContext();
            String sessionCookie = (String) serviceContext.getProperty(HTTPConstants.COOKIE_STRING);
            LOG.info("authentication to " + serverUrl + " successful.");
            return sessionCookie;
        } catch (MalformedURLException | RemoteException | LoginAuthenticationExceptionException e) {
            throw new RuntimeException(e);
        }
    }

    private void configureStub(Stub stub) {
        String sessionCookie = createSessionCookie();
        stub._getServiceClient().getOptions().setManageSession(true);
        stub._getServiceClient().getOptions().setProperty(HTTPConstants.COOKIE_STRING, sessionCookie);
    }

}