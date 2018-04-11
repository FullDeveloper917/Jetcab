package com.david.jetcab.Fragments.BookingFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.david.jetcab.Adapters.BookingPlanesListAdapter;
import com.david.jetcab.BaseActivity;
import com.david.jetcab.MainActivity;
import com.david.jetcab.R;
import com.david.jetcab.Utils.APIs;
import com.david.jetcab.Utils.FontManager;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("ValidFragment")
public class BookingSecondFragment extends Fragment {

    private MainActivity activity;
    private Button btnPrev;
    private Button btnContinue;
    private ListView listPlanes;
    private BookingPlanesListAdapter bookingPlanesListAdapter;

    public RelativeLayout layoutDetailOfPlane;
    public TextView txtPlaneName;
    public TextView txtSeatCount;
    public TextView txtPlaneSpeed;
    public ImageView imgPlane;
    public Button btnCloseOfDetails;

    public LinearLayout layoutFlightInfo;
    public TextView txtFromCity;
    public TextView txtToCity;
    public TextView txtDepartureTime;
    public TextView txtArrivalTime;
    public TextView txtFinalPrice;

    public BookingSecondFragment(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking_second, container, false);

        findViews(view);

        setInit();

        setEvents();

        return view;

    }

    private void findViews(View view) {
        btnPrev = view.findViewById(R.id.btnPrev_BookingSecondFrg);
        btnContinue = view.findViewById(R.id.btnContinue_BookingSecondFrg);
        listPlanes = view.findViewById(R.id.listPlanes_BookingSecondFrg);
        layoutDetailOfPlane = view.findViewById(R.id.layoutDetailOfPlane_BookingSecondFrg);
        txtPlaneName = view.findViewById(R.id.txtPlaneName_BookingSecondFrg);
        txtSeatCount = view.findViewById(R.id.txtSeatCount_BookingSecondFrg);
        txtPlaneSpeed = view.findViewById(R.id.txtPlaneSpeed_BookingSecondFrg);
        imgPlane = view.findViewById(R.id.imgPlane_BookingSecondFrg);
        btnCloseOfDetails = view.findViewById(R.id.btnCloseOfDetails_BookingSecondFrg);
        txtFromCity = view.findViewById(R.id.txtFromCity_BookingSecondFrg);
        txtToCity = view.findViewById(R.id.txtToCity_BookingSecondFrg);
        txtDepartureTime = view.findViewById(R.id.txtDepartureTime_BookingSecondFrg);
        txtArrivalTime = view.findViewById(R.id.txtArrivalTime_BookingSecondFrg);
        txtFinalPrice = view.findViewById(R.id.txtFinalPrice_BookingSecondFrg);
        layoutFlightInfo = view.findViewById(R.id.layoutFlightInfo_BookingSecondFrg);
        FontManager.setFontType(view.findViewById(R.id.layoutMain_BookingSecondFrg), FontManager.getTypeface(activity, FontManager.OPTIMA));
    }

    private void setInit() {
        bookingPlanesListAdapter = new BookingPlanesListAdapter(activity, R.layout.list_item_of_booking_planes);
        bookingPlanesListAdapter.setFragment(this);
        bookingPlanesListAdapter.setList(BaseActivity.allPlanesList);
        listPlanes.setAdapter(bookingPlanesListAdapter);

        //Declare the timer
        Timer t = new Timer();
        //Set the schedule function and rate
        t.scheduleAtFixedRate(new TimerTask() {

                                  @Override
                                  public void run() {
                                      //Called each time when 1000 milliseconds (1 second) (the period parameter)

                                      bookingPlanesListAdapter.changeFirstOrSecond();
                                      activity.runOnUiThread(new Runnable() {
                                          @Override
                                          public void run() {
                                              bookingPlanesListAdapter.notifyDataSetChanged();
                                          }
                                      });

                                  }

                              },
                //Set how long before to start calling the TimerTask (in milliseconds)
                0,
                //Set the amount of time between each execution (in milliseconds)
                2000);

        layoutDetailOfPlane.setVisibility(View.GONE);
        txtFromCity.setText(activity.creatingFlight.getFromCity().getName());
        txtToCity.setText(activity.creatingFlight.getToCity().getName());
        txtDepartureTime.setText(BaseActivity.convertTimeStampToDateString(activity.creatingFlight.getDepartureTime()));
    }

    private void setEvents() {
        btnCloseOfDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutDetailOfPlane.setVisibility(View.GONE);
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("origin", activity.creatingFlight.getFromCity().getId());
                jsonObject.addProperty("destination", activity.creatingFlight.getToCity().getId());
                jsonObject.addProperty("departureHour", activity.creatingFlight.getDepartureTime());
                jsonObject.addProperty("arrivalHour", activity.creatingFlight.getArrivalTime());
                jsonObject.addProperty("travelTime", (activity.creatingFlight.getArrivalTime() - activity.creatingFlight.getDepartureTime()) / 60000);
                jsonObject.addProperty("plane", activity.creatingFlight.getPlane().getId());
                jsonObject.addProperty("isAuction", false);
                activity.progressBar.setVisibility(View.VISIBLE);
                Ion.with(activity)
                        .load(APIs.CREATE_FLIGHT_LIST_API)
                        .setJsonObjectBody(jsonObject)
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
                                        Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        });
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.getSupportFragmentManager().beginTransaction().remove(BookingSecondFragment.this).commit();
            }
        });
    }

}
