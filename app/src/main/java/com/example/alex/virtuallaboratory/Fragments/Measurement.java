package com.example.alex.virtuallaboratory.Fragments;

/**
 * Created by Alex on 28.01.2018.
 */
public class Measurement {

  public  String friction;
  public  String angle;
  public  int measurement;



    public Measurement(int measurement, String angle, String friction){

        this.angle=angle;
        this.friction = friction;
        this.measurement = measurement;
    }
}

