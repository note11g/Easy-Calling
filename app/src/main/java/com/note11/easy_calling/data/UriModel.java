package com.note11.easy_calling.data;

import java.util.HashMap;

public class UriModel {

    public HashMap<String,String> map;

    public UriModel(){}

    public UriModel(HashMap<String, String> map) {
        this.map = map;
    }

    public HashMap<String, String> getMap() {
        return map;
    }

    public void setMap(HashMap<String, String> map) {
        this.map = map;
    }
}
