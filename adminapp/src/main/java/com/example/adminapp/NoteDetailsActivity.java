package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.annotation.Documented;
import java.util.HashMap;

public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleEditText,contentEditText;
    ImageButton saveNoteBtn;
    TextView pageTitleTextView;
    String title, content, docId;
    boolean isEditMode = false;
    TextView deleteNoteTextViewBtn;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        titleEditText = findViewById(R.id.notes_title_text);
        contentEditText = findViewById(R.id.notes_content_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);
        pageTitleTextView = findViewById(R.id.page_title);
        deleteNoteTextViewBtn = findViewById(R.id.delete_note_text_view_btn);


        databaseReference = FirebaseDatabase.getInstance().getReference("notes2");

        //receive data
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if (docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }

        titleEditText.setText(title);
        contentEditText.setText(content);
        if (isEditMode){
            pageTitleTextView.setText("Edit your note");
            deleteNoteTextViewBtn.setVisibility(View.VISIBLE);
        }

        saveNoteBtn.setOnClickListener((v) -> saveNote());

        deleteNoteTextViewBtn.setOnClickListener((v)-> deleteNoteFromFirebase());
    }
    void saveNote()
    {
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();
        if (noteTitle.isEmpty()){  //noteTitle==null ||
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

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.child(""+currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.getValue()!=null){
                    int count= (int) snapshot.getChildrenCount();
                   DatabaseReference d= databaseReference.child(""+currentUser.getUid()).child(""+(count+1));
                   d.child("title").setValue(note.getTitle());
                   d.child("content").setValue(note.getContent());
                   d.child("time").setValue(note.getTimestamp().getSeconds());
                }
                else{
                    DatabaseReference d= databaseReference.child(""+currentUser.getUid()).child("1");
                    d.child("title").setValue(note.getTitle());
                    d.child("content").setValue(note.getContent());
                    d.child("time").setValue(note.getTimestamp().getSeconds());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        DocumentReference documentReference;
        HashMap<String, Boolean> hashMap1 = new HashMap<>();
        hashMap1.put("is", true);
        FirebaseFirestore.getInstance().collection("notes")
                .document(currentUser.getEmail()).set(hashMap1);
        if (isEditMode)
        {   //update note
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        }
        else
        {   //create new note
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    //note is added
                    Utility.showToast(NoteDetailsActivity.this,"Note added successfully");
                    finish();
                }
                else {
                    Utility.showToast(NoteDetailsActivity.this,"Failed while adding note");

                }
            }
        });
    }

    void deleteNoteFromFirebase(){
        DocumentReference documentReference;
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    //note is deleted
                    Utility.showToast(NoteDetailsActivity.this,"Note deleted successfully");
                    finish();
                }
                else {
                    Utility.showToast(NoteDetailsActivity.this,"Failed while deleted note");

                }
            }
        });

    }

}