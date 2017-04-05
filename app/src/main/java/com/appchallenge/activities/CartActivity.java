package com.appchallenge.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.appchallenge.R;
import com.appchallenge.adapter.EventAdapter;
import com.appchallenge.model.Event;
import com.appchallenge.util.CartDatabaseHandler;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private static String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TAG = this.getClass().getSimpleName();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initToolbar("Cart");

        initViews();
    }

    public void initToolbar(String title) {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView titleView = (TextView) findViewById(R.id.titleTextView);
        titleView.setText(title);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ((Toolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.cartImageView).setVisibility(View.INVISIBLE);

    }

    private void initViews() {

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(layoutManager);

        CartDatabaseHandler cartDatabaseHandler = new CartDatabaseHandler(this);

        ArrayList<Event> eventList = cartDatabaseHandler.getEventList();

        EventAdapter mAdapter = new EventAdapter(this, eventList, TAG);

        mRecyclerView.setAdapter(mAdapter);

    }
}
