package com.david.jetcab.Fragments.MenuFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.david.jetcab.Adapters.FaqListAdapter;
import com.david.jetcab.Adapters.FlightsListAdapter;
import com.david.jetcab.Dialogs.CreateFaq;
import com.david.jetcab.MainActivity;
import com.david.jetcab.Models.Faq;
import com.david.jetcab.Models.Flight;
import com.david.jetcab.R;
import com.david.jetcab.Utils.APIs;
import com.david.jetcab.Utils.FontManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.tjerkw.slideexpandable.library.SlideExpandableListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.david.jetcab.BaseActivity.allFlightsList;


@SuppressLint("ValidFragment")
public class FaqFragment extends Fragment {

    private SearchView searchView;
    private ListView listViewFaq;
    private List<Faq> listFaq;
    private List<Faq> searchedListFaq;
    private FaqListAdapter faqListAdapter;
    private LinearLayout layoutAdd;

    private MainActivity activity;

    public MainActivity getMyActivity() {
        return activity;
    }

    public FaqFragment(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_faq, container, false);

        findViews(view);
        setInit();
        setEvents();

        return view;
    }

    private void findViews(View view) {
        searchView = view.findViewById(R.id.searchFaq_Faq);
        listViewFaq = view.findViewById(R.id.listViewFaq_Faq);
        layoutAdd = view.findViewById(R.id.layoutAdd_Faq);
        FontManager.setFontType(view.findViewById(R.id.layoutMain_FaqFrg), FontManager.getTypeface(activity, FontManager.OPTIMA));
    }

    public void setInit() {
        listFaq = new ArrayList<>();
        searchedListFaq = new ArrayList<>();
        activity.progressBar.setVisibility(View.VISIBLE);
        Ion.with(activity)
            .load(APIs.GET_FAQ_API)
            .asJsonObject()
            .withResponse()
            .setCallback(new FutureCallback<Response<JsonObject>>() {
                @Override
                public void onCompleted(Exception e, Response<JsonObject> result) {
                    activity.progressBar.setVisibility(View.GONE);
                    if (result == null)
                        return;
                    switch (result.getHeaders().code()) {
                        case 200:

                            JsonArray faqsJsonArray = result.getResult().get("faqs").getAsJsonArray();

                            for (JsonElement oneFaqJsonElement : faqsJsonArray) {

                                JsonObject oneFaqJsonObject = oneFaqJsonElement.getAsJsonObject();

                                Faq oneFaq = new Faq();

                                oneFaq.setByJsonObject(oneFaqJsonObject);

                                listFaq.add(oneFaq);
                            }

                            faqListAdapter = new FaqListAdapter(activity, R.layout.list_item_of_faq);
                            faqListAdapter.setList(listFaq);
                            listViewFaq.setAdapter(new SlideExpandableListAdapter(faqListAdapter, R.id.txtQuestion_OneFaq, R.id.expandable));

                            break;
                        default:
                            if (result.getResult().has("errors"))
                                Toast.makeText(activity, result.getResult().get("errors").toString(), Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
    }

    private void setEvents() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.isEmpty()) {
                    faqListAdapter.setList(listFaq);
                    listViewFaq.setAdapter(new SlideExpandableListAdapter(faqListAdapter, R.id.txtQuestion_OneFaq, R.id.expandable));
                } else {
                    searchedListFaq.clear();
                    for (Faq oneFaq: listFaq) {
                        if (oneFaq.getQuestion().toLowerCase().contains(newText.toLowerCase())) {
                            searchedListFaq.add(oneFaq);
                        }
                    }

                    faqListAdapter.setList(searchedListFaq);
                    listViewFaq.setAdapter(new SlideExpandableListAdapter(faqListAdapter, R.id.txtQuestion_OneFaq, R.id.expandable));
                }

                return false;
            }
        });

        layoutAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CreateFaq(FaqFragment.this).show();
            }
        });
    }

}
