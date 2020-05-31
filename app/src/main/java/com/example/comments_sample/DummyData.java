package com.example.comments_sample;


import java.util.ArrayList;
import java.util.List;

public class DummyData{
    String data;
    boolean is_main;
    ArrayList<DummyData> dummyDataList;
    DummyData parent;

    public DummyData(String data, ArrayList<DummyData> dummyDataList,DummyData parent) {
        this.data = data;
        this.dummyDataList = dummyDataList;
        this.is_main = false;
        this.parent = parent;
    }

    public String toString() {
        return data;
    }

    public List getChildren() {
        return dummyDataList;
    }

    public void clearChildren() {
        dummyDataList = new ArrayList<DummyData>();
    }
}
