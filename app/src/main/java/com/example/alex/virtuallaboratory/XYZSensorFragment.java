package com.example.alex.virtuallaboratory;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Фрагмент {@link android.support.v4.app.Fragment} экрана.
 * Активности с данным фрагментом должны реализовать
 * {@link FragmentCallback} интерфейс
 * Для обработки событий взаимодействия.
 * Параметр Фрагмента - тип датчика
 */

public class XYZSensorFragment extends android.support.v4.app.Fragment implements SeekBar.OnSeekBarChangeListener, SensorEventListener{

    public static int[] Accuracy = {3,3,3};
    /**
     * 0 - Accelerometer
     * 1 - Gyroscope
     * 2 - Magnetic field
     * */

    private static final String ARG_SENSORTYPE = "sensorType";
    private static final String ACCELEROMETER_KEY="Accelerometer.key";
    private static final String GYROSCOPE_KEY = "Gyroscope.key";
    private static final String MAGNETIC_KEY = "Magnetic.key";

    private int sensorType;
    private FragmentCallback callback;
    private int typeIndex;
    private Sensor sensor;
    private SensorManager manager;
    private SharedPreferences preference;
    private TextView tv_sensorInfo, tv_XY, tv_XZ, tv_ZY, tv_accuracy;
    private double ang_XY, ang_XZ, ang_ZY;
    Timer timer;

    private boolean isEmpty=false;
   // private SeekBar sb_accuracy;


    public XYZSensorFragment(){

    }

    public static XYZSensorFragment newInstance(int Device){
        XYZSensorFragment fragment = new XYZSensorFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SENSORTYPE, Device);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sensorType = getArguments().getInt(ARG_SENSORTYPE);
            switch (sensorType){
                case Sensor.TYPE_ACCELEROMETER:{
                    typeIndex = 0;
                    break;
                }
                case  Sensor.TYPE_GYROSCOPE:{
                    typeIndex = 1;
                    break;
                }
                case Sensor.TYPE_MAGNETIC_FIELD:{
                    typeIndex = 2;
                }
            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_xyz_sensors, container,false);
        initialization(view);
        setListeners();
       // load();

        manager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(sensorType);
        if(sensor==null){
            isEmpty=true;
            View toastView = inflater.inflate(R.layout.toast_message, (ViewGroup)view.findViewById(R.id.toast_layout));
            TextView toastMessage = (TextView)toastView.findViewById(R.id.tv_toast);
            toastMessage.setText("Извините, на вашем устройстве нет данного датчика");
            Toast toast = new Toast(getActivity().getApplicationContext());
            toast.setView(toastView);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();

        }else{
            sensorInformation();
        }

        return view;
    }



    private void sensorInformation() {
        StringBuilder sensorInformation = new StringBuilder();
        sensorInformation.append("===========================================\n")
                .append(getResources().getString(R.string.name)+ " = " + sensor.getName())
                .append("\n")
                .append(getResources().getString(R.string.vendor)+ " = " + sensor.getVendor())
                .append("\n")
                .append(getResources().getString(R.string.version)+ " = " + sensor.getVersion())
                .append("\n")
                .append(getResources().getString(R.string.type)+" = " + sensor.getType())
                .append("\n")
                .append(getResources().getString(R.string.resolution)+ " = " + sensor.getResolution())
                .append("\n")
                .append(getResources().getString(R.string.max_range)+ " = " + sensor.getMaximumRange())
                .append("\n===========================================");
        tv_sensorInfo.setText(sensorInformation);

    }

    private void load() {

       // preference = getActivity().getPreferences(Context.MODE_PRIVATE);
       // Accuracy[0] = preference.getInt(ACCELEROMETER_KEY,10);
      //  Accuracy[1] = preference.getInt(GYROSCOPE_KEY,10);
       // Accuracy[2] = preference.getInt(MAGNETIC_KEY,10);
    }

    private void save(){
        preference = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt(ACCELEROMETER_KEY, Accuracy[0]);
        editor.putInt(GYROSCOPE_KEY, Accuracy[1]);
        editor.putInt(MAGNETIC_KEY,Accuracy[2]);
        editor.apply();
    }

    private void setListeners() {
       // sb_accuracy.setOnSeekBarChangeListener(this);

    }

    private void initialization(View view) {
        tv_sensorInfo = (TextView)view.findViewById(R.id.tv_Sensor_info);
        tv_XY = (TextView)view.findViewById(R.id.tv_XY);
        tv_XZ = (TextView)view.findViewById(R.id.tv_XZ);
        tv_ZY = (TextView)view.findViewById(R.id.tv_ZY);
      //  tv_accuracy = (TextView)view.findViewById(R.id.tv_accuracy);
       // sb_accuracy = (SeekBar)view.findViewById(R.id.sb_accuracy);
    }



    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
       // Accuracy[typeIndex] = sb_accuracy.getProgress();
       // tv_accuracy.setText("Accuracy"+" = "+ Accuracy[typeIndex]);

      //  updateInterface();
    }

    private void updateInterface() {
        tv_XY.setText(" XY \n"+ ang_XY+"\n");
        tv_XZ.setText(" XZ \n"+ ang_XZ+"\n");
        tv_ZY.setText(" ZY \n"+ ang_ZY+"\n");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

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
        if(!isEmpty){
            load();

            if(sensor!=null){
                manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
            if(typeIndex==0){
                getActivity().setTitle(getResources().getString(R.string.accelerometer));
                // callback.getCurrentFragment(FragmentCallback.FRAGMENT_ACCELEROMETER);
            }
            if(typeIndex==1){
                getActivity().setTitle(getResources().getString(R.string.gyroscope));
                //callback.getCurrentFragment(FragmentCallback.FRAGMENT_GYROSCOPE);
            }
            if(typeIndex == 2){
                getActivity().setTitle(getResources().getString(R.string.magnetic_field_sensor));
                //callback.getCurrentFragment(FragmentCallback.FRAGMENT_MAGNETIC_FIELD);
            }

            timer=new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           updateInterface();
                        }
                    });
                }
            };
            timer.schedule(task,0,300);

        }
    }

    @Override
    public void onPause(){
        super.onPause();
        save();
        if(manager!=null) {
            manager.unregisterListener(this);
        }

        if(timer!=null){
            timer.cancel();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() != -1){
            ang_XY = new BigDecimal(event.values[0]).
                    setScale(Accuracy[typeIndex], RoundingMode.HALF_UP).doubleValue();
            ang_XZ = new BigDecimal(event.values[1]).
                    setScale(Accuracy[typeIndex], RoundingMode.HALF_UP).doubleValue();
            ang_ZY = new BigDecimal(event.values[2]).
                    setScale(Accuracy[typeIndex], RoundingMode.HALF_UP).doubleValue();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
