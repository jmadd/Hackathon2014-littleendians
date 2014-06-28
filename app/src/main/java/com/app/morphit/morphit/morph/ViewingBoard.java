package com.app.morphit.morphit.morph;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

/**
 * Created by vilaaron on 6/27/2014.
 */
public class ViewingBoard extends View {

    private Path mPath;
    private Paint mBitmapPaint;
    private Paint mPaint;

    float scaleX;
    float scaleY;
    int width;
    int height;
    int frameBufferWidth;
    int frameBufferHeight;

    public Bitmap mBitmap;
    public Canvas mCanvas;

    private static final int DELAY = 40; //delay between frames in milliseconds
    private int play_frame = 0;
    private long last_tick = 0;
    private boolean mIsPlaying = false;
    private boolean mStartPlaying = true;

    int numTotalFrames = 30;



    ArrayList<MyPath> pointSets = new ArrayList<MyPath>();

    public ViewingBoard(Context c, AttributeSet attrs) {
        super(c, attrs);
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFF000000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);

        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

        WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);

        height = metrics.heightPixels;
        width = metrics.widthPixels;
        // Scale the window size
        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        frameBufferWidth = isPortrait ? 480 : 800;
        frameBufferHeight = isPortrait ? 800 : 480;
        scaleX = (float) frameBufferWidth
                / width;
        scaleY = (float) frameBufferHeight
                / height;

        mBitmap = Bitmap.createBitmap(frameBufferWidth, frameBufferHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        Log.d("testing", "viewingboard constructor");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.scale((float) width / 480.0f, (float) height / 800.0f);


        mBitmap.eraseColor(Color.WHITE);
        // draw paths
       // Log.d("arrays", "VIEWER! " + MorphActivity.firstImagePoints.size() + " " + MorphActivity.secondImagePoints.size());
        mIsPlaying = true;
        if(play_frame == pointSets.size()) // how many sets
        {
            play_frame = 0;
        }
        if (mStartPlaying)
        {
            //Log.d(TAG, "starting animation...");
            play_frame = 0;
            mStartPlaying = false;
            mIsPlaying = true;
            postInvalidate();
        }
        else if (mIsPlaying)
        {

            if (play_frame >= pointSets.size())
            {
                mIsPlaying = false;
            }
            else
            {

                long time = (System.currentTimeMillis() - last_tick);

                if (time >= DELAY) //the delay time has passed. set next frame
                {

                    last_tick = System.currentTimeMillis();
                    drawPathFromPoints2(pointSets.get(play_frame).points);
                    play_frame++;
                    postInvalidate();

                }
                else //still within delay. redraw current frame
                {

                    drawPathFromPoints2(pointSets.get(play_frame).points);
                    postInvalidate();

                }
            }
        }

        canvas.drawBitmap(mBitmap, 0,0, mBitmapPaint);




        postInvalidate();
    }


    /**
     * We know how many frames to generate. Let's say we start out with 10. 10 frames between the two sets of points.
     *
     *
     * @param points
     */


    public void drawPathFromPoints(ArrayList<Point> points) {
        Path mPath = new Path();
        int mX;
        int mY;
        for(int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            if(i == 0) {
                mPath.reset();

                mPath.moveTo(p.x, p.y);
                mX = p.x;
                mY = p.y;
            }

            if(i > 0 && i < points.size() - 1) {
                Point prevPoint = points.get(i - 1);
                mPath.quadTo(prevPoint.x, prevPoint.y, (p.x + prevPoint.x) / 2, (p.y + prevPoint.y) / 2);
            }

            if(i == points.size() - 1) {

               // mPath.reset();
            }
        }
        mCanvas.drawPath(mPath, mPaint);
    }

    public void drawPathFromPoints2(ArrayList<MyPoint> points) {
        Path mPath = new Path();
        int mX;
        int mY;

        for(int i = 0; i < points.size(); i++) {
            MyPoint p = points.get(i);
            if(i == 0) {
                mPath.reset();

                mPath.moveTo((float)p.x, (float)p.y);
                mX = (int)p.x;
                mY = (int)p.y;
            }

            if(i > 0 && i < points.size() - 1) {
                MyPoint prevPoint = points.get(i - 1);
                mPath.quadTo((float)prevPoint.x, (float)prevPoint.y, (float) (p.x + prevPoint.x) / 2,(float) (p.y + prevPoint.y) / 2);
            }

            if(i == points.size() - 1) {
               // mCanvas.drawPath(mPath, mPaint);
               // mPath.reset();
            }
        }
        mCanvas.drawPath(mPath, mPaint);
    }


    public void startDrawing() {
        SubdividePoints.NUM_POINTS_PER_SET = MorphActivity.initialPointSets.get(0).points.size();

        //pointSets = SubdividePoints.runSubdividePoints(convert(MorphActivity.firstImagePoints), convert(MorphActivity.secondImagePoints));
        pointSets = SubdividePoints.runSubdividePoints(MorphActivity.initialPointSets);
      //  currentPoints = MorphActivity.initialPointSets.get(0).size();
        mStartPlaying = true;
        postInvalidate();
    }

    public ArrayList<MyPoint> convert(ArrayList<MyPoint> points) {
        ArrayList<MyPoint> result = new ArrayList<MyPoint>();
        for(MyPoint p : points) {
            result.add(new MyPoint((double)p.x, (double)p.y));
        }
        return result;
    }

    public void startNew(){
        //drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        //pathBitmaps = new ArrayList<ArrayList<Bitmap>>();

        //DrawingBoardDoodling(ctx,


        mPath = new Path();


        mBitmapPaint = new Paint();
        mBitmapPaint.setColor(0xFF000F0);

         mCanvas = new Canvas();

         invalidate();
    }

}
