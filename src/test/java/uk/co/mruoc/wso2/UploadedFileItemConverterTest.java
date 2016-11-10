package uk.co.mruoc.wso2;

import org.junit.Test;
import org.wso2.developerstudio.eclipse.carbonserver.base.capp.uploader.CarbonAppUploaderStub.UploadedFileItem;

import javax.activation.DataHandler;
import javax.activation.URLDataSource;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class UploadedFileItemConverterTest {

    private final UploadedFileItemConverter converter = new UploadedFileItemConverter();

    private final File validFile = new File("test/valid.car");

    @Test
    public void shouldSetFileName() {
        UploadedFileItem[] items = converter.toUploadedFileItem(validFile);
        UploadedFileItem item = items[0];

        assertThat(item.getFileName()).isEqualTo(validFile.getName());
    }

    @Test
    public void shouldSetFileType() {
        UploadedFileItem[] items = converter.toUploadedFileItem(validFile);
        UploadedFileItem item = items[0];

        assertThat(item.getFileType()).isEqualTo("jar");
    }

    @Test
    public void shouldSetDataHandlerWithFileUrl() throws IOException {
        UploadedFileItem[] items = converter.toUploadedFileItem(validFile);
        UploadedFileItem item = items[0];

        URL url = toUrl(item);
        assertThat(url.getFile()).isEqualTo(validFile.getAbsolutePath());
    }

    @Test
    public void shouldReturnMultipleItemsForMultipleFiles() throws IOException {
        UploadedFileItem[] items = converter.toUploadedFileItem(validFile, validFile);

        assertThat(items.length).isEqualTo(2);
    }

    private URL toUrl(UploadedFileItem item) {
        DataHandler dataHandler = item.getDataHandler();
        URLDataSource dataSource = (URLDataSource) dataHandler.getDataSource();
        return dataSource.getURL();
    }

}
