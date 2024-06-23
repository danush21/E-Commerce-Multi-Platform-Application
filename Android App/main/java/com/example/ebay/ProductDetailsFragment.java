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
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;

public class ProductDetailsFragment extends Fragment {

    private LinearLayout imageContainer;

    public ProductDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);

        imageContainer = view.findViewById(R.id.imageContainer);

        Bundle args = getArguments();
//        assert args != null;
        String[] imageURLs = args.getStringArray("imageURLs");
        populateImages(imageURLs);
        Log.d("image", Arrays.toString(imageURLs));
        String title = args.getString("title");
        Double price = args.getDouble("price");
        String shipping = args.getString("shipping");

        TextView textTitle = view.findViewById(R.id.textTitle);
        TextView textPriceShipping = view.findViewById(R.id.textPriceShipping);

        textTitle.setText(title);
        if(shipping.equals("0") || shipping.equals("0.0"))
            shipping="Free";
        String priceWithShipping = "$" + price + "with $" + shipping + "shipping\n";
        textPriceShipping.setText(priceWithShipping);

        // Initialize TextViews
        TextView textPrice = view.findViewById(R.id.textPrice);
        TextView textBrand = view.findViewById(R.id.textBrand);

        String highprice = "$" + price;
        String brand = args.getString("brand"); // Replace getProductBrand() with your method to get the brand

        // Set fetched values to TextViews
        textPrice.setText(highprice);
        textBrand.setText(brand);

        ArrayList<String> specificationsList = args.getStringArrayList("values"); // Replace with your method to fetch specifications

        // Find the LinearLayout where the specifications will be displayed
        LinearLayout layoutSpecifications = view.findViewById(R.id.layoutSpecifications);

        // Add specifications to TextViews
        for (String specification : specificationsList) {
            TextView textView = new TextView(requireContext());
            textView.setText("\u2022 " + specification); // Add bullet point
            textView.setTextSize(16); // Set text size as needed
            layoutSpecifications.addView(textView);
        }

        return view;

    }

    private void populateImages(String[] imageUrls) {
        if (imageUrls != null) {
            for (String imageUrl : imageUrls) {
                ImageView imageView = new ImageView(requireContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        getResources().getDimensionPixelSize(R.dimen.image_width),
                        getResources().getDimensionPixelSize(R.dimen.image_height)
                );
                imageView.setLayoutParams(layoutParams);
                Picasso.get().load(imageUrl).into(imageView);
                imageContainer.addView(imageView);
            }
        }
    }
}
