package uk.co.mruoc.wso2;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlConverter {

    public String toHost(String url) {
        try {
            return new URL(url).getHost();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
