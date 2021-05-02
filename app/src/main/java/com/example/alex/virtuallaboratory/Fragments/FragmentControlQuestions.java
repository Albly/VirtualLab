package com.example.alex.virtuallaboratory.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.alex.virtuallaboratory.R;


public class FragmentControlQuestions extends android.support.v4.app.Fragment {

    public FragmentControlQuestions() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_control_questions,container,false);

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Контрольные вопросы");
    }
}
