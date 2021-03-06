package uk.co.mruoc.wso2;

import org.wso2.developerstudio.eclipse.carbonserver.base.capp.uploader.CarbonAppUploaderStub.UploadedFileItem;

import javax.activation.DataHandler;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class UploadedFileItemConverter {

    public UploadedFileItem[] toUploadedFileItem(File... files) {
        List<UploadedFileItem> items = new ArrayList<>();
        for (File file : files)
            items.add(toUploadedFileItem(file));
        return items.toArray(new UploadedFileItem[items.size()]);
    }

    private UploadedFileItem toUploadedFileItem(File file) {
        try {
            UploadedFileItem item = new UploadedFileItem();
            URI uri = file.toURI();
            DataHandler param = new DataHandler(uri.toURL());
            item.setDataHandler(param);
            item.setFileName(file.getName());
            item.setFileType("jar");
            return item;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
