package com.example.ebay;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class ShippingFragment extends Fragment {

    public ShippingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shipping, container, false);

        Bundle args = getArguments();

        String storename = args.getString("storename");
        String storeurl = args.getString("storeurl");
        int fscore = args.getInt("fscore");
        Double popularity = args.getDouble("popularity");
        String fstar = args.getString("fstar");
        String shipping = args.getString("shipping");
        Boolean global = args.getBoolean("global");
        int handling = args.getInt("handling");
        String policy = args.getString("policy");
        String rwithin = args.getString("rwithin");
        String refund = args.getString("refund");
        String shipped = args.getString("shipped");

        TextView storeNameLink = view.findViewById(R.id.storeNameLink);
        TextView feedbackScoreValue = view.findViewById(R.id.feedbackScoreValue);
        TextView popularityValue = view.findViewById(R.id.popularityValue);
        TextView feedbackStarValue = view.findViewById(R.id.feedbackStarValue);
        TextView textshipping = view.findViewById(R.id.shippingcost);
        TextView textglobal = view.findViewById(R.id.global);
        TextView texthandling = view.findViewById(R.id.handling);
        TextView textpolicy = view.findViewById(R.id.policy);
        TextView textrwithin = view.findViewById(R.id.rwithin);
        TextView textrefund = view.findViewById(R.id.refund);
        TextView textshipped = view.findViewById(R.id.shipped);

        storeNameLink.setText(storename);
        feedbackScoreValue.setText(String.valueOf(fscore));
        popularityValue.setText(String.valueOf(popularity));
        feedbackStarValue.setText(fstar);
        textshipping.setText(shipping);
        textglobal.setText(String.valueOf(global));
        texthandling.setText(String.valueOf(handling));
        textpolicy.setText("Returns Accepted");
        textrwithin.setText(rwithin);
        textrefund.setText(refund);
        textshipped.setText(shipped);

        return view;

    }
}