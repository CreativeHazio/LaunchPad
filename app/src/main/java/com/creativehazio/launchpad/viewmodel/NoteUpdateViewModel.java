package com.creativehazio.launchpad.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.creativehazio.launchpad.database.NoteRoomDatabase;
import com.creativehazio.launchpad.model.Note;
import com.creativehazio.launchpad.repository.NoteRepository;

public class NoteUpdateViewModel extends AndroidViewModel {

    private NoteRepository noteRepository = NoteRepository.getInstance();

    private MutableLiveData<Note> data;

    public NoteUpdateViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Note> getNoteById(String id){
        data = noteRepository.getNoteByIdFromDatabase(getApplication(), id);
        return data;
    }

    public void updateNote(Note note) {
        noteRepository.updateNoteIntoDatabase(getApplication(), note);
    }
}
