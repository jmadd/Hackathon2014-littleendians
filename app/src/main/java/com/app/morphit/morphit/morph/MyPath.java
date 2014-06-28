package com.app.morphit.morphit.morph;

import java.util.ArrayList;

/**
 * Created by madjared on 6/28/2014.
 */
public class MyPath {
    ArrayList<MyPoint> points;
    int color;

    public MyPath(ArrayList<MyPoint> points, int color) {
        this.points = points;
        this.color = color;
    }

    public MyPath(MyPath p) {
        this.points = p.points;
        this.color = p.color;
    }

    public MyPath() {
        this(new ArrayList<MyPoint>(), 0x0);  // Calls other constructor.
    }
}


