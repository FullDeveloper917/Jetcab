package com.david.jetcab.Models;

import com.david.jetcab.BaseActivity;
import com.google.gson.JsonObject;

import java.util.Date;

/**
 * Created by david on 03/12/2017.
 */

public class Flight {
    private String id;
    private Plane plane;
    private City fromCity;
    private City toCity;
    private boolean regular;
    private boolean auction;
    private double pricePerSeat;
    private double totalPrice;
    private double lowPrice;
    private double highPrice;
    private double finalPrice;
    private long departureTime;
    private long arrivalTime;
    private int orderingSeatCount;
    private int totalSeatCount;
    private int availableSeatCount;
    private int confirmSeatNumber;
    private int remainDayNumber;

    public Flight() {
        this.id = null;
        this.plane = new Plane();
        this.fromCity = new City();
        this.toCity = new City();
        this.auction = false;
        this.pricePerSeat = 0.0;
        this.totalPrice = 0.0;
        this.lowPrice = 0.0;
        this.highPrice = 0.0;
        this.finalPrice = 0.0;
        this.departureTime = System.currentTimeMillis();
        this.arrivalTime = System.currentTimeMillis();
        this.totalSeatCount = 0;
        this.availableSeatCount = 0;
        this.confirmSeatNumber = 0;
        this.remainDayNumber = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Plane getPlane() {
        return plane;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    public City getFromCity() {
        return fromCity;
    }

    public void setFromCity(City fromCity) {
        this.fromCity = fromCity;
    }

    public City getToCity() {
        return toCity;
    }

    public void setToCity(City toCity) {
        this.toCity = toCity;
    }

    public boolean isRegular() {
        return regular;
    }

    public void setRegular(boolean regular) {
        this.regular = regular;
    }

    public boolean isAuction() {
        return auction;
    }

    public void setAuction(boolean auction) {
        this.auction = auction;
    }

    public double getPricePerSeat() {
        return pricePerSeat;
    }

    public void setPricePerSeat(double pricePerSeat) {
        this.pricePerSeat = pricePerSeat;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public long getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(long departureTime) {
        this.departureTime = departureTime;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getOrderingSeatCount() {
        return orderingSeatCount;
    }

    public void setOrderingSeatCount(int orderingSeatCount) {
        this.orderingSeatCount = orderingSeatCount;
    }

    public int getTotalSeatCount() {
        return totalSeatCount;
    }

    public void setTotalSeatCount(int totalSeatCount) {
        this.totalSeatCount = totalSeatCount;
    }

    public int getAvailableSeatCount() {
        return availableSeatCount;
    }

    public void setAvailableSeatCount(int availableSeatCount) {
        this.availableSeatCount = availableSeatCount;
    }

    public int getConfirmSeatNumber() {
        return confirmSeatNumber;
    }

    public void setConfirmSeatNumber(int confirmSeatNumber) {
        this.confirmSeatNumber = confirmSeatNumber;
    }

    public int getRemainDayNumber() {
        return remainDayNumber;
    }

    public void setRemainDayNumber(int remainDayNumber) {
        this.remainDayNumber = remainDayNumber;
    }

    public void setByJsonObject(JsonObject oneFlightJsonObject) {

        if (oneFlightJsonObject == null || oneFlightJsonObject.isJsonNull())
            return;

        if (oneFlightJsonObject.has("_id"))
            this.id = oneFlightJsonObject.get("_id").getAsString();

        if (oneFlightJsonObject.has("plane") && !oneFlightJsonObject.get("plane").isJsonNull()) {

            Plane onePlane = new Plane();

            JsonObject onePlaneJsonObject = oneFlightJsonObject.get("plane").getAsJsonObject();

            onePlane.setByJsonObject(onePlaneJsonObject);

            this.plane = onePlane;
        }

        if (oneFlightJsonObject.has("origin") && !oneFlightJsonObject.get("origin").isJsonNull()) {

            City fromCity = new City();

            JsonObject fromCityJsonObject = oneFlightJsonObject.get("origin").getAsJsonObject();

            fromCity.setByJsonObject(fromCityJsonObject);

            this.fromCity = fromCity;
        }

        if (oneFlightJsonObject.has("destination") && !oneFlightJsonObject.get("destination").isJsonNull()) {

            City toCity = new City();

            JsonObject toCityJsonObject = oneFlightJsonObject.get("destination").getAsJsonObject();

            toCity.setByJsonObject(toCityJsonObject);

            this.toCity = toCity;
        }

        if (oneFlightJsonObject.has("pricePerSeat") && !oneFlightJsonObject.get("pricePerSeat").isJsonNull())
            this.pricePerSeat = oneFlightJsonObject.get("pricePerSeat").getAsDouble();

        if (oneFlightJsonObject.has("departureHour") && !oneFlightJsonObject.get("departureHour").isJsonNull())
            this.departureTime = BaseActivity.convertServerDateStringToTimeStamp(oneFlightJsonObject.get("departureHour").getAsString());

        if (oneFlightJsonObject.has("arrivalHour") && !oneFlightJsonObject.get("arrivalHour").isJsonNull())
            this.arrivalTime = BaseActivity.convertServerDateStringToTimeStamp(oneFlightJsonObject.get("arrivalHour").getAsString());

        if (oneFlightJsonObject.has("isRegular") && !oneFlightJsonObject.get("isRegular").isJsonNull())
            this.regular = oneFlightJsonObject.get("isRegular").getAsBoolean();

        if (oneFlightJsonObject.has("isAuction") && !oneFlightJsonObject.get("isAuction").isJsonNull())
            this.auction = oneFlightJsonObject.get("isAuction").getAsBoolean();

        if (oneFlightJsonObject.has("seatCount") && !oneFlightJsonObject.get("seatCount").isJsonNull())
            this.totalSeatCount = oneFlightJsonObject.get("seatCount").getAsInt();

    }
}
