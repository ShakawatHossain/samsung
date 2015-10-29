package com.gnt.samsung.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;
import java.nio.charset.Charset;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.util.EntityUtils;


public class FileUploadActivity extends Activity {

    public final static String TAG = FileUploadActivity.class.getSimpleName();

    private ProgressBar progressBar;
    private String filePath = null;
    private TextView txtPercentage;
    //private ImageView imgPreview;
    //private VideoView vidPreview;
    //private Button btnUpload;
    long totalSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_upload);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtPercentage = (TextView) findViewById(R.id.textPercentage);
    }

    @SuppressWarnings("deprecation")
    public void up(View v){
        new UploadtoServer().execute();
//        Log.d("File Upload","Going");
//        String url = "http://www.geeksntechnology.com/app/upload.php";
//        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
//                "/Download/images.png");
//        String s = Environment.getExternalStorageDirectory().getAbsolutePath();
//        Log.d("File Address: ",""+s);
//        try {
//            HttpClient httpclient = new DefaultHttpClient();
//
//            HttpPost httppost = new HttpPost(url);
//
//            InputStreamEntity reqEntity = new InputStreamEntity(
//                    new FileInputStream(file), -1);
//            reqEntity.setContentType("binary/octet-stream");
//            reqEntity.setChunked(true); // Send in multiple parts if needed
//            httppost.setEntity(reqEntity);
//            HttpResponse response = httpclient.execute(httppost);
//            //Do something with response...
//
//        } catch (Exception e) {
//            // show error
//            Log.e("File Upload","Failed Due to "+e.getMessage()+e);
//        }
    }
    public class UploadtoServer extends AsyncTask<Void,Integer,String>{

        @Override
        protected void onPreExecute() {
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
            txtPercentage.setText(String.valueOf(values[0]) + "%");
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Void... params) {
            return upload();
        }

        @SuppressWarnings("deprication")
        public String upload(){
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://www.geeksntechnology.com/app/upload.php");
            try{
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                    @Override
                    public void transferred(long num) {
                        publishProgress((int) ((num / (float) totalSize) * 100));
                    }
                });
                File sourceFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
//                "/Download/images.png");
                        "/Abc.mp3");
                String s = Environment.getExternalStorageDirectory().getAbsolutePath();
                Log.d("File Location: ", s +
                        "/Download/Abc.mp3");

                entity.addPart("cv_file", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server
                entity.addPart("website",
                        new StringBody("www.androidhive.info"));
                entity.addPart("email", new StringBody("abc@gmail.com"));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }
            }catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;
        }


        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);

            // showing the server response in an alert dialog
            showAlert(result);

            super.onPostExecute(result);
        }
        /**
         * Method to show alert dialog
         * */
        private void showAlert(String message) {
            AlertDialog.Builder builder = new AlertDialog.Builder(FileUploadActivity.this);
            builder.setMessage(message).setTitle("Response from Servers")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // do nothing
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

    }


}

@SuppressWarnings("deprecation")
class AndroidMultiPartEntity extends MultipartEntity

{

    private final ProgressListener listener;

    public AndroidMultiPartEntity(final ProgressListener listener) {
        super();
        this.listener = listener;
    }

    public AndroidMultiPartEntity(final HttpMultipartMode mode,
                                  final ProgressListener listener) {
        super(mode);
        this.listener = listener;
    }

    public AndroidMultiPartEntity(HttpMultipartMode mode, final String boundary,
                                  final Charset charset, final ProgressListener listener) {
        super(mode, boundary, charset);
        this.listener = listener;
    }

    @Override
    public void writeTo(final OutputStream outstream) throws IOException {
        super.writeTo(new CountingOutputStream(outstream, this.listener));
    }

    public static interface ProgressListener {
        void transferred(long num);
    }

    public static class CountingOutputStream extends FilterOutputStream {

        private final ProgressListener listener;
        private long transferred;

        public CountingOutputStream(final OutputStream out,
                                    final ProgressListener listener) {
            super(out);
            this.listener = listener;
            this.transferred = 0;
        }

        public void write(byte[] b, int off, int len) throws IOException {
            out.write(b, off, len);
            this.transferred += len;
            this.listener.transferred(this.transferred);
        }

        public void write(int b) throws IOException {
            out.write(b);
            this.transferred++;
            this.listener.transferred(this.transferred);
        }
    }
}
