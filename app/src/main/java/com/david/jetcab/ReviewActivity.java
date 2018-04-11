package com.david.jetcab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.david.jetcab.Adapters.FaqListAdapter;
import com.david.jetcab.Models.Faq;
import com.david.jetcab.Utils.APIs;
import com.david.jetcab.Utils.FontManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.tjerkw.slideexpandable.library.SlideExpandableListAdapter;

public class ReviewActivity extends AppCompatActivity {

    private TextView txtFlightName;
    private RatingBar rateReview;
    private EditText edtOpinion;
    private Button btnSubmit;
    private LinearLayout layoutForm;
    private TextView txtThanks;

    private float rate;
    private String opinion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        findViews();
        setInit();
        setEvents();

    }

    private void findViews() {
        txtFlightName = findViewById(R.id.txtFlightName_Review);
        rateReview = findViewById(R.id.rateReview_Review);
        edtOpinion = findViewById(R.id.edtOpinion_Review);
        btnSubmit = findViewById(R.id.btnSubmit_Review);
        layoutForm = findViewById(R.id.layoutForm_Review);
        txtThanks = findViewById(R.id.txtReviewThanks_Review);

        FontManager.setFontType(findViewById(R.id.layoutMain_Review), FontManager.getTypeface(this, FontManager.OPTIMA));
    }

    private void setInit() {
        rate = 5.0f;
        opinion = "";
        layoutForm.setVisibility(View.VISIBLE);
        txtThanks.setVisibility(View.INVISIBLE);
    }

    private void setEvents() {
        rateReview.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                rate = rating;
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                opinion = edtOpinion.getText().toString();
                JsonObject bodyJsonObject = new JsonObject();
                bodyJsonObject.addProperty("review", opinion);
                bodyJsonObject.addProperty("rate", rate);

                Ion.with(getApplicationContext())
                        .load(APIs.GIVE_REVIEW_API)
                        .setJsonObjectBody(bodyJsonObject)
                        .asJsonObject()
                        .withResponse()
                        .setCallback(new FutureCallback<Response<JsonObject>>() {
                            @Override
                            public void onCompleted(Exception e, Response<JsonObject> result) {

                                if (result == null) return;
                                switch (result.getHeaders().code()) {
                                    case 200:

                                        layoutForm.setVisibility(View.INVISIBLE);
                                        txtThanks.setVisibility(View.VISIBLE);

                                        break;
                                    default:
                                        if (result.getResult().has("errors"))
                                            Toast.makeText(ReviewActivity.this, result.getResult().get("errors").toString(), Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        });
            }
        });
    }
}
