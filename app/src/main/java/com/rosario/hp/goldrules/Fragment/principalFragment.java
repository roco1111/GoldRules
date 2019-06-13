package com.rosario.hp.goldrules.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rosario.hp.goldrules.MainActivity;
import com.rosario.hp.goldrules.R;
import com.rosario.hp.goldrules.activity_comienzo;


public class principalFragment extends Fragment {
    public static final String ARG_ARTICLES_NUMBER = "principal";


    Activity activity;
    private int posicion;
    private String posicion_string;

    private Button comenzar;

    public principalFragment(){
        // Constructor vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.principal, container, false);

        activity = getActivity();

        setHasOptionsMenu(true);

        Resources res = getActivity().getApplicationContext().getResources();

        final Bundle args1 = new Bundle();

        comenzar = rootView.findViewById(R.id.comenzar);

        comenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), activity_comienzo.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });


       return rootView;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return false;
    }

}