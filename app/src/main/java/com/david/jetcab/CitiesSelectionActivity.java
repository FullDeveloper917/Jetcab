package com.david.jetcab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.david.jetcab.Models.City;
import com.david.jetcab.Utils.APIs;
import com.david.jetcab.Utils.Constants;
import com.david.jetcab.Utils.FontManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CitiesSelectionActivity extends BaseActivity {

    private Button btnPrev;
    private Button btnFinish;
    private AutoCompleteTextView actSearchCity;
    private ListView listViewCities;

    private CitiesListAdapter citiesListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities_selection);

        findViews();

        setInit();

        setEvents();
    }

    private void findViews() {
        btnPrev = findViewById(R.id.btnPrev_CitiesSelection);
        btnFinish = findViewById(R.id.btnFinish_CitiesSelection);
        actSearchCity = findViewById(R.id.actSearchCity_CitiesSelection);
        listViewCities = findViewById(R.id.listCities_CitiesSelection);
        progressBar = findViewById(R.id.progressBar_CitiesSelection);

    }

    private void setInit() {

        FontManager.setFontType(findViewById(R.id.layoutMain_CitiesSelection), FontManager.getTypeface(this, FontManager.OPTIMA));

        citiesListAdapter = new CitiesListAdapter(this, R.layout.list_item_of_cities);
        citiesListAdapter.setList(user.getFavoriteCities());
        listViewCities.setAdapter(citiesListAdapter);

        actSearchCity.setThreshold(1);
        actSearchCity.setAdapter(new ArrayAdapter<String> (this, android.R.layout.select_dialog_item, allCitiesName));
    }

    private void setEvents() {
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CitiesSelectionActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, List<String>> idsOfFavoriteCities = new HashMap<>();
                idsOfFavoriteCities.put("favoriteCities", user.getIdsList());

                if (user.getIdsList().size() < 2) {
                    Toast.makeText(CitiesSelectionActivity.this, "You must pick at least 2 cities!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                Ion.with(getApplicationContext())
                        .load(APIs.SIGN_UP_API)
                        .setBodyParameter("email", user.getEmail())
                        .setBodyParameter("password", user.getPassword())
                        .setBodyParameter("phoneNumber", user.getPhoneNumber())
                        .setBodyParameter("firstName", user.getFirstName())
                        .setBodyParameter("lastName", user.getLastName())
                        .setBodyParameters(idsOfFavoriteCities)
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

                                        SharedPreferences sharedPref = getSharedPreferences(Constants.USER_INFO, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString(Constants.EMAIL, user.getEmail());
                                        editor.putString(Constants.PHONE_NUMBER, user.getPhoneNumber());
                                        editor.putString(Constants.FIRST_NAME, user.getFirstName());
                                        editor.putString(Constants.LAST_NAME, user.getLastName());
                                        editor.putString(Constants.PASSWORD, user.getPassword());
                                        editor.apply();

                                        Intent intent = new Intent(CitiesSelectionActivity.this, SignInActivity.class);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    default:
                                        if (result.getResult().has("errors"))
                                            Toast.makeText(CitiesSelectionActivity.this, result.getResult().get("errors").toString(), Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        break;
                                }
                            }
                        });

            }
        });

        actSearchCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String newCityName = actSearchCity.getText().toString();
                actSearchCity.setText("");
                if (isNewCity(newCityName)) {
                    user.addFavoriteCity(getCityFromList(newCityName));
                    citiesListAdapter.setList(user.getFavoriteCities());
                    listViewCities.setAdapter(citiesListAdapter);
                }
            }
        });
    }

    private class CitiesListAdapter extends ArrayAdapter {
        private Context context;
        private int resource;
        private List<City> lists = new ArrayList<>();

        private Button btnClose;
        private TextView txtName;

        private CitiesListAdapter(@NonNull Context context, int resource) {
            super(context, resource);
            this.context = context;
            this.resource = resource;
        }

        public void setList(List<City> lists){
            this.lists = lists;
        }


        @Override
        public View getView(int position, final View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(resource, parent, false);

            txtName = rowView.findViewById(R.id.txtName_OneCity);
            txtName.setText(lists.get(position).getName());

            btnClose = rowView.findViewById(R.id.btnClose_OneCity);
            btnClose.setTag(position);
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = (int) view.getTag();
                    lists.remove(i);
                    notifyDataSetChanged();
                }
            });

            return rowView;
        }

        @Override
        public int getCount() {
            return lists.size();
        }
    }

    private boolean isNewCity(String cityName) {
        for (City oneCity : user.getFavoriteCities()) {
            if (cityName.equalsIgnoreCase(oneCity.getName()))
                return false;
        }
        return true;
    }


}
