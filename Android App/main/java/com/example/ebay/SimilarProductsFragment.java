package com.example.ebay;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SimilarProductsFragment extends Fragment {

//    private List<SimilarProductModel> similarproductList;
    private List<SimilarProductModel> similarproductList = new ArrayList<>();

    public SimilarProductsFragment() {
    // Required empty public constructor
}

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_similar_products, container, false);

        String itemId = getArguments().getString("itemId");
        Log.d("itemId", itemId);

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = "https://webtech02.wl.r.appspot.com/api/similaritems?itemid=" + itemId; // Replace with your image API endpoint

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Process the response containing image URLs
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            JSONObject getSimilarItemsResponse = responseObject.getJSONObject("getSimilarItemsResponse");
                            JSONObject itemRecommendations = getSimilarItemsResponse.getJSONObject("itemRecommendations");
                            JSONArray items = itemRecommendations.getJSONArray("item");

                            for (int i = 0; i < items.length(); i++) {
                                JSONObject item = items.getJSONObject(i);
                                SimilarProductModel similarproduct = new SimilarProductModel();

                                similarproduct.setImageURL(item.optString("imageURL"));
                                similarproduct.setTitle(item.optString("title"));
                                similarproduct.setShippingCost(item.getJSONObject("shippingCost").optString("__value__"));
                                similarproduct.setdays(item.optString("timeLeft").substring(1, item.optString("timeLeft").indexOf("D")));
                                similarproduct.setProductCost(item.getJSONObject("buyItNowPrice").optString("__value__"));
                                similarproduct.setlink(item.optString("viewItemURL"));
                                Log.d("itemurl", similarproduct.getlink());

                                similarproductList.add(similarproduct);

                                // Extracting fields
//                                String imageUrl = item.optString("imageURL");
//                                String title = item.optString("title");
//                                String shippingCost = item.getJSONObject("shippingCost").optString("__value__");
//                                String timeLeft = item.optString("timeLeft").substring(1, item.optString("timeLeft").indexOf("D"));
//                                String buyItNowPrice = item.getJSONObject("buyItNowPrice").optString("__value__");
                            }
//                            adapter.notifyDataSetChanged();
                            } catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error for the image URL request
                    }
                });

        queue.add(stringRequest);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Create and set adapter
        SimilarProductsAdapter adapter = new SimilarProductsAdapter(similarproductList, requireContext());
        recyclerView.setAdapter(adapter);

        return view;
    }
}
