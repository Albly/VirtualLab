package com.example.alex.virtuallaboratory.Fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.alex.virtuallaboratory.FragmentCallback;
import com.example.alex.virtuallaboratory.R;


/**
 * Фрагмент {@link Fragment} экрана.
 * Активности с данным фрагментом должны реализовать
 * {@link FragmentCallback} интерфейс
 * Для обработки событий взаимодействия.
 */
public class FragmentLabList extends android.support.v4.app.Fragment implements Button.OnClickListener{

    private FragmentCallback callback;

    Button btn_Lab1,btn_Lab2,btn_Lab3,btn_Lab4,btn_Lab5,btn_Lab6,btn_Lab7,btn_Lab8;

    public FragmentLabList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lab_list,container,false);
        //TODO: Переделать в массив
        btn_Lab1 = (Button)view.findViewById(R.id.btn_lab1);
        btn_Lab2 = (Button)view.findViewById(R.id.btn_lab2);
        btn_Lab3 = (Button)view.findViewById(R.id.btn_lab3);
        btn_Lab4 = (Button)view.findViewById(R.id.btn_lab4);
        btn_Lab5 = (Button)view.findViewById(R.id.btn_lab5);
        btn_Lab6 = (Button)view.findViewById(R.id.btn_lab6);
        btn_Lab7 = (Button)view.findViewById(R.id.btn_lab7);
        btn_Lab8 = (Button)view.findViewById(R.id.btn_lab8);

        btn_Lab1.setOnClickListener(this);
        btn_Lab2.setOnClickListener(this);
        btn_Lab3.setOnClickListener(this);
        btn_Lab4.setOnClickListener(this);
        btn_Lab5.setOnClickListener(this);
        btn_Lab6.setOnClickListener(this);
        btn_Lab7.setOnClickListener(this);
        btn_Lab8.setOnClickListener(this);

        return view;
    }

    /**
     * onAttach(Context context) doesn't work with API lowest than 23
     * TODO: Remake it for checking API and use (Activity) for API<23 and (Context) for API>22
     * "http://stackoverflow.com/questions/32083053/android-fragment-onattach-deprecated"
     * **/
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if(activity instanceof FragmentCallback){
            callback = (FragmentCallback)activity;
        }else {
            throw new RuntimeException((activity.toString()+" must implement FragmentCallback"));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @Override
    public void onResume(){
        super.onResume();
        getActivity().setTitle(getResources().getString(R.string.perform));
        //callback.getCurrentFragment(FragmentCallback.FRAGMENT_LABLIST);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_lab1: {
                callback.getPressedButton(FragmentCallback.BTN_LAB1);
                break;
            }
        }
    }
}
