package uk.co.mruoc.wso2;

import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.wso2.carbon.authenticator.stub.AuthenticationAdminStub;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.developerstudio.eclipse.carbonserver.base.capp.uploader.CarbonAppUploaderStub;

import javax.activation.DataHandler;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import static org.wso2.developerstudio.eclipse.carbonserver.base.capp.uploader.CarbonAppUploaderStub.*;

public class DefaultCarDeployer implements CarDeployer {

    private static final Logger LOG = LogManager.getLogger(DefaultCarDeployer.class);

    private final String serverUrl;

    public DefaultCarDeployer(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void deploy(File file) {
        try {
            deployCApp("admin", "admin", serverUrl, file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void deployCApp(String username, String pwd, String url, File carFile) throws Exception {
        CarbonAppUploaderStub carbonAppUploaderStub = getCarbonAppUploaderStub(username, pwd, url);
        UploadedFileItem uploadedFileItem = new UploadedFileItem();
        DataHandler param=new DataHandler(carFile.toURI().toURL());
        uploadedFileItem.setDataHandler(param);
        uploadedFileItem.setFileName(carFile.getName());
        uploadedFileItem.setFileType("jar");
        UploadedFileItem[] fileItems=new UploadedFileItem[]{uploadedFileItem};
        LOG.info("uploading " + carFile.getName() + " to " + serverUrl + "...");
        carbonAppUploaderStub.uploadApp(fileItems);
    }

    private CarbonAppUploaderStub getCarbonAppUploaderStub(String username, String pwd, String url) throws RemoteException,
            MalformedURLException, LoginAuthenticationExceptionException {
        String sessionCookie = createSessionCookie(url, username, pwd);
        CarbonAppUploaderStub carbonAppUploaderStub = new CarbonAppUploaderStub(serverUrl + "/services/CarbonAppUploader");
        carbonAppUploaderStub._getServiceClient().getOptions().setManageSession(true);
        carbonAppUploaderStub._getServiceClient().getOptions().setProperty(HTTPConstants.COOKIE_STRING, sessionCookie);
        return carbonAppUploaderStub;
    }

    private String createSessionCookie(String serverURL, String username, String pwd) throws RemoteException, MalformedURLException, LoginAuthenticationExceptionException {
        LOG.info("creating session cookie using server url " + serverURL);
        URL url = new URL(serverURL);
        AuthenticationAdminStub authenticationStub = new AuthenticationAdminStub(serverURL+ "/services/AuthenticationAdmin");
        authenticationStub._getServiceClient().getOptions().setManageSession(true);
        if (authenticationStub.login(username, pwd, url.getHost())) {
            ServiceContext serviceContext = authenticationStub._getServiceClient().getLastOperationContext().getServiceContext();
            String sessionCookie = (String) serviceContext.getProperty(HTTPConstants.COOKIE_STRING);
            LOG.info("authentication to " + serverURL + " successful.");
            return sessionCookie;
        }
        return null;
    }

}