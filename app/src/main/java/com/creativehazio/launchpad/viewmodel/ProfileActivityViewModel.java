package com.creativehazio.launchpad.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.creativehazio.launchpad.model.Note;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//TODO: Use this for profile activity with user model
public class ProfileActivityViewModel extends AndroidViewModel {

    public ProfileActivityViewModel(@NonNull Application application) {
        super(application);
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
