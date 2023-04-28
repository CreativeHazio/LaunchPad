package com.creativehazio.launchpad.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.creativehazio.launchpad.database.NoteRoomDatabase;
import com.creativehazio.launchpad.model.Note;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

//TODO: Sync notes to cloud, get notes from cloud.
public class NoteRepository {

    private FirebaseFirestore firestore;
    private FirebaseUser firebaseUser;

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

    public void syncNotesToCloud(Context context){
//        new SyncNotesToCloudAsyncTask(context).execute();
    }

    private class SyncNotesToCloudAsyncTask extends AsyncTask<Void,Void,Void> {

        private Context context;

        public SyncNotesToCloudAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            MutableLiveData<List<Note>> data = getAllNotesFromDatabase(context);
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            firestore = FirebaseFirestore.getInstance();

            firestore.collection("users_notes")
                    .document(firebaseUser.getUid())
                    .set(data)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
            return null;
        }
    }

}
