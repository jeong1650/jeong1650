package com.example.kaon.ims;

public class Personinfoitem {
    private String NAME;
    private String YEAR;
    private String POSITION;
    private String ETC;
    private int PROJECT_ID;
    private int INDEX_ID;
    private String resumepath;
    private String portpath;
    private String MASTER_ID;
    private String STATUS;
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getMASTER_ID() {
        return MASTER_ID;
    }

    public void setMASTER_ID(String MASTER_ID) {
        this.MASTER_ID = MASTER_ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getYEAR() {
        return YEAR;
    }

    public void setYEAR(String YEAR) {
        this.YEAR = YEAR;
    }

    public String getPOSITION() {
        return POSITION;
    }

    public void setPOSITION(String POSITION) {
        this.POSITION = POSITION;
    }

    public String getETC() {
        return ETC;
    }

    public void setETC(String ETC) {
        this.ETC = ETC;
    }

    public int getPROJECT_ID() {
        return PROJECT_ID;
    }

    public void setPROJECT_ID(int PROJECT_ID) {
        this.PROJECT_ID = PROJECT_ID;
    }

    public int getINDEX_ID() {
        return INDEX_ID;
    }

    public void setINDEX_ID(int INDEX_ID) {
        this.INDEX_ID = INDEX_ID;
    }

    public String getResumepath() {
        return resumepath;
    }

    public void setResumepath(String resumepath) {
        this.resumepath = resumepath;
    }

    public String getPortpath() {
        return portpath;
    }

    public void setPortpath(String portpath) {
        this.portpath = portpath;
    }
}
