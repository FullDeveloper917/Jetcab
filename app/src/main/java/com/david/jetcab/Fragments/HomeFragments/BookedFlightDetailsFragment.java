package com.david.jetcab.Fragments.HomeFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.david.jetcab.BaseActivity;
import com.david.jetcab.MainActivity;
import com.david.jetcab.Models.Flight;
import com.david.jetcab.R;
import com.david.jetcab.Utils.FontManager;
import com.koushikdutta.ion.Ion;

import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("ValidFragment")
public class BookedFlightDetailsFragment extends Fragment {
    private Flight flight;
    private boolean isFistOrSecond;
    private MainActivity activity;
    private Button btnClose;
    
    
    private TextView txtUserSeatCount;
    private TextView txtSeatPerCost;
    private TextView txtTotalCost;
    private TextView txtFromCityName;
    private TextView txtToCityName;
    private TextView txtDepartureTime;
    private TextView txtArrivalTime;
    private TextView txtPlaneName;
    private TextView txtPlaneSpeed;
    private ImageView imgPlane;

    private String imgUrl;

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public BookedFlightDetailsFragment(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booked_flight_details, container, false);

        findViews(view);

        setInit();

        setEvents();

        return view;
    }

    private void findViews(View view) {
        btnClose = view.findViewById(R.id.btnClose_BookedFlightDetailsFrg);
        txtUserSeatCount = view.findViewById(R.id.txtUserSeatCount_HomeFlightDetailFrg);
        txtSeatPerCost = view.findViewById(R.id.txtSeatPerCost_HomeFlightDetailFrg);
        txtTotalCost = view.findViewById(R.id.txtTotalCost_HomeFlightDetailFrg);
        txtFromCityName = view.findViewById(R.id.txtFromCityName_BookedFlightDetailsFrg);
        txtToCityName = view.findViewById(R.id.txtToCityName_BookedFlightDetailsFrg);
        txtDepartureTime = view.findViewById(R.id.txtDepartureTime_HoemFlightDetailFrg);
        txtArrivalTime = view.findViewById(R.id.txtArrivalTime_HomeFlightDetailFrg);
        txtPlaneName = view.findViewById(R.id.txtPlaneName_BookedFlightDetailsFrg);
        txtPlaneSpeed = view.findViewById(R.id.txtPlaneSpeed_BookedFlightDetailsFrg);
        imgPlane = view.findViewById(R.id.imgPlane_BookedFlightDetailsFrg);
        FontManager.setFontType(view.findViewById(R.id.layoutMain_BookedFlightDetailsFrg), FontManager.getTypeface(activity, FontManager.OPTIMA));
    }

    private void setInit() {

        txtPlaneName.setText(flight.getPlane().getBranch());
        txtPlaneSpeed.setText(new StringBuilder().append(flight.getPlane().getSpeed()));
        txtFromCityName.setText(flight.getFromCity().getName());
        txtToCityName.setText(flight.getToCity().getName());
        txtDepartureTime.setText(BaseActivity.convertTimeStampToDateString(flight.getDepartureTime()));
        txtArrivalTime.setText(BaseActivity.convertTimeStampToDateString(flight.getArrivalTime()));

        isFistOrSecond = true;

        if (flight.getPlane().getSecondImageUrl().trim().equals("")){
            Ion.with(imgPlane)
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.no_image)
                    .load(flight.getPlane().getFirstImageUrl());
        } else {
            Timer t = new Timer();
            //Set the schedule function and rate
            t.scheduleAtFixedRate(new TimerTask() {

                                      @Override
                                      public void run() {
                                          //Called each time when 1000 milliseconds (1 second) (the period parameter)

                                          if (isFistOrSecond)
                                              imgUrl = flight.getPlane().getFirstImageUrl();
                                          else
                                              imgUrl = flight.getPlane().getSecondImageUrl();

                                          isFistOrSecond = !isFistOrSecond;
                                          activity.runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {
                                                  Ion.with(imgPlane).load(imgUrl);
                                              }
                                          });
                                      }

                                  },
                    //Set how long before to start calling the TimerTask (in milliseconds)
                    0,
                    //Set the amount of time between each execution (in milliseconds)
                    3000);
        }
    }

    private void setEvents() {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.getSupportFragmentManager().beginTransaction().remove(BookedFlightDetailsFragment.this).commit();
            }
        });
    }
}
