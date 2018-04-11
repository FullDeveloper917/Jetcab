package com.david.jetcab.Fragments.MenuFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.david.jetcab.MainActivity;
import com.david.jetcab.Models.User;
import com.david.jetcab.R;
import com.david.jetcab.ReviewActivity;
import com.david.jetcab.SignInActivity;
import com.david.jetcab.Utils.Constants;
import com.david.jetcab.Utils.FontManager;

import static com.david.jetcab.BaseActivity.user;

@SuppressLint("ValidFragment")
public class MenuFragment extends Fragment {

    private LinearLayout layoutDestinations;
    private LinearLayout layoutProfile;
    private LinearLayout layoutFaq;
    private LinearLayout layoutReview;
    private LinearLayout layoutLogout;
    private LinearLayout layoutAbout;
    private LinearLayout layoutLegal;

    private MainActivity activity;

    public MenuFragment(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        findViews(view);

        setEvents();

        return view;

    }


    private void findViews(View view) {
        layoutDestinations = view.findViewById(R.id.layoutDestinations_MenuFrg);
        layoutProfile = view.findViewById(R.id.layoutProfile_MenuFrg);
        layoutFaq = view.findViewById(R.id.layoutFAQ_MenuFrg);
        layoutReview = view.findViewById(R.id.layoutReview_MenuFrg);
        layoutLogout = view.findViewById(R.id.layoutLogOut_MenuFrg);
        layoutAbout = view.findViewById(R.id.layoutAbout_MenuFrg);
        layoutLegal = view.findViewById(R.id.layoutLegal_MenuFrg);

        FontManager.setFontType(view.findViewById(R.id.layoutMain_MenuFrg), FontManager.getTypeface(activity, FontManager.OPTIMA));
    }

    private void setEvents() {

        layoutDestinations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.getSupportFragmentManager().beginTransaction().add(R.id.frgMain, new DestinationsFragment(activity)).commit();
            }
        });

        layoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.getSupportFragmentManager().beginTransaction().add(R.id.frgMain, new ProfileFragment(activity)).commit();
            }
        });

        layoutFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.getSupportFragmentManager().beginTransaction().add(R.id.frgMain, new FaqFragment(activity)).commit();
            }
        });

        layoutReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ReviewActivity.class);
                startActivity(intent);
            }
        });

        layoutLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = activity.getSharedPreferences(Constants.USER_INFO, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(Constants.EMAIL, "");
                editor.putString(Constants.PHONE_NUMBER, "");
                editor.putString(Constants.FIRST_NAME, "");
                editor.putString(Constants.LAST_NAME, "");
                editor.putString(Constants.PASSWORD, "");
                editor.apply();

                user = new User();

                Intent intent = new Intent(activity, SignInActivity.class);
                startActivity(intent);
                activity.finish();
            }
        });

        layoutAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.getSupportFragmentManager().beginTransaction().add(R.id.frgMain, new AboutFragment(activity)).commit();
            }
        });

        layoutLegal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.getSupportFragmentManager().beginTransaction().add(R.id.frgMain, new LegalFragment(activity)).commit();
            }
        });
    }

}
