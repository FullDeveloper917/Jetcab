package com.david.jetcab.Models;

import android.widget.TextView;

import com.david.jetcab.Adapters.PlanesListAdapter;
import com.david.jetcab.R;
import com.google.gson.JsonObject;

/**
 * Created by david on 04/12/2017.
 */

public class Plane {
    private String id;
    private String branch;
    private String model;
    private String planeCode;
    private int seatCount;
    private double speed;
    private double pricePerKm;
    private double pricePerHour;
    private String firstImageUrl;
    private String secondImageUrl;

    public Plane() {
        this.id = null;
        this.branch = "";
        this.model = "";
        this.planeCode = "";
        this.seatCount = 0;
        this.speed = 0;
        this.pricePerKm = 0;
        this.pricePerHour = 0;
        this.firstImageUrl = null;
        this.secondImageUrl = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPlaneCode() {
        return planeCode;
    }

    public void setPlaneCode(String planeCode) {
        this.planeCode = planeCode;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getPricePerKm() {
        return pricePerKm;
    }

    public void setPricePerKm(double pricePerKm) {
        this.pricePerKm = pricePerKm;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public String getFirstImageUrl() {
        return firstImageUrl;
    }

    public void setFirstImageUrl(String firstImageUrl) {
        this.firstImageUrl = firstImageUrl;
    }

    public String getSecondImageUrl() {
        return secondImageUrl;
    }

    public void setSecondImageUrl(String secondImageUrl) {
        this.secondImageUrl = secondImageUrl;
    }


    public void setByJsonObject(JsonObject onePlaneJsonObject){
        if (onePlaneJsonObject == null || onePlaneJsonObject.isJsonNull())
            return;

        if (onePlaneJsonObject.has("_id"))
            id = onePlaneJsonObject.get("_id").getAsString();

        if (onePlaneJsonObject.has("branch"))
            branch = onePlaneJsonObject.get("branch").getAsString();

        if (onePlaneJsonObject.has("model"))
            model = onePlaneJsonObject.get("model").getAsString();

        if (onePlaneJsonObject.has("planeCode"))
            planeCode = onePlaneJsonObject.get("planeCode").getAsString();

        if (onePlaneJsonObject.has("seatCount"))
            seatCount = onePlaneJsonObject.get("seatCount").getAsInt();

        if (onePlaneJsonObject.has("speed"))
            speed = onePlaneJsonObject.get("speed").getAsDouble();

        if (onePlaneJsonObject.has("pricePerKm"))
            pricePerKm = onePlaneJsonObject.get("pricePerKm").getAsDouble();

        if (onePlaneJsonObject.has("pricePerHour"))
            pricePerKm = onePlaneJsonObject.get("pricePerHour").getAsDouble();

        if (onePlaneJsonObject.has("imageUrl"))
            firstImageUrl = onePlaneJsonObject.get("imageUrl").getAsString();

        if (onePlaneJsonObject.has("imageUrl1"))
            secondImageUrl = onePlaneJsonObject.get("imageUrl1").getAsString();

    }
}
