package uk.co.mruoc.wso2;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.wso2.developerstudio.eclipse.carbonserver.base.capp.uploader.CarbonAppUploaderStub;

import java.io.File;
import java.rmi.RemoteException;

import static org.wso2.developerstudio.eclipse.carbonserver.base.capp.uploader.CarbonAppUploaderStub.*;

public class CarDeployer {

    private static final Logger LOG = LogManager.getLogger(CarDeployer.class);

    private final UploadedFileItemConverter uploadedFileItemConverter;
    private final CarbonAppUploaderStub carbonAppUploader;

    public CarDeployer(StubFactory stubFactory) {
        this(new UploadedFileItemConverter(), stubFactory.createCarbonAppUploaderStub());
    }

    public CarDeployer(UploadedFileItemConverter uploadedFileItemConverter, CarbonAppUploaderStub carbonAppUploader) {
        this.uploadedFileItemConverter = uploadedFileItemConverter;
        this.carbonAppUploader = carbonAppUploader;
    }

    public void deploy(File file) {
        try {
            LOG.info("deploying car " + file.getName());
            UploadedFileItem[] items = uploadedFileItemConverter.toUploadedFileItem(file);
            carbonAppUploader.uploadApp(items);
        } catch (RemoteException e) {
            throw new DeployCarFailedException(e);
        }
    }

}