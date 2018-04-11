package com.david.jetcab.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.david.jetcab.MainActivity;
import com.david.jetcab.Models.Faq;
import com.david.jetcab.Models.Plane;
import com.david.jetcab.R;
import com.david.jetcab.Utils.FontManager;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 04/12/2017.
 */

public class FaqListAdapter extends ArrayAdapter {

    private final MainActivity activity;
    private final int resource;

    private List<Faq> list = new ArrayList<>();

    private TextView txtQuestion;
    private TextView txtAnswer;

    public void setList(List<Faq> list) {
        this.list = list;
    }

    public FaqListAdapter(@NonNull MainActivity activity, @LayoutRes int resource) {
        super(activity, resource);
        this.activity = activity;
        this.resource =resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource, parent, false);

        findViews(view);

        setValues(position);

        return view;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    private void findViews(View view) {
        txtQuestion = view.findViewById(R.id.txtQuestion_OneFaq);
        txtAnswer = view.findViewById(R.id.txtAnswer_OneFaq);
        FontManager.setFontType(view.findViewById(R.id.layoutMain_OneFaq), FontManager.getTypeface(activity, FontManager.OPTIMA));
    }

    private void setValues(int i) {
        txtQuestion.setText(list.get(i).getQuestion());

        String answer = list.get(i).getAnswer();
        if (!answer.equals("")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                txtAnswer.setText(Html.fromHtml(list.get(i).getAnswer(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                txtAnswer.setText(Html.fromHtml(list.get(i).getAnswer()));
            }
        } else {
            txtAnswer.setVisibility(View.GONE);
        }
    }
}

