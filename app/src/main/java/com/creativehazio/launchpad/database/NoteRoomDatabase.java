package com.creativehazio.launchpad.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.creativehazio.launchpad.model.Note;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteRoomDatabase extends RoomDatabase {

    private static NoteRoomDatabase instance;
    private final static String DB_NAME = "NOTE_DB";
    public abstract NoteDao noteDAO();

    public static NoteRoomDatabase getInstance(Context context){
        synchronized (context){
            if (instance == null){
                instance = Room.databaseBuilder(context.getApplicationContext(),
                                NoteRoomDatabase.class,DB_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return instance;
    }
}
