package com.gnt.samsung.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;


public class MainActivity extends Activity {

    RelativeLayout relativeLayout;
    Intent callIntent;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    MyLocation myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        relativeLayout = (RelativeLayout) findViewById(R.id.rl_main);
        sharedPrefferenceInit();
        startActivity(new Intent(this, OutGoingCallActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean rtn = WritinReport.write(MainActivity.this,"data.xls");
        Log.d("Return: ", "WritinReport " + rtn);
    }

    public void file(View v){
        startActivity(new Intent(MainActivity.this, FiledownActivity.class));
    }
    public void recieveCall(View v){
        startActivity(new Intent(MainActivity.this,AutoRecieve.class));
    }

    public void sharedPrefferenceInit(){
        sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        Calendar now = Calendar.getInstance();
        int date = now.get(Calendar.DAY_OF_MONTH);
//        editor.putInt("date",date);
//        editor.putInt("value", 0);
//        editor.commit();
        int i = sharedpreferences.getInt("date", 0);
        if(i ==0 || i !=date){
            Log.d("Date","Got value: "+i);
            Log.d("Value","Got value: "+i);
            editor.putInt("date", date);
            editor.putInt("value", 0);
            editor.commit();
        }
        else{
            Log.d("Date","Got value: "+i);
            Log.d("Value","Got value: "+i);
        }
    }
    public void getLocation(){
        myLocation = new MyLocation(MainActivity.this);
        if(!myLocation.canGetLocation){
            myLocation.showSettingsAlert();
        }
    }
    public void up(View v){
        startActivity(new Intent(this,FileUploadActivity.class));
    }



//    public void startCall(){
//        callIntent = new Intent(Intent.ACTION_CALL)
//                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        callIntent.setData(Uri.parse("tel:" + phone));
//        MainActivity.this.startActivity(callIntent);
//    }

//    public boolean killCall(Context context) {
//        Log.e("Kill call", "Called");
//        // Get the boring old TelephonyManager
//        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//
//
//        Class classTelephony = null;
//        try {
//            // Get the getITelephony() method
//            classTelephony = Class.forName(telephonyManager.getClass().getName());
//
//            Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");
//
//            // Ignore that the method is supposed to be private
//            methodGetITelephony.setAccessible(true);
//
//            // Invoke getITelephony() to get the ITelephony interface
//            Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);
//
//            // Get the endCall method from ITelephony
//            Class telephonyInterfaceClass =
//                    Class.forName(telephonyInterface.getClass().getName());
//            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");
//
//            // Invoke endCall()
//            methodEndCall.invoke(telephonyInterface);
//            Log.e("Kill call", "end");
//
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//            Log.e("Exception","MethodException");
//            return false;
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//            Log.e("Exception", "ClassNotFoundException");
//            return false;
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//            Log.e("Exception", "InvocationTargetException");
//            return false;
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//            Log.e("Exception", "IllegalAccessException");
//            return false;
//        }
//        return true;
//    }


}
