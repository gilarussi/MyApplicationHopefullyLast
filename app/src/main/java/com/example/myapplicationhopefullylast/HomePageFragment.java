package com.example.myapplicationhopefullylast;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.myapplicationhopefullylast.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class HomePageFragment extends Fragment {

    ActivityMainBinding binding;
   // String value;


//    ArrayList<String> s;
    ArrayList<Person> p;
    ArrayAdapter arrayAdapter;
    int n=0;
    private static final String TAG = "Swipe Position";
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding=ActivityMainBinding.inflate(getLayoutInflater());
        // Inflate the layout for this fragment

       // View v =  binding.getRoot();
        View v=inflater.inflate(R.layout.fragment_home_page, container, false);


        p=new ArrayList<>();
         //Get a reference to the Firestore database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
       // FirebaseUser current= FirebaseAuth.getInstance().getCurrentUser();
        db.collection("notes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<DocumentSnapshot> d = value.getDocuments();

                for (DocumentSnapshot documentSnapshot : d)
                {
                   // Toast.makeText(getActivity(), ":" + d.size(), Toast.LENGTH_SHORT).show();
                   CollectionReference collection= FirebaseFirestore.getInstance().collection("notes")
                            .document(documentSnapshot.getId()).collection("my_notes");
                   collection.addSnapshotListener(new EventListener<QuerySnapshot>() {
                       @Override
                       public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                           if (!value.isEmpty()){
                               for (DocumentSnapshot documentSnapshot1:value.getDocuments())
                               {
                                   String title = documentSnapshot1.getString("title");
                                   String content = documentSnapshot1.getString("content");
                                   Note note = new Note();
                                   note.setTitle(title);
                                   note.setContent(content);
                                   Person person = new Person(documentSnapshot.getId(),note);
                                   p.add(person);
                               }
                           }
                       }
                   });
                }
            }
        });


        p.add(new Person("hello",new Note()));
        SwipeFlingAdapterView swipeFlingAdapterView=(SwipeFlingAdapterView) v.findViewById(R.id.card);
        arrayAdapter=new ArrayAdapter<Person>(getActivity(),R.layout.details, R.id.textView,p);
        swipeFlingAdapterView.setAdapter(arrayAdapter);
        swipeFlingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter()
            {
                p.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o)
            {
                Toast.makeText(getActivity(), "Left is swiped", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Left Swipe");

            }

            @Override
            public void onRightCardExit(Object o)
            {
                Toast.makeText(getActivity(), "Right is swiped", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Right Swipe");
                String email  = "gil2005.ar@gmail.com";
                String subject = "job offer";
                String message = "hey";

                String[] addresses = email.split(",");

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL,addresses);
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, message);

                PackageManager packageManager = requireActivity().getPackageManager();
                if (intent.resolveActivity(packageManager) != null)
                {
                    startActivity(Intent.createChooser(intent, "Send Email"));
                }
                else
                {
                    Toast.makeText(getActivity(), "NO APP IS INSTALLED", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onAdapterAboutToEmpty(int i) {

            }

            @Override
            public void onScroll(float v) {

            }
        });


        return v;


    }
}