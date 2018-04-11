package com.david.jetcab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.david.jetcab.Utils.APIs;
import com.david.jetcab.Utils.Constants;
import com.david.jetcab.Utils.FontManager;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

public class ConfirmPassActivity extends AppCompatActivity {

    private Button btnConfirm;
    private Button btnContact;
    private EditText edtEmail;
    private LinearLayout layoutForm;
    private LinearLayout layoutReportMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_pass);

        findViews();
        setInit();
        setEvents();
    }

    private void findViews() {
        btnConfirm = findViewById(R.id.btnConfirm_ConfirmPass);
        btnContact = findViewById(R.id.btnContact_ConfirmPass);
        edtEmail = findViewById(R.id.edtEmail_ConfirmPass);
        layoutForm = findViewById(R.id.layoutForm_ConfirmPass);
        layoutReportMessage = findViewById(R.id.layoutReportMessage_ConfirmPass);

        FontManager.setFontType(findViewById(R.id.layoutMain_ConfirmPass), FontManager.getTypeface(this, FontManager.OPTIMA));
    }

    private void setInit() {
        layoutForm.setVisibility(View.VISIBLE);
        layoutReportMessage.setVisibility(View.INVISIBLE);
    }

    private void setEvents() {
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Ion.with(getApplicationContext())
                        .load(APIs.RESET_PASSWORD_API)
                        .setBodyParameter("email", edtEmail.getText().toString().trim())
                        .asJsonObject()
                        .withResponse()
                        .setCallback(new FutureCallback<Response<JsonObject>>() {
                            @Override
                            public void onCompleted(Exception e, Response<JsonObject> result) {

                                if (result == null) {
                                    return;
                                }

                                switch (result.getHeaders().code()) {
                                    case 200:
                                        layoutForm.setVisibility(View.INVISIBLE);
                                        layoutReportMessage.setVisibility(View.VISIBLE);

                                        break;

                                    default:
                                        if (result.getResult().has("errors"))
                                            Toast.makeText(ConfirmPassActivity.this, result.getResult().get("errors").toString(), Toast.LENGTH_SHORT).show();
                                        break;
                                }

                            }
                        });

            }
        });

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri number = Uri.parse("tel:123456789");
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
