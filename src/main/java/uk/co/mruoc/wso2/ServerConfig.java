package uk.co.mruoc.wso2;

public class ServerConfig {

    private final String serverUrl;
    private final String sessionCookie;

    public ServerConfig(String serverUrl, String sessionCookie) {
        this.serverUrl = serverUrl;
        this.sessionCookie = sessionCookie;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getSessionCookie() {
        return sessionCookie;
    }

}
