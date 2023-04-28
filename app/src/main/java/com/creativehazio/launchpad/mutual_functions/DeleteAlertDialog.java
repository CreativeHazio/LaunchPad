package com.creativehazio.launchpad.mutual_functions;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.creativehazio.launchpad.adapter.NoteListAdapter;
import com.creativehazio.launchpad.model.Note;
import com.creativehazio.launchpad.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class DeleteAlertDialog {

    public static void deleteNote(Context context, MainActivityViewModel viewModel,
                                  NoteListAdapter noteListAdapter, int position,
                                  ArrayList<Note> noteArrayList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to delete note? \nNote title: "
                + noteArrayList.get(position).getTitle());
        builder.setCancelable(false);

        builder.setPositiveButton("Delete", (DialogInterface.OnClickListener) (dialog, which) -> {
            viewModel.deleteNoteFromDatabase(noteArrayList.get(position));
            viewModel.getAllNotes().getValue().remove(position);
            noteListAdapter.notifyItemRemoved(position);
            viewModel.getAllNotes().observe((LifecycleOwner) context, new Observer<List<Note>>() {
                @Override
                public void onChanged(List<Note> notes) {
//                    noteArrayList.remove(position);
                    NoteSorter.sort((ArrayList<Note>) notes);
                    noteListAdapter.refreshNoteArrayList(notes);
                }
            });
        });

        builder.setNegativeButton("Keep", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
