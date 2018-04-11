package com.david.jetcab.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.david.jetcab.Adapters.PlanesListAdapter;
import com.david.jetcab.BaseActivity;
import com.david.jetcab.MainActivity;
import com.david.jetcab.R;
import com.david.jetcab.Utils.APIs;
import com.david.jetcab.Utils.FontManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("ValidFragment")
public class PlanesFragment extends Fragment {

    private ListView listPlanes;
    private PlanesListAdapter planesListAdapter;

    private MainActivity activity;

    public PlanesFragment(MainActivity activity) {
        super();
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_planes, container, false);

        findViews(view);

        setInit();

        setEvents();

        return view;
    }

    private void findViews(View view) {
        listPlanes = view.findViewById(R.id.listPlanes_PlanesFrg);

        FontManager.setFontType(view.findViewById(R.id.layoutMain_PlanesFrg), FontManager.getTypeface(activity, FontManager.OPTIMA));
    }

    private void setInit() {
        planesListAdapter = new PlanesListAdapter(activity, R.layout.list_item_of_planes);
    }


    private void setEvents() {
        planesListAdapter.setList(BaseActivity.allPlanesList);
        listPlanes.setAdapter(planesListAdapter);

        //Declare the timer
        Timer t = new Timer();
        //Set the schedule function and rate
        t.scheduleAtFixedRate(new TimerTask() {

                                  @Override
                                  public void run() {
                                      //Called each time when 1000 milliseconds (1 second) (the period parameter)

                                      planesListAdapter.changeFirstOrSecond();
                                      activity.runOnUiThread(new Runnable() {
                                          @Override
                                          public void run() {
                                              planesListAdapter.notifyDataSetChanged();
                                          }
                                      });

                                  }

                              },
                //Set how long before to start calling the TimerTask (in milliseconds)
                0,
                //Set the amount of time between each execution (in milliseconds)
                2000);


    }
}
