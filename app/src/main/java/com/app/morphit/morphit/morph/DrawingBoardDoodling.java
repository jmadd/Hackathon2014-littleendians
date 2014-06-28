package com.app.morphit.morphit.morph;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.TypedValue;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DrawingBoardDoodling extends View {


    private boolean erase=false;
    ArrayList<ArrayList<Bitmap>> pathBitmaps = new ArrayList<ArrayList<Bitmap>>();

    float scaleX;
    float scaleY;
    int width;
    int height;
    int frameBufferWidth;
    int frameBufferHeight;

    Path mPath;


    private Canvas drawCanvas;
    Paint pathPaint;
    //initial color
    private int paintColor = 0xFF660000;
    Bitmap framebuffer;
    Bitmap savedPathsBitmap;
    Canvas mCanvas;

    Canvas mCanvasSaved;

    Paint mBitmapPaint;

    int numDrawings = 0;

    private static final int DELAY = 60; //delay between frames in milliseconds
    private int play_frame = 0;
    private long last_tick = 0;
    private boolean mIsPlaying = false;
    private boolean mStartPlaying = true;

    public DrawingBoardDoodling(Context c, AttributeSet attrs) {
        super(c, attrs);

        WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);

        mPath = new Path();
        pathPaint = new Paint();
        pathPaint.setDither(true);
        pathPaint.setColor(0xFF000000);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeJoin(Paint.Join.ROUND);
        pathPaint.setStrokeCap(Paint.Cap.ROUND);
        pathPaint.setStrokeWidth(16);

        mBitmapPaint = new Paint();
        mBitmapPaint.setColor(0xFF000F00);

        framebuffer = Bitmap.createBitmap(480, 800, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(framebuffer);

        framebuffer.eraseColor(Color.WHITE);

        savedPathsBitmap = Bitmap.createBitmap(480, 800, Bitmap.Config.ARGB_8888);
        mCanvasSaved = new Canvas(savedPathsBitmap);
        savedPathsBitmap.eraseColor(Color.WHITE);


        height = metrics.heightPixels;
        width = metrics.widthPixels;

        // Scale the canvas for all devices based on the screen dimensions
        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        frameBufferWidth = isPortrait ? 480 : 800;
        frameBufferHeight = isPortrait ? 800 : 480;
        scaleX = (float) frameBufferWidth / width;
        scaleY = (float) frameBufferHeight / height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.scale((float) width / 480.0f, (float) height / 800.0f);

        if(pathBitmaps.size() != 0) { // there should be a path here already

            if (play_frame == pathBitmaps.get(0).size() - 1) { // 3 is how many jittered bitmaps are generated.
                play_frame = 0;
            } else {
                long time = System.currentTimeMillis() - last_tick;
                if (time >= 150) {
                    last_tick = System.currentTimeMillis();
                    play_frame++;
                }
            }

            savedPathsBitmap.eraseColor(Color.WHITE);
            for(ArrayList<Bitmap> currentFrameData : pathBitmaps) {
                mCanvasSaved.drawBitmap(currentFrameData.get(play_frame), 0, 0, mBitmapPaint);
            }

        }

        mCanvasSaved.drawPath(mPath, pathPaint);
        //  canvas.drawBitmap(framebuffer, 0, 0, mBitmapPaint);
        canvas.drawBitmap(savedPathsBitmap, 0, 0, mBitmapPaint);
        postInvalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX() * scaleX;
        float y = event.getY() * scaleY;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }


    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 2;

    private void touch_start(float x, float y) {

        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);

        // commit the path to the other canvas with the other framebuffer.
        mCanvasSaved.drawPath(mPath, pathPaint);
        // convert this mPath to an array of points.


        ArrayList<MyPoint> pathPoints = DoodleActivity.convertPathToPoints(mPath,10);

        ArrayList<ArrayList<MyPoint> > outputPoints = JitterPoints.runJitterPoints(pathPoints);

        // after, run it thru jitterpoints, to get 3 more arrays.


        // convert each array to a path, and draw that path into a bitmap.
        ArrayList<Path> jitteredPathVersions = new ArrayList<Path>();

        ArrayList<Bitmap> individualPathBitmaps = new ArrayList<Bitmap>();

        Canvas canvas = new Canvas();

        Log.d("doodle", "num of jittered paths " + outputPoints.size());
        for(ArrayList<MyPoint> pointPath : outputPoints) {
            Bitmap bmp = Bitmap.createBitmap(480, 800, Bitmap.Config.ARGB_8888);
            canvas.setBitmap(bmp);

            Path p = new Path(convertPointsToPaths(pointPath));
            jitteredPathVersions.add(p);
            canvas.drawPath(p, pathPaint);
            Log.d("doodle", "adding bitmap");
            individualPathBitmaps.add(bmp);
        }

        pathBitmaps.add(individualPathBitmaps);

        // now put all bitmaps in an array.


        // on the onDraw() up top, constantly iterate thru this array, switching


/*
        if(numDrawings == 0) {
            DoodleActivity.path1 = new Path(mPath);
        } else {
            DoodleActivity.path2 = new Path(mPath);
        }*/

      //  numDrawings++;
        if(numDrawings > 1) {
            numDrawings = 0;
            DoodleActivity.drawingDone();
        }
        mPath.reset();
        mPath = new Path();
    }


    public void setColor(String newColor){
//set color
        invalidate();
        paintColor = Color.parseColor(newColor);
        pathPaint.setColor(paintColor);
    }


    public Path convertPointsToPaths(ArrayList<MyPoint> points) {
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
             //   mCanvas.drawPath(mPath, mPaint);
             //   mPath.reset();
            }
        }
        return mPath;
    }

    public void setErase(boolean isErase){
        erase=isErase;
        if(erase) pathPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        else pathPaint.setXfermode(null);

    }

    public void startNew(Context ctx){
        //drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        pathBitmaps = new ArrayList<ArrayList<Bitmap>>();

        //DrawingBoardDoodling(ctx,


        mPath = new Path();
        pathPaint = new Paint();
        pathPaint.setDither(true);
        pathPaint.setColor(0xFF000000);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeJoin(Paint.Join.ROUND);
        pathPaint.setStrokeCap(Paint.Cap.ROUND);
        pathPaint.setStrokeWidth(16);

        mBitmapPaint = new Paint();
        mBitmapPaint.setColor(0xFF000F00);

        framebuffer = Bitmap.createBitmap(480, 800, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(framebuffer);

        framebuffer.eraseColor(Color.WHITE);

        savedPathsBitmap = Bitmap.createBitmap(480, 800, Bitmap.Config.ARGB_8888);
        mCanvasSaved = new Canvas(savedPathsBitmap);
        savedPathsBitmap.eraseColor(Color.WHITE);

        invalidate();
    }
}
