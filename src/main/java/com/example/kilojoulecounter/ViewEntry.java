package com.example.kilojoulecounter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewEntry extends AppCompatActivity {

    EntryBook obj = new EntryBook();
    ArrayList<String> entrylist = obj.getEntry();
    private int placeholder;

    private Button ret_home;
    private Button addNew;
    private Button previous;
    private Button next;

    private String entry;
    private String entry1;
    private String[] items;
    private String foodcal;
    private String excal;
    private String netcal;

    private TextView foodtot;
    private TextView extot;
    private TextView tot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entry);

        ret_home = findViewById(R.id.button5);
        addNew = findViewById(R.id.button6);
        previous = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        extot =  findViewById(R.id.textView);
        foodtot = findViewById(R.id.textView8);
        tot = findViewById(R.id.textView9);

        //creates toolbar at the top of each activity page
        Toolbar toolbar2 = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar2);
        getSupportActionBar().setTitle("Kilojoule Intake");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //getting data from recyclerview
        if(getIntent().hasExtra("entry")){
            entry1 = getIntent().getStringExtra("entry");
            items = entry1.split(",");
            foodcal = items[2];
            excal = items[3];
            netcal = items[4];
        }

        //getting data from new entry
        if(getIntent().hasExtra("entry1")){
            entry = getIntent().getStringExtra("entry1");
            items = entry.split(",");
            foodcal = items[2];
            excal = items[3];
            netcal = items[4];
        }

        //buttons onClicks
        ret_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnHome();
            }
        });

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCal();

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIntent().hasExtra("entry2")){
                    placeholder = getIntent().getIntExtra("entry2", 0);
                    if (placeholder == (entrylist.size()-1)){
                        Toast.makeText(ViewEntry.this, "End of Entries", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        entry = entrylist.get(placeholder+1);
                        nextPage(entry,(placeholder+1));
                    }
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIntent().hasExtra("entry2")){
                    placeholder = getIntent().getIntExtra("entry2", 0);
                    if (placeholder == 0){
                        Toast.makeText(ViewEntry.this, "Beginning of Entries", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        entry = entrylist.get(placeholder-1);
                        prevPage(entry,(placeholder-1));
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {

        foodtot.setText(foodcal);
        extot.setText(excal);
        tot.setText(netcal);

        // This happens when the Activity comes into view
        super.onStart();
    }

    public void returnHome(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void addCal(){
        Intent intent = new Intent(this, entryPage.class);
        startActivity(intent);
    }

    public void nextPage(String entry, int placeholder){
        Intent intent = new Intent(this, ViewEntry.class);
        intent.putExtra("entry", entry);
        intent.putExtra("entry2", placeholder);
        startActivity(intent);
    }

    public void prevPage(String entry, int placeholder){
        Intent intent = new Intent(this, ViewEntry.class);
        intent.putExtra("entry", entry);
        intent.putExtra("entry2", placeholder);
        startActivity(intent);
    }
}
