package com.david.jetcab.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.david.jetcab.BaseActivity;
import com.david.jetcab.Fragments.HomeFragments.BookedFlightDetailsFragment;
import com.david.jetcab.MainActivity;
import com.david.jetcab.Models.Flight;
import com.david.jetcab.R;
import com.david.jetcab.Utils.FontManager;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 05/12/2017.
 */

public class BookedFlightListAdapter extends ArrayAdapter{
    private MainActivity activity;
    private int resource;

    private List<Flight> list = new ArrayList<>();

    private ImageView imgPlane;
    private TextView txtCities;
    private TextView txtETD;
    private TextView txtETA;
    private TextView txtPrice;
    private TextView txtTailNum;
    private TextView txtSeatCount;
    private LinearLayout layoutItem;

    private String imgUrl;
    private boolean isFistOrSecond;

    public void setList(List<Flight> list) {
        this.list = list;
    }

    public BookedFlightListAdapter(@NonNull MainActivity activity, @LayoutRes int resource) {
        super(activity, resource);
        this.activity = activity;
        this.resource =resource;
        isFistOrSecond = false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource, parent, false);

        findViews(view);

        setValues(position);

        setEvents(position);

        return view;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public void changeFirstOrSecond() {
        isFistOrSecond = !isFistOrSecond;
    }

    private void findViews(View view) {
        txtCities = view.findViewById(R.id.txtCities_OneBookedFlight);
        txtETD = view.findViewById(R.id.txtETD_OneBookedFlight);
        txtETA = view.findViewById(R.id.txtETA_OneBookedFlight);
        txtPrice = view.findViewById(R.id.txtPrice_OneBookedFlight);
        txtTailNum = view.findViewById(R.id.txtTailNum_OneBookedFlight);
        txtSeatCount = view.findViewById(R.id.txtSeatCount_OneBookedFlight);
        imgPlane = view.findViewById(R.id.imgPlane_OneBookedFlight);
        layoutItem = view.findViewById(R.id.layoutItem_OneBookedFlight);

        FontManager.setFontType(layoutItem, FontManager.getTypeface(activity, FontManager.OPTIMA));
    }

    private void setValues(int i) {
        Flight currentFlight = list.get(i);
        txtCities.setText(currentFlight.getFromCity().getName() + "-" + currentFlight.getToCity().getName());
        txtETD.setText(BaseActivity.convertTimeStampToDateString(currentFlight.getDepartureTime()));
        txtETA.setText(BaseActivity.convertTimeStampToDateString(currentFlight.getArrivalTime()));
        txtTailNum.setText(currentFlight.getPlane().getPlaneCode());
        txtSeatCount.setText(String.valueOf(currentFlight.getOrderingSeatCount()));

        if (currentFlight.getPlane().getSecondImageUrl() == null || currentFlight.getPlane().getSecondImageUrl().trim().equals("")) {
            imgUrl = currentFlight.getPlane().getFirstImageUrl();
        } else {
            if (isFistOrSecond) {
                imgUrl = currentFlight.getPlane().getFirstImageUrl();
            } else {
                imgUrl = currentFlight.getPlane().getSecondImageUrl();
            }
        }
        Ion.with(imgPlane).load(imgUrl);
    }

    private void setEvents(int position) {
        layoutItem.setTag(position);
        layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = (int) view.getTag();
                BookedFlightDetailsFragment bookedFlightDetailsFragment = new BookedFlightDetailsFragment(activity);
                bookedFlightDetailsFragment.setFlight(list.get(i));
                activity.getSupportFragmentManager().beginTransaction().add(R.id.frgMain, bookedFlightDetailsFragment).commit();
            }
        });
    }
}
