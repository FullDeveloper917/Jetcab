package com.david.jetcab.Models;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 02/12/2017.
 */

public class User {
    private String id;
    private String email;
    private String password;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private List<City> favoriteCities;

    public User() {
        this.email = "";
        this.password = "";
        this.phoneNumber = "";
        this.firstName = "";
        this.lastName = "";
        this.favoriteCities = new ArrayList<>();
    }

    public User(User clone) {
        this.email = clone.getEmail();
        this.password = clone.getPassword();
        this.phoneNumber = clone.getPhoneNumber();
        this.firstName = clone.getFirstName();
        this.lastName = clone.getLastName();
        this.favoriteCities = new ArrayList<>(clone.getFavoriteCities());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<City> getFavoriteCities() {
        return favoriteCities;
    }

    public void setFavoriteCities(List<City> favoriteCities) {
        this.favoriteCities = new ArrayList<>(favoriteCities);
    }

    public void addFavoriteCity(City oneCity) {
        this.favoriteCities.add(oneCity);
    }

    public List<String> getIdsList() {
        List<String> idsList = new ArrayList<>();
        for (City oneCity: this.favoriteCities) {
            idsList.add(oneCity.getId());
        }
        return idsList;
    }

    public void setByJsonObject(JsonObject userJsonObject) {
        if (userJsonObject == null || userJsonObject.isJsonNull())
            return;

        if (userJsonObject.has("_id"))
            this.id = userJsonObject.get("_id").getAsString();

        if (userJsonObject.has("email"))
            this.email = userJsonObject.get("email").getAsString();

        if (userJsonObject.has("phoneNumber"))
            this.phoneNumber = userJsonObject.get("phoneNumber").getAsString();

        if (userJsonObject.has("firstName"))
            this.firstName = userJsonObject.get("firstName").getAsString();

        if (userJsonObject.has("lastName"))
            this.lastName = userJsonObject.get("lastName").getAsString();

        if (userJsonObject.has("favoriteCities")) {

            JsonArray citiesJsonArray = userJsonObject.get("favoriteCities").getAsJsonArray();

            for (JsonElement oneCityJsonElement: citiesJsonArray) {
                JsonObject oneCityJsonObject = oneCityJsonElement.getAsJsonObject();

                City oneCity = new City();

                oneCity.setByJsonObject(oneCityJsonObject);

                favoriteCities.add(oneCity);
            }
        }
    }
}
