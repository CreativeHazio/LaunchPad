package com.creativehazio.launchpad.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.creativehazio.launchpad.R;
import com.creativehazio.launchpad.adapter.NoteListAdapter;
import com.creativehazio.launchpad.model.Note;
import com.creativehazio.launchpad.mutual_functions.DeleteAlertDialog;
import com.creativehazio.launchpad.mutual_functions.DeleteAlertDialogForCategories;
import com.creativehazio.launchpad.mutual_functions.NoteSorter;
import com.creativehazio.launchpad.view.activity.NoteUpdateActivity;
import com.creativehazio.launchpad.viewmodel.MainActivityViewModel;

import java.util.ArrayList;

public class HealthFragment extends Fragment {

    private MainActivityViewModel viewModel;
    private RecyclerView recyclerView;
    private NoteListAdapter adapter;
    private ArrayList<Note> healthNoteList;

    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_health, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this)
                .get(MainActivityViewModel.class);

        healthNoteList = (ArrayList<Note>) viewModel.getNotesByCategory(getString(R.string.health))
                .getValue();
        NoteSorter.sort(healthNoteList);

        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new NoteListAdapter(getContext(), healthNoteList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));

        adapter.setListener(new NoteListAdapter.Listener() {
            @Override
            public void onClick(ArrayList<Note> noteArrayList, int position) {
                Intent intent = new Intent(getContext(), NoteUpdateActivity.class);
                intent.putExtra(NoteUpdateActivity.NOTE_ID, noteArrayList.get(position).getId());
                intent.putExtra(NoteUpdateActivity.CATEGORY_POSITION, noteArrayList.get(position).getCategory());
                startActivity(intent);
            }

            @Override
            public void onLongClick(ArrayList<Note> noteArrayList, int position) {
                DeleteAlertDialogForCategories.deleteNote(getContext(),
                        viewModel, adapter, position, noteArrayList,getString(R.string.health));
            }
        });

        searchView = view.findViewById(R.id.note_search);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);

                return false;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.getNotesByCategory(getString(R.string.health)).observe(this, notes -> {
            healthNoteList = (ArrayList<Note>) notes;
            NoteSorter.sort(healthNoteList);
            adapter.refreshNoteArrayList(healthNoteList);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        searchView.setQuery("", false);
        searchView.clearFocus();
    }
}