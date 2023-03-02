package com.creativehazio.launchpad.mutual_functions;

import com.creativehazio.launchpad.model.Note;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class NoteSorter {

    public static void sort(ArrayList<Note> noteArrayList){
        if(noteArrayList == null){
            return;
        }
        Collections.sort(noteArrayList, new Comparator<Note>() {
            @Override
            public int compare(Note note, Note t1) {
                Date noteDate = getStringToDate(note.getDate(), "MMMM d, yyyy h:mm aa");
                Date t1Date = getStringToDate(t1.getDate(), "MMMM d, yyyy h:mm aa");

                return t1Date.compareTo(noteDate);
            }
        });
    }

    private static Date getStringToDate(String aDate, String aFormat){
        if (aDate == null) {
            return null;
        } else {
            ParsePosition position = new ParsePosition(0);
            SimpleDateFormat dateFormat = new SimpleDateFormat(aFormat);
            Date stringToDate = null;
            try {
                stringToDate = dateFormat.parse(aDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return stringToDate;
        }
    }
}
