package com.hxsn.ssk.beans;

public class AppVersion {

    private int id;
    private String createDateString;
    private String desc;
    private String url;
    private String version;
    private int nshversion;

    public int getNshversion() {
        return nshversion;
    }

    public void setNshversion(int nshversion) {
        this.nshversion = nshversion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateDateString() {
        return createDateString;
    }

    public void setCreateDateString(String createDateString) {
        this.createDateString = createDateString;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
