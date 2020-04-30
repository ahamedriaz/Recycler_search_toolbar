package com.allright.android_filterrecyclerviewusingsearchviewintoolbar;


import java.io.Serializable;


public class Model implements Serializable {

    private String name;
    private String version;

    public Model(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}