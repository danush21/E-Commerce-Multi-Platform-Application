package com.example.ebay;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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

public class GooglePhotosFragment extends Fragment {

    public GooglePhotosFragment() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_google_photos, container, false);

        String title = getArguments().getString("title");
        Log.d("title", title);

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = "https://webtech02.wl.r.appspot.com/api/photos?q=" + title; // Replace with your image API endpoint

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Process the response containing image URLs
                        try {
//                            JSONArray imageUrls = new JSONArray(response);

                            JSONObject responseObject = new JSONObject(response);
                            JSONArray itemsArray = responseObject.getJSONArray("items");
                            Log.d("items", String.valueOf(itemsArray));
                            JSONArray imageUrls = new JSONArray();
                            for (int i = 0; i < itemsArray.length(); i++) {
                                JSONObject item = itemsArray.getJSONObject(i);
                                String link = item.getString("link");
                                Log.d("link", link);
                                imageUrls.put(link);
//                                linksList.add(link);
                            }

                            // Load images into ImageViews using Picasso
                            if (imageUrls != null) {
                                // Assuming you have 8 ImageViews defined in your layout
                                ImageView[] imageViews = new ImageView[]{
                                        view.findViewById(R.id.image_view_1),
                                        view.findViewById(R.id.image_view_2),
                                        view.findViewById(R.id.image_view_3),
                                        view.findViewById(R.id.image_view_4),
                                        view.findViewById(R.id.image_view_5),
                                        view.findViewById(R.id.image_view_6),
                                        view.findViewById(R.id.image_view_7),
                                        view.findViewById(R.id.image_view_8)
                                };

                                // Load images using Picasso into each ImageView
                                for (int i = 0; i < Math.min(imageUrls.length(), imageViews.length); i++) {
                                    try {
                                        String imageUrl = imageUrls.getString(i);
                                        Picasso.get().load(imageUrl).into(imageViews[i]);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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

        return view;
    }
}