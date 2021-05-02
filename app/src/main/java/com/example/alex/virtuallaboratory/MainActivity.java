package com.example.alex.virtuallaboratory;

import android.app.AlertDialog;                                                                     // API level 1
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;                                                  // API level 4
import android.content.Context;                                                                     // API level 1
import android.content.DialogInterface;                                                             // API level 1
import android.hardware.Sensor;                                                                     // API level 3
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.app.AppCompatActivity;                                                    // API level 7
import android.os.Bundle;                                                                           // API level 1
import android.view.Menu;                                                                           // API level 1
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import android.widget.Toast;                                                                        // API level 1

import com.example.alex.virtuallaboratory.Fragments.FragmentAbout;
import com.example.alex.virtuallaboratory.Fragments.FragmentControlQuestions;
import com.example.alex.virtuallaboratory.Fragments.FragmentDescription;
import com.example.alex.virtuallaboratory.Fragments.FragmentLabList;
import com.example.alex.virtuallaboratory.Fragments.FragmentLabOptions;
import com.example.alex.virtuallaboratory.Fragments.FragmentMain;
import com.example.alex.virtuallaboratory.Fragments.FragmentSensor;
import com.example.alex.virtuallaboratory.Fragments.FragmentTESTING;
import com.example.alex.virtuallaboratory.Fragments.FragmentTheory;
import com.example.alex.virtuallaboratory.LabWorks.FragmentLabWorkFirst;

/**
 * Основная и единтсвенная активность {@link AppCompatActivity} приложения
 * Должна реализовывать методы интерфейса {@link FragmentCallback}
 * Содержимое layout - контейнер для фрагментов
 * Экраны приложения - фрагменты, помещенные в контейнер
 * */

public class MainActivity extends AppCompatActivity implements FragmentCallback
{

    /**Фрагменты экрана*/
    FragmentMain fragMain;
    FragmentLabList fragLabList;
    FragmentSensor fragSensors;
    FragmentAbout fragAbout;
    XYZSensorFragment fragAccelerometer;
    XYZSensorFragment fragGyroscope;
    XYZSensorFragment fragMagneticField;
    FragmentLabWorkFirst fragLabWorkFirst;
    FragmentLabOptions fraqLabOptions;
    FragmentTheory fragTheory;
    FragmentControlQuestions fragControlQuestions;
    FragmentDescription fragDescription;
    FragmentTESTING fragmentTESTING;

    FragmentTransaction transactor;

    Context context;
/**==================================================================================================Activity-Lifecycle**/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;

        //TODO: Создавать фрагменты по мере их необходимости

        fragMain = new FragmentMain();
        fragLabList = new FragmentLabList();
        fragSensors = new FragmentSensor();
        fragAbout = new FragmentAbout();
        fragLabWorkFirst = new FragmentLabWorkFirst();
        fragTheory = new FragmentTheory();
        fragControlQuestions = new FragmentControlQuestions();
        fragDescription = new FragmentDescription();
        fragmentTESTING = new FragmentTESTING();

        fragAccelerometer = new XYZSensorFragment().newInstance(Sensor.TYPE_ACCELEROMETER);
        fragGyroscope = new XYZSensorFragment().newInstance(Sensor.TYPE_GYROSCOPE);
        fragMagneticField = new XYZSensorFragment().newInstance(Sensor.TYPE_MAGNETIC_FIELD);
        fraqLabOptions = new FragmentLabOptions().newInstance("LAB1");

        transactor = getSupportFragmentManager().beginTransaction();
        transactor.add(R.id.container, fragMain);
        transactor.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                transactor = getSupportFragmentManager().beginTransaction();
                transactor.replace(R.id.container, fragmentTESTING);
                transactor.addToBackStack(null);
                transactor.commit();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

/**=====================================================================================================================**/
    /**
     * Переопределение метода интерфейса {@link FragmentCallback }
     *  {@param tag} - Метка, передаваемая нажатием кнопки
     *  TODO: Сделать метки типа int
     * **/
    @Override
    public void getPressedButton(String tag) {
        switch (tag){
            case FragmentCallback.BTN_PERFORM:{
                transactor = getSupportFragmentManager().beginTransaction();
                transactor.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transactor.replace(R.id.container,fragLabList);
                transactor.addToBackStack(null);
                transactor.commit();
                break;
            }
            case FragmentCallback.BTN_SENSORS:{
                transactor = getSupportFragmentManager().beginTransaction();
                transactor.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transactor.replace(R.id.container,fragSensors);
                transactor.addToBackStack(null);
                transactor.commit();
                break;
            }
            case FragmentCallback.BTN_ABOUT:{
                transactor = getSupportFragmentManager().beginTransaction();
                transactor.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                 transactor.replace(R.id.container,fragAbout);
                 transactor.addToBackStack(null);
                transactor.commit();
                break;
            }
            case FragmentCallback.BTN_SHARE:{
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT,"\n\n");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "Присоединяйся к виртуальной лаборатории, с ней урок физики станет намного интересней и увлекательней http://donsoftltd.ru/mobillab.html");
                startActivity(Intent.createChooser(sharingIntent,getResources().getString(R.string.share)));
                break;
            }
            case FragmentCallback.BTN_ACCELEROMETER:{
                transactor = getSupportFragmentManager().beginTransaction();
                transactor.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transactor.replace(R.id.container, fragAccelerometer);
                transactor.addToBackStack(null);
                transactor.commit();
                break;
            }
            case FragmentCallback.BTN_GYROSCOPE:{
                transactor = getSupportFragmentManager().beginTransaction();
                transactor.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transactor.replace(R.id.container, fragGyroscope);
                transactor.addToBackStack(null);
                transactor.commit();
                break;
            }
            case FragmentCallback.BTN_MAGNETIC_FIELD:{
                transactor = getSupportFragmentManager().beginTransaction();
                transactor.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transactor.replace(R.id.container,fragMagneticField);
                transactor.addToBackStack(null);
                transactor.commit();
                break;
            }

            case FragmentCallback.BTN_LAB1:{
                dialog();
                break;
            }
            case FragmentCallback.BTN_LAB_PERFORM:{
                transactor = getSupportFragmentManager().beginTransaction();
                transactor.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transactor.replace(R.id.container,fragLabWorkFirst);
                transactor.addToBackStack(null);
                transactor.commit();
                break;
            }
            case FragmentCallback.BTN_THEORY:{
                transactor = getSupportFragmentManager().beginTransaction();
                transactor.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transactor.replace(R.id.container,fragTheory);
                transactor.addToBackStack(null);
                transactor.commit();
                break;
            }
            case FragmentCallback.BTN_CONTROL_QUESTIONS:{
                transactor = getSupportFragmentManager().beginTransaction();
                transactor.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transactor.replace(R.id.container,fragControlQuestions);
                transactor.addToBackStack(null);
                transactor.commit();
                break;
            }

            case FragmentCallback.BTN_DESCRIPTION:{
                transactor = getSupportFragmentManager().beginTransaction();
                transactor.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transactor.replace(R.id.container, fragDescription);
                transactor.addToBackStack(null);
                transactor.commit();
            }

            default:{break;}
        }
    }

    /** Переопределение метода @Link {@link FragmentCallback}
     * @param tag - метка, указывающая на активный фрагмент
     * Для установления заговолок в ToolBar @Link {@link android.support.v7.widget.Toolbar}
     * TODO: Устанавливать заголовок Toolbar в OnResume самих фрагментов */

    @Override
    public void getCurrentFragment(int tag){
    }

   /** Создает и выводит диалоговое окно {@link AlertDialog}
    *  с предупреждением*/

    public void dialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getResources().getString(R.string.attention));
        builder.setMessage(getResources().getString(R.string.information));
        builder.setPositiveButton(getResources().getString(R.string.continuebtn), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                transactor = getSupportFragmentManager().beginTransaction();
                transactor.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transactor.replace(R.id.container,fraqLabOptions);
                transactor.addToBackStack(null);
                transactor.commit();
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setCancelable(false);
        builder.show();

    }
}