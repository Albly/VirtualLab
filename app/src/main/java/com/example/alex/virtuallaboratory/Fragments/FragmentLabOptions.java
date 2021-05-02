package com.example.alex.virtuallaboratory.Fragments;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.alex.virtuallaboratory.FragmentCallback;
import com.example.alex.virtuallaboratory.R;

/**
 * Фрагмент {@link Fragment} экрана.
 * Активности с данным фрагментом должны реализовать
 * {@link FragmentCallback} интерфейс
 * Для обработки событий взаимодействия.
 */
public class FragmentLabOptions extends android.support.v4.app.Fragment implements View.OnClickListener{

    Button btn_performLabWork, btn_description, btn_theory, btn_controlQuestions;
    FragmentCallback callback;

    private static final String LAB_NUMBER_LABEL = "LabWorkNumber";
    private String labWorkNumber;

    public FragmentLabOptions() {
        // Required empty public constructor
    }

    /**
     * Передача параметров во фрагмент осуществляется с помощью
     * статического метода newInstance
     * Необходимые значения обернуты в {@link Bundle}
     * @param param - Номер лабораторной работы, параметр для фрагмента
     * TODO: param сделать типа int
     * @return фрагмент с параметром
     * */
    public static FragmentLabOptions newInstance(String param) {
        FragmentLabOptions fragment = new FragmentLabOptions();
        Bundle args = new Bundle();
        args.putString(LAB_NUMBER_LABEL, param);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Параметр фрагмента извлекается из {@link Bundle}
     * методом getArguments()
     * TODO: Менять содержимое фрагмента в зависимости от номера лабораторной работы
     * */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            labWorkNumber = getArguments().getString(LAB_NUMBER_LABEL); // номер лабораторной работы
        }
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
    public void onResume(){
        super.onResume();
        getActivity().setTitle(getResources().getString(R.string.laboratory_work)+" 1");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lab_options, container, false);
        btn_controlQuestions=(Button)view.findViewById(R.id.btn_controlQuestions);
        btn_description = (Button)view.findViewById(R.id.btn_description);
        btn_performLabWork = (Button)view.findViewById(R.id.btn_performLab);
        btn_theory = (Button)view.findViewById(R.id.btn_theory);

        btn_theory.setOnClickListener(this);
        btn_performLabWork.setOnClickListener(this);
        btn_description.setOnClickListener(this);
        btn_controlQuestions.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
    switch (v.getId()){
        //TODO: Добавить действия для обработки нажатий
        case R.id.btn_controlQuestions:{
            callback.getPressedButton(FragmentCallback.BTN_CONTROL_QUESTIONS);
            break;
        }
        case R.id.btn_description:{
            callback.getPressedButton(FragmentCallback.BTN_DESCRIPTION);
            break;
        }
        case R.id.btn_performLab:{
            callback.getPressedButton(FragmentCallback.BTN_LAB_PERFORM);
            break;
        }
        case R.id.btn_theory:{
            callback.getPressedButton(FragmentCallback.BTN_THEORY);
            break;
        }
        default:{break;}
    }
    }
}
