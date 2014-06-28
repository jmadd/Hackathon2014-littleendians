
package com.app.morphit.morphit.morph;
import android.util.Log;
import java.util.*;

public class SubdividePoints {

    public static  int NUM_POINTS_PER_SET; // how many points in the original lin
    public static  int NUM_POINT_SETS_INPUT; // how many initial drawings
    public static  int NUM_POINT_SETS_OUTPUT = 20; // frames

    // public static ArrayList<ArrayList<MyPoint>> runSubdividePoints(ArrayList<MyPoint> startPoints, ArrayList<MyPoint> endPoints) {
    public static ArrayList<ArrayList<MyPoint>> runSubdividePoints(ArrayList<ArrayList<MyPoint>> inputPointSet) {
     //   ArrayList<ArrayList<MyPoint> > inputPointSet = new ArrayList <ArrayList<MyPoint>>();

      //  inputPointSet.add(startPoints);
      //  inputPointSet.add(endPoints);



        ArrayList <ArrayList <MyPoint> > outputPointSet = subdivide(inputPointSet);

        //Log.d("Subdivide", "Output1 = (" + outputPointSet.get(1).get(0).x + "," + outputPointSet.get(1).get(0).y + ")");

        return outputPointSet;
    }

    private static ArrayList <ArrayList <MyPoint> > subdivide (ArrayList <ArrayList <MyPoint> > inputPointSet) {
        ArrayList <ArrayList <MyPoint> > outputPointSet = new ArrayList < ArrayList<MyPoint> >();

        for(int i = 0; i < NUM_POINT_SETS_OUTPUT * (NUM_POINT_SETS_INPUT-1); i++) {

        // initialize

            ArrayList <MyPoint> points = new ArrayList<MyPoint>();
            for (int j = 0; j < NUM_POINTS_PER_SET; j++) {
                points.add(new MyPoint());
            }
            outputPointSet.add(points);
        }

        //Add first input set
        outputPointSet.set(0, (inputPointSet.get(0)));

        for(int i = 0; i < NUM_POINT_SETS_INPUT-1; i++) {
            ArrayList<MyPoint> points1 = inputPointSet.get(i);
            ArrayList<MyPoint> points2 = inputPointSet.get(i+1);

            for(int j = 0; j < NUM_POINTS_PER_SET; j++) {
                double x1 = points1.get(j).x;
                double y1 = points1.get(j).y;
                double x2 = points2.get(j).x;
                double y2 = points2.get(j).y;

                //Log.d("subdivide", "p1= ("+ "(" + x1 + "," + y1 + ")");
                //Log.d("subdivide", "p2= ("+ "(" + x2 + "," + y2 + ")");
                double xDist = x2-x1;
                double xInc  = xDist / (NUM_POINT_SETS_OUTPUT-1);
                for(int k = 1; k < NUM_POINT_SETS_OUTPUT-1; k++) {
                    double x = x1+xInc*k;
                    double y  = y1 + (y2-y1)*(x-x1)/(x2-x1);
                    outputPointSet.get(k+i*NUM_POINT_SETS_OUTPUT).get(j).x = x;
                    outputPointSet.get(k+i*NUM_POINT_SETS_OUTPUT).get(j).y = y;
                    //Log.d("subdivide", "Point" + j + "(" + x + "," + y + ")");
                }
            }
        }

        //Add last input set
        outputPointSet.add(inputPointSet.get(NUM_POINT_SETS_INPUT-1));

        return outputPointSet;
    }


//    public static ArrayList<MyPoint> getStartPoints() {
//        ArrayList<MyPoint> startPoints = new ArrayList<MyPoint>();
//        startPoints.add(new MyPoint(0,0));
//        startPoints.add(new MyPoint(.5,1));
//        startPoints.add(new MyPoint(1,0));
//
//        return startPoints;
//    }
//
//    public static ArrayList<MyPoint> getEndPoints() {
//        ArrayList<MyPoint> endPoints = new ArrayList<MyPoint>();
//        endPoints.add(new MyPoint(1,0));
//        endPoints.add(new MyPoint(0,1));
//        endPoints.add(new MyPoint(.5,1));
//
//        return endPoints;
//    }


    /*
    private static ArrayList<MyPoint> calculateMidpoints (ArrayList<MyPoint> points1, ArrayList<MyPoint> points2) {

        ArrayList<MyPoint> midpoints = new ArrayList<MyPoint>();

        if(points1.size() != points2.size()) {
            System.out.print("Input point set sizes do not match");
            //Maybe throw exception instead
            return midpoints;
        }

        for(int i = 0; i < points1.size(); i++) {
            double midx = (points1.get(i).x + points2.get(i).x) / 2;
            double midy = (points1.get(i).y + points2.get(i).y) / 2;
            MyPoint midpoint = new MyPoint(midx, midy);
            midpoints.add(midpoint);
        }

        return  midpoints;
    }
*/
}