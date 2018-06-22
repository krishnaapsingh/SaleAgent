package com.trio.app.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.trio.app.R;
import com.trio.app.adapters.ItemAdapter;

public class AddItemsActivity extends AppCompatActivity {

    RecyclerView rvItems;
    ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);
        rvItems = findViewById(R.id.rvItems);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(manager);
    }
}
