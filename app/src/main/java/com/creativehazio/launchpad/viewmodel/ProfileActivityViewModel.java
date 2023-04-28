package com.creativehazio.launchpad.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.IntentSender;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.creativehazio.launchpad.model.Note;
import com.creativehazio.launchpad.model.User;
import com.creativehazio.launchpad.mutual_functions.NetworkStatus;
import com.creativehazio.launchpad.repository.UserRepository;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//TODO: Use this for profile activity with user model
public class ProfileActivityViewModel extends AndroidViewModel {

    private UserRepository userRepository = UserRepository.getInstance();
    private NetworkStatus internetStatus = new NetworkStatus(getApplication());

    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;

    private MutableLiveData<User> userData;

    public ProfileActivityViewModel(@NonNull Application application) {
        super(application);
        firebaseAuth = FirebaseAuth.getInstance();
    }



    public LiveData<User> getUserData(){
        userData = new MutableLiveData<>();
        if (getCurrentUser() != null) {
            User user = new User(getCurrentUser().getPhotoUrl(), getCurrentUser().getDisplayName(),
                    getCurrentUser().getEmail());
            userData.setValue(user);
        } else {
            User user = new User(null,"Hey Creative", "Sign in to sync notes to cloud");
            userData.setValue(user);
        }

        return userData;
    }

    public FirebaseUser getCurrentUser(){
        currentUser = firebaseAuth.getCurrentUser();
        return currentUser;
    }

    public void signOut () {
        getFirebaseAuth().signOut();
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public void addUserToFireStore (FirebaseUser firebaseUser){
        userRepository.addUserToFirestore(firebaseUser, getApplication());
    }

    public void syncNotesToUserCloud() {
        userRepository.syncNotesToUserCloud(getApplication(), getCurrentUser());
    }

    public void downloadNotesFromUserCloud(){
        userRepository.downloadNotesFromCloud(getApplication(), getCurrentUser());
    }

//    public LiveData<List<>> setNullDataForAllNotes(){
//        Note note = new Note("1","Personal",java.text.DateFormat
//                .getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).
//                format(new Date()),
//                "Launchpad", "Write down all your ideas...");
//        ArrayList<Note> noteList = new ArrayList<>();
//        noteList.add(note);
//        MutableLiveData<List<Note>> nullData = new MutableLiveData<>();
//        nullData.setValue(noteList);
//        return nullData;
//    }

}
