package com.example.alex.virtuallaboratory.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import com.example.alex.virtuallaboratory.FragmentCallback;
import com.example.alex.virtuallaboratory.R;

import java.io.IOException;


/**
 * Первый запускаемый
 * Фрагмент {@link Fragment} экрана.
 * Активности с данным фрагментом должны реализовать
 * {@link FragmentCallback} интерфейс
 * Для обработки событий взаимодействия.
 */
public class FragmentMain extends android.support.v4.app.Fragment implements View.OnClickListener {

   private Button btn_Perform, btn_Sensors, btn_About, btn_Share;

    FragmentCallback callback;

    public FragmentMain() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_main,container,false);
        btn_Perform = (Button)view.findViewById(R.id.btn_perform);
        btn_Sensors =(Button)view.findViewById(R.id.btn_sensors);
        btn_About = (Button)view.findViewById(R.id.btn_about);
        btn_Share = (Button)view.findViewById(R.id.btn_share);

        btn_Perform.setOnClickListener(this);
        btn_Sensors.setOnClickListener(this);
        btn_About.setOnClickListener(this);
        btn_Share.setOnClickListener(this);

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
        getActivity().setTitle(getResources().getString(R.string.app_name));
        //callback.getCurrentFragment(FragmentCallback.FRAGMENT_MAIN);
    }

    @Override
    public void onClick(View v) {
        String msg = new String();
        switch(v.getId()){
            case R.id.btn_perform:{
                msg = FragmentCallback.BTN_PERFORM;
                break;
            }
            case  R.id.btn_sensors:{
                msg = FragmentCallback.BTN_SENSORS;
                break;
            }
            case  R.id.btn_about:{
                msg = FragmentCallback.BTN_ABOUT;
                break;
            }
            case R.id.btn_share:{
                msg = FragmentCallback.BTN_SHARE;
                break;
            }
            default:{break;}
        }
        if(msg!=null){
            callback.getPressedButton(msg);
        }
    }
}
