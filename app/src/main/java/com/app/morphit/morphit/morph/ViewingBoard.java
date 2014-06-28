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

        mBitmap.eraseColor(Color.BLUE);

        // draw paths
        Log.d("arrays", "VIEWER! " + MorphActivity.firstImagePoints.size() + " " + MorphActivity.secondImagePoints.size());
        drawPathFromPoints(MorphActivity.firstImagePoints);
        drawPathFromPoints(MorphActivity.secondImagePoints);
        canvas.drawBitmap(mBitmap, 0,0, mBitmapPaint);
    }

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
                mCanvas.drawPath(mPath, mPaint);
                mPath.reset();
            }
        }
    }

    public void startDrawing() {
        postInvalidate();
    }


}
