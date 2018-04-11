package com.david.jetcab.Models;

import com.google.gson.JsonObject;

/**
 * Created by david on 03/12/2017.
 */

public class City {
    private String id;
    private String name;
    private String airportCode;

    private double latitude;
    private double longitude;



    public City() {
        this.id = "";
        this.name = "";
        this.airportCode = "";
        this.latitude = 0.0;
        this.longitude = 0.0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setByJsonObject(JsonObject oneCityJsonObject) {

        if (oneCityJsonObject == null || oneCityJsonObject.isJsonNull())
            return;

        if (oneCityJsonObject.has("_id"))
            id = oneCityJsonObject.get("_id").getAsString();

        if (oneCityJsonObject.has("name"))
            name = oneCityJsonObject.get("name").getAsString();

        if (oneCityJsonObject.has("airportCode"))
            airportCode = oneCityJsonObject.get("airportCode").getAsString();

        if (oneCityJsonObject.has("location")) {
            JsonObject locationJsonObject = oneCityJsonObject.get("location").getAsJsonObject();
            if (locationJsonObject.has("lat"))
                latitude = Double.parseDouble(locationJsonObject.get("lat").getAsString());
            if (locationJsonObject.has("lng"))
                latitude = Double.parseDouble(locationJsonObject.get("lng").getAsString());
        }

    }
}
