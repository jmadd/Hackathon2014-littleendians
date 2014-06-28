package com.app.morphit.morphit.morph;


public class MyPoint {
    double x;
    double y;

    public MyPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public MyPoint() {
        this(0, 0);  // Calls other constructor.
    }
}