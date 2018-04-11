package com.david.jetcab.Fragments.HomeFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.david.jetcab.Adapters.BookedFlightListAdapter;
import com.david.jetcab.Adapters.FlightsListAdapter;
import com.david.jetcab.BaseActivity;
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
public class HomeFragment extends Fragment {

    private ListView listFlights;
    private FlightsListAdapter flightsListAdapter;
    private BookedFlightListAdapter bookedFlightListAdapter;

    private Button btnInterest;
    private Button btnBooked;
    private Button btnAvailable;
    private Button btnMyFlights;

    private MainActivity activity;

    private List<Flight> interestedFlight;

    public HomeFragment(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        findViews(view);

        setEvents();

        setInit();

        return view;
    }

    private void findViews(View view) {
        listFlights = view.findViewById(R.id.listFlights_HomeFrg);
        btnInterest = view.findViewById(R.id.btnInterest_HomeFrg);
        btnBooked = view.findViewById(R.id.btnBooked_HomeFrg);
        btnAvailable = view.findViewById(R.id.btnAvailable_HomeFrg);
        btnMyFlights = view.findViewById(R.id.btnMyFlights_HomeFrg);

        FontManager.setFontType(view.findViewById(R.id.layoutMain_HomeFrg), FontManager.getTypeface(activity, FontManager.OPTIMA));
    }

    private void setInit() {

        btnInterest.performClick();
    }

    private void setEvents() {

        btnInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                btnInterest.setBackgroundColor(getResources().getColor(R.color.colorDarkGray));
                btnBooked.setBackgroundColor(getResources().getColor(R.color.colorBrigitGray));
                btnAvailable.setBackgroundColor(getResources().getColor(R.color.colorBrigitGray));
                btnMyFlights.setBackgroundColor(getResources().getColor(R.color.colorBrigitGray));

                interestedFlight = new ArrayList<>();
                activity.progressBar.setVisibility(View.VISIBLE);
                Ion.with(activity)
                        .load(APIs.FLIGHTS_LIST_API)
                        .asJsonObject()
                        .withResponse()
                        .setCallback(new FutureCallback<Response<JsonObject>>() {
                            @Override
                            public void onCompleted(Exception e, Response<JsonObject> result) {

                                activity.progressBar.setVisibility(View.GONE);
                                if (result == null)
                                    return;
                                switch (result.getHeaders().code()) {
                                    case 200:


                                        JsonArray flightsJsonArray = result.getResult().get("flights").getAsJsonArray();

                                        for (JsonElement oneFlightJsonElement : flightsJsonArray) {

                                            JsonObject oneFlightJsonObject = oneFlightJsonElement.getAsJsonObject();

                                            Flight oneFlight = new Flight();

                                            oneFlight.setByJsonObject(oneFlightJsonObject);

                                            if (isInterested(oneFlight))
                                                interestedFlight.add(oneFlight);

                                        }


                                        flightsListAdapter = new FlightsListAdapter(activity, R.layout.list_item_of_flights);
                                        flightsListAdapter.setList(interestedFlight);
                                        listFlights.setAdapter(flightsListAdapter);

                                        //Declare the timer
                                        Timer t = new Timer();
                                        //Set the schedule function and rate
                                        t.scheduleAtFixedRate(new TimerTask() {

                                                                  @Override
                                                                  public void run() {
                                                                      //Called each time when 1000 milliseconds (1 second) (the period parameter)

                                                                      flightsListAdapter.changeFirstOrSecond();
                                                                      activity.runOnUiThread(new Runnable() {
                                                                          @Override
                                                                          public void run() {
                                                                              flightsListAdapter.notifyDataSetChanged();
                                                                          }
                                                                      });

                                                                  }

                                                              },
                                                //Set how long before to start calling the TimerTask (in milliseconds)
                                                0,
                                                //Set the amount of time between each execution (in milliseconds)
                                                2000);

                                        break;
                                    default:
                                        if (result.getResult().has("errors"))
                                            Toast.makeText(activity, result.getResult().get("errors").toString(), Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        });
            }
        });

        btnAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnInterest.setBackgroundColor(getResources().getColor(R.color.colorBrigitGray));
                btnBooked.setBackgroundColor(getResources().getColor(R.color.colorBrigitGray));
                btnAvailable.setBackgroundColor(getResources().getColor(R.color.colorDarkGray));
                btnMyFlights.setBackgroundColor(getResources().getColor(R.color.colorBrigitGray));



                allFlightsList = new ArrayList<>();
                activity.progressBar.setVisibility(View.VISIBLE);
                Ion.with(activity)
                        .load(APIs.FLIGHTS_LIST_API)
                        .asJsonObject()
                        .withResponse()
                        .setCallback(new FutureCallback<Response<JsonObject>>() {
                            @Override
                            public void onCompleted(Exception e, Response<JsonObject> result) {

                                activity.progressBar.setVisibility(View.GONE);
                                if (result == null)
                                    return;
                                switch (result.getHeaders().code()) {
                                    case 200:


                                        JsonArray flightsJsonArray = result.getResult().get("flights").getAsJsonArray();

                                        for (JsonElement oneFlightJsonElement : flightsJsonArray) {

                                            JsonObject oneFlightJsonObject = oneFlightJsonElement.getAsJsonObject();

                                            Flight oneFlight = new Flight();

                                            oneFlight.setByJsonObject(oneFlightJsonObject);

                                            allFlightsList.add(oneFlight);
                                        }


                                        flightsListAdapter = new FlightsListAdapter(activity, R.layout.list_item_of_flights);
                                        flightsListAdapter.setList(allFlightsList);
                                        listFlights.setAdapter(flightsListAdapter);

                                        //Declare the timer
                                        Timer t = new Timer();
                                        //Set the schedule function and rate
                                        t.scheduleAtFixedRate(new TimerTask() {

                                                                  @Override
                                                                  public void run() {
                                                                      //Called each time when 1000 milliseconds (1 second) (the period parameter)

                                                                      flightsListAdapter.changeFirstOrSecond();
                                                                      activity.runOnUiThread(new Runnable() {
                                                                          @Override
                                                                          public void run() {
                                                                              flightsListAdapter.notifyDataSetChanged();
                                                                          }
                                                                      });

                                                                  }

                                                              },
                                                //Set how long before to start calling the TimerTask (in milliseconds)
                                                0,
                                                //Set the amount of time between each execution (in milliseconds)
                                                2000);

                                        break;
                                    default:
                                        if (result.getResult().has("errors"))
                                            Toast.makeText(activity, result.getResult().get("errors").toString(), Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        });
            }
        });


        btnBooked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                myFlightsList = new ArrayList<>();

                btnInterest.setBackgroundColor(getResources().getColor(R.color.colorBrigitGray));
                btnBooked.setBackgroundColor(getResources().getColor(R.color.colorDarkGray));
                btnAvailable.setBackgroundColor(getResources().getColor(R.color.colorBrigitGray));
                btnMyFlights.setBackgroundColor(getResources().getColor(R.color.colorBrigitGray));

                myFlightsList.clear();
                activity.progressBar.setVisibility(View.VISIBLE);
                Ion.with(activity)
                        .load(APIs.GET_MY_FLIGHT_API)
                        .asJsonObject()
                        .withResponse()
                        .setCallback(new FutureCallback<Response<JsonObject>>() {
                            @Override
                            public void onCompleted(Exception e, Response<JsonObject> result) {

                                activity.progressBar.setVisibility(View.GONE);
                                if (result == null)
                                    return;

                                switch (result.getHeaders().code()) {
                                    case 200:


                                        JsonArray myFlightsJsonArray = result.getResult().get("flights").getAsJsonArray();

                                        for (JsonElement oneFlightJsonElement : myFlightsJsonArray) {

                                            JsonObject oneFlightJsonObject = oneFlightJsonElement.getAsJsonObject();

                                            Flight oneFlight = new Flight();

                                            oneFlight.setByJsonObject(oneFlightJsonObject);

                                            myFlightsList.add(oneFlight);
                                        }

                                        bookedFlightListAdapter = new BookedFlightListAdapter(activity, R.layout.list_item_of_booked_flight);
                                        bookedFlightListAdapter.setList(myFlightsList);
                                        listFlights.setAdapter(bookedFlightListAdapter);

                                        //Declare the timer
                                        Timer t = new Timer();
                                        //Set the schedule function and rate
                                        t.scheduleAtFixedRate(new TimerTask() {

                                                                  @Override
                                                                  public void run() {
                                                                      //Called each time when 1000 milliseconds (1 second) (the period parameter)

                                                                      bookedFlightListAdapter.changeFirstOrSecond();
                                                                      activity.runOnUiThread(new Runnable() {
                                                                          @Override
                                                                          public void run() {
                                                                              bookedFlightListAdapter.notifyDataSetChanged();
                                                                          }
                                                                      });

                                                                  }

                                                              },
                                                //Set how long before to start calling the TimerTask (in milliseconds)
                                                0,
                                                //Set the amount of time between each execution (in milliseconds)
                                                2000);

                                        break;
                                    default:
                                        if (result.getResult().has("errors"))
                                            Toast.makeText(activity, result.getResult().get("errors").toString(), Toast.LENGTH_SHORT).show();
                                        break;
                                }

                            }
                        });
            }
        });

        btnMyFlights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnBooked.performClick();
                btnInterest.setBackgroundColor(getResources().getColor(R.color.colorBrigitGray));
                btnBooked.setBackgroundColor(getResources().getColor(R.color.colorBrigitGray));
                btnAvailable.setBackgroundColor(getResources().getColor(R.color.colorBrigitGray));
                btnMyFlights.setBackgroundColor(getResources().getColor(R.color.colorDarkGray));
            }
        });
    }

    private boolean isInterested(Flight flight) {
        for (City oneCity : user.getFavoriteCities()) {
            String fromCityRawName = flight.getFromCity().getName().toLowerCase();
            String toCityRawName = flight.getToCity().getName().toLowerCase();
            String onCityRawName = oneCity.getName().toLowerCase();
            if (onCityRawName.equals(fromCityRawName))
                return true;
            if (onCityRawName.equals(toCityRawName))
                return true;
        }
        return false;
    }


}
