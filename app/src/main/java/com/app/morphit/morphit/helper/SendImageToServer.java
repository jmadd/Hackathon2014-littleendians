package com.app.morphit.morphit.morph;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by gysr on 6/27/2014.
 */
public class SendImageToServer extends AsyncTask <Void, Void, String> {

    private String TAG = getClass().getName();
    private String fileName ;
    private String path;
    private MorphActivity object;

    public SendImageToServer(String fileName, MorphActivity obj){
        this.fileName = fileName;
        this.object = obj;
        //this.fileName = "/storage/emulated/0/Download/google-home.jpg";
       // this.fileName = "/storage/emulated/0/Download/a.gif";
    }

    @Override
    protected String doInBackground(Void... params) {
        //Looper.prepare(); //For Preparing Message Pool for the child Thread
        HttpClient client = new DefaultHttpClient();
        //HttpConnectionParams.setConnectionTimeout(client.getParams(), 1000000); //Timeout Limit
        HttpResponse response;
        try {
            String url = new String("http://54.191.20.185:5000/image"); // public IP
            HttpPost post = new HttpPost(url);
            Log.e("h###", fileName);
            FileEntity reqEntity = new FileEntity(new File(fileName), "image/gif");
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

    protected void onPostExecute(String path) {
        Log.i(TAG,"OnPostExecute !!");
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
        /*replace with path*/
        share.putExtra(Intent.EXTRA_TEXT, path);
        object.startActivity(Intent.createChooser(share, "Share link!"));
    }
}
