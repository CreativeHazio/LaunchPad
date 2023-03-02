package com.creativehazio.launchpad.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.creativehazio.launchpad.model.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertNote(Note note);

    @Query("SELECT * FROM note")
    List<Note> getAllNotes();

    @Query("SELECT * FROM note WHERE id LIKE :id")
    Note getNoteById(String id);

    @Query("SELECT * FROM note WHERE category LIKE :category")
    List<Note> getNotesByCategory(String category);

    @Update
    void updateNote(Note note);

    @Delete
    int deleteNote(Note note);

}
