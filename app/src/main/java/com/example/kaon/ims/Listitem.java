package com.example.kaon.ims;

public class Listitem {
     private String NAME;
     private String START_DATE;
     private String END_DATE;
     private String TYPE;
     private int INDEX_ID;


    public int getINDEX_ID() {
        return INDEX_ID;
    }

    public void setINDEX_ID(int INDEX_ID) {
        this.INDEX_ID = INDEX_ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getSTART_DATE() {
        return START_DATE;
    }

    public void setSTART_DATE(String START_DATE) {
        this.START_DATE = START_DATE;
    }

    public String getEND_DATE() {
        return END_DATE;
    }

    public void setEND_DATE(String END_DATE) {
        this.END_DATE = END_DATE;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }
}
