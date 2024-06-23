package com.example.ebay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductSearch extends AppCompatActivity {

    EditText editTextKeyword, editTextZipCode;
    CheckBox checkBoxNew, checkBoxUsed, checkBoxUnspecified;
    RadioButton radioButtonCurrentLocation, radioButtonZipCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);

        Button buttonSearch = findViewById(R.id.buttonSearch);
        Button buttonWishlist = findViewById(R.id.tabWishlist);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSearch();
//                Intent intent = new Intent(ProductSearch.this, ResultsActivity.class);
//                startActivity(intent);
            }
        });

        buttonWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Wishlist button click
                // Add your logic here
            }
        });

        CheckBox checkBoxNearbySearch = findViewById(R.id.checkBoxNearbySearch);
        LinearLayout layoutDistanceOptions = findViewById(R.id.layoutDistanceOptions);
        RadioButton radioButtoncurrloc = findViewById(R.id.radioButtonCurrentLocation);
        RadioButton radioButtonZipCode = findViewById(R.id.radioButtonZipCode);
        EditText editTextZipCode = findViewById(R.id.editTextZipCode);

        checkBoxNearbySearch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                layoutDistanceOptions.setVisibility(View.VISIBLE);
            } else {
                layoutDistanceOptions.setVisibility(View.GONE);
                // Additionally, clear any entered text in editTextZipCode
                editTextZipCode.setText("zipcode");
            }
        });

        radioButtonZipCode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                editTextZipCode.setVisibility(View.VISIBLE);
                radioButtoncurrloc.setChecked(false);
            } else {
//                editTextZipCode.setVisibility(View.GONE);
                radioButtonZipCode.setChecked(true);
                editTextZipCode.setText("");
            }
        });

        Spinner categorySpinner = findViewById(R.id.spinnerCategory);

// Define an array of categories (replace this with your categories)
        String[] categories = {"All", "Art", "Baby", "Books", "Clothing, Shoes and Accessories", "Computer, Tablets and Network", "Health and Beauty", "Music", "Video, Games and Consoles"};

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);

// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Apply the adapter to the spinner
        categorySpinner.setAdapter(adapter);

    }

    public void onNearbySearchClicked(View view) {
        CheckBox checkBoxNearbySearch = findViewById(R.id.checkBoxNearbySearch);
        LinearLayout layoutDistanceOptions = findViewById(R.id.layoutDistanceOptions);
        EditText editTextZipCode = findViewById(R.id.editTextZipCode);

        if (checkBoxNearbySearch.isChecked()) {
            layoutDistanceOptions.setVisibility(View.VISIBLE);
        } else {
            layoutDistanceOptions.setVisibility(View.GONE);
            editTextZipCode.setText("");
        }
    }

    public void onClearClicked(View view) {
        EditText editTextKeyword = findViewById(R.id.editTextKeyword);
        Spinner spinnerCategory = findViewById(R.id.spinnerCategory);
        CheckBox checkBoxNew = findViewById(R.id.checkBoxNew);
        CheckBox checkBoxUsed = findViewById(R.id.checkBoxUsed);
        CheckBox checkBoxUnspecified = findViewById(R.id.checkBoxUnspecified);
        CheckBox checkBoxLocalPickup = findViewById(R.id.checkBoxLocalPickup);
        CheckBox checkBoxFreeShipping = findViewById(R.id.checkBoxFreeShipping);
        CheckBox checkBoxNearbySearch = findViewById(R.id.checkBoxNearbySearch);
        EditText editTextMilesFrom = findViewById(R.id.editTextMilesFrom);
        RadioButton radioButtonCurrentLocation = findViewById(R.id.radioButtonCurrentLocation);
        RadioButton radioButtonZipCode = findViewById(R.id.radioButtonZipCode);
        EditText editTextZipCode = findViewById(R.id.editTextZipCode);

        // Reset all fields to their original states
        editTextKeyword.setText("");
        spinnerCategory.setSelection(0);
        checkBoxNew.setChecked(false);
        checkBoxUsed.setChecked(false);
        checkBoxUnspecified.setChecked(false);
        checkBoxLocalPickup.setChecked(false);
        checkBoxFreeShipping.setChecked(false);
        checkBoxNearbySearch.setChecked(false);
        editTextMilesFrom.setText("");
        radioButtonCurrentLocation.setChecked(true);
        radioButtonZipCode.setChecked(false);
        editTextZipCode.setText("");
    }

    private void validateAndSearch() {
        EditText editTextKeyword = findViewById(R.id.editTextKeyword);
        RadioButton radioButtonZipCode = findViewById(R.id.radioButtonZipCode);
        EditText editTextZipCode = findViewById(R.id.editTextZipCode);

        TextView keywordErrorTextView = findViewById(R.id.text_view_keyword_error);
        TextView zipErrorTextView = findViewById(R.id.text_view_zip_error);

        String keyword = editTextKeyword.getText().toString().trim();
        String zipCode = editTextZipCode.getText().toString().trim();

        if (TextUtils.isEmpty(keyword)) {
            // Show error message below keyword EditText
            keywordErrorTextView.setText(getString(R.string.error_empty_keyword));
            keywordErrorTextView.setVisibility(View.VISIBLE);
            displayError("Please fix all fields with errors");
        } else {
            // Hide error message
            keywordErrorTextView.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(zipCode) && radioButtonZipCode.isChecked()) {
            // Show error message below zip code EditText
            zipErrorTextView.setText(getString(R.string.error_empty_keyword));
            zipErrorTextView.setVisibility(View.VISIBLE);
            displayError("Please fix all fields with errors");
        } else {
            // Hide error message
            zipErrorTextView.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(keyword) && !(TextUtils.isEmpty(zipCode) && radioButtonZipCode.isChecked())){
            makeVolleyRequest();
        }



//        if (keyword.isEmpty()) {
//            displayError("Please fix all fields with errors");
//        } else if (radioButtonZipCode.isChecked() && zipCode.isEmpty()) {
//            displayError("Please fix all fields with errors");
//        } else {
//            makeVolleyRequest();
//        }
    }

    private void displayError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void makeVolleyRequest() {

        editTextKeyword = findViewById(R.id.editTextKeyword);
        editTextZipCode = findViewById(R.id.editTextZipCode);
        checkBoxNew = findViewById(R.id.checkBoxNew);
        checkBoxUsed = findViewById(R.id.checkBoxUsed);
        checkBoxUnspecified = findViewById(R.id.checkBoxUnspecified);
        radioButtonCurrentLocation = findViewById(R.id.radioButtonCurrentLocation);
        radioButtonZipCode = findViewById(R.id.radioButtonZipCode);

        String keyword = editTextKeyword.getText().toString();
//        String zipCode = editTextZipCode.getText().toString();
        String zipCode="90007";
//        String conditions = getConditions();
//        String location = getLocationOption();

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://webtech02.wl.r.appspot.com/api/ebaydata?"+"keyword="+keyword+"&from=90007";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        showToast("Response: " + response);
//                        Log.d("Response",response);
                        // Retrieve JSON response and parse it
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            JSONArray itemsArray = jsonResponse.getJSONArray("findItemsAdvancedResponse")
                                    .getJSONObject(0)
                                    .optJSONArray("searchResult")
                                    .getJSONObject(0)
                                    .optJSONArray("item");

//                            Log.d("JSON", String.valueOf(itemsArray));

                            List<ProductModel> productList = new ArrayList<>();
                            Context context;

                            for (int i = 0; i < itemsArray.length(); i++) {
                                JSONObject item = itemsArray.getJSONObject(i);
                                ProductModel product = new ProductModel();

                                // Extract necessary fields from each item and set them in the ProductModel
                                product.setImageURL(item.getJSONArray("galleryURL").getString(0));
                                product.setTitle(item.getJSONArray("title").getString(0));

                                product.setItemID(item.getJSONArray("itemId").getString(0));

                                JSONObject jsonObject = item;
                                if (jsonObject.has("postalCode")) {
                                    product.setZipCode(item.getJSONArray("postalCode").getString(0));
//                                    String postalCode = jsonObject.getString("postalCode");
                                } else {
                                    product.setZipCode("N/A");
                                }


//                                product.setZipCode(item.getJSONArray("postalCode").getString(0));
                                product.setShippingCost(item.getJSONArray("shippingInfo")
                                        .getJSONObject(0)
                                        .getJSONArray("shippingServiceCost")
                                        .getJSONObject(0)
                                        .getString("__value__"));
                                product.setProductCost(item.getJSONArray("sellingStatus")
                                        .getJSONObject(0)
                                        .getJSONArray("currentPrice")
                                        .getJSONObject(0)
                                        .getString("__value__"));
                                product.setCondition(item.getJSONArray("condition")
                                        .getJSONObject(0)
                                        .getJSONArray("conditionDisplayName")
                                        .getString(0));

                                productList.add(product);
                            }

                            setContentView(R.layout.activity_display_results);

                            // Initialize RecyclerView
                            RecyclerView recyclerView = findViewById(R.id.recyclerView);
                            if (recyclerView != null) {
                                recyclerView.setLayoutManager(new GridLayoutManager(ProductSearch.this, 2)); // Set your layout manager here
                            }
                            // Create and set RecyclerView adapter
                            ProductAdapter productAdapter = new ProductAdapter(productList);
                            recyclerView.setAdapter(productAdapter);

                            productAdapter.setOnItemClickListener(position -> {
                                // Handle item click
                                // Fetch product details based on the clicked position
                                ProductModel clickedProduct = productList.get(position);

                                // Make HTTP request for product details using clickedProduct
                                // Start a new activity to display the product details
                                Intent intent = new Intent(ProductSearch.this, ProductDetails.class);
                                intent.putExtra("itemId", clickedProduct.getItemID()); // Pass any necessary data
                                intent.putExtra("galleryURL", clickedProduct.getImageUrl()); // Pass any necessary data
                                intent.putExtra("ShippingCost", clickedProduct.getShippingCost()); // Pass any necessary data
                                intent.putExtra("title", clickedProduct.getTitle()); // Pass any necessary data
                                startActivity(intent);
                            });

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

//    private String getConditions() {
//        StringBuilder conditions = new StringBuilder();
//        if (checkBoxNew.isChecked()) {
//            conditions.append("New,");
//        }
//        if (checkBoxUsed.isChecked()) {
//            conditions.append("Used,");
//        }
//        if (checkBoxUnspecified.isChecked()) {
//            conditions.append("Unspecified,");
//        }
//        return conditions.toString();
//    }
//

//    private String getLocationOption() {
//        if (radioButtonCurrentLocation.isChecked()) {
//            return "Current Location";
//        } else if (radioButtonZipCode.isChecked()) {
//            return "Zip Code";
//        }
//        return "";
//    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}