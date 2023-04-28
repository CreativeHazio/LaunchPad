package com.creativehazio.launchpad.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.creativehazio.launchpad.R;
import com.creativehazio.launchpad.adapter.NoteListAdapter;
import com.creativehazio.launchpad.model.Note;
import com.creativehazio.launchpad.mutual_functions.DeleteAlertDialog;
import com.creativehazio.launchpad.mutual_functions.NoteSorter;
import com.creativehazio.launchpad.view.activity.NoteUpdateActivity;
import com.creativehazio.launchpad.viewmodel.MainActivityViewModel;

import java.util.ArrayList;

public class AllFragment extends Fragment {

    private RecyclerView recyclerView;
    private NoteListAdapter noteListAdapter;
    private MainActivityViewModel viewModel;

    private ArrayList<Note> allNotes;

    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_all, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewsAndConfigs(view);

        viewModel = new ViewModelProvider(this)
                .get(MainActivityViewModel.class);

        allNotes = (ArrayList<Note>) viewModel.getAllNotes().getValue();
        NoteSorter.sort(allNotes);

        noteListAdapter = new NoteListAdapter(getContext(), allNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(noteListAdapter);

        noteListAdapter.setListener(new NoteListAdapter.Listener() {
            @Override
            public void onClick(ArrayList<Note> noteArrayList, int position) {
                Intent intent = new Intent(getContext(), NoteUpdateActivity.class);
                intent.putExtra(NoteUpdateActivity.NOTE_ID, noteArrayList.get(position).getId());
                intent.putExtra(NoteUpdateActivity.CATEGORY_POSITION, noteArrayList.get(position).getCategory());
                startActivity(intent);
            }

            @Override
            public void onLongClick(ArrayList<Note> noteArrayList, int position) {
                DeleteAlertDialog.deleteNote(getContext(),
                        viewModel, noteListAdapter, position, noteArrayList);
            }
        });

        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                noteListAdapter.getFilter().filter(newText);

                return false;
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.getAllNotes().observe(this, notes -> {
            allNotes = (ArrayList<Note>) notes;
            NoteSorter.sort(allNotes);
            noteListAdapter.refreshNoteArrayList(allNotes);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        searchView.setQuery("", false);
        searchView.clearFocus();
    }

    private void initViewsAndConfigs(View rootView) {
        recyclerView = rootView.findViewById(R.id.all_recycler_view);
        searchView = rootView.findViewById(R.id.all_note_search);
    }
}