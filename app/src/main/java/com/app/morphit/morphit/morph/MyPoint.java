package com.app.morphit.morphit.morph;


public class MyPoint {
    double x;
    double y;

    public MyPoint(double x, double y) {
        x = x;
        y = y;
    }

    public MyPoint() {
        this(0, 0);  // Calls other constructor.
    }
}