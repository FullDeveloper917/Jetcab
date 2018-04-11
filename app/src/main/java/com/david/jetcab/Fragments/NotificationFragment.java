package com.david.jetcab.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.david.jetcab.Adapters.BookedFlightListAdapter;
import com.david.jetcab.Adapters.FlightsListAdapter;
import com.david.jetcab.MainActivity;
import com.david.jetcab.Models.City;
import com.david.jetcab.Models.Flight;
import com.david.jetcab.R;
import com.david.jetcab.Utils.APIs;
import com.david.jetcab.Utils.FontManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.david.jetcab.BaseActivity.allFlightsList;
import static com.david.jetcab.BaseActivity.myFlightsList;
import static com.david.jetcab.BaseActivity.user;

@SuppressLint("ValidFragment")
public class NotificationFragment extends Fragment {

    private TextView txtSubject;
    private ListView listViewNotification;
    private List<Flight> notificationFlights;
    private BookedFlightListAdapter notificationFlightsListAdapter;

    private MainActivity activity;

    public NotificationFragment(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        findViews(view);
        setInit();

        return view;
    }

    private void findViews(View view) {
        listViewNotification = view.findViewById(R.id.listNotification_NotificationFrg);

        FontManager.setFontType(view.findViewById(R.id.layoutMain_NotificationFrg), FontManager.getTypeface(activity, FontManager.OPTIMA));
    }

    private void setInit() {

//                Ion.with(activity)
//                        .load(APIs.FLIGHTS_LIST_API)
//                        .asJsonObject()
//                        .withResponse()
//                        .setCallback(new FutureCallback<Response<JsonObject>>() {
//                            @Override
//                            public void onCompleted(Exception e, Response<JsonObject> result) {
//
//                                activity.progressBar.setVisibility(View.GONE);
//                                if (result == null)
//                                    return;
//                                switch (result.getHeaders().code()) {
//                                    case 200:
//
//                                        JsonArray flightsJsonArray = result.getResult().get("flights").getAsJsonArray();
//                                        for (JsonElement oneFlightJsonElement : flightsJsonArray) {
//                                            JsonObject oneFlightJsonObject = oneFlightJsonElement.getAsJsonObject();
//                                            Flight oneFlight = new Flight();
//                                            oneFlight.setByJsonObject(oneFlightJsonObject);
//                                            notificationFlights.add(oneFlight);
//                                        }
//
//                                        notificationFlightsListAdapter = new BookedFlightListAdapter(activity, R.layout.list_item_of_booked_flight);
//                                        notificationFlightsListAdapter.setList(notificationFlights);
//                                        listViewNotification.setAdapter(notificationFlightsListAdapter);
//
//                                        new Timer().scheduleAtFixedRate(new TimerTask() {
//                                                                  @Override
//                                                                  public void run() {
//                                                                      notificationFlightsListAdapter.changeFirstOrSecond();
//                                                                      activity.runOnUiThread(new Runnable() {
//                                                                          @Override
//                                                                          public void run() {
//                                                                              notificationFlightsListAdapter.notifyDataSetChanged();
//                                                                          }
//                                                                      });
//                                                                  }
//                                                              },0,2000);
//
//                                        break;
//                                    default:
//                                        if (result.getResult().has("errors"))
//                                            Toast.makeText(activity, result.getResult().get("errors").toString(), Toast.LENGTH_SHORT).show();
//                                        break;
//                                }
//                            }
//                        });

    }
}
