package com.david.jetcab.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.david.jetcab.BaseActivity;
import com.david.jetcab.Fragments.HomeFragments.HomeFlightDetailsFragment;
import com.david.jetcab.MainActivity;
import com.david.jetcab.Models.Flight;
import com.david.jetcab.Models.Plane;
import com.david.jetcab.R;
import com.david.jetcab.Utils.FontManager;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 03/12/2017.
 */

public class FlightsListAdapter extends ArrayAdapter {

    private MainActivity activity;
    private int resource;

    private List<Flight> list = new ArrayList<>();


    private ImageView imgPlane;
    private TextView txtFromCity;
    private TextView txtToCity;
    private TextView txtCost;
    private TextView txtTime;
    private TextView txtSeatNumber;
    private TextView txtConfirmSeatNumber;
    private TextView txtRemainSeatNumber;
    private LinearLayout layoutOneFlight;
    private LinearLayout layoutNonAuctionInfo;
    private LinearLayout layoutAuctionInfo;
    private String imgUrl;
    private boolean isFistOrSecond;

    public void setList(List<Flight> list) {
        this.list = list;
    }

    public FlightsListAdapter(MainActivity activity, @LayoutRes int resource) {
        super(activity, resource);
        this.activity = activity;
        this.resource =resource;
        isFistOrSecond = false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        txtFromCity = view.findViewById(R.id.txtFromCity_OneFlight);
        txtToCity = view.findViewById(R.id.txtToCity_OneFlight);
        txtCost = view.findViewById(R.id.txtCost_OneFlight);
        txtTime = view.findViewById(R.id.txtTime_OneFlight);
        txtSeatNumber = view.findViewById(R.id.txtSeatNumber_OneFlight);
        txtConfirmSeatNumber = view.findViewById(R.id.txtConfirmSeatNumber_OneFlight);
        txtRemainSeatNumber = view.findViewById(R.id.txtRemainDayNumber_OneFlight);
        layoutOneFlight = view.findViewById(R.id.layoutOneFlight_OneFlight);
        layoutNonAuctionInfo = view.findViewById(R.id.layoutNonAuctionFlightInfo_OneFlight);
        layoutAuctionInfo = view.findViewById(R.id.layoutAuctionFlightInfo_OneFlight);
        imgPlane = view.findViewById(R.id.imgPlane_OneFlight);

        FontManager.setFontType(view.findViewById(R.id.layoutOneFlight_OneFlight), FontManager.getTypeface(activity, FontManager.OPTIMA));
    }

    private void setValues(int i) {
        Flight currentFlight = list.get(i);
        txtFromCity.setText(currentFlight.getFromCity().getName());
        txtToCity.setText(currentFlight.getToCity().getName());
        txtTime.setText(BaseActivity.convertTimeStampToDateString(currentFlight.getDepartureTime()));

        if (currentFlight.isRegular()) {
            layoutAuctionInfo.setVisibility(View.GONE);
            layoutNonAuctionInfo.setVisibility(View.VISIBLE);
            txtCost.setText(new StringBuilder().append("$").append(currentFlight.getTotalPrice()));
            txtConfirmSeatNumber.setText(new StringBuilder().append(currentFlight.getConfirmSeatNumber()).append(" seats to confirm"));
            txtRemainSeatNumber.setText(new StringBuilder().append(currentFlight.getRemainDayNumber()).append(" days remaining"));
        } else if (currentFlight.isAuction()) {
            layoutAuctionInfo.setVisibility(View.VISIBLE);
            layoutNonAuctionInfo.setVisibility(View.GONE);

            txtCost.setText(new StringBuilder().append("$").append(currentFlight.getLowPrice()).append(" - $").append(currentFlight.getHighPrice()));
            txtSeatNumber.setText(new StringBuilder().append(currentFlight.getAvailableSeatCount()));
        }

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
        layoutOneFlight.setTag(position);
        layoutOneFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = (int) view.getTag();
                Flight currentFlight = list.get(i);

                HomeFlightDetailsFragment homeFlightDetailsFragment = new HomeFlightDetailsFragment(activity);
                homeFlightDetailsFragment.setFlight(currentFlight);
                activity.getSupportFragmentManager().beginTransaction().add(R.id.frgMain, homeFlightDetailsFragment).commit();
            }
        });
    }
}
