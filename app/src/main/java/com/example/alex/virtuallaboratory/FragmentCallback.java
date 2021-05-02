package com.example.alex.virtuallaboratory;

/**
 * Created by Alex on 11.12.2016.
 * Интерфейс для связи между активностью {@link MainActivity}
 * И фрагментами {@link android.support.v4.app.Fragment}
 * Активность должна реализовывать данный интерфейс
 */

public interface FragmentCallback {

      /**Метки нажатых кнопок
       * Передается из фрагмента в активность
       * активность принимает дальнейшее решение
       * TODO: Сделать параметры типа int
       */

      String BTN_ACCELEROMETER = "BTN_ACCELEROMETER";
      String BTN_GYROSCOPE = "BTN_GYROSCOPE";
      String BTN_PERFORM = "BTN_PERFORM";
      String BTN_SENSORS = "BTN_SENSORS";
      String BTN_ABOUT = "BTN_ABOUT";
      String BTN_SHARE = "BTN_SHARE";
      String BTN_LAB1= "LAB1";
      String BTN_MAGNETIC_FIELD= "MAGNETIC_FIELD";
      String BTN_LAB_PERFORM="LAB_PERFORM";
      String BTN_THEORY="BTN_THEORY";
      String BTN_CONTROL_QUESTIONS="CONTROL_QUESTIONS";
      String BTN_DESCRIPTION="DESCRIPTION";

      /**Метки текущего фрагмента
       * Для заголовка Toolbar
       * TODO: Заменять заголовки в самих фрагментах
       * */

    /**
       * Передает параметр нажатой кнопки
       * @param tag - Метка нажатой кнопки
       * */
      void getPressedButton(String tag);


      /**
       * Передает метку текущего фрагмента
       * @param tag - Метка текущего фрагмента
       * */
      void getCurrentFragment(int tag);
}
