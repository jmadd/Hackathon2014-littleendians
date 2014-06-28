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

import java.util.ArrayList;

public class DrawingBoard extends View {

    float scaleX;
    float scaleY;
    int width;
    int height;
    int frameBufferWidth;
    int frameBufferHeight;

    Path mPath;


    Paint pathPaint;

    Bitmap framebuffer;
    Bitmap savedPathsBitmap;
    Canvas mCanvas;

    Canvas mCanvasSaved;

    Paint mBitmapPaint;

    int numDrawings = 0;


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
        mBitmapPaint.setColor(0xFF000000);

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


      //  framebuffer.eraseColor(Color.WHITE);

        mCanvasSaved.drawPath(mPath, pathPaint);


      //  canvas.drawBitmap(framebuffer, 0, 0, mBitmapPaint);
        canvas.drawBitmap(savedPathsBitmap, 0, 0, mBitmapPaint);
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

        if(numDrawings == 0) {
            MorphActivity.path1 = new Path(mPath);
        } else {
            MorphActivity.path2 = new Path(mPath);
        }

        numDrawings++;
        if(numDrawings > 1) {
            numDrawings = 0;
            MorphActivity.drawingDone();
        }
        mPath.reset();
        mPath = new Path();
    }




}
