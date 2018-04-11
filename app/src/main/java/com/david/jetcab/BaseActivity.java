package com.david.jetcab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.david.jetcab.Models.City;
import com.david.jetcab.Models.Flight;
import com.david.jetcab.Models.Plane;
import com.david.jetcab.Models.User;
import com.david.jetcab.Utils.APIs;
import com.david.jetcab.Utils.Constants;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by david on 02/12/2017.
 */

public class BaseActivity extends AppCompatActivity {
    public static User user;
    public static List<City> allCitiesList;
    public static List<String> allCitiesName;
    public static List<Plane> allPlanesList;
    public static List<Flight> allFlightsList;
    public static List<Flight> myFlightsList;
    public static Plane defaultPlane;


    private boolean planesGetFlag;
    private boolean defaultPlaneGetFlag;

    public ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static City getCityFromList(String cityName) {
        for (City oneCity: allCitiesList) {
            if (cityName.equalsIgnoreCase(oneCity.getName()))
                return oneCity;
        }
        return null;
    }

    public static String getCurrentDate(){
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yy/MM/dd");
        return df.format(c.getTime());
    }

    public static String getCurrentTime(){
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(c.getTime());
    }

    public static String getCurrentDatAndTime(){
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
        return df.format(c.getTime());
    }


    public static long convertDateStringToTimeStamp(String strDate) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm");
        Date date = null;
        try {
            date = (Date)formatter.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null)
            return -1;
        else
            return date.getTime();
    }

    public static String convertTimeStampToDateString(long timeStamp) {

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeStamp);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
        return df.format(c.getTime());
    }

    public static double calcCrow(City city1, City city2) {
        double lat1 = city1.getLatitude();
        double lon1 = city1.getLongitude();
        double lat2 = city2.getLatitude();
        double lon2 = city2.getLongitude();
        int  R = 6371; // km
        double dLat = toRad(lat2-lat1);
        double dLon = toRad(lon2-lon1);
        lat1 = toRad(lat1);
        lat2 = toRad(lat2);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }

    // Converts numeric degrees to radians
    private static double toRad(double value) {
        return value * Math.PI / 180;
    }

    public static long convertServerDateStringToTimeStamp(String strDate) {
        strDate = strDate.substring(0, 10) + " " + strDate.substring(11, 16);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = (Date)formatter.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null)
            return -1;
        else
            return date.getTime();
    }

    public void getInfo(final boolean isUpdated) {
        planesGetFlag = false;
        Ion.with(getApplicationContext())
                .load(APIs.PLANES_LIST_API)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (result == null) {
                            progressBar.setVisibility(View.GONE);
                            return;
                        }
                        switch (result.getHeaders().code()) {
                            case 200:
                                JsonArray planesJsonArray = result.getResult().get("planes").getAsJsonArray();

                                allPlanesList = new ArrayList<>();
                                for (JsonElement onePlaneJsonElement : planesJsonArray) {

                                    JsonObject onePlaneJsonObject = onePlaneJsonElement.getAsJsonObject();

                                    Plane onePlane = new Plane();

                                    onePlane.setByJsonObject(onePlaneJsonObject);

                                    allPlanesList.add(onePlane);

                                }

                                if (!isUpdated) {
                                    planesGetFlag = true;
                                    goMainActivity();
                                }

                                break;
                            default:
                                if (result.getResult().has("errors"))
                                    Toast.makeText(getApplicationContext(), result.getResult().get("errors").toString(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                break;
                        }
                    }
                });

        defaultPlaneGetFlag = false;
        Ion.with(getApplicationContext())
                .load(APIs.GET_DEFAULT_PLANE_API)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (result == null) {
                            progressBar.setVisibility(View.GONE);
                            return;
                        }
                        switch (result.getHeaders().code()) {
                            case 200:
                                if (result.getResult().get("config").isJsonNull()) {
                                    defaultPlane = null;
                                } else {
                                    JsonObject defaultPlaneJsonObject = result.getResult().get("config").getAsJsonObject().get("defaultPlane").getAsJsonObject();

                                    defaultPlane = new Plane();

                                    defaultPlane.setByJsonObject(defaultPlaneJsonObject);
                                }
                                if (!isUpdated) {
                                    defaultPlaneGetFlag = true;
                                    goMainActivity();
                                }

                                break;
                            default:
                                if (result.getResult().has("errors"))
                                    Toast.makeText(getApplicationContext(), result.getResult().get("errors").toString(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                break;
                        }
                    }
                });
    }

    private void goMainActivity() {

        if (!planesGetFlag) return;
        if (!defaultPlaneGetFlag) return;
        progressBar.setVisibility(View.GONE);

        Intent intent = new Intent(BaseActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
