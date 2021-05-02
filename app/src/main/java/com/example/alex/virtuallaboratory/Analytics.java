package com.example.alex.virtuallaboratory;

import android.util.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.alex.virtuallaboratory.LabWorks.FragmentLabWorkFirst.g;

/**
 * Created by Alex on 05.08.2017.
 * Обрабатывает данные во время эксперимента
 * Содержит статические методы для вычисления различных величин
 * С помощью фильтра исследует поведение данных
 */

public abstract class Analytics {

    /**

     * ---------------------------------------------------------------------------------------------
     *                Переменные
     * @param isEnded - Эксперимент окончен?
     * @param state - Этап эксперимента
     * @param isChanged - Этап эксперимента изменился? Чтобы один раз графический интерфейс рисовать
     * @param index - Индекс показывающий, что выполняется условие следующего этапа
     * ---------------------------------------------------------------------------------------------
     *                Этапы эксперимента:
     * 0 - Устройство пытается стабилизироваться
     * 1 - Resting - Устройство покоится, или ускорение растет
     * 3 - State - Устройство съежает, ускорение колеблется возле точки
     * 4 - Hit Момент удара устройства об горизонталюную поверхность, ускорение скачет - не реализовано
     * * **/

    public static boolean isEnded = false;                                                          // Эксперимент закончен?
    public static int state = 0;                                                                    // Этап эксперимента
    public static boolean isChanged = false;                                                        // Изменился ли этап экспериента. Для графического интерфейса
    private static int index = 0;                                                                    // Индекс отвечающий за ложные условия этапа
   // private static int secondIndex=0;

   // private static int i=0;
   // private static double filterAverage=0;
   // private static double lastFilterAverage=0;
  //  private static double deltaFilter=-1;
   // public static double zero=0;


    /** Константы
     * @param SIZE_FILTER - размер фильтра
     * @param START_RESTING_DELTA - величина ниже которой начинается состояние покоя
     * @param ASCENDING_DELTA - Величина при значении которой устройство начало ускореное движение
     * @param HIT_DELTA - Величина достигаемая при ударе устройства об поверхнсть
     * */

    private static final int SIZE_FILTER = 12;
    private static final double START_RESTING_DELTA =0.09;
    private static final double ASCENDING_DELTA = 0.14;
    private static final double HIT_DELTA = 0.5;
    public static double start_resting_delta= 0.09;
    public static final double DELTA_PLUS=0.01;


   // private static final double RESTING_MAX_THRESHOLD = 0.5;                                         // Величина, выше которой начинается этап "съезжания" устройства
   // private static final double ASCENDING_THRESHOLD = 0.1;                                           // Величина, ниже которой этап с постоянным ускорением. Разница между данными
   // private static final double STATE_THRESHOLD = 1;                                                 // Разница между данными. Момент удара об поверхность
   // private static final double MAX_DELTA=0.01;
   // public static double resting_threshold = 0.08;
   // public static final double DELTA = 0.05;

    private static ArrayList<Double> filter;                                                          // Фильтр

    /** Анализирует данные и переключает последовательно этапы эксперимента
     *  @param value - текущее ускорение устройства
     *  @return - Принадлежит ли текущее ускорение этапу 3
     * */
    public static boolean flow(double value){

        if(filter==null){
            filter = new ArrayList<>();
        }

        checkSize();
        filter.add(value);
        if(filter.size()>1) {

            switch (state) {
                case 0: {

                    //   i++;
                    //   filterAverage+=value;                                                               //добавляем значение в контейнер
                    //   if(i >= SIZE_FILTER){                                                                 //если в контейнере значений размерности фильтра
                    //       filterAverage/=SIZE_FILTER;                                                     // усредняем
                    //       deltaFilter=filterAverage-lastFilterAverage;                                    // Находим разницу между прошлым контейнером и новым
                    //       if(Math.abs(deltaFilter)<MAX_DELTA){                                            // если разница меньше порога
                    //           secondIndex++;
                    //       }
                    //       else {
                    //           secondIndex--;
                    //      }
                    //       lastFilterAverage=filterAverage;                                                // новый контейнер загружаем в старый
                    //       filterAverage=0;                                                                // контейнер обнуляем
                    //       i=0;
                    //   }
//
                    if (Math.abs(value - filter.get(filter.size() - 2)) < start_resting_delta) {                //условие с которого начинается "состояние покоя"
                        index++;                                                                        // запись количества выполненного условия
                    }
                    if (index >= SIZE_FILTER-1) {                                                             // если фильтр полностью в элементах для которых условние выполнилось
                        nextStage();                                                                        // Обнулить счетчик выполненных условий
                        isChanged = false;// Флажок для первого входа в сотояние обнулить
                        Log.i("STATE", "1 state");
                    }

                    // if(secondIndex==SIZE_FILTER*20){
                    //     isChanged= false;
                    //     zero=Math.abs(lastFilterAverage);
                    // }

                    return false;                                                                       // значение не записывать в массив
                }
                case 1: {
                    if (Math.abs(value - filter.get(filter.size() - 2)) > ASCENDING_DELTA)
                    //        && value - filter.get(filter.size() - 2) < 0)
                    {
                        index++;                                                                        //запись количества выполненного условия
                    }
                    if (index >= (SIZE_FILTER / 3)) {                                                             // если фильтр полностью в элементах для которых условние выполнилось
                        nextStage();                                                                    // Обнулить счетчик выполненных условий
                        isChanged = false;                                                              // Обнулить флажок для первого входа в состояние
                        Log.i("STATE","2 state");
                    }
                    return false;                                                                       // Значениене записывать
                }
                case 2: {
                    if (Math.abs(value - filter.get(filter.size() - 2)) > HIT_DELTA) {              // Условие для состояния для равноускоренного движения
                        index++;
                    }

                    if (index >= SIZE_FILTER / 4) {
                        state = 0;
                        index = 0;
                        isEnded = true;
                        Log.i("STATE", "END");

                    }
                    return true;
                }

                default: {
                    return false;
                }
            }
        }
        return false;
    }



    /**
     * Проверяет размер фильтра и укорачивает в случае превышения размера*/
    private static void checkSize(){
        if(filter.size() >= SIZE_FILTER){
            if(state==0){
                if(Math.abs(filter.get(1)-filter.get(0)) < start_resting_delta){
                    index--;
                }
                filter.remove(0);
            }
            if(state==1){
                if(Math.abs(filter.get(1)-filter.get(0)) > ASCENDING_DELTA )
                       // && (filter.get(1)-filter.get(0)) < 0)
                {
                    index--;
                }
                filter.remove(0);
            }
            if(state==2){
                if(Math.abs(filter.get(0)-filter.get(1)) > HIT_DELTA){
                    index--;
                }
                filter.remove(0);
            }

        }
    }

    /**Вычисляет среднее арифметическое
     * @param array - массив данных
     * @return Среднее ариметическое данных массива
     * Возвращает -1 Если массив пуст*/
    public static double getAverage(ArrayList<Double> array){
        if(array.size()==0){
            return -1;
        }
        double sum =0;
        for(double val : array) sum+=val;
        return sum/array.size();
    }

    /**Вычисляет среднеквадратическую погрешность
     * @param array - массив данных
     * @return Среднеквадратическое отклонение
     * Возвращает -1 если массив пуст*/
    public static double getAccuracy(ArrayList<Double> array){
        if(array.size()==0){
            return -1;
        }

        double average =  getAverage(array);
        double sum =0;
        for (int i =0; i<array.size()-1;i++){
            sum +=((array.get(i)-average)*(array.get(i)-average));
        }
        sum = sum/(array.size()-1);
        return Math.sqrt(sum);
    }

    public static double getFormulaAccuracy(double angle, double fullGravity){
        return Math.abs(Math.tan(Math.toRadians(angle))-(Math.abs(fullGravity-g))/(g*Math.cos(Math.toRadians(angle))));
    }

    /**Обнуляет все перменные класса для повтороного исследования*/
    public static void restartVariables(boolean restart){


        isEnded =false;
        // isChanged =false;
        state = 0;
        index = 0;
        filter = null;

        //secondIndex=0;
       // i=0;
       // filterAverage=0;
       // lastFilterAverage=0;
       // zero=0;
        //deltaFilter=-1;


        if(restart){
 //           resting_threshold=RESTING_THRESHOLD;
            start_resting_delta=START_RESTING_DELTA;
            isChanged=false;
        }
    }

    private static void nextStage(){
        state++;                                                                    // перейдти к следующему состоянию
        index=0;
     //   secondIndex=0;
    }
}
