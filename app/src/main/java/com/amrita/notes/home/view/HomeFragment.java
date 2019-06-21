package com.amrita.notes.home.view;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amrita.notes.AddNote.AddNoteFragment;
import com.amrita.notes.MainActivity;
import com.amrita.notes.R;
import com.amrita.notes.helper.DatabaseUtils;
import com.amrita.notes.helper.EventBusData;
import com.amrita.notes.helper.SharedPrefs;
import com.amrita.notes.home.model.NoteListDetails;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.amrita.notes.helper.DatabaseUtils.INPUT_COLUMN_Description;
import static com.amrita.notes.helper.DatabaseUtils.INPUT_COLUMN_ID;
import static com.amrita.notes.helper.DatabaseUtils.INPUT_COLUMN_Important;
import static com.amrita.notes.helper.DatabaseUtils.INPUT_COLUMN_Title;

public class HomeFragment extends Fragment {

    private Context context;
    private DatabaseUtils databaseUtils;
    private List<NoteListDetails> dataList = new ArrayList<>();

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.fabAddNew)
    FloatingActionButton fabAddNew;
    @BindView(R.id.welcomeUser)
    TextView tvWelcomeUser;
    @BindView(R.id.welcomeUserMessage)
    TextView welcomeUserMessage;
    @BindView(R.id.emptyListLayout)
    RelativeLayout emptyListLayout;

    private NotesRecyclerAdapter notesRecyclerAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        context = getContext();
        SharedPrefs sharedPrefs = new SharedPrefs(context);
        databaseUtils = new DatabaseUtils(context);
        Objects.requireNonNull(((MainActivity) context).getSupportActionBar()).show();

        tvWelcomeUser.setText("Hi "+ sharedPrefs.getUserName()+ " !");

        //Recycler View
        notesRecyclerAdapter = new NotesRecyclerAdapter(context, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(notesRecyclerAdapter);

        //Floating Action Button
        fabAddNew.setOnClickListener(v -> {
            EventBus.getDefault().postSticky(new EventBusData(null, false));
            ((MainActivity) getContext()).addFragment(new AddNoteFragment(), "Add Fragment");
        });

        //Load Notes
        loadNotes();

        return view;
    }

    private void loadNotes(){
        dataList.clear();
        Cursor cursor = databaseUtils.getAllNotes();
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()) {
                NoteListDetails noteListDetails = new NoteListDetails(
                        cursor.getString(cursor.getColumnIndex(INPUT_COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(INPUT_COLUMN_Title)),
                        cursor.getString(cursor.getColumnIndex(INPUT_COLUMN_Description)),
                        cursor.getString(cursor.getColumnIndex(INPUT_COLUMN_Important))
                );
                dataList.add(noteListDetails);
                cursor.moveToNext();
            }
        }
        if(dataList.isEmpty()){
            emptyListLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            welcomeUserMessage.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyListLayout.setVisibility(View.GONE);
            welcomeUserMessage.setVisibility(View.VISIBLE);
        }
        notesRecyclerAdapter.setData(dataList);
        notesRecyclerAdapter.notifyDataSetChanged();
    }

    void deleteNote(String id){
        databaseUtils.deleteNote(id);
        loadNotes();
    }

    void editNote(NoteListDetails noteListDetails){
        EventBus.getDefault().postSticky(new EventBusData(noteListDetails, true));
        ((MainActivity) getContext()).addFragment(new AddNoteFragment(), "Add Fragment");
    }

    void markImportant(NoteListDetails noteListDetails, String isImportant){
        databaseUtils.updateNote(noteListDetails.getId(), noteListDetails.getName(), noteListDetails.getDescription(), isImportant);
        loadNotes();
        notesRecyclerAdapter.notifyDataSetChanged();
    }

    void shareNote(NoteListDetails noteListDetails){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, noteListDetails.getName() + "\n" + noteListDetails.getDescription());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

}
