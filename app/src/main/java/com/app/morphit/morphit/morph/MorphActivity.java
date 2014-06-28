package com.app.morphit.morphit.morph;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.graphics.Paint;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import android.widget.Button;
import android.widget.ViewFlipper;

import com.app.morphit.morphit.R;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class MorphActivity extends Activity {



    private ImageButton currPaint;
    private int paintColor = 0xFF660000;
    Paint pathPaint;

    static DrawingBoard drawingBoard;
    static ViewingBoard viewingBoard;
    static ViewFlipper flipper;
    Button viewButton;

    static ArrayList<Point> firstImagePoints;
    static ArrayList<Point> secondImagePoints;

    static ArrayList<ArrayList<MyPoint>> initialPointSets;
    static ArrayList<Path> initialPaths;

   // static ArrayList<Path> firstPath;
  //  static ArrayList<Path> secondPath;

    static Path path1;
    static Path path2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        firstImagePoints = new ArrayList<Point>();
        secondImagePoints = new ArrayList<Point>();
        initialPointSets = new ArrayList<ArrayList<MyPoint>>();
        initialPaths = new ArrayList<Path>();
        path1 = new Path();
        path2 = new Path();

        setContentView(R.layout.activity_morphit);

        drawingBoard = (DrawingBoard) findViewById(R.id.drawBoard);
        LinearLayout paintLayout = (LinearLayout) findViewById(R.id.paint_colors);
        currPaint = (ImageButton) paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_passed));


        View.OnClickListener handler = new View.OnClickListener() {
            public void onClick(View v) {
                switch(v.getId()) {

                    case R.id.i1:
                        paintClicked(v);
                    case R.id.i2:
                        paintClicked(v);
                    case R.id.i3:
                        paintClicked(v);
                    case R.id.i4:
                        paintClicked(v);
                    case R.id.i5:
                        paintClicked(v);
                    case R.id.i6:
                        paintClicked(v);
                    case R.id.i7:
                        paintClicked(v);
                    case R.id.i8:
                        paintClicked(v);
                    case R.id.i9:
                        paintClicked(v);
                    case R.id.i10:
                        paintClicked(v);
                    case R.id.i11:
                        paintClicked(v);
                    case R.id.i12:
                        paintClicked(v);
                        break;
                }
            }
        };
        flipper = (ViewFlipper) findViewById(R.id.flipper);
        drawingBoard = (DrawingBoard) findViewById(R.id.drawBoard);
        viewingBoard = (ViewingBoard) findViewById(R.id.viewBoard);

        flipper.setDisplayedChild(0);
    }

    public void paintClicked(View view){
        //use chosen color
        if(view!=currPaint){
            //update color
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            drawingBoard.setColor(color);
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_passed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint=(ImageButton)view;
        }
    }


    static void drawingDone() {


        // create an array of arrays of points from the array of paths.
        ArrayList<ArrayList<MyPoint>> tempInitialPointSets = new ArrayList<ArrayList<MyPoint>>();
        int max = -1;
        for(Path p : initialPaths) {
            ArrayList<MyPoint> pointSet = convertPathToPoints(p,10);
            if(max < pointSet.size())
                max = pointSet.size();
            if(pointSet.size() > 2)
                tempInitialPointSets.add(convertPathToPoints(p, 10));
        }
        // now that we have a max, we normalize all points to this.

        if(max <= 2)
            return;
        for(int i = 0; i < tempInitialPointSets.size(); i++) {
            initialPointSets.add(normalize(tempInitialPointSets.get(i), max));
            Log.d("length", "" + i + " " + initialPointSets.get(i).size());
        }

        flipper.showNext();

        viewingBoard.startDrawing();

        // all lengths should be equal.

        // figure out which of all the points is the largest.


/*
        ArrayList<MyPoint> points1 = convertPathToPoints(path1, 10);
        ArrayList<MyPoint> points2 = convertPathToPoints(path2, 10);


        if(points1.size() < 2 || points2.size() < 2) {
            return;
        }
        Log.d("arrays", "" + points1.size() + " " + points2.size());
        // divide small by bigger one to get a factor
        double factor = 0.0;
        if(points1.size() > points2.size()) {
            int factor1 = points1.size() /points2.size();
            int remain = points1.size() % points2.size();
            Log.d("arrays", "factor2 " + factor1);
            firstImagePoints = points1;
            secondImagePoints = convertPathToPoints3(secondImagePoints, factor1, remain, points2);
            //points2 = convertPathToPoints2(path2, factor, points1.size()); // points2 is less than the other one

        } else if(points1.size() < points2.size()){
            int factor1 = points2.size() /points1.size();
            int remain = points2.size() % points1.size();
            Log.d("arrays", "factor1 " + factor1);
            Log.d("arrays", "remain " + remain);
            secondImagePoints = points2;
            firstImagePoints = convertPathToPoints3(firstImagePoints, factor1, remain, points1);
   */        /* if(factor > 0) {
                points1 = convertPathToPoints2(path1, factor, points2.size());
            }*/
 /*       }
        else {
            firstImagePoints = points1;
            secondImagePoints = points2;
        }
        Log.d("arraySize", "" + firstImagePoints.size() + " " + secondImagePoints.size());

        //firstImagePoints = points1;
        //secondImagePoints = points2;
        //Log.d("arraySize", "" + points1.size() + " " + points2.size());
        flipper.showNext();

        viewingBoard.startDrawing();
        */
    }

    public static ArrayList<MyPoint> normalize(ArrayList<MyPoint> points, int goal) {
        int factor1 = goal /points.size();
        int remain = goal % points.size();
        return convertPathToPoints3(new ArrayList<MyPoint>(), factor1, remain, points);
    }

    public static ArrayList<MyPoint> convertPathToPoints3(ArrayList<MyPoint> newImagePoints, int factor, int remain, ArrayList<MyPoint> orignal){
        Log.d("arrays", "factor1 " + factor);
        Log.d("arrays", "remain " + remain);
        int count = 0;
        for(MyPoint point: orignal){
            if(count<remain){
                for(int i=0; i<=factor; i++){
                    newImagePoints.add(new MyPoint(point));
                }
            }
            else{
                for(int i=0; i<factor; i++){
                    newImagePoints.add(new MyPoint(point));
                }
            }
            Log.d("arrays", "number " + newImagePoints.size());
            count++;
        }
        return newImagePoints;

    }

    public static ArrayList<MyPoint> convertPathToPoints(Path mPath, double factor) {
        ArrayList<MyPoint> points = new ArrayList<MyPoint>();
        PathMeasure pm = new PathMeasure(mPath, false);
        float[] coords = new float[2];
        float[] tang = new float[2];
        //Log.d("arrays", "path length is " + pm.getLength());
        for(double i = 0; i < (double)pm.getLength(); i+= (factor)) {
            // getPosTan(float distance, float[] pos, float[] tan)
            //Log.d("arrays", "i is " + i);
            pm.getPosTan((int)i, coords, tang);
            MyPoint p = new MyPoint(coords[0], coords[1]);
           // p.set((int)coords[0], (int)coords[1]);
            points.add(p);
        }
        //Log.d("arraySize", "" + points.size());
        return points;
    }


    public static ArrayList<Point> convertPathToPoints2(Path mPath, double factor, int goalLength) {
        ArrayList<Point> points = new ArrayList<Point>();
        PathMeasure pm = new PathMeasure(mPath, false);
        float[] coords = new float[2];
        float[] tang = new float[2];
        Log.d("arrays", "path length is " + pm.getLength());
        for(double i = 0; i < (double)(pm.getLength()); i++) {
            // getPosTan(float distance, float[] pos, float[] tan)
            Log.d("arrays", "i is " + i);
            pm.getPosTan((int)(i), coords, tang);
            Point p = new Point();
            p.set((int)coords[0], (int)coords[1]);
            points.add(p);
        }
        //Log.d("arraySize", "" + points.size());
        return points;
    }




    /*public void setColor(String newColor){
//set color
        invalidate();
        paintColor = Color.parseColor(newColor);
        pathPaint.setColor(paintColor);
    }*/
}
