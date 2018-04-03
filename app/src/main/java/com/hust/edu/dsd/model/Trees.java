package com.hust.edu.dsd.model;

/**
 * Created by tungts on 3/13/2018.
 */

public class Trees{

    /**
     * tree_id : 1
     * tree_name : C9_301
     * tree_type : 1
     * current_water : 2.5
     * current_state : NORMAL
     * x : 1
     * y : 10
     * tree_state : 1
     * is_blocking : 0
     */

    private int tree_id;
    private String tree_name;
    private int tree_type;
    private double current_water;
    private String current_state;
    private int x;
    private int y;
    private int tree_state;
    private int is_blocking;

    public int getTree_id() {
        return tree_id;
    }

    public void setTree_id(int tree_id) {
        this.tree_id = tree_id;
    }

    public String getTree_name() {
        return tree_name;
    }

    public void setTree_name(String tree_name) {
        this.tree_name = tree_name;
    }

    public int getTree_type() {
        return tree_type;
    }

    public void setTree_type(int tree_type) {
        this.tree_type = tree_type;
    }

    public double getCurrent_water() {
        return current_water;
    }

    public void setCurrent_water(double current_water) {
        this.current_water = current_water;
    }

    public String getCurrent_state() {
        return current_state;
    }

    public void setCurrent_state(String current_state) {
        this.current_state = current_state;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getTree_state() {
        return tree_state;
    }

    public void setTree_state(int tree_state) {
        this.tree_state = tree_state;
    }

    public int getIs_blocking() {
        return is_blocking;
    }

    public void setIs_blocking(int is_blocking) {
        this.is_blocking = is_blocking;
    }
}
