package com.example.kilojoulecounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class entryPage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener , AdapterView.OnItemSelectedListener {

    private EditText food1;
    private Button tot_food;
    private TextView food_tot;
    private int ftot =0;

    private EditText ex1;
    private  Button tot_ex;
    private TextView ex_tot;
    private int extot = 0;

    private Button nett_cal;
    private  int tot =0;
    private TextView total;

    private String currentDateString;
    private TextView textView;

    private Button discard;

    private Button save;
    public int count;
    public int tots = 0;

    private String entry = "";
    EntryBook obj = new EntryBook();
    ArrayList<String> entrylist = obj.getEntry();

    //storage of entries
    public SharedPreferences eb;
    public SharedPreferences.Editor ebEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_page);
        //initialise everything for food total
        food1 =  findViewById(R.id.meal1);
        food_tot =  findViewById(R.id.textView);
        tot_food = findViewById(R.id.add_meal);
        //initialise everything for exercise total
        ex1 =  findViewById(R.id.exercise1);
        ex_tot =  findViewById(R.id.textView2);
        tot_ex = findViewById(R.id.add_exercise);
        //initialise everything for total
        nett_cal = findViewById(R.id.button2);
        total = findViewById(R.id.textView3);

        //initialise button to discard entry
        discard = findViewById(R.id.button4);

        //initialise button to save entry
        save = findViewById(R.id.button3);

        //creates toolbar at the top of each activity page
        Toolbar toolbar2 = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar2);
        getSupportActionBar().setTitle("Daily Kilojoule Intake");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //creates [Spinner] drop down menu to select food categories
        final Spinner spinner = findViewById(R.id.Food);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Food_Category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //creates [Spinner] drop down menu to select exercise categories
        final Spinner spinner2 = findViewById(R.id.Exercise);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.Exercise_Category, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);

        //button to select the date of the entry
        Button button = (Button) findViewById(R.id.date_input);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });

        //increase food total every time meal is added
        tot_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int f1 = Integer.parseInt(food1.getText().toString());
                ftot = ftot + f1;
                food_tot.setText(String.valueOf(ftot));
                spinner.setSelection(0);
                food1.getText().clear();
            }
        });
        //increase exercise total every time meal is added
        tot_ex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int e1 = Integer.parseInt(ex1.getText().toString());
                extot = extot + e1;
                ex_tot.setText(String.valueOf(extot));
                spinner2.setSelection(0);
                ex1.getText().clear();

            }
        });
        //increase total once everything has been added (net cal button pressed)
        nett_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tot = ftot - extot;
                tots = tots + tot;
                total.setText(String.valueOf(tot));
                //System.out.println(test);

            }
        });

        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                discardEntry();

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entry = entry + (Integer.toString(ftot) + "," + Integer.toString(extot) + "," + Integer.toString(tot));
                saveEntry();
                //System.out.println(entry);
            }
        });

        //sets up SharedPrefs
        eb = getSharedPreferences("entrybook", Activity.MODE_PRIVATE);
        ebEditor = eb.edit();

        String bookJSONString = eb.getString("entries","[]"); //sets default value

        if (EntryBook.entries == null){
            processEntries(bookJSONString);
        }

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

        ebEditor.putString("notes", jsonNoteList.toString());
        ebEditor.apply();
    }

    //setting up calendar to choose date from
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, i);
        c.set(Calendar.MONTH, i1);
        c.set(Calendar.DAY_OF_MONTH, i2);

        currentDateString = DateFormat.getDateInstance().format(c.getTime());
        System.out.println(currentDateString);

        textView = (TextView) findViewById(R.id.selected_date);
        entry = currentDateString + ",";
        textView.setText(currentDateString);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //String text = adapterView.getItemAtPosition(i).toString();
        //Toast.makeText(adapterView.getContext(),text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        String text = "Please enter a value";
        Toast.makeText(adapterView.getContext(),text, Toast.LENGTH_SHORT).show();
    }

    public void discardEntry(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void saveEntry(){
        EntryBook.addEntry(entry);
        int pos = entrylist.indexOf(entry);
        count++;
        Intent intent = new Intent(this, ViewEntry.class);
        intent.putExtra("entry1", entry);
        intent.putExtra("entry2", pos);
        startActivity(intent);

        int avg = tots/count;
        Intent intent2 = new Intent(this, MainActivity.class);
        intent2.putExtra("avg",avg);
    }

    //save state when rotating screen
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("date", currentDateString);
        outState.putInt("food", ftot);
        outState.putInt("exercise",extot);
        outState.putInt("total",tot);
    }

    //only called if saved instance state is NOT NULL
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        currentDateString = savedInstanceState.getString("date");
        textView.setText(currentDateString);

        ftot = savedInstanceState.getInt("food");
        food_tot.setText(String.valueOf(ftot));

        extot = savedInstanceState.getInt("exercise");
        ex_tot.setText(String.valueOf(extot));

        tot = savedInstanceState.getInt("total");
        total.setText(String.valueOf(tot));

    }
}
