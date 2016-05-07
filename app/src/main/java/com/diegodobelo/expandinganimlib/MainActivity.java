package com.diegodobelo.expandinganimlib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_main);
        List<String[]> subItems = new ArrayList<>();
        subItems.add(new String[]{"Cat"});
        subItems.add(new String[]{"House", "Boat", "Dog"});
        subItems.add(new String[]{"Cat", "Bed"});
        ExpandingAdapter adapter = new ExpandingAdapter(new String[]{"John", "Mary", "Ana", "Luke", "Paul", "Peter"}, subItems);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }
}
