package com.david.jetcab.Fragments.MenuFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.david.jetcab.CitiesSelectionActivity;
import com.david.jetcab.MainActivity;
import com.david.jetcab.R;
import com.david.jetcab.Utils.APIs;
import com.david.jetcab.Utils.Constants;
import com.david.jetcab.Utils.FontManager;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import static com.david.jetcab.BaseActivity.user;

@SuppressLint("ValidFragment")
public class ProfileFragment extends Fragment {

    private MainActivity activity;

    private Button btnSave;
    private EditText edtFirstName;
    private EditText edtLastName;
    private EditText edtPhoneNumber;
    private EditText edtPassWord;
    private EditText edtConfirmPass;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String userName;
    private String passWord;
    private String confirmPass;

    public ProfileFragment(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        findViews(view);
        
        setInit();
        
        setEvents();
        
        return view;
    }


    private void findViews(View view) {
        btnSave = view.findViewById(R.id.btnSave_ProfileFrg);
        edtFirstName = view.findViewById(R.id.edtFirstName_ProfileFrg);
        edtLastName = view.findViewById(R.id.edtLastName_ProfileFrg);
        edtPhoneNumber = view.findViewById(R.id.edtTelePhone_ProfileFrg);
        edtPassWord = view.findViewById(R.id.edtPassword_ProfileFrg);
        edtConfirmPass = view.findViewById(R.id.edtConfirmPassword_ProfileFrg);

        FontManager.setFontType(view.findViewById(R.id.layoutMain_ProfileFrg), FontManager.getTypeface(activity, FontManager.OPTIMA));
    }

    private void setInit() {
        edtFirstName.setText(user.getFirstName());
        edtLastName.setText(user.getLastName());
        edtPhoneNumber.setText(user.getPhoneNumber());
        edtPassWord.setText(user.getPassword());
        edtConfirmPass.setText(user.getPassword());
    }

    private void setEvents() {

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName = edtFirstName.getText().toString();
                lastName = edtLastName.getText().toString();
                phoneNumber = edtPhoneNumber.getText().toString();
                passWord = edtPassWord.getText().toString();
                confirmPass = edtConfirmPass.getText().toString();

                if (validation()) {

                    activity.progressBar.setVisibility(View.VISIBLE);
                    Ion.with(activity)
                            .load(APIs.UPDATE_PROFILE_API)
                            .setBodyParameter("password", passWord)
                            .setBodyParameter("phoneNumber", phoneNumber)
                            .setBodyParameter("firstName", userName)
                            .setBodyParameter("lastName", lastName)
                            .setBodyParameter("userName", firstName)
                            .asJsonObject()
                            .withResponse()
                            .setCallback(new FutureCallback<Response<JsonObject>>() {
                                @Override
                                public void onCompleted(Exception e, Response<JsonObject> result) {

                                    activity.progressBar.setVisibility(View.GONE);

                                    switch (result.getHeaders().code()) {
                                        case 200:

                                            SharedPreferences sharedPref = activity.getSharedPreferences(Constants.USER_INFO, Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPref.edit();
                                            editor.putString(Constants.PHONE_NUMBER, phoneNumber);
                                            editor.putString(Constants.FIRST_NAME, firstName);
                                            editor.putString(Constants.LAST_NAME, lastName);
                                            editor.putString(Constants.USER_NAME, userName);
                                            editor.putString(Constants.PASSWORD, passWord);
                                            editor.apply();

                                            JsonObject userJsonObject = result.getResult().get("user").getAsJsonObject();

                                            user.setByJsonObject(userJsonObject);

                                            Toast.makeText(activity, "saved", Toast.LENGTH_SHORT).show();
                                            break;
                                        default:
                                            if (result.getResult().has("errors"))
                                                Toast.makeText(activity, result.getResult().get("errors").getAsString(), Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                }
                            });
                }
            }
        });
    }

    private boolean validation(){

        boolean result = true;

        if (firstName.isEmpty()) {
            edtFirstName.setHintTextColor(getResources().getColor(R.color.colorWarning));
            result = false;
        }

        if (lastName.isEmpty()) {
            edtLastName.setHintTextColor(getResources().getColor(R.color.colorWarning));
            result = false;
        }

        if (phoneNumber.isEmpty() || !isValidPhone(phoneNumber)) {
            edtPhoneNumber.setText("");
            edtPhoneNumber.setHintTextColor(getResources().getColor(R.color.colorWarning));
            result = false;
        }

        if (passWord.isEmpty() || confirmPass.isEmpty() || !passWord.equals(confirmPass)) {
            edtPassWord.setText("");
            edtConfirmPass.setText("");
            edtPassWord.setHintTextColor(getResources().getColor(R.color.colorWarning));
            edtConfirmPass.setHintTextColor(getResources().getColor(R.color.colorWarning));
            result = false;
        }

        if (passWord.length() < 6 || passWord.length() > 32) {
            edtPassWord.setText("");
            edtConfirmPass.setText("");
            edtPassWord.setHintTextColor(getResources().getColor(R.color.colorWarning));
            edtConfirmPass.setHintTextColor(getResources().getColor(R.color.colorWarning));
            Toast.makeText(activity, "Password length should be in range of 6 ~ 30.", Toast.LENGTH_SHORT).show();
            result = false;
        }

        return result;
    }

    public boolean isValidPhone(CharSequence target) {
        return Patterns.PHONE.matcher(target).matches();
    }
}
