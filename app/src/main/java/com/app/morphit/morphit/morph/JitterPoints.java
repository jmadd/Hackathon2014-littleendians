package com.app.morphit.morphit.morph;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by madjared on 6/27/2014.
 */
public class JitterPoints {

    private static final int NUM_SCENES = 5;
    private static final double JITTER_RANGE = 4;


    public static void testJitterPoints() {
        ArrayList<MyPoint> inputPoints = new ArrayList<MyPoint>();
        Random rand = new Random();
        for(int i = 0; i < 10; i++) {
            MyPoint point = new MyPoint();
            point.x = rand.nextInt(10);
            point.y = rand.nextInt(10);
            inputPoints.add(point);
        }
        runJitterPoints(inputPoints);
    }

    public static ArrayList <ArrayList <MyPoint> > runJitterPoints(ArrayList<MyPoint> inputPoints) {
        ArrayList<ArrayList<MyPoint> > outputPoints = new ArrayList<ArrayList<MyPoint> >();

        int j =  0;

        for(int i = 0; i < NUM_SCENES; i++) {
            ArrayList<MyPoint> sceneOutputPoints = new ArrayList<MyPoint>();
            j=0;
            for (MyPoint point : inputPoints) {
                j++;
                Log.d("Jitter", i + ", " + j);
                Random rand  = new Random();
                MyPoint outputPoint = new MyPoint();
                outputPoint.x = point.x + (((rand.nextDouble()-.5)* JITTER_RANGE*2));
                outputPoint.y = point.y + (((rand.nextDouble()-.5)* JITTER_RANGE*2));
                sceneOutputPoints.add(outputPoint);
                Log.d("Jitter", "point= (" + point.x + "," + point.y + ")");
                Log.d("Jitter", "jitter= (" + outputPoint.x + "," + outputPoint.y + ")");
            }
            outputPoints.add(sceneOutputPoints);
        }

        // outputPoints = smooth(outputPoints);
        return outputPoints;
    }

    //output should be twice the size
    public static ArrayList< ArrayList<MyPoint> > smooth(ArrayList < ArrayList<MyPoint> > input) {
        ArrayList < ArrayList<MyPoint> > output = new ArrayList <ArrayList <MyPoint>>();
        
        //first element
        output.add(input.get(0));
        //average elements
        for (int i = 0; i<input.size(); i+= 2) {
            for(int j = 0; j < input.get(i).size()-1; j += 2) {
                MyPoint p0 = input.get(i).get(j);
                MyPoint p1 = input.get(i).get(j+1);

                MyPoint Q = new MyPoint(0.75f * p0.x + 0.25f * p1.x, 0.75f * p0.y + 0.25f * p1.y);
                MyPoint R = new MyPoint(0.25f * p0.x + 0.75f * p1.x, 0.25f * p0.y + 0.75f * p1.y);
                output.get(i).add(Q);
                output.get(i).add(R);
            }
        }

        //last element
        output.add(input.get(input.size()-1));

        return output;
    }
}
