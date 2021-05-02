package com.example.alex.virtuallaboratory.Fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class FragmentSensor extends android.support.v4.app.Fragment implements Button.OnClickListener {

    FragmentCallback callback;
    Button btn_Accel, btn_Gyro, btn_MagField;

    public FragmentSensor() {
        // Required empty public constructor
    }



/**-Fragment-lifecycle-------------------------------------------------------------------------------Fragment-lifecycle**/
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensor, container, false);
        initialization(view);

        setListeners();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @Override
    public void onResume(){
        super.onResume();
        getActivity().setTitle(getResources().getString(R.string.sensors));
        //callback.getCurrentFragment(FragmentCallback.FRAGMENT_SENSOR);
    }

/**======================================================================================================================**/
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_accel:{
                callback.getPressedButton(FragmentCallback.BTN_ACCELEROMETER);
                break;
            }
            case R.id.btn_gyro:{
                callback.getPressedButton(FragmentCallback.BTN_GYROSCOPE);
                break;
            }
            case R.id.btn_magneticField:{
                callback.getPressedButton(FragmentCallback.BTN_MAGNETIC_FIELD);
            }
            default:{
                break;
            }
        }
    }

    public void initialization(View view){
        btn_Accel = (Button)view.findViewById(R.id.btn_accel);
        btn_Gyro = (Button)view.findViewById(R.id.btn_gyro);
        btn_MagField = (Button)view.findViewById(R.id.btn_magneticField);
    }
    public void setListeners(){
        btn_Accel.setOnClickListener(this);
        btn_Gyro.setOnClickListener(this);
        btn_MagField.setOnClickListener(this);

    }
}
