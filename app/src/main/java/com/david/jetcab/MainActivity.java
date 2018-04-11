package com.david.jetcab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.david.jetcab.Fragments.NotificationFragment;
import com.david.jetcab.Fragments.PlanesFragment;
import com.david.jetcab.Fragments.BookingFragments.BookingFirstFragment;
import com.david.jetcab.Fragments.HomeFragments.HomeFragment;
import com.david.jetcab.Fragments.MenuFragments.MenuFragment;
import com.david.jetcab.Models.Flight;
import com.david.jetcab.Utils.APIs;
import com.david.jetcab.Utils.FontManager;
import com.david.jetcab.Utils.MqttHelper;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;

public class MainActivity extends BaseActivity {

    private ImageButton btnHome;
    private ImageButton btnBook;
    private ImageButton btnPlanes;
    private ImageButton btnNotifications;
    private ImageButton btnMenu;
    private Button btnContact;

    public Flight creatingFlight;

    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        setInit();

        setEvents();

        getSupportFragmentManager().beginTransaction().add(R.id.frgMain, new HomeFragment(this)).commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragmentList = fragmentManager.getFragments();
        if (fragmentList.size() > 1) {
            Fragment currentFragment = fragmentList.get(fragmentList.size() - 1);
            fragmentManager.beginTransaction().remove(currentFragment).commit();
        } else {
            super.onBackPressed();
        }
    }


    private void findViews() {
        btnHome = findViewById(R.id.btnHome_Main);
        btnBook = findViewById(R.id.btnBook_Main);
        btnPlanes = findViewById(R.id.btnPlanes_Main);
        btnNotifications = findViewById(R.id.btnNotifications_Main);
        btnMenu = findViewById(R.id.btnMenu_Main);
        btnContact = findViewById(R.id.btnContact_Main);
        progressBar = findViewById(R.id.progressBar_Main);
    }

    private void setInit() {

        FontManager.setFontType(findViewById(R.id.layoutMain_Main), FontManager.getTypeface(this, FontManager.OPTIMA));

        sendRegistrationToServer();

//        startMqtt();
    }

    private void setEvents() {
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frgMain, new HomeFragment(MainActivity.this)).commit();
            }
        });

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frgMain, new BookingFirstFragment(MainActivity.this)).commit();
            }
        });

        btnPlanes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frgMain, new PlanesFragment(MainActivity.this)).commit();
            }
        });

        btnNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frgMain, new NotificationFragment(MainActivity.this)).commit();
            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frgMain, new MenuFragment(MainActivity.this)).commit();
            }
        });

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri number = Uri.parse("tel:123456789");
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
            }
        });
    }

    private void sendRegistrationToServer() {
        String token = FirebaseInstanceId.getInstance().getToken();
        if (token == null) return;

        Ion.with(getApplicationContext())
                .load("POST", APIs.SAVE_FCM_TOKEN_API)
                .setBodyParameter("fireToken", token)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (result == null)
                            return;
                        switch (result.getHeaders().code()) {
                            case 200:
                                Toast.makeText(getApplicationContext(), "Saved your notification token successfully!", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), "Not Saved your notification token!", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    private void startMqtt() {
        final MqttHelper mqttHelper = new MqttHelper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

//                try {
//                    mqttHelper.publishMessage("ok--------android--------notification", 1);
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                } catch (MqttException e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void connectionLost(Throwable throwable) {
                Toast.makeText(getApplicationContext(), "connectionLost", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug",mqttMessage.toString());
//                dataReceived.setText(mqttMessage.toString());
                Toast.makeText(getApplicationContext(), mqttMessage.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                Toast.makeText(getApplicationContext(), "deliveryComplete", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void calculateOtherInfoOfCreatingFlight() {
        long departureTimeStamp = creatingFlight.getDepartureTime();
        if (departureTimeStamp == -1) return;

        double distance = calcCrow(creatingFlight.getFromCity(), creatingFlight.getToCity());

        long travelTime = Math.round(distance * 3600 /  creatingFlight.getPlane().getSpeed());

        long arrivalTimeStamp = departureTimeStamp + travelTime * 1000;

        creatingFlight.setFinalPrice(creatingFlight.getPlane().getPricePerKm() * distance);

        creatingFlight.setArrivalTime(arrivalTimeStamp);
    }

}
