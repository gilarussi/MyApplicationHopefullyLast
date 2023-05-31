package com.example.myapplicationhopefullylast;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.myapplicationhopefullylast.databinding.ActivityMainBinding;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;

public class HomePageFragment extends Fragment {

    ActivityMainBinding binding;

    ArrayList<String> s;
    ArrayAdapter arrayAdapter;
    int n=0;
    private static final String TAG = "Swipe Position";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding=ActivityMainBinding.inflate(getLayoutInflater());
        // Inflate the layout for this fragment

       // View v =  binding.getRoot();
        View v=inflater.inflate(R.layout.fragment_home_page, container, false);

        s=new ArrayList<String>();
        s.add("one");
        s.add("two");
        s.add("three");
        s.add("four");
        s.add("five");

        SwipeFlingAdapterView swipeFlingAdapterView=(SwipeFlingAdapterView) v.findViewById(R.id.card);
        arrayAdapter=new ArrayAdapter<String>(getActivity(),R.layout.details, R.id.textView,s);
        swipeFlingAdapterView.setAdapter(arrayAdapter);
        swipeFlingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter()
            {
                s.remove(0);
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