package com.example.kilojoulecounter;

import java.util.ArrayList;


public class EntryBook {

    public static ArrayList<String> entries;

    public static ArrayList<String> getEntry() {
        if (entries==null){
            entries = new ArrayList<String>();
        }

        return entries;
    }

    public static void addEntry(String entry) {
        if (entries == null) {
            entries = new ArrayList<String>();
        }

        entries.add(entry);
    }

    public static int size() {
        if (entries == null) {
            entries = new ArrayList<String>();
        }

        return entries.size();
    }

    public static void clearNotes() {
        if (entries != null) {
            entries.clear();
        }
    }






}
