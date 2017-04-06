package com.appchallenge.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.appchallenge.R;
import com.appchallenge.adapter.EventAdapter;
import com.appchallenge.model.Event;
import com.appchallenge.util.NetworkHelper;
import com.appchallenge.util.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static String TAG;
    private RequestQueue mRequestQueue;
    private ArrayList<Event> eventList = new ArrayList<>();
    private EventAdapter mAdapter;
    private ProgressDialog mProgressDialog;
    private static final String url = "https://guidebook.com/service/v2/upcomingGuides/";
    private RecyclerView mRecyclerView;
    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TAG = this.getClass().getSimpleName();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initToolbar("Events List");

        initViews();

        if (NetworkHelper.isConnectedToInternet(this))
            getDataFromServer();
        else
            Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.internet_error_msg, Snackbar.LENGTH_SHORT).show();

    }

    public void initToolbar(String title) {

        TextView titleView = (TextView) findViewById(R.id.titleTextView);
        titleView.setText(title);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    private void initViews() {


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView cartImageView = (ImageView) toolbar.findViewById(R.id.cartImageView);
        cartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, CartActivity.class));

            }
        });

        findViewById(R.id.cartImageView).setVisibility(View.VISIBLE);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new EventAdapter(this, eventList, TAG);

//        mRecyclerView.setAdapter(mAdapter);

    }

    private void getDataFromServer() {

        showProgressDialog();

        mRequestQueue = VolleySingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        hideProgressDialog();

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject eventObject = jsonArray.getJSONObject(i);

                                Event event = new Event();
                                event.setTitle(eventObject.getString("name"));
                                event.setEndDate(eventObject.getString("endDate"));
                                event.setThumbnailUrl(eventObject.getString("icon"));
                                event.setEventCount(1);
                                event.setEventId(eventObject.getString("url"));

                                eventList.add(event);
                            }


//                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.setAdapter(mAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d(TAG, "Volley Error === " + error.toString());
                        hideProgressDialog();

                        Snackbar.make(MainActivity.this.findViewById(R.id.coordinatorLayout), R.string.error_msg, Snackbar.LENGTH_SHORT).show();

                    }
                });

        jsObjRequest.setTag(TAG);

        VolleySingleton.getInstance(this).addToRequestQueue(jsObjRequest);

    }

    @Override
    protected void onPause() {
        hideProgressDialog();
        super.onPause();

    }

    @Override
    protected void onStop() {
        hideProgressDialog();
        super.onStop();

        if (mRequestQueue != null)
            mRequestQueue.cancelAll(TAG);
    }

    @Override
    protected void onDestroy() {
        hideProgressDialog();
        super.onDestroy();

    }

    private void showProgressDialog() {

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
            mProgressDialog = null;
        }
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();

        } else {
            doubleBackToExitPressedOnce = true;
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

            Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.please_click_again_for_back), Snackbar.LENGTH_SHORT).show();
        }


    }
}
