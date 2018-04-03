package com.hust.edu.dsd.model;

/**
 * Created by tungts on 3/16/2018.
 */

public class WaterStation {

    /**
     * water_id : 2
     * water_name : WC_D35_F1
     * water_location : WC tần 1 tòa nhà D3-5
     * x : 0
     * y : 0
     * state : 1
     */

    private int water_id;
    private String water_name;
    private String water_location;
    private int x;
    private int y;
    private int state;

    public int getWater_id() {
        return water_id;
    }

    public void setWater_id(int water_id) {
        this.water_id = water_id;
    }

    public String getWater_name() {
        return water_name;
    }

    public void setWater_name(String water_name) {
        this.water_name = water_name;
    }

    public String getWater_location() {
        return water_location;
    }

    public void setWater_location(String water_location) {
        this.water_location = water_location;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
