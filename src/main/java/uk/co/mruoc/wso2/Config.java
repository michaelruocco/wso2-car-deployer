package uk.co.mruoc.wso2;

public class Config {

    private final String username;
    private final String password;
    private final String url;

    private Config(ConfigBuilder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.url = builder.url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }

    public static class ConfigBuilder {

        private String username;
        private String password;
        private String url;

        public ConfigBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public ConfigBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public ConfigBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Config build() {
            return new Config(this);
        }

    }

}
