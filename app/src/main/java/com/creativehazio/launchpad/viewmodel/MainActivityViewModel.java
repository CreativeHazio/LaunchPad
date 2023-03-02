package com.creativehazio.launchpad.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.creativehazio.launchpad.model.Note;
import com.creativehazio.launchpad.repository.NoteRepository;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MainActivityViewModel extends AndroidViewModel {

    private NoteRepository noteRepository = NoteRepository.getInstance();

    private MutableLiveData<List<Note>> data;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public void insertNoteIntoDatabase(Note note){
        noteRepository.insertNoteIntoDatabase(getApplication(), note);
    }

    public void deleteNoteFromDatabase(Note note) {
        noteRepository.deleteNoteFromDatabase(getApplication(), note);
    }

    public LiveData<List<Note>> getNotesByCategory(String category){
        data = noteRepository.getNotesByCategoryFromDatabase(getApplication(), category);
        return data.getValue() == null || data.getValue().isEmpty() ? setNullDataForCategoryNotes(category) : data;
    }

    public LiveData<List<Note>> getAllNotes(){
        data = noteRepository.getAllNotesFromDatabase(getApplication());
        return data.getValue() == null || data.getValue().isEmpty() ? setNullDataForAllNotes() : data;
    }

    public LiveData<List<Note>> setNullDataForAllNotes(){
        String id = UUID.randomUUID().toString();
        Note note = new Note(id,"Personal",java.text.DateFormat
                .getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).
                format(new Date()),
                "Launchpad", "Write down all your ideas...");
        ArrayList<Note> noteList = new ArrayList<>();
        noteList.add(note);
        MutableLiveData<List<Note>> nullData = new MutableLiveData<>();
        nullData.setValue(noteList);
        return nullData;
    }

    public LiveData<List<Note>> setNullDataForCategoryNotes(String category){
        Note note = new Note("1",category,java.text.DateFormat
                .getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).
                format(new Date()),
                "Launchpad", "Write down all your "+category+" ideas...");
        ArrayList<Note> noteList = new ArrayList<>();
        noteList.add(note);
        MutableLiveData<List<Note>> nullData = new MutableLiveData<>();
        nullData.setValue(noteList);
        return nullData;
    }

}
