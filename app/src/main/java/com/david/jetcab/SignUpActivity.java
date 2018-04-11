package com.david.jetcab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.david.jetcab.Utils.APIs;
import com.david.jetcab.Utils.Constants;
import com.david.jetcab.Utils.FontManager;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends BaseActivity {

    private Button btnNext;
    private EditText edtFirstName;
    private EditText edtLastName;
    private EditText edtPhoneNumber;
    private EditText edtEmail;
    private EditText edtPassWord;
    private EditText edtConfirmPass;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String userName;
    private String passWord;
    private String confirmPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        findViews();

        setInit();

        setEvents();

    }

    private void findViews() {
        btnNext = findViewById(R.id.btnNext_SignUp);
        edtEmail = findViewById(R.id.edtEmail_SignUp);
        edtFirstName = findViewById(R.id.edtFirstName_SignUp);
        edtLastName = findViewById(R.id.edtLastName_SignUp);
        edtPhoneNumber = findViewById(R.id.edtTelePhone_SignUp);
        edtPassWord = findViewById(R.id.edtPassword_SignUp);
        edtConfirmPass = findViewById(R.id.edtConfirmPassword_SignUp);
    }

    private void setInit() {
        FontManager.setFontType(findViewById(R.id.layoutMain_SignUp), FontManager.getTypeface(this, FontManager.OPTIMA));
        edtFirstName.setText(user.getFirstName());
        edtLastName.setText(user.getLastName());
        edtEmail.setText(user.getEmail());
        edtPhoneNumber.setText(user.getPhoneNumber());
        edtPassWord.setText(user.getPassword());
        edtConfirmPass.setText(user.getPassword());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void setEvents() {


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName = edtFirstName.getText().toString();
                lastName = edtLastName.getText().toString();
                phoneNumber = edtPhoneNumber.getText().toString();
                email = edtEmail.getText().toString();
                passWord = edtPassWord.getText().toString();
                confirmPass = edtConfirmPass.getText().toString();

                if (validation()) {
                    user.setEmail(email);
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setPhoneNumber(phoneNumber);
                    user.setPassword(passWord);

                    Intent intent = new Intent(SignUpActivity.this, CitiesSelectionActivity.class);
                    startActivity(intent);
                    finish();
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

        if (email.isEmpty() || !isValidEmail(email)) {
            edtEmail.setText("");
            edtEmail.setHintTextColor(getResources().getColor(R.color.colorWarning));
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
            Toast.makeText(this, "Password length should be in range of 6 ~ 30.", Toast.LENGTH_SHORT).show();
            result = false;
        }

        return result;
    }

    public boolean isValidEmail(CharSequence target) {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public boolean isValidPhone(CharSequence target) {
        return Patterns.PHONE.matcher(target).matches();
    }

}
