package com.david.jetcab.Dialogs;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.david.jetcab.Fragments.MenuFragments.FaqFragment;
import com.david.jetcab.MainActivity;
import com.david.jetcab.R;
import com.david.jetcab.ReviewActivity;
import com.david.jetcab.Utils.APIs;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
//import com.property.david.tp.Fragments.SearchFragment;

/**
 * Created by david on 11/8/17.
 */

public class CreateFaq extends Dialog implements View.OnClickListener{
    public FaqFragment myFragment;
    public Button btnCreate, btnCancel;
    public EditText edtTitle;

    public CreateFaq(FaqFragment myFragment) {
        super(myFragment.getMyActivity());
        this.myFragment = myFragment;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_create_faq);
        btnCreate= (Button) findViewById(R.id.btnCreate_CreateFaq);
        btnCancel = (Button) findViewById(R.id.btnCancel_CreateFaq);
        edtTitle = findViewById(R.id.edtQuestion_CreateFaq);
        btnCreate.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreate_CreateFaq:
                String question = edtTitle.getText().toString();

                Ion.with(myFragment.getMyActivity())
                        .load(APIs.REGISTER_FAQ_API)
                        .setBodyParameter("question", question)
                        .asJsonObject()
                        .withResponse()
                        .setCallback(new FutureCallback<Response<JsonObject>>() {
                            @Override
                            public void onCompleted(Exception e, Response<JsonObject> result) {

                                if (result == null) {
                                    Toast.makeText(myFragment.getMyActivity(), "Please check your internet connection.", Toast.LENGTH_SHORT).show();
                                } else {
                                    switch (result.getHeaders().code()) {
                                        case 200:
                                            Toast.makeText(myFragment.getMyActivity(), "Your question is submitted successfully.", Toast.LENGTH_SHORT).show();
                                            break;
                                        default:
                                            if (result.getResult().has("errors"))
                                                Toast.makeText(myFragment.getMyActivity(), result.getResult().get("errors").toString(), Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                }
                                dismiss();
                                myFragment.setInit();
                            }
                        });

                break;
            case R.id.btnCancel_CreateFaq:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }


}
