package com.david.jetcab.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.david.jetcab.BaseActivity;
import com.david.jetcab.Fragments.BookingFragments.BookingSecondFragment;
import com.david.jetcab.MainActivity;
import com.david.jetcab.Models.Plane;
import com.david.jetcab.R;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 05/12/2017.
 */

public class BookingPlanesListAdapter extends ArrayAdapter{
    private MainActivity activity;
    private int resource;
    private BookingSecondFragment fragment;

    private List<Plane> list = new ArrayList<>();
    private boolean[] checked;

    private ImageView imgPlane;
    private TextView txtName;
    private TextView txtPrice;
    private CheckBox chkSelected;
    private LinearLayout layoutItem;

    private String imgUrl;
    private boolean isFistOrSecond;

    public void setFragment(BookingSecondFragment fragment) {
        this.fragment = fragment;
    }

    public void setList(List<Plane> list) {
        this.list = list;
        this.checked = new boolean[list.size()];
    }

    public BookingPlanesListAdapter(@NonNull MainActivity activity, @LayoutRes int resource) {
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
        chkSelected = view.findViewById(R.id.chkSelected_OneBookingPlane);
        txtName = view.findViewById(R.id.txtName_OneBookingPlane);
        txtPrice = view.findViewById(R.id.txtPrice_OneBookingPlane);
        imgPlane = view.findViewById(R.id.imgPlane_OneBookingPlane);
        layoutItem = view.findViewById(R.id.layoutItem_OneBookingPlane);
    }

    private void setValues(int i) {
        Plane currentPlane = list.get(i);
        txtName.setText(currentPlane.getBranch());
        txtPrice.setText("$" + currentPlane.getPricePerKm());
        chkSelected.setChecked(checked[i]);

        if (currentPlane.getSecondImageUrl() == null || currentPlane.getSecondImageUrl().trim().equals("")) {
            imgUrl = currentPlane.getFirstImageUrl();
        } else {
            if (isFistOrSecond) {
                imgUrl = currentPlane.getFirstImageUrl();
            } else {
                imgUrl = currentPlane.getSecondImageUrl();
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
                Plane currentPlane = list.get(i);

                fragment.layoutDetailOfPlane.setVisibility(View.VISIBLE);

                fragment.layoutDetailOfPlane.setY(view.getY() + fragment.layoutDetailOfPlane.getHeight());


                fragment.txtPlaneName.setText(currentPlane.getBranch());

                if (currentPlane.getSeatCount() == 1)
                    fragment.txtSeatCount.setText(new StringBuilder().append(currentPlane.getSeatCount()).append(" seat"));
                else
                    fragment.txtSeatCount.setText(new StringBuilder().append(currentPlane.getSeatCount()).append(" seats"));

                fragment.txtPlaneSpeed.setText(new StringBuilder().append(currentPlane.getSpeed()).append(" km/hr"));

                Ion.with(fragment.imgPlane).placeholder(R.drawable.no_image).error(R.drawable.no_image).load(currentPlane.getFirstImageUrl());



            }
        });

        chkSelected.setTag(position);
        chkSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = (int) view.getTag();
                CheckBox currentCheckBox = (CheckBox) view;

                checked = new boolean[list.size()];

                if (currentCheckBox.isChecked()) {
                    checked[i] = true;
                }
                notifyDataSetChanged();

                activity.creatingFlight.setPlane(list.get(i));
                activity.calculateOtherInfoOfCreatingFlight();

                fragment.layoutFlightInfo.setVisibility(View.VISIBLE);
                fragment.txtArrivalTime.setText(BaseActivity.convertTimeStampToDateString(activity.creatingFlight.getArrivalTime()));
                fragment.txtFinalPrice.setText("$" + Math.round(activity.creatingFlight.getFinalPrice()));
            }
        });
    }
}
