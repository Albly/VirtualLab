package com.example.alex.virtuallaboratory.Fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.alex.virtuallaboratory.FragmentCallback;
import com.example.alex.virtuallaboratory.R;

/**
 * Фрагмент {@link Fragment} экрана.
 * Активности с данным фрагментом должны реализовать
 * {@link FragmentCallback} интерфейс
 * Для обработки событий взаимодействия.
 */
public class FragmentAbout extends android.support.v4.app.Fragment {

    private TextView textView;

    private FragmentCallback callback;

    public FragmentAbout() {
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
        View view =inflater.inflate(R.layout.fragment_about, container, false);
        WebView webView = (WebView)view.findViewById(R.id.web);
        WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        webView.loadData("<!doctype html><html> <head><meta charset=\\\"utf-8\\\"></head> <body> <p align=\"center\"><strong>О программе.</strong></p>\n" +
                "<p><strong>Приложение &laquo;Мобильная лаборатория&raquo; позволит современному школьнику легко и с интересом освоить основные законы физики и выполнить классические лабораторные работы с применением своего смартфона в качестве измерительного устройства.</strong></p>\n" +
                "<p><strong>Известно, что современный смартфон &ndash; это не только средство связи, но и мощная измерительная установка, позволяющая с помощью собственных датчиков измерять скорость и ускорение, угол наклона устройства, величину магнитного поля, уровень освещенности и многие другие физические величины. </strong></p>\n" +
                "<p><strong>Приложение позволит освоить принцип работы датчиков, научит правильно использовать их для проведения измерений.</strong></p>\n" +
                "<p><strong>Каждая лабораторная работа посвящена изучению определенного физического эффекта или явления и включает в себя краткий теоретический материал по теме, описание порядка проведения измерений и оформления результатов, а также набор контрольных вопросов.</strong></p>\n" +
                "<p><strong>Теперь у Вас в руках настоящая лаборатория, с которой урок физики станет намного интересней и увлекательней!</strong></p>\n" +
                " </body></html>","text/html; charset=utf-8","UTF-8");
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
    public void onResume() {
        super.onResume();
       // callback.getCurrentFragment(FragmentCallback.FRAGMENT_ABOUT);
        getActivity().setTitle(getResources().getString(R.string.about));
    }
}
