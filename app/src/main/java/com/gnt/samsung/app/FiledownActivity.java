package com.gnt.samsung.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;


public class FiledownActivity extends Activity {


    String dwnload_file_path = "https://upload.wikimedia.org/wikipedia/commons/a/a9/Alfa_147_GTA_Brooklands_May_2010_IMG_9018.jpg";
    String dest_file_path = "/sdcard/car.jpg";
    ProgressDialog dialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filedown);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dialog = new ProgressDialog(FiledownActivity.this);
        dialog.setMessage("Downloading");
        dialog.setIndeterminate(true);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(true);
    }

    public void down(View v){
        new DownloadTask(FiledownActivity.this).execute(dwnload_file_path);
    }

    public class DownloadTask extends AsyncTask<String, Integer, String>{

        public Context ctx;
        public PowerManager.WakeLock wakeLock;

        public DownloadTask(Context context){
            this.ctx = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            wakeLock.acquire();
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            InputStream io = null;
            OutputStream op = null;
            HttpURLConnection connection = null;
            try{
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                    return "Url Connection Error!"+connection.getResponseMessage();
                }

                int fileLength = connection.getContentLength();

                io = connection.getInputStream();

                op = new FileOutputStream(dest_file_path);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while((count =io.read(data)) != -1){
                    if(isCancelled()){
                        io.close();
                        return null;
                    }
                    total += count;
                    if(fileLength >0) {
                        publishProgress((int) total * 100 / fileLength);
                    }
                    op.write(data,0,count);
                }
            }catch (Exception ex){
                Log.e("Exception: ",""+ex.getMessage());
            }finally {
                try{
                    if(io != null){
                        io.close();
                    }
                    if (op !=null){
                        op.close();
                    }
                }catch (Exception ignored){}
                if(connection != null){
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            dialog.setIndeterminate(false);
            dialog.setMax(100);
            dialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            wakeLock.release();
            dialog.dismiss();
            if (s != null)
                Toast.makeText(ctx,"Download error: "+s, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(ctx,"File downloaded", Toast.LENGTH_SHORT).show();
        }
    }
}
