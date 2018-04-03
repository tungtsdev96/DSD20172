package com.hust.edu.dsd.model.staff;

/**
 * Created by tungts on 3/14/2018.
 */

public class HistoryWork {

    /**
     * id : 2
     * tree_id : 1
     * volume : 1
     * tree_name : C9_301
     */

    private int id;
    private int tree_id;
    private double volume;
    private String tree_name;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTree_id() {
        return tree_id;
    }

    public void setTree_id(int tree_id) {
        this.tree_id = tree_id;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public String getTree_name() {
        return tree_name;
    }

    public void setTree_name(String tree_name) {
        this.tree_name = tree_name;
    }
}
