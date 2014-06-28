package com.app.morphit.morphit.morph;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by madjared on 6/27/2014.
 */
public class JitterPoints {

    private static final int NUM_SCENES = 3;
    private static final double JITTER_RANGE = .1;

    public static ArrayList <ArrayList <MyPoint> > runJitterPoints(ArrayList<MyPoint> inputPoints) {
        ArrayList<ArrayList<MyPoint> > outputPoints = new ArrayList<ArrayList<MyPoint> >();

        for(int i = 0; i < NUM_SCENES; i++) {
            ArrayList<MyPoint> sceneOutputPoints = new ArrayList<MyPoint>();
            for (MyPoint point : inputPoints) {
                Random rand  = new Random();
                MyPoint outputPoint = new MyPoint();
                outputPoint.x = point.x * ((rand.nextDouble()* JITTER_RANGE) + (1 - JITTER_RANGE/2));
                outputPoint.y = point.y * ((rand.nextDouble()* JITTER_RANGE) + (1 - JITTER_RANGE/2));
                sceneOutputPoints.add(outputPoint);
            }
            outputPoints.add(sceneOutputPoints);
        }
        return outputPoints;
    }

    //output should be twice the size
    public static ArrayList<MyPoint> smooth(ArrayList<MyPoint> input) {
        ArrayList<MyPoint> output = new ArrayList<MyPoint>();
        
        //first element
        output.add(input.get(0));
        //average elements
        for (int i=0; i<input.size()-1; i++) {
            MyPoint p0 = input.get(i);
            MyPoint p1 = input.get(i+1);

            MyPoint Q = new MyPoint(0.75f * p0.x + 0.25f * p1.x, 0.75f * p0.y + 0.25f * p1.y);
            MyPoint R = new MyPoint(0.25f * p0.x + 0.75f * p1.x, 0.25f * p0.y + 0.75f * p1.y);
            output.add(Q);
            output.add(R);
        }

        //last element
        output.add(input.get(input.size()-1));

        return output;
    }
}
