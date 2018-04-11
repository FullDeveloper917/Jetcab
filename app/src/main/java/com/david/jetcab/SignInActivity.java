package com.david.jetcab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.david.jetcab.Models.City;
import com.david.jetcab.Models.Flight;
import com.david.jetcab.Models.Plane;
import com.david.jetcab.Models.User;
import com.david.jetcab.Utils.APIs;
import com.david.jetcab.Utils.Constants;
import com.david.jetcab.Utils.FontManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.ArrayList;

public class SignInActivity extends BaseActivity {

    private Button btnLogin;
    private Button btnSignUp;
    private Button btnForgot;
    private Button btnContact;
    private EditText edtEmail;
    private EditText edtPassword;

    private String email;
    private String passWord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getCities();

        findViews();

        setEvents();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setInit();
    }

    private void findViews() {
        btnLogin = findViewById(R.id.btnLogin_SignIn);
        btnForgot = findViewById(R.id.btnForgot_SignIn);
        btnSignUp = findViewById(R.id.btnSignUp_SignIn);
        btnContact = findViewById(R.id.btnContact_SignIn);
        edtEmail = findViewById(R.id.edtEmail_SignIn);
        edtPassword = findViewById(R.id.edtPassword_SignIn);
        progressBar = findViewById(R.id.progressBar_SignIn);
    }

    private void setInit() {
        FontManager.setFontType(findViewById(R.id.layoutMain_SignIn), FontManager.getTypeface(this, FontManager.OPTIMA));

        SharedPreferences sharedPref = getSharedPreferences(Constants.USER_INFO, Context.MODE_PRIVATE);
        user = new User();
        user.setFirstName(sharedPref.getString(Constants.FIRST_NAME, ""));
        user.setLastName(sharedPref.getString(Constants.LAST_NAME, ""));
        user.setEmail(sharedPref.getString(Constants.EMAIL, ""));
        user.setPhoneNumber(sharedPref.getString(Constants.PHONE_NUMBER, ""));
        user.setPassword(sharedPref.getString(Constants.PASSWORD, ""));

        edtEmail.setText(user.getEmail());
        edtPassword.setText(user.getPassword());
    }

    private void setEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = edtEmail.getText().toString();
                passWord = edtPassword.getText().toString();

                if (!validation()) return;

                user.setPassword(passWord);

                progressBar.setVisibility(View.VISIBLE);

                Ion.with(getApplicationContext())
                        .load(APIs.SIGN_IN_API)
                        .setBodyParameter("email", email)
                        .setBodyParameter("password", passWord)
                        .asJsonObject()
                        .withResponse()
                        .setCallback(new FutureCallback<Response<JsonObject>>() {
                            @Override
                            public void onCompleted(Exception e, Response<JsonObject> result) {

                                if (result == null) {
                                    progressBar.setVisibility(View.GONE);
                                    return;
                                }

                                switch (result.getHeaders().code()) {
                                    case 200:
                                        JsonObject userJsonObject = result.getResult().get("user").getAsJsonObject();
                                        user.setByJsonObject(userJsonObject);

                                        SharedPreferences sharedPref = getSharedPreferences(Constants.USER_INFO, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString(Constants.EMAIL, user.getEmail());
                                        editor.putString(Constants.PHONE_NUMBER, user.getPhoneNumber());
                                        editor.putString(Constants.FIRST_NAME, user.getFirstName());
                                        editor.putString(Constants.LAST_NAME, user.getLastName());
                                        editor.putString(Constants.PASSWORD, user.getPassword());
                                        editor.apply();

                                        getInfo(false);

                                        break;
                                    default:
                                        if (result.getResult().has("errors"))
                                            Toast.makeText(SignInActivity.this, result.getResult().get("errors").toString(), Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        break;
                                }

                            }
                        });


            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, ConfirmPassActivity.class);
                startActivity(intent);
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

    private boolean validation(){

        boolean result = true;

        if (passWord.isEmpty()) {
            edtPassword.setHintTextColor(getResources().getColor(R.color.colorWarning));
            Toast.makeText(this, "Please insert Email!", Toast.LENGTH_SHORT).show();
            result = false;
        }

        if (email.isEmpty()) {
            edtEmail.setHintTextColor(getResources().getColor(R.color.colorWarning));
            Toast.makeText(this, "Please insert Email!", Toast.LENGTH_SHORT).show();
            result = false;
        }

        if (!isValidEmail(email)) {
            edtEmail.setText("");
            edtEmail.setHintTextColor(getResources().getColor(R.color.colorWarning));
            Toast.makeText(this, "Please insert correct Email!", Toast.LENGTH_SHORT).show();
            result = false;
        }

        return result;
    }

    public boolean isValidEmail(CharSequence target) {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public void getCities() {
        Ion.with(getApplicationContext())
                .load("POST", APIs.SEARCH_CITY_API)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (result == null) return;
                        switch (result.getHeaders().code()) {
                            case 200:

                                JsonArray citiesJsonArray = result.getResult().get("cities").getAsJsonArray();

                                allCitiesList = new ArrayList<>();
                                allCitiesName = new ArrayList<>();

                                for (JsonElement oneCityJsonElement : citiesJsonArray) {

                                    JsonObject oneCityJsonObject = oneCityJsonElement.getAsJsonObject();

                                    City oneCity = new City();

                                    oneCity.setByJsonObject(oneCityJsonObject);

                                    allCitiesList.add(oneCity);
                                    allCitiesName.add(oneCity.getName());
                                }

                                break;
                            default:
                                if (result.getResult().has("errors"))
                                    Toast.makeText(getApplicationContext(), result.getResult().get("errors").toString(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

}
