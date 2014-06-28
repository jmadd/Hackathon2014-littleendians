package com.app.morphit.morphit.morph;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.app.morphit.morphit.R;
import com.app.morphit.morphit.gifutilities.AnimatedGifEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by bulbul on 6/27/2014.
 */
public class DoodleActivity extends Activity{

    private ImageButton currPaint, eraseButton, newButton;
    private int paintColor = 0xFF660000;
    Paint pathPaint;

    static DrawingBoardDoodling drawingBoard;
    static ViewFlipper flipper;
    Button viewButton;

    static ArrayList<MyPoint> firstImagePoints;
    static ArrayList<MyPoint> secondImagePoints;

    static Path path1;
    static Path path2;

    ProgressDialog loadingDialog;
    ContextWrapper cw;

    public String TAG = "network";

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        final Context ctx = this;

        firstImagePoints = new ArrayList<MyPoint>();
        secondImagePoints = new ArrayList<MyPoint>();
        cw = new ContextWrapper(this);
        path1 = new Path();
        path2 = new Path();

        setContentView(R.layout.activity_doodle);

        drawingBoard = (DrawingBoardDoodling) findViewById(R.id.drawBoard);
        LinearLayout paintLayout = (LinearLayout) findViewById(R.id.paint_colors);
        currPaint = (ImageButton) paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_passed));

        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage("Loading...");


        final AlertDialog.Builder confirmationDlog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Save / Share")
                .setMessage("Would you like to share this gif?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new SaveTask().execute();
                    }
                });


        ImageButton saveButton = (ImageButton) findViewById(R.id.save_btn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Confirmation dialog
                confirmationDlog.show();
            }
        });



        /*eraseButton = (ImageButton)findViewById(R.id.erase);
        eraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingBoard.setErase(true);
                //switch to erase - choose size

            }
        });*/

        newButton = (ImageButton)findViewById(R.id.new_btn);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
                newDialog.setTitle("New drawing");
                newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
                newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        drawView.startNew();
                        dialog.dismiss();
                    }
                });
                newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                newDialog.show();*/
                drawingBoard.startNew(ctx);

                //switch to erase - choose size

            }
        });
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
        drawingBoard = (DrawingBoardDoodling) findViewById(R.id.drawBoard);

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
            currPaint =(ImageButton)view;
        }
    }

    public static ArrayList<MyPoint> convertPathToPoints(Path mPath, double factor) {
        ArrayList<MyPoint> points = new ArrayList<MyPoint>();
        PathMeasure pm = new PathMeasure(mPath, false);
        float[] coords = new float[2];
        float[] tang = new float[2];
        //Log.d("arrays", "path length is " + pm.getLength());
        for(double i = 0; i < (double)pm.getLength(); i+= (factor)) {

            pm.getPosTan((int)i, coords, tang);
            MyPoint p = new MyPoint();
            p.set((int)coords[0], (int)coords[1]);
            points.add(p);
        }
        //Log.d("arraySize", "" + points.size());
        return points;
    }

    static void drawingDone() {

        ArrayList<MyPoint> points1 = convertPathToPoints(path1, 10);
        ArrayList<MyPoint> points2 = convertPathToPoints(path2, 10);


        if(points1.size() < 2 || points2.size() < 2) {
            return;
        }

        // divide small by bigger one to get a factor
        double factor = 0.0;
        if(points1.size() > points2.size()) {
            int factor1 = points1.size() /points2.size();
            int remain = points1.size() % points2.size();

            firstImagePoints = points1;
            secondImagePoints = convertPathToPoints3(secondImagePoints, factor1, remain, points2);
            //points2 = convertPathToPoints2(path2, factor, points1.size()); // points2 is less than the other one

        } else if(points1.size() < points2.size()){
            int factor1 = points2.size() /points1.size();
            int remain = points2.size() % points1.size();

            secondImagePoints = points2;
            firstImagePoints= convertPathToPoints3(firstImagePoints, factor1, remain, points1);
           /* if(factor > 0) {
points1 = convertPathToPoints2(path1, factor, points2.size());
}*/
        }
        else{
            firstImagePoints = points1;
            secondImagePoints = points2;
        }

        //firstImagePoints = points1;
        //secondImagePoints = points2;
        //Log.d("arraySize", "" + points1.size() + " " + points2.size());
        flipper.showNext();
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

    String filePath = "";

    private class SaveTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            loadingDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            ArrayList<Bitmap> bmpArray = drawingBoard.getBitmapArray();

            File saveDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Morphit/");
            if(!saveDir.exists())
                saveDir.mkdir();
            String gifName =  "/" +  System.currentTimeMillis() + ".gif";
            String gifPath = saveDir.getAbsolutePath() + gifName;
            generateGif(bmpArray, gifPath, 140);
            Log.d("filePath", gifPath);

            File file = new File(gifPath);
            filePath = gifPath;
            String url = getGifLink(file);

            return url;
        }

        @Override
        protected void onPostExecute(String param) {
            loadingDialog.dismiss();

            Intent intent1 =
                    	      new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            	intent1.setData(Uri.fromFile(new File(filePath)));
            	sendBroadcast(intent1);

            Log.d("network", "url " + param);
            // when everything is done,
            // open intent dialog with the actual link to share
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, param); // the link
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "I made a gif with the DoodleMorph App!");
            startActivity(Intent.createChooser(intent, "Share"));
        }
    }

    private void generateGif(ArrayList<Bitmap> bms, String gitPath, int duration){
        try {
            AnimatedGifEncoder e = new AnimatedGifEncoder();
            e.setRepeat(0);
            e.start(gitPath);
            for(Bitmap bm: bms){
                e.setDelay(duration);
                e.addFrame(bm);
            }
            e.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i(TAG, "Generate Bitmap");
    }


    public String getGifLink(File fileName) {
        String path = "";
        //Looper.prepare(); //For Preparing Message Pool for the child Thread
        HttpClient client = new DefaultHttpClient();
        //HttpConnectionParams.setConnectionTimeout(client.getParams(), 1000000); //Timeout Limit
        HttpResponse response;
        try {
            String url = new String("http://54.191.20.185:5000/image"); // public IP
            HttpPost post = new HttpPost(url);
            FileEntity reqEntity = new FileEntity(fileName, "image/gif");
            post.setEntity(reqEntity);
            response = client.execute(post);
            StringBuilder sb= new StringBuilder();
            /*Checking response */
            if(response!=null){
                InputStream in = response.getEntity().getContent(); //Get the data in the entity
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                String line = br.readLine();
                while(line != null){
                    Log.i(TAG,line);
                    sb.append(line);
                    line = br.readLine();
                }
                JSONObject data = new JSONObject(sb.toString());
                path = data.getString("fileName");
            }else
                Log.i(TAG,"Response is NULL ...but SENT !!!");
        } catch(Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "Background task over");
        return path;
    }

}
