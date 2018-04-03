package com.hust.edu.dsd.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by tungts on 3/16/2018.
 */

public class WaterAndTree {

    private WaterStation waterStation;

    @SerializedName("trees")
    private ArrayList<Trees> trees;

    public WaterAndTree() {
    }

    public WaterAndTree(WaterStation waterStation, ArrayList<Trees> trees) {
        this.waterStation = waterStation;
        this.trees = trees;
    }

    public WaterStation getWaterStation() {
        return waterStation;
    }

    public void setWaterStation(WaterStation waterStation) {
        this.waterStation = waterStation;
    }

    public ArrayList<Trees> getTrees() {
        return trees;
    }

    public void setTrees(ArrayList<Trees> trees) {
        this.trees = trees;
    }
}
