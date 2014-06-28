
package com.app.morphit.morphit.morph;
import android.util.Log;
import java.util.*;

public class SubdividePoints {

    public static  int NUM_POINTS_PER_SET; // how many points in the original lin
    public static  int NUM_POINT_SETS_INPUT; // how many initial drawings
    public static  int NUM_POINT_SETS_OUTPUT = 20; // frames

    // public static ArrayList<MyPath> runSubdividePoints(MyPath startPoints, MyPath endPoints) {
    public static ArrayList<MyPath> runSubdividePoints(ArrayList<MyPath> inputPointSet) {
     //   ArrayList<ArrayList<MyPath> > inputPointSet = new ArrayList <ArrayList<MyPath>>();

      //  inputPointSet.add(startPoints);
      //  inputPointSet.add(endPoints);



        ArrayList<MyPath> outputPointSet = subdivide(inputPointSet);

        //Log.d("Subdivide", "Output1 = (" + outputPointSet.get(1).get(0).x + "," + outputPointSet.get(1).get(0).y + ")");

        return outputPointSet;
    }

    private static ArrayList<MyPath> subdivide (ArrayList<MyPath> inputPointSet) {
        ArrayList<MyPath> outputPointSet = new ArrayList<MyPath>();

        // initialize
        for(int i = 0; i < NUM_POINT_SETS_OUTPUT * (NUM_POINT_SETS_INPUT-1); i++) {
            MyPath path = new MyPath();
            outputPointSet.add(path);
        }

        //Add first input set
        outputPointSet.set(0, (inputPointSet.get(0)));

        for(int i = 0; i < NUM_POINT_SETS_INPUT-1; i++) {
            MyPath path1 = inputPointSet.get(i);
            MyPath path2 = inputPointSet.get(i+1);

            for(int j = 0; j < NUM_POINTS_PER_SET; j++) {
                double x1 = path1.points.get(j).x;
                double y1 = path1.points.get(j).y;
                double x2 = path2.points.get(j).x;
                double y2 = path2.points.get(j).y;

                //Log.d("subdivide", "p1= ("+ "(" + x1 + "," + y1 + ")");
                //Log.d("subdivide", "p2= ("+ "(" + x2 + "," + y2 + ")");
                double xDist = x2-x1;
                double xInc  = xDist / (NUM_POINT_SETS_OUTPUT-1);
                for(int k = 1; k < NUM_POINT_SETS_OUTPUT-1; k++) {

                    int color = midColor(path1.color, path2.color);
                    double x = x1+xInc*k;
                    double y  = y1 + (y2-y1)*(x-x1)/(x2-x1);
                    outputPointSet.get(k+i*NUM_POINT_SETS_OUTPUT).points.add(new MyPoint(x,y));
                    outputPointSet.get(k+i*NUM_POINT_SETS_OUTPUT).color = color;
                   // Log.d("Subdivide", "color= " + outputPointSet.get(k+i*NUM_POINT_SETS_OUTPUT).color);
                            //Log.d("subdivide", "Point" + j + "(" + x + "," + y + ")");
                }
            }
        }
        //Add last input set
        outputPointSet.add(inputPointSet.get(NUM_POINT_SETS_INPUT-1));

        return outputPointSet;
    }

    public static int midColor (int color1, int color2) {
        int color = (color1 + color2) / 2;
        return color;
    }
}