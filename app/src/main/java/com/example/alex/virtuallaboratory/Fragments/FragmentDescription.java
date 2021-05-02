package com.example.alex.virtuallaboratory.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.alex.virtuallaboratory.R;

/**
 * Created by Alex on 04.11.2017.
 */

public class FragmentDescription extends android.support.v4.app.Fragment{

    public FragmentDescription(){


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lab_description, container, false);

        return view;
    }
    @Override
    public void onResume(){
        super.onResume();
        getActivity().setTitle("Описание лабораторной");
    }

}
