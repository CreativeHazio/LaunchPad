package com.creativehazio.launchpad.mutual_functions;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.AndroidViewModel;

import com.creativehazio.launchpad.R;
import com.creativehazio.launchpad.adapter.NoteListAdapter;
import com.creativehazio.launchpad.model.Note;
import com.creativehazio.launchpad.view.MainActivity;
import com.creativehazio.launchpad.viewmodel.MainActivityViewModel;

import java.util.ArrayList;

public class DeleteAlertDialog {

    public static void deleteNote(Context context, String noteTitle, MainActivityViewModel viewModel,
                                  Note note, NoteListAdapter noteListAdapter, int position,
                                  ArrayList<Note> noteArrayList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to delete note? \nNote title: "+ noteTitle);
        builder.setCancelable(false);

        builder.setPositiveButton("Delete", (DialogInterface.OnClickListener) (dialog, which) -> {
            viewModel.deleteNoteFromDatabase(note);
            noteArrayList.remove(position);
            noteListAdapter.notifyItemRemoved(position);
        });

        builder.setNegativeButton("Keep", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
