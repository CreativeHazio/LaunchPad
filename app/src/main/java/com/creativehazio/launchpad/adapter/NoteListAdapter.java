package com.creativehazio.launchpad.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativehazio.launchpad.R;
import com.creativehazio.launchpad.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteListAdapter
        extends RecyclerView.Adapter<NoteListAdapter.ViewHolder>
        implements Filterable {

    private Context context;
    private ArrayList<Note> noteArrayList;
    private ArrayList<Note> noteArrayListFull;
    private Listener listener;

    public interface Listener {
        void onClick(ArrayList<Note> noteArrayList, int position);
        void onLongClick(ArrayList<Note> noteArrayList, int position);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public NoteListAdapter(Context context, ArrayList<Note> noteArrayList) {
        this.context = context;
        this.noteArrayListFull = noteArrayList;
        this.noteArrayList = new ArrayList<>(noteArrayListFull);
    }

    @Override
    public Filter getFilter() {
        return notesFilter;
    }

    private final Filter notesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Note> filteredNoteList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                filteredNoteList.addAll(noteArrayListFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Note notes : noteArrayListFull) {
                    if (notes.getTitle().toLowerCase().contains(filterPattern)
                            || notes.getContent().toLowerCase().contains(filterPattern)) {
                        filteredNoteList.add(notes);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredNoteList;
            results.count = filteredNoteList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            noteArrayList.clear();
            noteArrayList.addAll((ArrayList)filterResults.values);
            notifyDataSetChanged();
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (getCurrentFragment((AppCompatActivity) context).getClass().getSimpleName()
                .contentEquals("AllFragment")){
            view = LayoutInflater.from(context)
                    .inflate(R.layout.note_cardview,parent,false);
        } else {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.note_category_cardview,parent,false);
        }

        return new ViewHolder(view);
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CardView cardView = holder.cardView;
        TextView currentDate = cardView.findViewById(R.id.date);
        TextView category = cardView.findViewById(R.id.note_category);
        TextView title = cardView.findViewById(R.id.title);
        TextView contents = cardView.findViewById(R.id.contents);

        if (getCurrentFragment((AppCompatActivity) context).getClass().getSimpleName()
                .contentEquals("AllFragment")) {

            currentDate.setText(noteArrayList.get(position).getDate());

            category.setText(noteArrayList.get(position).getCategory());

            title.setText(noteArrayList.get(position).getTitle());

            contents.setText(noteArrayList.get(position).getContent());

        } else {

            currentDate.setText(noteArrayList.get(position).getDate());

            title.setText(noteArrayList.get(position).getTitle());

            contents.setText(noteArrayList.get(position).getContent());

        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null){
                    listener.onClick(noteArrayList, holder.getAdapterPosition());
                }
            }
        });

        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listener != null){
                    listener.onLongClick(noteArrayList, holder.getAdapterPosition());
                }
                return true;
            }
        });

    }

    public void refreshNoteArrayList(List<Note> notes) {
        this.noteArrayList = (ArrayList<Note>) notes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return noteArrayList == null ? 0 : noteArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
        }
    }
}
