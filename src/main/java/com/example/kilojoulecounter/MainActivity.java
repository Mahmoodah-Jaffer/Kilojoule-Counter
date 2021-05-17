package com.example.kilojoulecounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //recycler view
    private RecyclerView rvEntries;
    private EntryAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;

    EntryBook obj = new EntryBook();
    ArrayList<String> entrylist = obj.getEntry();

    private Button button;

    //storage of entries
    public SharedPreferences eb;
    public SharedPreferences.Editor ebEditor;

    private TextView avg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //sets up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //average cals
        avg = findViewById(R.id.textView10);

        if(getIntent().hasExtra("avg")){
            avg.setText(getIntent().getIntExtra("avg",0));
        }

        //setting up the recycler view for the diary entries
        rvEntries = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        adapter = new EntryAdapter();

        rvEntries.setLayoutManager(mLayoutManager);
        rvEntries.setAdapter(adapter);

        adapter.setOnItemClickListener(new EntryAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                viewPage(entrylist.get(position));
            }
        });


        //sets up SharedPrefs
        eb = getSharedPreferences("entrybook", Activity.MODE_PRIVATE);
        ebEditor = eb.edit();

        String bookJSONString = eb.getString("entries","[]"); //sets default value

        if (EntryBook.entries == null){
            processEntries(bookJSONString);
        }

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                addCal();
            }
        });
    }

    public void processEntries(String bookJSONString){
        JSONArray bookJSON = null;

        try{
            bookJSON = new JSONArray(bookJSONString);
        }catch (JSONException e){
            e.printStackTrace();
        }

        EntryBook.clearNotes();

        for (int i = 0; i < bookJSON.length(); i++) {
            try {
                EntryBook.addEntry((String) bookJSON.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void viewPage(String entry){
        int pos = entrylist.indexOf(entry);
        Intent intent = new Intent(this, ViewEntry.class);
        intent.putExtra("entry", entry);
        intent.putExtra("entry2", pos);
        startActivity(intent);
    }

    public void addCal(){
        Intent intent = new Intent(this, entryPage.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        // This happens when the Activity comes into view
        super.onStart();
    }

    @Override
    protected void onPause() {
        saveNotes();
        super.onPause();
    }


    @Override
    public void finish() {
        saveNotes();
        super.finish();
    }

    private void saveNotes() {
        JSONArray jsonNoteList = new JSONArray(EntryBook.getEntry());
        System.out.println(jsonNoteList);
        ebEditor.putString("notes", jsonNoteList.toString());
        ebEditor.apply();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
