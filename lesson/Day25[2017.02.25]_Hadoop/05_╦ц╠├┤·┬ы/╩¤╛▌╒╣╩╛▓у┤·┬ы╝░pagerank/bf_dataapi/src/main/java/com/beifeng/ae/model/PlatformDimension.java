package com.beifeng.ae.model;

public class PlatformDimension {
    private int id;
    private String platform;
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public PlatformDimension() {
        super();
    }

    public PlatformDimension(int id) {
        super();
        this.id = id;
    }

    public PlatformDimension(String platform, String version) {
        super();
        this.platform = platform;
        this.version = version;
    }

    public PlatformDimension(int id, String platform, String version) {
        super();
        this.id = id;
        this.platform = platform;
        this.version = version;
    }

}
