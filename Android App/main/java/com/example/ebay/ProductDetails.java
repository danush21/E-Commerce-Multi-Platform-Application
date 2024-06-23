package com.example.ebay;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ProductDetails extends AppCompatActivity {

    private static ProductDetailsFragment fragment;
    private static GooglePhotosFragment fragment1;

    private static ShippingFragment fragment2;

    private static SimilarProductsFragment fragment3;

    ProductDetailsPagerAdapter adapter = new ProductDetailsPagerAdapter(getSupportFragmentManager());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        fragment = new ProductDetailsFragment();
        fragment1 = new GooglePhotosFragment();
        fragment2 = new ShippingFragment();
        fragment3 = new SimilarProductsFragment();

        // Retrieve data passed from the previous activity
        Intent intent = getIntent();
        String itemId = intent.getStringExtra("itemId");

        String galleryURL = intent.getStringExtra("galleryURL");
        String ShippingCost = intent.getStringExtra("ShippingCost");

        String title = intent.getStringExtra("title");

        // Find views from the custom ActionBar layout
        LinearLayout customActionBar = findViewById(R.id.customActionBar);
        ImageButton backButton = findViewById(R.id.backButton);
        TextView titleTextView = findViewById(R.id.titleTextView);
        ImageButton facebookButton = findViewById(R.id.facebookButton);

        // Set click listener for the back button in the ActionBar
        backButton.setOnClickListener(v -> onBackPressed());

        // Set the product title dynamically
        titleTextView.setText(title);

        // Set a click listener for the Facebook share button
        facebookButton.setOnClickListener(v -> {
            // Handle Facebook share action
            // Open the Facebook sharing functionality here
        });
//
//        // Get views from custom ActionBar layout
//        ImageButton backButton = getSupportActionBar().getCustomView().findViewById(R.id.backButton);
//        TextView titleTextView = getSupportActionBar().getCustomView().findViewById(R.id.titleTextView);
//        ImageButton facebookButton = getSupportActionBar().getCustomView().findViewById(R.id.facebookButton);
//
        // Set onClickListener for backButton and facebookButton
//        backButton.setOnClickListener(view -> onBackPressed());
//        facebookButton.setOnClickListener(view -> {
//            // Handle Facebook sharing here
//        });

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://webtech02.wl.r.appspot.com/api/productinfo?itemid=" + itemId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        showToast("Response: " + response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject item = jsonObject.getJSONObject("Item");

                            // Extracting fields
                            JSONArray pictureURL = item.getJSONArray("PictureURL");
                            String title = item.getString("Title");
                            JSONObject currentPriceObject = item.getJSONObject("CurrentPrice");
                            double currentPriceValue = currentPriceObject.getDouble("Value");
                            // Extract other fields similarly

                            // Accessing ItemSpecifics as an array
                            JSONArray itemSpecifics = item.getJSONObject("ItemSpecifics").getJSONArray("NameValueList");
                            // Iterate through itemSpecifics JSONArray to access its elements

                            String brand = ""; // Default value if not found
                            for (int i = 0; i < itemSpecifics.length(); i++) {
                                JSONObject items = itemSpecifics.getJSONObject(i);
                                String name = items.getString("Name");
                                if (name.equals("Brand")) {
                                    JSONArray valueArray = items.getJSONArray("Value");
                                    if (valueArray.length() > 0) {
                                        brand = valueArray.getString(0);
                                        break;
                                    }
                                }
                            }

                            ArrayList<String> valuesList = new ArrayList<>();

                            for (int i = 0; i < itemSpecifics.length(); i++) {
                                JSONObject itemv = itemSpecifics.optJSONObject(i);
                                if (itemv != null) {
                                    JSONArray valuesArray = itemv.optJSONArray("Value");
                                    if (valuesArray != null) {
                                        for (int j = 0; j < valuesArray.length(); j++) {
                                            String value = valuesArray.optString(j);
                                            valuesList.add(value);
                                        }
                                    }
                                }
                            }

                            // Accessing Storefront
                            JSONObject storefront = item.getJSONObject("Storefront");
                            String storeName = storefront.getString("StoreName");
                            String storeURL = storefront.getString("StoreURL");

                            // Accessing Seller details
                            JSONObject seller = item.getJSONObject("Seller");
                            int feedbackScore = seller.getInt("FeedbackScore");
                            double positiveFeedbackPercent = seller.getDouble("PositiveFeedbackPercent");
                            String feedbackRatingStar = seller.getString("FeedbackRatingStar");

                            // Accessing GlobalShipping, HandlingTime, ConditionDescription, ReturnsAccepted, and other fields similarly

                            JSONObject returnPolicy = item.getJSONObject("ReturnPolicy");
                            String returnsAccepted = returnPolicy.getString("ReturnsAccepted");
                            String returnsWithin = returnPolicy.getString("ReturnsWithin");
                            String refund = returnPolicy.getString("Refund");
                            String shippingCostPaidBy = returnPolicy.getString("ShippingCostPaidBy");

                            boolean globalShipping = item.getBoolean("GlobalShipping");
                            int handlingTime = item.getInt("HandlingTime");
                            String conditionDescription = item.getString("ConditionDescription");

                            String[] imageURLs = new String[pictureURL.length()];

                            // Extract image URLs from the JSONArray
                            for (int i = 0; i < pictureURL.length(); i++) {
                                imageURLs[i] = pictureURL.getString(i);
                            }

                            // Pass imageURLs to your fragment using arguments
                            Bundle args = new Bundle();
                            args.putStringArray("imageURLs", imageURLs);

                            Log.d("images", Arrays.toString(imageURLs));

                            args.putString("title", title);
                            args.putDouble("price", currentPriceValue);
                            args.putString("shipping",ShippingCost);

                            args.putString("brand", brand);

                            args.putStringArrayList("values", valuesList);

                            fragment.setArguments(args);

                            Bundle bundle = new Bundle();
                            bundle.putString("title", title);

                            fragment1.setArguments(bundle);

                            Bundle bundle1 = new Bundle();
                            bundle1.putString("storename", storeName);
                            bundle1.putString("storeurl", storeURL);
                            bundle1.putInt("fscore", feedbackScore);
                            bundle1.putDouble("popularity", positiveFeedbackPercent);
                            bundle1.putString("fstar", feedbackRatingStar);
                            bundle1.putString("shipping", ShippingCost);
                            bundle1.putBoolean("global", globalShipping);
                            bundle1.putInt("handling", handlingTime);
                            bundle1.putString("policy", String.valueOf(returnPolicy));
                            bundle1.putString("rwithin", returnsWithin);
                            bundle1.putString("refund", refund);
                            bundle1.putString("shipped", shippingCostPaidBy);

                            fragment2.setArguments(bundle1);

                            Bundle bundle2 = new Bundle();
                            bundle2.putString("itemId", itemId);

                            fragment3.setArguments(bundle2);

                            adapter.addFragment(fragment, "Product");
                            adapter.addFragment(fragment2, "Shipping");
                            adapter.addFragment(fragment1, "Photos");
                            adapter.addFragment(fragment3, "Similar");

                            ViewPager viewPager = findViewById(R.id.viewPager);
                            viewPager.setAdapter(adapter);

                            TabLayout tabLayout = findViewById(R.id.tabLayout);
                            tabLayout.setupWithViewPager(viewPager);


                            // Use the extracted data as needed
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                showToast("Error: " + error.getMessage());
            }
        });

        queue.add(stringRequest);

    }

    static class ProductDetailsPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ProductDetailsPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Add any additional logic if needed
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
