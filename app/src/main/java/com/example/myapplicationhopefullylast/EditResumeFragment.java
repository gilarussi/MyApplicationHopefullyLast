package com.example.myapplicationhopefullylast;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;


public class EditResumeFragment extends Fragment {

    public interface FragmentFinishListener {
        void onFragmentFinish();
    }

    private FragmentFinishListener fragmentFinishListener;

    EditText titleEditText,contentEditText;
    ImageButton saveNoteBtn;
    String id="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view= inflater.inflate(R.layout.fragment_edit_resume, container, false);

        titleEditText = view.findViewById(R.id.notes_title_text);
        contentEditText = view.findViewById(R.id.notes_content_text);
        saveNoteBtn = view.findViewById(R.id.save_note_btn);

        saveNoteBtn.setOnClickListener((v) -> saveNote());

        return view;
    }
    void saveNote() {
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();
        if (noteTitle.isEmpty()) {  //noteTitle==null ||
            titleEditText.setError("Title is required");
            return;
        }
        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        saveNoteToFirebase(note);
    }
    void saveNoteToFirebase(Note note){

        if (fragmentFinishListener != null) {
            fragmentFinishListener.onFragmentFinish();
        }

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        HashMap<String, Boolean> hashMap1 = new HashMap<>();
        hashMap1.put("is", true);
        FirebaseFirestore.getInstance().collection("resume")
                .document(currentUser.getEmail()).set(hashMap1);

        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNotes().document("1");

//        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                if (value.exists()){
//                    id=value.getId();
//                    Toast.makeText(getActivity(), ""+id, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        if (!id.equals("")){
//            Utility.getCollectionReferenceForNotes().document(id).delete();
//        }
        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    //note is added
                    Utility.showToast(getActivity(),"Resume added successfully");
                   // finish();
                }
                else {
                    Utility.showToast(getActivity(),"Failed while adding resume");

                }
            }
        });


    }

}