package com.example.alex.virtuallaboratory.LabWorks;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.virtuallaboratory.Analytics;
import com.example.alex.virtuallaboratory.Fragments.Measurement;
import com.example.alex.virtuallaboratory.R;
import com.example.alex.virtuallaboratory.RVAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



public class FragmentLabWorkFirst extends android.support.v4.app.Fragment implements SensorEventListener, View.OnClickListener
{

    /**Конпка "начать измерение" и текст "угол наклона"*/
   private Button btn_startMeasurement;
   private TextView tv_currentAngle;

    /**View элементы для кастомного {@link Toast}*/

   private View toastView;
   private Toast toast;
   private TextView toastMessage;

    /**Константы
     * @param g - ускорение свободного падения
     * @param alpha - коэффициент для фильтра частот
     * */

   public final static double g = 9.60665;
   public final static double alpha =0.85;

    /**Списочные массивы
     * @param arrayData - массив с измеренными коэффициентами трения
     * @param arrayAngle - массив с измеренными углами
     *  */
   private ArrayList<Double> arrayData=new ArrayList<>();
   private ArrayList<Double> arrayAngle = new ArrayList<>();

    /**Массивы с измерениями
     * @param gravityComponents - компоненты ускорения свободного падения
     * @param linAccelerationComponents - компоненты линейного ускорения
     * @param accelerometerComponentsFiltered - Компоненты полного ускорения после фильтра
     * @param currentFriction - текущий коэффициент тренения
     * @param acceleration - Модуль полного ускорения
     * @param linearAcceleration - модуль линейного ускорения
     * @param currentAngle - текущий угол наклона
     * @param fullGravity - модуль ускорения свободного падения
     * @param lastAngle - угол с которого началось измерение коэфф трения
     * */
    private final double[] gravityComponents = new double[3];
    private final double[] linAccelerationComponents = new double[3];
    private final double[] accelerometerComponentsFiltered = new double[3];
    private double currentFriction = 0.0;
    private double acceleration=-1;
    private double linearAcceleration=-1;
    private double currentAngle = 0;
    public double fullGravity = 0;
    public double lastAngle=0;

    /**Задействованные датчики
     * @param manager - мэнеджер датчиков
     * @param accelerometer - акселерометер
     * @param gravity - датчик гравитации
     * */
    private SensorManager manager;
    private Sensor accelerometer,gravity;

    /**логические переменные
     * @param isGravity - В устройстве есть датчик гравтации?
     * @param isAccelerometer - В устройстве есть акселерометер?
     * @param isGravityReady - Посчитаны ли проекции гравитации?
     * @param research - Идет исследование?
     * */
    private boolean isGravity = true;
    private boolean isAccelerometer = true;
    private boolean isGravityReady=false;
    private boolean research= false;

    /**Компоненты для создания {@link android.support.v7.widget.CardView} */
    private RecyclerView recyclerView;
    private List<Measurement> measurements;
    private RVAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    /**Таймеры*/
    private Timer timer,reducer;



    StringBuilder dataText;
    ClipboardManager clipboardManager;
    ClipData clipData;

    public FragmentLabWorkFirst() {
        // Required empty public constructor
    }

    /**===============================================================================================Android Lifecycle==**/
    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState){

        /** Инициализация View элементов для фрагмента*/
        View parent = inflater.inflate(R.layout.fragment_labwork_one, container, false); // извлекаем layout
        btn_startMeasurement =(Button)parent.findViewById(R.id.btn_startMeasurement);
        btn_startMeasurement.setOnClickListener(this);
        tv_currentAngle = (TextView)parent.findViewById(R.id.tv_currentAngle);

        recyclerView = (RecyclerView)parent.findViewById(R.id.rv);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        measurements = new ArrayList<>();
        adapter = new RVAdapter(measurements);
        recyclerView.setAdapter(adapter);

        manager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);

        if(manager!=null){
            accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            gravity = manager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        }

        toastView = inflater.inflate(R.layout.toast_message, (ViewGroup)parent.findViewById(R.id.toast_layout));
        toastMessage = (TextView)toastView.findViewById(R.id.tv_toast);

        if(gravity==null) {
            isGravity = false;
            toastMessage.setText("Положите устройство на наклонную плоскость и нажмите кнопку \"Начать измерение\" ");

                Timer t = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {

                        getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            toastMessage.setText("Устройство не должно поворачиваться на плоскости во время движения!");
                                                            if (toast == null) {
                                                                toast = new Toast(getActivity().getApplicationContext());
                                                            }
                                                            toast.setView(toastView);
                                                            toast.setDuration(Toast.LENGTH_LONG);
                                                            toast.show();
                                                        }
                                                    });
                    }
                };
                t.schedule(task, 3000);
            }

        if(accelerometer==null){
            toastMessage.setText("Извините, на вашем устройстве нет акселерометра, выполнение лабораторной работы невозможно");
            getActivity().setTitle("Нет акселерометра!");
            isAccelerometer=false;
        }

        else {
            toastMessage.setText(getResources().getString(R.string.instruction1));
        }

        /**
         * Инициализация View элементов для кастромного {@link Toast}
         * Его создание и вывод на экран
         * */
        toast = new Toast(getActivity().getApplicationContext());
        toast.setView(toastView);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();

        return parent;
    }

    @Override
    public void onPause() {
        super.onPause();

        if(accelerometer!=null){
            manager.unregisterListener(this);
        }

        if(timer!=null){
            timer.cancel();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        /** Регистрируем дачтики*/
        if(isAccelerometer){
            manager.registerListener(this, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
        }
        if(isGravity){
            manager.registerListener(this, manager.getDefaultSensor(Sensor.TYPE_GRAVITY), SensorManager.SENSOR_DELAY_FASTEST);
        }

        timer=new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_currentAngle.setText("Угол наклона: "+ format(currentAngle,1)+"°");

                    }
                });
            }
        };
        timer.schedule(task,0,300);
    }

    /**===============================================================================================Sensor implements**/
    /**
     * Вызывается когда приходят новые данные датчиков
     * @param event - содержит тип датчика и массив с его данными
     *
     * Данные с акселерометра фильтруются фильтром низких частот
     * https://www.built.io/blog/applying-low-pass-filter-to-android-sensor-s-readings
     * Линейное ускорение вычисляется как разность данных акселерометра и компонент
     * ускорения свободного падения
     * */

    @Override
    public void onSensorChanged(SensorEvent event) {

        /**Копоненты гравитации*/
        if(event.sensor.getType() == Sensor.TYPE_GRAVITY){
            for(int i = 0; i < 3 ; i++){
                gravityComponents[i]=event.values[i];
            }
        }

        /**копоненты полного ускорения и вычисление компонент линейного ускорения*/
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            for(int i = 0; i < 3; i++){
                accelerometerComponentsFiltered[i] =
                        alpha*accelerometerComponentsFiltered[i]+ event.values[i]*(1-alpha);

                if(isGravity || isGravityReady){
                    linAccelerationComponents[i] =
                            accelerometerComponentsFiltered[i]-gravityComponents[i];
                }
            }
        }
        calculateOther();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //TODO: Найти применение функции реагирующей на измение погрешности датчиков
    }
    /**======================================================================================================================**/

    public void calculateOther() {

        if(dataText==null){
            dataText= new StringBuilder();
        }
        dataText.append(format(acceleration,4)+"\n");
        clipboardManager= (ClipboardManager)getActivity().getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        clipData = ClipData.newPlainText("DATA", dataText);
        clipboardManager.setPrimaryClip(clipData);


        /**Модуль полного ускорения*/
        acceleration = Math.sqrt(accelerometerComponentsFiltered[0]*accelerometerComponentsFiltered[0]+
                accelerometerComponentsFiltered[1]*accelerometerComponentsFiltered[1]+
                accelerometerComponentsFiltered[2]*accelerometerComponentsFiltered[2]);

        /**Модуль линейного ускорения*/
        if(isGravity || isGravityReady) {

            fullGravity=Math.sqrt(gravityComponents[0]*gravityComponents[0]
            +gravityComponents[1]*gravityComponents[1]+
            gravityComponents[2]*gravityComponents[2]);

            linearAcceleration = Math.sqrt(linAccelerationComponents[0]*linAccelerationComponents[0]
                    +linAccelerationComponents[1]*linAccelerationComponents[1]+
                    linAccelerationComponents[2]*linAccelerationComponents[2]);
        }

        /**Текущий угол наклона*/
        currentAngle = Math.toDegrees(Math.acos(accelerometerComponentsFiltered[2]/acceleration));

        if(linearAcceleration!=0.0){
            currentFriction = Math.abs(Math.tan(Math.toRadians(currentAngle))-(linearAcceleration)/(g*Math.cos(Math.toRadians(currentAngle))));
        }

        if(research){
            if(arrayData==null){
                arrayData=new ArrayList<>();
            }
            if (arrayAngle == null) {
                arrayAngle = new ArrayList<>();
            }

            if (!Analytics.isEnded) {
                if (Analytics.state == 0 && !Analytics.isChanged) {
                    Analytics.isChanged = true;
                    toastMessage.setText(getResources().getString(R.string.instruction2));
                    getActivity().setTitle("Стабилизация...");
                    toast.show();
                    btn_startMeasurement.setText(getResources().getString(R.string.cancelMeasurement));

                    reducer = new Timer();
                    TimerTask task2 = new TimerTask() {
                        @Override
                        public void run() {
                         //  Analytics.start_resting_delta+=Analytics.DELTA_PLUS;
                         //   Analytics.resting_threshold+=Analytics.DELTA;
                           // Analytics.restartVariables(false);

                        }
                    };
                    reducer.schedule(task2, 3000, 1000);
                }

                else if (Analytics.state == 1 && !Analytics.isChanged) {

                    if (reducer != null) {
                        reducer.cancel();
                        reducer = null;
                    }

                    arrayAngle.add(currentAngle);
                    toastMessage.setText(getResources().getString(R.string.instruction3));
                    getActivity().setTitle("Отпустите устройство");
                    lastAngle = currentAngle;

                    if (!isGravity && !Analytics.isChanged) {
                        for (int i = 0; i < 3; i++) {
                            gravityComponents[i] = accelerometerComponentsFiltered[i];
                        }
                        isGravityReady = true;
                    }

                    Analytics.isChanged = true;
                    toast.show();
                }

                else if (Analytics.flow(acceleration)) {
                    arrayData.add(currentFriction);
                }
            }

            else{
                Analytics.isEnded=false;
                research = false;

                btn_startMeasurement.setText(getResources().getText(R.string.startMeasurement));

                for(int i = (arrayData.size()/9); i>0; i--){
                    arrayData.remove(arrayData.size()-1);
                    arrayData.remove(0);

                }
                StringBuilder builder = new StringBuilder()
                        .append(" "+ format(Analytics.getAverage(arrayData),3))
                        .append("±"+format(Analytics.getAccuracy(arrayData)+0.04,3));
                         // StringBuilder angle = new StringBuilder()
                       // .append(" "+format(Analytics.getAverage(arrayAngle),1))
                       // .append("±"+format(Analytics.getAccuracy(arrayAngle),1));

                addMeasurement(measurements.size()+1,format(lastAngle,1), builder.toString());
                getActivity().setTitle("Измерено!");
                Analytics.restartVariables(true);
                isGravityReady=false;

                    gravityComponents[0]=gravityComponents[1]=gravityComponents[2]=0;
                    linearAcceleration=0;
            }
        }
    }

    public void cancelMeasurement(){

        research=false;
        arrayData=null;
        arrayAngle = null;
        Analytics.restartVariables(true);
        getActivity().setTitle("Лабораторная работа 1");
        isGravityReady=false;

        btn_startMeasurement.setText(getResources().getText(R.string.startMeasurement));
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
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_startMeasurement:{
                if(research){
                    cancelMeasurement();
                    toastMessage.setText("Измерение отменено");
                    toast.show();
                }
                else {
                    cancelMeasurement();
                    research=true;
                }
            }

        }

    }

    public void addMeasurement(int measurement, String angle, String fricton){
        measurements.add(new Measurement(measurement,angle,fricton));
        adapter.notifyItemInserted(measurements.size()-1);
        recyclerView.scrollToPosition(measurements.size()-1);
    }
}

