package com.creativehazio.launchpad.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "note")
public class Note {

    @PrimaryKey()
    @NonNull
    private String id;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "content")
    private String content;

    @Ignore
    public Note() {
    }

    public Note(@NonNull String id, String category, String date, String title, String content) {
        this.id = id;
        this.category = category;
        this.date = date;
        this.title = title;
        this.content = content;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id='" + id + '\'' +
                ", category='" + category + '\'' +
                ", date='" + date + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
