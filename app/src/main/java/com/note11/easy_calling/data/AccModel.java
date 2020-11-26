package com.note11.easy_calling.data;

public class AccModel {

    private TelModel tm;
    private String iu;

    public AccModel(){}

    public AccModel(TelModel tm, String iu) {
        this.tm = tm;
        this.iu = iu;
    }

    public TelModel getTm() {
        return tm;
    }

    public void setTm(TelModel tm) {
        this.tm = tm;
    }

    public String getIu() {
        return iu;
    }

    public void setIu(String iu) {
        this.iu = iu;
    }
}
