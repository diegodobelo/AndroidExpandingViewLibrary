/**
 Copyright (c) 2016, Diego Bezerra <diego.bezerra@gmail.com>
 Permission to use, copy, modify, and/or distribute this software for any purpose
 with or without fee is hereby granted, provided that the above copyright notice
 and this permission notice appear in all copies.
 THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH
 REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT,
 OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE,
 DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS
 ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 **/
package com.diegodobelo.expandinganimlib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.diegodobelo.expandingview.ExpandingItem;
import com.diegodobelo.expandingview.ExpandingList;

public class MainActivity extends AppCompatActivity {
    private ExpandingList mExpandingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mExpandingList = (ExpandingList) findViewById(R.id.expanding_list_main);
        createSubItems();
    }

    private void createSubItems() {
        final ExpandingItem one = mExpandingList.createNewItem(R.layout.expanding_layout);
        if (one != null) {
            one.setIndicatorColorRes(R.color.pink);
            one.setIndicatorIconRes(R.drawable.ic_ghost);
            ((TextView) one.findViewById(R.id.title)).setText("John");
            String[] oneSub = {"House", "Boat", "Candy", "Collection", "Sport", "Ball", "Head"};
            one.createSubItems(oneSub.length);
            for (int i = 0; i < oneSub.length; i++) {
                View view = one.getSubItemView(i);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        one.removeSubItem(v);
                    }
                });
                ((TextView) view.findViewById(R.id.sub_title)).setText(oneSub[i]);
            }
//            one.collapse();
        }

        ExpandingItem two = mExpandingList.createNewItem(R.layout.expanding_layout);
        if (two != null) {
            two.setIndicatorColorRes(R.color.blue);
                two.setIndicatorIconRes(R.drawable.ic_ghost);
            ((TextView) two.findViewById(R.id.title)).setText("Mary");
            String[] twoSub = {"Cat", "Mouse", "House", "Boat", "Candy", "Collection", "Sport", "Ball", "Head", "Dog", "Horse", "Boat"};
            two.createSubItems(twoSub.length);
            for (int i = 0; i < twoSub.length; i++) {
                View view = two.getSubItemView(i);
                ((TextView) view.findViewById(R.id.sub_title)).setText(twoSub[i]);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        one.toggleExpanded();
                        View view = one.createSubItem(3);
                        ((TextView) view.findViewById(R.id.sub_title)).setText("Blastoise");
                    }
                });
            }
//            two.collapse();
        }

        ExpandingItem three = mExpandingList.createNewItem(R.layout.expanding_layout);
        if (three != null) {
            three.setIndicatorColorRes(R.color.purple);
            three.setIndicatorIconRes(R.drawable.ic_ghost);
            ((TextView) three.findViewById(R.id.title)).setText("Ana");
//            three.collapse();
        }

        ExpandingItem four = mExpandingList.createNewItem(R.layout.expanding_layout);
        if (four != null) {
            four.setIndicatorColorRes(R.color.orange);
            four.setIndicatorIconRes(R.drawable.ic_ghost);
            ((TextView) four.findViewById(R.id.title)).setText("Paul");
            String[] fourSub = {"Dog", "Horse", "Boat"};
            four.createSubItems(fourSub.length);
            for (int i = 0; i < fourSub.length; i++) {
                View view = four.getSubItemView(i);
                ((TextView) view.findViewById(R.id.sub_title)).setText(fourSub[i]);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View view = one.createSubItem(10);
                        ((TextView) view.findViewById(R.id.sub_title)).setText("Blastoise");
                    }
                });
            }
//            four.collapse();
        }

        ExpandingItem five = mExpandingList.createNewItem(R.layout.expanding_layout);
        if (five != null) {
            five.setIndicatorColorRes(R.color.green);
            five.setIndicatorIconRes(R.drawable.ic_ghost);
            ((TextView) five.findViewById(R.id.title)).setText("Rey");
//            five.collapse();
        }

        ExpandingItem six = mExpandingList.createNewItem(R.layout.expanding_layout);
        if (six != null) {
            six.setIndicatorColorRes(R.color.pink);
            six.setIndicatorIconRes(R.drawable.ic_ghost);
            ((TextView) six.findViewById(R.id.title)).setText("Finn");
            String[] sixSub = {"Blastoise", "Charmander"};
            six.createSubItems(sixSub.length);
            for (int i = 0; i < sixSub.length; i++) {
                View view = six.getSubItemView(i);
                ((TextView) view.findViewById(R.id.sub_title)).setText(sixSub[i]);
            }
//            six.collapse();
        }

        ExpandingItem seven = mExpandingList.createNewItem(R.layout.expanding_layout);
        if (seven != null) {
            seven.setIndicatorColorRes(R.color.orange);
            seven.setIndicatorIconRes(R.drawable.ic_ghost);
            ((TextView) seven.findViewById(R.id.title)).setText("Peter");
            String[] sevenSub = {"R2D2", "BB8"};
            seven.createSubItems(sevenSub.length);
            for (int i = 0; i < sevenSub.length; i++) {
                View view = seven.getSubItemView(i);
                ((TextView) view.findViewById(R.id.sub_title)).setText(sevenSub[i]);
            }
//            seven.collapse();
        }

        ExpandingItem eight = mExpandingList.createNewItem(R.layout.expanding_layout);
        if (eight != null) {
            eight.setIndicatorColorRes(R.color.green);
            eight.setIndicatorIconRes(R.drawable.ic_ghost);
            ((TextView) eight.findViewById(R.id.title)).setText("Scott");
            String[] eightSub = {"C3PO"};
            eight.createSubItems(eightSub.length);
            for (int i = 0; i < eightSub.length; i++) {
                View view = eight.getSubItemView(i);
                ((TextView) view.findViewById(R.id.sub_title)).setText(eightSub[i]);
            }
//            eight.collapse();
        }
    }
}
