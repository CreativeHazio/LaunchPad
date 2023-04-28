package com.creativehazio.launchpad.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

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
import com.creativehazio.launchpad.viewmodel.NoteUpdateViewModel;

import java.text.DateFormat;
import java.util.Date;

public class NoteUpdateActivity extends AppCompatActivity {

    public final static String CATEGORY_POSITION = "note_category";
    public static final String NOTE_ID = "note_id";

    private final String PERSONAL = "Personal";
    private final String WORK = "Work";
    private final String HEALTH = "Health";
    private final String FINANCE = "Finance";
    private final String HOBBY = "Hobbies";
    private final String OTHER = "Other";

    private NoteUpdateViewModel viewModel;
    private LiveData<Note> note;

    private Toolbar toolbar;
    private Spinner noteCategorySpinner;
    private TextView noteDateTextView;
    private EditText noteTitleEdt;
    private EditText noteContentEdt;
    private Button addReminderBtn;
    private Button addBulletBtn;

    private String oldTitle,oldContent, oldCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_update);
        initViewsAndConfigs();

        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.note));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = new ViewModelProvider(this).get(NoteUpdateViewModel.class);

        showNote();

        addBulletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteContentEdt.append("\n"+"\u25CB"+"\t\t");
            }
        });

    }

    private void showNote() {
        Intent intent = getIntent();
        String noteId = intent.getStringExtra(NOTE_ID);
        String categoryPositionIntent = intent.getStringExtra(CATEGORY_POSITION);

        int categoryPosition = 0;
        switch (categoryPositionIntent) {
            case PERSONAL:
                categoryPosition = 0;
                break;
            case WORK:
                categoryPosition = 1;
                break;
            case HEALTH:
                categoryPosition = 2;
                break;
            case FINANCE:
                categoryPosition = 3;
                break;
            case HOBBY:
                categoryPosition = 4;
                break;
            case OTHER:
                categoryPosition = 5;
                break;
            default:
                break;
        }

        note = viewModel.getNoteById(noteId);
        noteCategorySpinner.setSelection(categoryPosition);
        noteDateTextView.setText(note.getValue().getDate());
        noteTitleEdt.setText(note.getValue().getTitle());
        noteContentEdt.setText(note.getValue().getContent());

        oldCategory = noteCategorySpinner.getSelectedItem().toString();
        oldTitle = noteTitleEdt.getText().toString();
        oldContent = noteContentEdt.getText().toString();

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
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        if (!TextUtils.isEmpty(noteTitleEdt.getText().toString().trim())
                || !TextUtils.isEmpty(noteContentEdt.getText().toString().trim())) {

            updateNote();
        }
        super.onBackPressed();
    }

    private void updateNote() {

        if (!oldCategory.contentEquals(noteCategorySpinner.getSelectedItem().toString())
                || !oldTitle.contentEquals(noteTitleEdt.getText().toString())
                || !oldContent.contentEquals(noteContentEdt.getText().toString())) {

            Intent intent = getIntent();
            String noteId = intent.getStringExtra(NOTE_ID);
            Note note = new Note(noteId, noteCategorySpinner.getSelectedItem().toString(),
                    java.text.DateFormat
                            .getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).
                            format(new Date()), noteTitleEdt.getText().toString(), noteContentEdt.getText().toString());

            viewModel.updateNote(note);
            finish();
        } else {
            finish();
        }
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