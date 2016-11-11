package uk.co.mruoc.wso2;

import org.wso2.carbon.application.mgt.stub.ApplicationAdminStub;
import org.wso2.developerstudio.eclipse.carbonserver.base.capp.uploader.CarbonAppUploaderStub;

import java.rmi.RemoteException;

/**
 * Created by michaelruocco on 11/11/2016.
 */
public interface StubFactory {
    CarbonAppUploaderStub buildCarbonAppUploaderStub() throws RemoteException;

    ApplicationAdminStub buildApplicationAdminStub() throws RemoteException;
}
