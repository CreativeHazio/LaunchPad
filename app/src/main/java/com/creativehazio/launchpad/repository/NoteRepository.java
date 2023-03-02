package com.creativehazio.launchpad.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.creativehazio.launchpad.database.NoteRoomDatabase;
import com.creativehazio.launchpad.model.Note;

import java.util.List;

//TODO: Sync notes to cloud, get notes from cloud.
public class NoteRepository {

    private static NoteRepository noteRepository;
    private NoteRoomDatabase database;

    public static NoteRepository getInstance() {
        if (noteRepository == null) {
            noteRepository = new NoteRepository();
        }
        return noteRepository;
    }

    public void insertNoteIntoDatabase(Context context, Note note) {
        database = NoteRoomDatabase.getInstance(context);
        new InsertNoteAsyncTask(database).execute(note);
    }

    private class InsertNoteAsyncTask extends AsyncTask<Note,Void,Void> {

        private NoteRoomDatabase database;

        public InsertNoteAsyncTask(NoteRoomDatabase database) {
            this.database = database;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            database.noteDAO().insertNote(notes[0]);
            return null;
        }
    }

    public MutableLiveData<List<Note>> getAllNotesFromDatabase(Context context){
        database = NoteRoomDatabase.getInstance(context);
        MutableLiveData<List<Note>> data = new MutableLiveData<>();
        data.setValue(database.noteDAO().getAllNotes());
        return data;
    }

    public MutableLiveData<List<Note>> getNotesByCategoryFromDatabase(Context context, String category){
        database = NoteRoomDatabase.getInstance(context);
        MutableLiveData<List<Note>> data = new MutableLiveData<>();
        data.setValue(database.noteDAO().getNotesByCategory(category));
        return data;
    }

    public MutableLiveData<Note> getNoteByIdFromDatabase(Context context, String id) {
        database = NoteRoomDatabase.getInstance(context);
        MutableLiveData<Note> data = new MutableLiveData<>();
        data.setValue(database.noteDAO().getNoteById(id));
        System.out.println(data.getValue());
        return data;
    }

    public void updateNoteIntoDatabase(Context context, Note note) {
        database = NoteRoomDatabase.getInstance(context);
        new UpdateNoteAsyncTask(database).execute(note);
    }

    private class UpdateNoteAsyncTask extends AsyncTask<Note,Void,Void> {

        private NoteRoomDatabase database;

        public UpdateNoteAsyncTask(NoteRoomDatabase database) {
            this.database = database;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            database.noteDAO().updateNote(notes[0]);
            return null;
        }
    }

    public void deleteNoteFromDatabase(Context context, Note note) {
        database = NoteRoomDatabase.getInstance(context);
        new DeleteNoteAsyncTask(database).execute(note);
    }

    private class DeleteNoteAsyncTask extends AsyncTask<Note,Void,Void> {

        private NoteRoomDatabase database;

        public DeleteNoteAsyncTask(NoteRoomDatabase database) {
            this.database = database;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            database.noteDAO().deleteNote(notes[0]);
            return null;
        }
    }
}
