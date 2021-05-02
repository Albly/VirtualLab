package com.example.alex.virtuallaboratory.Fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alex.virtuallaboratory.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Alex on 30.01.2018.
 */

public class FragmentTESTING extends android.support.v4.app.Fragment implements SensorEventListener, View.OnClickListener {

    TextView tv_GravityAngles, tv_AccelAngles, tv_AngleX, tv_AccelP, tv_Acceleration;

    private final double[] gravityComponents = new double[3];
    private final double[] accelComponents = new double[3];
    private final double[] gravityAngles = new double[3];
    private final double[] accelAngles = new double[3];
    private final double[] linearComponents = new double[3];
    double g = 9.806;

    double acceleration;
    double angleX;
    private SensorManager manager;
    Sensor accelerometer, gravity;
    private Timer timer;

    double linear,Linear;


    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState){

        View parent = inflater.inflate(R.layout.fragment_labwork_first, container, false);
        tv_GravityAngles = (TextView)parent.findViewById(R.id.tv_gravityA);
        tv_AccelAngles = (TextView)parent.findViewById(R.id.tv_accelA);
        tv_AngleX= (TextView)parent.findViewById(R.id.tv_angleX);
        tv_AccelP = (TextView)parent.findViewById(R.id.tv_accelP);
        tv_Acceleration = (TextView)parent.findViewById(R.id.tv_mod);

        manager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);

        if(manager!=null){
            accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            gravity = manager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        }

        return parent;
    }

    @Override
    public void onPause() {
        super.onPause();

        manager.unregisterListener(this);
        timer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();

            manager.registerListener(this, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
            manager.registerListener(this, manager.getDefaultSensor(Sensor.TYPE_GRAVITY), SensorManager.SENSOR_DELAY_FASTEST);

        timer=new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        draw();
                    }
                });
            }
        };
        timer.schedule(task,0,300);
    }

    public void draw(){

        tv_GravityAngles.setText(format(accelComponents[0],2)+" "+format(accelComponents[1],2)+" "+format(accelComponents[2],2));
        tv_AccelAngles.setText(format(accelAngles[0],2)+" "+format(accelAngles[1],2)+" "+format(accelAngles[2],2));
        tv_AngleX.setText(format(acceleration,2));
        tv_AccelP.setText(format(gravityComponents[0],2)+" "+format(gravityComponents[1],2)+" "+format(gravityComponents[2],2));



       // tv_GravityAngles.setText(format(gravityAngles[0],2)+" "+ format(gravityAngles[1],2)+" "+format(gravityAngles[2],2)+" ");

//        tv_AccelAngles.setText(format(accelAngles[0],2)+" "+format(accelAngles[1],2)+" "+format(accelAngles[2],2)+" ");
  //      tv_AngleX.setText(format(Linear,2));
    //    tv_Acceleration.setText(format(acceleration,2));
      //  tv_AccelP.setText(format(linear,2));
        //tv_AccelP.setText(format(accelComponents[0],2)+" "+format(accelComponents[1],2)+" "+format(accelComponents[2],2)+" ");

    }

    String format(double value, int count){
        if(count==1){
            return String.format(String.format("%(.1f",value));
        }
        if(count==2){
            return String.format(String.format("%(.2f",value));
        }
        if(count==3){
            return String.format(String.format("%(.3f",value));
        }
        else return String.format(String.format("%(.4f",value));
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_GRAVITY){
            for(int i = 0; i < 3 ; i++){
                gravityComponents[i]=event.values[i];
            }
        }

        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){                                      // Если данные приходят с акселерометра

            for(int i = 0; i < 3; i++){
                accelComponents[i] =  event.values[i];
            }
        }


        double gravity = Math.sqrt(

                gravityComponents[0]*gravityComponents[0]+
                        gravityComponents[1]*gravityComponents[1]+
                        gravityComponents[2]*gravityComponents[2]);

        acceleration = Math.sqrt(
                accelComponents[0]*accelComponents[0]+
                        accelComponents[1]*accelComponents[1]+
                        accelComponents[2]*accelComponents[2]);

        for(int i=0; i<3; i++){
            gravityAngles[i] = Math.toDegrees(Math.acos(gravityComponents[i]/gravity));
            accelAngles[i] = Math.toDegrees(Math.acos(accelComponents[i]/acceleration));

            linearComponents[i]=accelComponents[i]-gravityComponents[i];
        }


        Linear = Math.sqrt(linearComponents[0]*linearComponents[0]+
        linearComponents[1]*linearComponents[1]+
        linearComponents[2]*linearComponents[2]);



       // accelAngles[0] = Math.toDegrees(Math.atan((accelComponents[0]/(Math.sqrt(accelComponents[1]*accelComponents[1]
       //         +accelComponents[2]*accelComponents[2])))));
      //  accelAngles[1]=  Math.toDegrees(Math.atan( (accelComponents[1]/(Math.sqrt(accelComponents[0]*accelComponents[0]
        //        +accelComponents[2]*accelComponents[2])))));
       // accelAngles[2] =  Math.toDegrees(Math.atan((accelComponents[2]/(Math.sqrt(accelComponents[0]*accelComponents[0]
        //        +accelComponents[1]*accelComponents[1])))));


        linear = Math.sqrt(acceleration*acceleration-g*g*Math.pow(Math.sin(Math.toRadians(90-accelAngles[2])),2))-g*Math.cos(Math.toRadians(90-accelAngles[2]));


        //linear = Math.sqrt(acceleration*acceleration+g*g-2*acceleration*g*Math.cos(Math.toRadians(accelAngles[0])));

       // linear = Math.abs(Math.sqrt(acceleration*acceleration-g*g
       //         *Math.sin(Math.toRadians(accelAngles[2]))
       //         *Math.sin(Math.toRadians(accelAngles[2])))-g
        //        *Math.cos(Math.toRadians(accelAngles[2])));

        //angleX = Math.sqrt(accelAngles[0]*accelAngles[0]+accelAngles[1]*accelAngles[1]);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onClick(View view) {

    }
}
