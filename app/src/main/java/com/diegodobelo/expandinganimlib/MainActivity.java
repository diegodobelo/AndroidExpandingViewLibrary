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
        List<ExpandingItemData> subItems = createSubItems();

        ExpandingAdapter adapter = new ExpandingAdapter(subItems);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private List<ExpandingItemData> createSubItems() {
        List<ExpandingItemData> subItems = new ArrayList<>();

        ExpandingItemData one = new ExpandingItemData();
        one.setColorRes(R.color.pink);
        one.setIconRes(R.drawable.ic_ghost);
        one.setExpanded(false);
        one.setItemData("John");
        one.addSubItemData("House");
        one.addSubItemData("Boat");
        one.addSubItemData("Candy");
        one.addSubItemData("Collection");
        one.addSubItemData("Sport");
        one.addSubItemData("Ball");
        one.addSubItemData("Head");
        subItems.add(one);

        ExpandingItemData two = new ExpandingItemData();
        two.setColorRes(R.color.blue);
        two.setIconRes(R.drawable.ic_ghost);
        two.setExpanded(false);
        two.setItemData("Mary");
        two.addSubItemData("Dog");
        two.addSubItemData("Cat");
        two.addSubItemData("Duck");
        two.addSubItemData("Chicken");
        subItems.add(two);

        ExpandingItemData three = new ExpandingItemData();
        three.setColorRes(R.color.green);
        three.setIconRes(R.drawable.ic_ghost);
        three.setExpanded(false);
        three.setItemData("Ana");
        three.addSubItemData("Money");
        three.addSubItemData("Bike");
        three.addSubItemData("Kids");
        subItems.add(three);

        ExpandingItemData four = new ExpandingItemData();
        four.setColorRes(R.color.yellow);
        four.setIconRes(R.drawable.ic_ghost);
        four.setExpanded(false);
        four.setItemData("Luke");
        subItems.add(four);

        ExpandingItemData five = new ExpandingItemData();
        five.setColorRes(R.color.purple);
        five.setIconRes(R.drawable.ic_ghost);
        five.setExpanded(false);
        five.setItemData("Paul");
        five.addSubItemData("Poll");
        five.addSubItemData("Airplane");
        five.addSubItemData("Candy");
        subItems.add(five);

        return subItems;
    }
}
