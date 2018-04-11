package com.david.jetcab.Fragments.BookingFragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.david.jetcab.BaseActivity;
import com.david.jetcab.MainActivity;
import com.david.jetcab.Models.City;
import com.david.jetcab.Models.Flight;
import com.david.jetcab.R;
import com.david.jetcab.Utils.APIs;
import com.david.jetcab.Utils.FontManager;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.Calendar;
import java.util.TimeZone;

@SuppressLint("ValidFragment")
public class BookingFirstFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private MainActivity activity;
    private Button btnContinue;
    private AutoCompleteTextView actFromCity;
    private AutoCompleteTextView actToCity;
    private TextView txtDate;
    private TextView txtTime;
    private ToggleButton tglAuction;
    private EditText edtBuySeats;

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    private String fromCityName;
    private String toCityName;
    private String date;
    private String time;
    private Integer buySeatCount;

    public LinearLayout layoutFlightInfo;
    public TextView txtETA;
    public TextView txtLowPrice;
    public TextView txtTopPrice;

    public BookingFirstFragment(MainActivity activity) {
        super();
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking_first, container, false);

        findViews(view);

        setInit();

        setEvents();

        return view;
    }

    private void findViews(View view) {

        btnContinue = view.findViewById(R.id.btnContinue_BookingFirstFrg);
        actFromCity = view.findViewById(R.id.actFromCity_BookingFirstFrg);
        actToCity = view.findViewById(R.id.actToCity_BookingFirstFrg);
        txtDate = view.findViewById(R.id.txtDate_BookingFirstFrg);
        txtTime = view.findViewById(R.id.txtTime_BookingFirstFrg);
        tglAuction = view.findViewById(R.id.tglAuction_BookingFirstFrg);
        layoutFlightInfo = view.findViewById(R.id.layoutFlightInfo_BookingFirstFrg);
        txtLowPrice = view.findViewById(R.id.txtLowPrice_BookingFirstFrg);
        txtTopPrice = view.findViewById(R.id.txtTopPrice_BookingFirstFrg);
        txtETA = view.findViewById(R.id.txtEta_BookingFirstFrg);
        edtBuySeats = view.findViewById(R.id.edtBuySeats_BookingFirstFrg);
        FontManager.setFontType(view.findViewById(R.id.layoutMain_BookingFirstFrg), FontManager.getTypeface(activity, FontManager.OPTIMA));
    }

    private void setInit() {


        tglAuction.setSelected(false);
        layoutFlightInfo.setVisibility(View.GONE);

        activity.creatingFlight = new Flight();

        actFromCity.setThreshold(1);
        actFromCity.setAdapter(new ArrayAdapter<String> (activity, android.R.layout.select_dialog_item, BaseActivity.allCitiesName));
        actToCity.setThreshold(1);
        actToCity.setAdapter(new ArrayAdapter<String> (activity, android.R.layout.select_dialog_item, BaseActivity.allCitiesName));

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        datePickerDialog = new DatePickerDialog(activity, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        timePickerDialog = new TimePickerDialog(activity, this, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), false);

    }

    private void setEvents() {
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog.show();
            }
        });


        tglAuction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BaseActivity.defaultPlane == null) {
                    Toast.makeText(activity, "Auction Plane is not setted.", Toast.LENGTH_SHORT).show();
                    tglAuction.setChecked(false);
                }
                if (tglAuction.isChecked()) {
                    if (isValidate()) {
                        tglAuction.setChecked(false);
                        layoutFlightInfo.setVisibility(View.VISIBLE);
                        activity.creatingFlight.setPlane(BaseActivity.defaultPlane);
                        activity.calculateOtherInfoOfCreatingFlight();
                        txtETA.setText(BaseActivity.convertTimeStampToDateString(activity.creatingFlight.getArrivalTime()));
                    }
                } else {
                    layoutFlightInfo.setVisibility(View.GONE);
                }
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isValidate()) {

                    if (!tglAuction.isChecked()) {
                        activity.getSupportFragmentManager().beginTransaction().add(R.id.frgMain, new BookingSecondFragment(activity)).commit();
                    } else {

                        String buySeatsString = edtBuySeats.getText().toString();
                        if (buySeatsString.isEmpty()) {
                            Toast.makeText(activity, "Please insert seat count to order!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!isNumeric(buySeatsString)) {
                            Toast.makeText(activity, "Seat count is numeric!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        buySeatCount = Integer.parseInt(buySeatsString);

                        if (buySeatCount < 1 || buySeatCount > activity.creatingFlight.getPlane().getSeatCount()) {
                            Toast.makeText(activity, "Invalid Number!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("origin", activity.creatingFlight.getFromCity().getId());
                        jsonObject.addProperty("destination", activity.creatingFlight.getToCity().getId());
                        jsonObject.addProperty("departureHour", activity.creatingFlight.getDepartureTime());
                        jsonObject.addProperty("arrivalHour", activity.creatingFlight.getArrivalTime());
                        jsonObject.addProperty("travelTime", (activity.creatingFlight.getArrivalTime() - activity.creatingFlight.getDepartureTime()) / 60000);
                        jsonObject.addProperty("plane", activity.creatingFlight.getPlane().getId());
                        jsonObject.addProperty("plane", activity.creatingFlight.getPlane().getId());
                        jsonObject.addProperty("buySeats", buySeatCount);
                        jsonObject.addProperty("isAuction", true);
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

                }

            }
        });

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        String _year, _month, _day;

        _year = String.valueOf(year);
        monthOfYear++;
        if (monthOfYear < 10)
            _month = "0" + String.valueOf(monthOfYear);
        else
            _month = String.valueOf(monthOfYear);

        if (dayOfMonth < 10)
            _day = "0" + String.valueOf(dayOfMonth);
        else
            _day = String.valueOf(dayOfMonth);

        txtDate.setText(new StringBuilder().append(_day).append("/").append(_month).append("/").append(_year.substring(2, 4)));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        String _hour, _minute;
        if (hourOfDay < 10)
            _hour = "0" + String.valueOf(hourOfDay);
        else
            _hour = String.valueOf(hourOfDay);

        if (minute < 10)
            _minute = "0" + String.valueOf(minute);
        else
            _minute = String.valueOf(minute);

        txtTime.setText(new StringBuilder().append(_hour).append(":").append(_minute));

    }

    private boolean isValidate() {

        fromCityName = actFromCity.getText().toString();
        toCityName = actToCity.getText().toString();
        date = txtDate.getText().toString();
        time = txtTime.getText().toString();


        if (fromCityName.isEmpty()) {
            Toast.makeText(activity, "Please insert original city!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidateCity(fromCityName)) {
            Toast.makeText(activity, "Original city name is not correct!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (toCityName.isEmpty()) {
            Toast.makeText(activity, "Please insert destination city!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidateCity(toCityName)) {
            Toast.makeText(activity, "Destination city name is not correct!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (date.isEmpty()) {
            Toast.makeText(activity, "Please insert departure date!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (time.isEmpty()) {
            Toast.makeText(activity, "Please insert departure time!", Toast.LENGTH_SHORT).show();
            return false;
        }


        activity.creatingFlight.setFromCity(BaseActivity.getCityFromList(fromCityName));
        activity.creatingFlight.setToCity(BaseActivity.getCityFromList(toCityName));
        activity.creatingFlight.setDepartureTime(BaseActivity.convertDateStringToTimeStamp(date + " " + time));
        return true;
    }

    private boolean isValidateCity(String cityName) {
        for (City oneCity : BaseActivity.allCitiesList) {
            if (cityName.equalsIgnoreCase(oneCity.getName()))
                return true;
        }
        return false;
    }

    private boolean isNumeric(String s) {
        return s.matches("\\d+(?:\\.\\d+)?");
    }


}
