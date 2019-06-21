package com.amrita.notes.AddNote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amrita.notes.R;
import com.amrita.notes.helper.DatabaseUtils;
import com.amrita.notes.helper.EventBusData;
import com.amrita.notes.home.model.NoteListDetails;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AddNoteFragment extends Fragment {

    private Context context;
    private DatabaseUtils databaseUtils;

    @BindView(R.id.noteName)
    EditText noteName;
    @BindView(R.id.noteDescription)
    EditText noteDescription;
    @BindView(R.id.save)
    Button save;

    private String id;
    private NoteListDetails noteListDetails;
    private boolean isEdit;

    public AddNoteFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusData eventBusData) {
        isEdit = eventBusData.isEdit();
        if(isEdit){
            noteListDetails = eventBusData.getNoteListDetails();
            id = noteListDetails.getId();
            noteName.setText(noteListDetails.getName());
            noteDescription.setText(noteListDetails.getDescription());
            save.setText("UPDATE");
        }
        else {
            id = "";
            noteName.setText("");
            noteDescription.setText("");
            save.setText("ADD NOTE");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_note, container, false);
        ButterKnife.bind(this, view);

        context = getContext();
        databaseUtils = new DatabaseUtils(context);

        save.setOnClickListener(v -> {
            String name = noteName.getText().toString();
            String description = noteDescription.getText().toString();
            if(validate()) {
                if(isEdit){
                    databaseUtils.updateNote(id, name, description, "false");
                } else {
                    databaseUtils.insertNote(name, description);
                }
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });

        return view;
    }

    private boolean validate(){
        String name = noteName.getText().toString();
        String description = noteDescription.getText().toString();
        boolean value = true;
        if (description.isEmpty()){
            Toast.makeText(context, "Please enter description!", Toast.LENGTH_SHORT).show();
            value = false;
        }

        if (name.isEmpty()){
            Toast.makeText(context, "Please enter title!", Toast.LENGTH_SHORT).show();
            value = false;
        }

        return value;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
