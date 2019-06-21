package com.amrita.notes.home.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.amrita.notes.R;
import com.amrita.notes.home.model.NoteListDetails;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<NoteListDetails> noteListDetailsArrayList = new ArrayList<>();
    private HomeFragment homeFragment;

    NotesRecyclerAdapter(Context context, HomeFragment homeFragment) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.homeFragment = homeFragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_rv_notes_list, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NoteListDetails noteListDetails = noteListDetailsArrayList.get(position);
        final NotesViewHolder notesViewHolder = (NotesViewHolder) holder;

        notesViewHolder.noteName.setText(noteListDetails.getName());
        notesViewHolder.noteDescription.setText(noteListDetails.getDescription());
        if(noteListDetails.isImportant().equals("true")){
            notesViewHolder.importantMark.setVisibility(View.VISIBLE);
        } else {
            notesViewHolder.importantMark.setVisibility(View.GONE);
        }
        notesViewHolder.noteCard.setOnClickListener(v -> {
            NoteListDetails noteItem = new NoteListDetails(noteListDetailsArrayList.get(position).getId(),
                    noteListDetailsArrayList.get(position).getName(),
                    noteListDetailsArrayList.get(position).getDescription(),
                    noteListDetailsArrayList.get(position).isImportant());
            PopupMenu popup = new PopupMenu(context, ((NotesViewHolder) holder).noteOptions);
            popup.inflate(R.menu.notes_options);
            if(noteListDetailsArrayList.get(position).isImportant().equals("true")){
                MenuItem menuItem = popup.getMenu().findItem(R.id.menu_mark_important);
                menuItem.setTitle("Remove Important");
            }

            try {
                Method method = popup.getMenu().getClass().getDeclaredMethod("setOptionalIconsVisible", boolean.class);
                method.setAccessible(true);
                method.invoke(popup.getMenu(), true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.menu_delete:
                        homeFragment.deleteNote(noteListDetailsArrayList.get(position).getId());
                        return true;
                    case R.id.menu_share:
                        homeFragment.shareNote(noteItem);
                        return true;
                    case R.id.menu_update:
                        homeFragment.editNote(noteItem);
                        return true;
                    case R.id.menu_mark_important:
                        if(noteListDetailsArrayList.get(position).isImportant().equals("true")){
                            homeFragment.markImportant(noteItem, "false");
                        } else {
                            homeFragment.markImportant(noteItem, "true");
                        }
                        return true;
                    default:
                        return false;
                }
            });
            popup.show();
        });

    }

    void setData(List<NoteListDetails> noteListDetails) {
        this.noteListDetailsArrayList = noteListDetails;
    }

    @Override
    public int getItemCount() {
        return noteListDetailsArrayList.size();
    }

    class NotesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.noteName)
        TextView noteName;
        @BindView(R.id.noteCard)
        CardView noteCard;
        @BindView(R.id.noteDescription)
        TextView noteDescription;
        @BindView(R.id.noteOptions)
        TextView noteOptions;
        @BindView(R.id.importantMark)
        ImageView importantMark;

        NotesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
