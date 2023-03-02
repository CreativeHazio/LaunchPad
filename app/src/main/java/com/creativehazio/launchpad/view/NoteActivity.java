package com.creativehazio.launchpad.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.creativehazio.launchpad.R;
import com.creativehazio.launchpad.model.Note;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

public class NoteActivity extends AppCompatActivity {

    public final static String CATEGORY_POSITION = "category";

    public static final String NOTE_CATEGORY = "note_category";
    public static final String NOTE_DATE = "note_date";
    public static final String NOTE_TITLE = "note_title";
    public static final String NOTE_CONTENT = "note_content";
    public static final String NOTE_ID = "note_id";

    private Toolbar toolbar;
    private Spinner noteCategorySpinner;
    private TextView noteDateTextView;
    private EditText noteTitleEdt;
    private EditText noteContentEdt;
    private Button addReminderBtn;
    private Button addBulletBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        initViewsAndConfigs();

        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.note));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noteDateTextView.setText(
                java.text.DateFormat
                        .getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).
                        format(new Date())
        );

        Intent intent = getIntent();
        int categoryPosition = intent.getIntExtra(CATEGORY_POSITION, 0);
        noteCategorySpinner.setSelection(categoryPosition);

        addBulletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteContentEdt.append("\n"+"\u25CB"+"\t\t");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
            case R.id.note_toolbar_save:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        saveNote();
    }

    private void saveNote() {
        Intent resultIntent = new Intent();

        String noteId = UUID.randomUUID().toString();
        String noteTitle = noteTitleEdt.getText().toString();
        String noteContent = noteContentEdt.getText().toString();
        String noteDate = java.text.DateFormat
                .getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).
                format(new Date());
        String noteCategory = noteCategorySpinner.getSelectedItem().toString();

        if (TextUtils.isEmpty(noteTitleEdt.getText().toString().trim())
                && TextUtils.isEmpty(noteContentEdt.getText().toString().trim())) {
            setResult(RESULT_CANCELED,resultIntent);
        } else {
            Note newNote = new Note(noteId,noteCategory,noteDate,noteTitle,noteContent);
            resultIntent.putExtra(NOTE_ID, newNote.getId());
            resultIntent.putExtra(NOTE_CATEGORY, newNote.getCategory());
            resultIntent.putExtra(NOTE_DATE, newNote.getDate());
            resultIntent.putExtra(NOTE_TITLE, newNote.getTitle());
            resultIntent.putExtra(NOTE_CONTENT, newNote.getContent());
            setResult(RESULT_OK, resultIntent);
        }

        finish();

    }

    private void initViewsAndConfigs() {
        toolbar = findViewById(R.id.note_toolbar);
        noteCategorySpinner = findViewById(R.id.change_category_spinner);
        noteDateTextView = findViewById(R.id.note_date);
        noteTitleEdt = findViewById(R.id.note_title);
        noteContentEdt = findViewById(R.id.note_content);
        addReminderBtn = findViewById(R.id.add_reminder_btn);
        addBulletBtn = findViewById(R.id.add_bullet_btn);
    }
}