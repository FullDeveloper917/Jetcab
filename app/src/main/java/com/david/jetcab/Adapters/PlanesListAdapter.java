package com.david.jetcab.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.david.jetcab.MainActivity;
import com.david.jetcab.Models.Flight;
import com.david.jetcab.Models.Plane;
import com.david.jetcab.R;
import com.david.jetcab.Utils.FontManager;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Future;

/**
 * Created by david on 04/12/2017.
 */

public class PlanesListAdapter extends ArrayAdapter {

    private final MainActivity activity;
    private final int resource;

    private List<Plane> list = new ArrayList<>();

    private ImageView imgPlane;
    private TextView txtName;
    private TextView txtSpeed;
    private TextView txtSeatCount;
    private String imgUrl;
    private Plane currentPlane;

    private boolean isFistOrSecond;

    public void setList(List<Plane> list) {
        this.list = list;
    }

    public PlanesListAdapter(@NonNull MainActivity activity, @LayoutRes int resource) {
        super(activity, resource);
        this.activity = activity;
        this.resource =resource;
        this.isFistOrSecond = false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource, parent, false);

        findViews(view);

        setValues(position);

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
        txtName = view.findViewById(R.id.txtName_OnePlane);
        txtSeatCount = view.findViewById(R.id.txtSeatCount_OnePlane);
        txtSpeed = view.findViewById(R.id.txtSpeed_OnePlane);
        imgPlane = view.findViewById(R.id.imgPlane_OnePlane);
        FontManager.setFontType(view.findViewById(R.id.layoutMain_OnePlane), FontManager.getTypeface(activity, FontManager.OPTIMA));
    }

    private void setValues(int i) {
        currentPlane = list.get(i);
        txtName.setText(currentPlane.getBranch());
        if (currentPlane.getSeatCount() < 2)
            txtSeatCount.setText(new StringBuilder().append(currentPlane.getSeatCount()).append(" seat"));
        else
            txtSeatCount.setText(new StringBuilder().append(currentPlane.getSeatCount()).append(" seats"));
        txtSpeed.setText(new StringBuilder().append(currentPlane.getSpeed()));

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

}

