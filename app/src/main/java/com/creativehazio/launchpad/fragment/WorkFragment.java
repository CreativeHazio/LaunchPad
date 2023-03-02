package com.creativehazio.launchpad.fragment;

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
import com.creativehazio.launchpad.mutual_functions.NoteSorter;
import com.creativehazio.launchpad.view.NoteUpdateActivity;
import com.creativehazio.launchpad.viewmodel.MainActivityViewModel;

import java.util.ArrayList;

public class WorkFragment extends Fragment {

    private MainActivityViewModel viewModel;
    private RecyclerView recyclerView;
    private NoteListAdapter adapter;
    private ArrayList<Note> workNoteList;

    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_work, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this)
                .get(MainActivityViewModel.class);

        workNoteList = (ArrayList<Note>) viewModel.getNotesByCategory(getString(R.string.work))
                .getValue();
        NoteSorter.sort(workNoteList);

        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new NoteListAdapter(getContext(), workNoteList);
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
                DeleteAlertDialog.deleteNote(getContext(),noteArrayList.get(position).getTitle(),
                        viewModel, noteArrayList.get(position), adapter, position, noteArrayList);
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
        viewModel.getNotesByCategory(getString(R.string.work)).observe(this, notes -> {
            workNoteList = (ArrayList<Note>) notes;
            NoteSorter.sort(workNoteList);
            adapter.refreshNoteArrayList(workNoteList);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        searchView.setQuery("", false);
        searchView.clearFocus();
    }
}