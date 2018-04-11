package com.david.jetcab.Fragments.MenuFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.david.jetcab.MainActivity;
import com.david.jetcab.R;
import com.david.jetcab.Utils.APIs;
import com.david.jetcab.Utils.FontManager;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

@SuppressLint("ValidFragment")
public class AboutFragment extends Fragment {

    private MainActivity activity;
    private TextView txtAbout;

    public AboutFragment(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        findViews(view);
        setInit();

        return view;
    }

    private void findViews(View view) {
        txtAbout = view.findViewById(R.id.txtAbout_AboutFrg);
        FontManager.setFontType(view.findViewById(R.id.layoutMain_AboutFrg), FontManager.getTypeface(activity, FontManager.OPTIMA));
    }

    private void setInit() {

        activity.progressBar.setVisibility(View.VISIBLE);
        Ion.with(activity)
                .load(APIs.OTHER_GET_API)
                .setBodyParameter("key", "about")
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (result == null) return;
                        switch (result.getHeaders().code()) {
                            case 200:

                                String aboutText = result.getResult().get("value").getAsString();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    txtAbout.setText(Html.fromHtml(aboutText, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    txtAbout.setText(Html.fromHtml(aboutText));
                                }

                                break;
                            default:
                                if (result.getResult().has("errors"))
                                    Toast.makeText(activity, result.getResult().get("errors").toString(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                        activity.progressBar.setVisibility(View.GONE);
                    }
                });
    }

}
