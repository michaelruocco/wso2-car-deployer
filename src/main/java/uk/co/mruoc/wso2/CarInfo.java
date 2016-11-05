package uk.co.mruoc.wso2;

public class CarInfo {

    private final String name;
    private final String version;

    public CarInfo(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getFullName() {
        return name + "_" + version;
    }

}
