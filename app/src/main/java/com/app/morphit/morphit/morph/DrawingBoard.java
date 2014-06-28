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
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;


import java.util.ArrayList;

public class DrawingBoard extends View {

    float scaleX;
    float scaleY;
    int width;
    int height;
    int frameBufferWidth;
    int frameBufferHeight;

    Path mPath;
    ArrayList<Integer> firstImagePoints;
    ArrayList<Integer> secondImagePoints;

    Paint pathPaint;
    //initial color
    private int paintColor = 0xFF660000;
    Bitmap framebuffer;
    Canvas mCanvas;

    Paint mBitmapPaint;

    public DrawingBoard(Context c, AttributeSet attrs) {
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
        pathPaint.setColor(0xFF000000);

        framebuffer = Bitmap.createBitmap(480, 800, Bitmap.Config.RGB_565);
        mCanvas = new Canvas(framebuffer);

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

        framebuffer.eraseColor(Color.WHITE);
        mCanvas.drawPath(mPath, pathPaint);

        canvas.drawBitmap(framebuffer, 0, 0, mBitmapPaint);
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

        mPath.reset();
        mPath = new Path();
    }

    public void setColor(String newColor){
//set color
        invalidate();
        paintColor = Color.parseColor(newColor);
        pathPaint.setColor(paintColor);
    }

}
