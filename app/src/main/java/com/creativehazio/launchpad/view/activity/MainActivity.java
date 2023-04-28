package com.creativehazio.launchpad.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.creativehazio.launchpad.R;
import com.creativehazio.launchpad.view.fragment.AllFragment;
import com.creativehazio.launchpad.view.fragment.FinanceFragment;
import com.creativehazio.launchpad.view.fragment.HealthFragment;
import com.creativehazio.launchpad.view.fragment.HobbyFragment;
import com.creativehazio.launchpad.view.fragment.OtherFragment;
import com.creativehazio.launchpad.view.fragment.PersonalFragment;
import com.creativehazio.launchpad.view.fragment.WorkFragment;
import com.creativehazio.launchpad.model.Note;
import com.creativehazio.launchpad.mutual_functions.CustomToast;
import com.creativehazio.launchpad.viewmodel.MainActivityViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final static int NOTE_REQUEST_CODE = 111;

    private static final String ALL_FRAGMENT = "AllFragment";
    private static final String PERSONAL_FRAGMENT = "PersonalFragment";
    private static final String WORK_FRAGMENT = "WorkFragment";
    private static final String HEALTH_FRAGMENT = "HealthFragment";
    private static final String FINANCE_FRAGMENT = "FinanceFragment";
    private static final String HOBBY_FRAGMENT = "HobbyFragment";
    private static final String OTHER_FRAGMENT = "OtherFragment";

    private Toolbar toolbar;
    private FloatingActionButton addNoteBtn;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;

    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewsAndConfigs();

        setSupportActionBar(toolbar);
        replaceFragment(new AllFragment());

        drawerToggle = new ActionBarDrawerToggle(this,
                        drawerLayout,
                        toolbar,
                        R.string.nav_open_drawer,
                        R.string.nav_close_drawer);
        drawerLayout.addDrawerListener(drawerToggle);
        navigationView.setNavigationItemSelectedListener(this);

        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                switch (getCurrentFragment(MainActivity.this).getClass().getSimpleName()) {
                    case ALL_FRAGMENT:
                    case PERSONAL_FRAGMENT:
                        intent.putExtra(NoteActivity.CATEGORY_POSITION, 0);
                        break;
                    case WORK_FRAGMENT:
                        intent.putExtra(NoteActivity.CATEGORY_POSITION, 1);
                        break;
                    case HEALTH_FRAGMENT:
                        intent.putExtra(NoteActivity.CATEGORY_POSITION, 2);
                        break;
                    case FINANCE_FRAGMENT:
                        intent.putExtra(NoteActivity.CATEGORY_POSITION, 3);
                        break;
                    case HOBBY_FRAGMENT:
                        intent.putExtra(NoteActivity.CATEGORY_POSITION, 4);
                        break;
                    case OTHER_FRAGMENT:
                        intent.putExtra(NoteActivity.CATEGORY_POSITION, 5);
                        break;
                }
                startActivityForResult(intent,
                        NOTE_REQUEST_CODE);
            }
        });

    }

    public Fragment getCurrentFragment(AppCompatActivity activity) {
        FragmentManager manager = activity.getSupportFragmentManager();
        List<Fragment> fragments = manager.getFragments();
        if (fragments != null){
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible()){
                    return fragment;
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED){
            CustomToast.showLongToast(MainActivity.this, getString(R.string.note_empty));
        } else if (requestCode == NOTE_REQUEST_CODE && data != null) {

            String noteId = data.getStringExtra(NoteActivity.NOTE_ID);
            String noteCategory = data.getStringExtra(NoteActivity.NOTE_CATEGORY);
            String noteDate = data.getStringExtra(NoteActivity.NOTE_DATE);
            String noteTitle = data.getStringExtra(NoteActivity.NOTE_TITLE);
            String noteContent = data.getStringExtra(NoteActivity.NOTE_CONTENT);

            Note newNote = new Note(noteId,noteCategory,noteDate,noteTitle,noteContent);
            viewModel.insertNoteIntoDatabase(newNote);

            CustomToast.showLongToast(MainActivity.this, getString(R.string.note_added_success));
        } else {
            CustomToast.showLongToast(MainActivity.this, getString(R.string.note_added_failed));
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_all:
                replaceFragment(new AllFragment());
                closeDrawer();
                return true;
            case R.id.nav_personal:
                replaceFragment(new PersonalFragment());
                closeDrawer();
                return true;
            case R.id.nav_work:
                replaceFragment(new WorkFragment());
                closeDrawer();
                return true;
            case R.id.nav_health:
                replaceFragment(new HealthFragment());
                closeDrawer();
                return true;
            case R.id.nav_finance:
                replaceFragment(new FinanceFragment());
                closeDrawer();
                return true;
            case R.id.nav_hobby:
                replaceFragment(new HobbyFragment());
                closeDrawer();
                return true;
            case R.id.nav_other:
                replaceFragment(new OtherFragment());
                closeDrawer();
                return true;
            case R.id.nav_ai_transcribe:
                startActivity(new Intent(MainActivity.this, AiTranscribe.class));
                return true;
            case R.id.nav_ai_summarize:
                startActivity(new Intent(MainActivity.this, AiSummarize.class));
                return true;
            case R.id.nav_get_premium:
                startActivity(new Intent(MainActivity.this, GetPremiumActivity.class));
                return true;
            default:
                return false;
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.note_fragment_container, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        transaction.commit();
    }

    private void closeDrawer(){
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile_menu:
                startActivity(new Intent(this,ProfileActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            closeDrawer();
        } else {
            exitApp();
        }
    }

    private void exitApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.exit_app))
                .setPositiveButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.continue_in_app), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog exitAppDialog = builder.create();
        exitAppDialog.show();
    }

    private void initViewsAndConfigs() {
        viewModel = new ViewModelProvider(MainActivity.this)
                .get(MainActivityViewModel.class);
        addNoteBtn = findViewById(R.id.add_note_btn);
        toolbar = findViewById(R.id.main_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.main_navigation_view);
    }
}