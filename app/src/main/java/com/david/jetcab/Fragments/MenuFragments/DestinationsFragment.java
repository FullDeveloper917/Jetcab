package com.david.jetcab.Fragments.MenuFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.david.jetcab.BaseActivity;
import com.david.jetcab.CitiesSelectionActivity;
import com.david.jetcab.MainActivity;
import com.david.jetcab.Models.City;
import com.david.jetcab.R;
import com.david.jetcab.Utils.APIs;
import com.david.jetcab.Utils.FontManager;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("ValidFragment")
public class DestinationsFragment extends Fragment {

    private Button btnSave;
    private AutoCompleteTextView actSearchCity;
    private ListView listViewCities;

    private List<City> citiesList;
    private CitiesListAdapter citiesListAdapter;
    private MainActivity activity;

    public DestinationsFragment(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_destinations, container, false);

        findViews(view);

        setInit();

        setEvents();

        return view;
    }

    private void findViews(View view) {
        btnSave = view.findViewById(R.id.btnSave_MyDestination);
        actSearchCity = view.findViewById(R.id.actSearchCity_MyDestination);
        listViewCities = view.findViewById(R.id.listCities_MyDestination);

        FontManager.setFontType(view.findViewById(R.id.layoutMain_DestinationFrg), FontManager.getTypeface(activity, FontManager.OPTIMA));
    }

    private void setInit() {
        citiesList = new ArrayList<>(BaseActivity.user.getFavoriteCities());
        citiesListAdapter = new CitiesListAdapter(activity, R.layout.list_item_of_cities);
        citiesListAdapter.setList(citiesList);
        listViewCities.setAdapter(citiesListAdapter);
        actSearchCity.setThreshold(1);
        actSearchCity.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.select_dialog_item, BaseActivity.allCitiesName));

    }

    private void setEvents() {

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final List<String> citiesNameList = new ArrayList<>();
                for (City oneCity: citiesList) {
                    citiesNameList.add(oneCity.getId());
                }

                Map<String, List<String>> idsOfFavoriteCities = new HashMap<>();
                idsOfFavoriteCities.put("cities", citiesNameList);

                if (citiesNameList.size() < 2) {
                    Toast.makeText(activity, "You must pick at least 2 cities!", Toast.LENGTH_SHORT).show();
                    return;
                }

                activity.progressBar.setVisibility(View.VISIBLE);
                Ion.with(activity)
                        .load(APIs.UPDATE_CITES_API)
                        .setBodyParameters(idsOfFavoriteCities)
                        .asJsonObject()
                        .withResponse()
                        .setCallback(new FutureCallback<Response<JsonObject>>() {
                            @Override
                            public void onCompleted(Exception e, Response<JsonObject> result) {

                                activity.progressBar.setVisibility(View.GONE);

                                if (result == null || result.getResult() == null)
                                    return;

                                switch (result.getHeaders().code()) {
                                    case 200:
                                        BaseActivity.user.setFavoriteCities(citiesList);
                                        Toast.makeText(activity, "saved", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        if (result.getResult().has("errors"))
                                            Toast.makeText(activity, result.getResult().get("errors").toString(), Toast.LENGTH_SHORT).show();
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
                    citiesList.add(BaseActivity.getCityFromList(newCityName));
                    citiesListAdapter.setList(citiesList);
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

        public CitiesListAdapter(@NonNull Context context, int resource) {
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

            FontManager.setFontType(txtName, FontManager.getTypeface(activity, FontManager.OPTIMA));

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
        for (City oneCity : citiesList) {
            if (cityName.equalsIgnoreCase(oneCity.getName()))
                return false;
        }
        return true;
    }

}
