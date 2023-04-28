package com.creativehazio.launchpad.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.creativehazio.launchpad.database.NoteRoomDatabase;
import com.creativehazio.launchpad.model.Note;
import com.creativehazio.launchpad.model.User;
import com.creativehazio.launchpad.model.UserRole;
import com.creativehazio.launchpad.mutual_functions.CustomToast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

//Add user data to firestore, get user data from firestore.
public class UserRepository {

    private static UserRepository userRepository;
    private NoteRoomDatabase noteRoomDatabase;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public static UserRepository getInstance() {
        if (userRepository == null) {
            userRepository = new UserRepository();
        }
        return userRepository;
    }

    public void addUserToFirestore(FirebaseUser firebaseUser, Context context) {
        new AddUserToFirestoreAsyncTask(context).execute(firebaseUser);
    }

    private class AddUserToFirestoreAsyncTask extends AsyncTask<FirebaseUser, Void, Void> {

        private Context context;

        public AddUserToFirestoreAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(FirebaseUser... firebaseUsers) {

            String userId = UUID.randomUUID().toString();
            firestore.collection("users")
                    .document(firebaseUsers[0].getUid())
                    .set(new User(userId, firebaseUsers[0].getEmail(), firebaseUsers[0].getDisplayName(),
                            UserRole.FREE_USER))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                CustomToast.showShortToast(context, "Your details has been stored");
                            } else {
                                CustomToast.showLongToast(context,"Unable to store your details,\n " +
                                        "Check your internet connection and try again");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            CustomToast.showLongToast(context, e.getMessage());
                        }
                    });
            return null;
        }
    }

    public void syncNotesToUserCloud(Context context, FirebaseUser user){
        noteRoomDatabase = NoteRoomDatabase.getInstance(context);
        List<Note> allNotesList = noteRoomDatabase.noteDAO().getAllNotes();
        Map<String, Note> allNotesMap = new HashMap<>();

        for (Note note : allNotesList) {
            new SyncNotesAsyncTask(context, note, user).execute();
        }
    }

    private class SyncNotesAsyncTask extends AsyncTask<Void,Void,Void> {

        private Context context;
        private Note data;
        private FirebaseUser user;

        public SyncNotesAsyncTask(Context context, Note data, FirebaseUser user) {
            this.context = context;
            this.data = data;
            this.user = user;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //TODO Set the data as a Map of Integer, Note pair
            firestore.collection("users_note")
                    .document(user.getUid())
                    .collection(user.getDisplayName() + "Notes")
                    .add(data)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                CustomToast.showLongToast(context, "Notes is syncing, please wait");
                            } else {
                                CustomToast.showLongToast(context, "Unable to sync notes");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            CustomToast.showLongToast(context, e.getMessage());
                        }
                    });
            return null;
        }
    }

    public void downloadNotesFromCloud(Context context, FirebaseUser user){
        noteRoomDatabase = NoteRoomDatabase.getInstance(context);
        firestore.collection("users_note")
                .document(user.getUid())
                .collection(user.getDisplayName()+"Notes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            MutableLiveData<List<Note>> data = new MutableLiveData<>();
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                Note note = snapshot.toObject(Note.class);
                                noteRoomDatabase.noteDAO().insertNote(note);
                            }
//                            List<HashMap<Integer, Note>> hashMapArrayList = new ArrayList<>();
//                            DocumentSnapshot snapshot = task.getResult();
//                            hashMapArrayList.addAll((List<HashMap<Integer,Note>>)snapshot.get("value"));
//
//                            for (HashMap<Integer, Note> noteHashMap : hashMapArrayList) {
//                                for (Note note : noteHashMap.values()) {
//                                    noteRoomDatabase.noteDAO().insertNote(note);
//                                }
//                            }

                        } else {
                            CustomToast.showLongToast(context, "Unable to download notes");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        CustomToast.showLongToast(context, e.getMessage());
                    }
                });

    }

    //Return firebaseUser Instance
    public void getUserDetailsFromFirebase(){
    }
}
