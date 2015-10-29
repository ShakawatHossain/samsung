package com.gnt.samsung.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class AutoRecieve extends Activity {

    MyPhonestateListener listener = new MyPhonestateListener();
    TelephonyManager tm;
    int connectionType;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_recieve);
        tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        connectionType = tm.getPhoneType();
        relativeLayout = (RelativeLayout) findViewById(R.id.auto_relative);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("On resume", " show state!!");
        if(connectionType == TelephonyManager.PHONE_TYPE_CDMA){
            Toast.makeText(AutoRecieve.this,"Calls CDMA network",Toast.LENGTH_SHORT).show();
        }else if(connectionType == TelephonyManager.PHONE_TYPE_GSM){
            Toast.makeText(AutoRecieve.this,"Calls GSM network",Toast.LENGTH_SHORT).show();
        }else if(connectionType == TelephonyManager.PHONE_TYPE_NONE){
            Toast.makeText(AutoRecieve.this,"Calls NONE network",Toast.LENGTH_SHORT).show();
        }else if(connectionType == TelephonyManager.PHONE_TYPE_SIP){
            Toast.makeText(AutoRecieve.this,"Calls SIP network",Toast.LENGTH_SHORT).show();
        }
    }

    public class MyPhonestateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            if(state == TelephonyManager.CALL_STATE_RINGING){
                Toast.makeText(AutoRecieve.this,"Calls Come WOW!",Toast.LENGTH_SHORT).show();
                Log.d("In coming", " Listened!!");
                try {
//                    recieveCall(AutoRecieve.this);
//                    tm.getClass().getMethod("answerRingingCall").invoke(tm);
//                    Thread.sleep(800);
//                    Process process = Runtime.getRuntime().exec(new String[]{ "su","-c","input keyevent 5"});
//                    process.waitFor();
                    //if(Build.VERSION.SDK_INT<=14){
                        Log.d("Phone Api is", " "+Build.VERSION.SDK_INT);
                        Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);// intent to recieve call
                        buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(
                                KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
                        sendOrderedBroadcast(buttonUp, "android.permission.CALL_PRIVILEGED");// add permission to intent
//                    }else{
//                        Log.d("Phone Api is", ""+ Build.VERSION.SDK_INT);
//                        Intent headSetUnPluggedintent = new Intent(Intent.ACTION_HEADSET_PLUG);
//                        headSetUnPluggedintent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY);
//                        headSetUnPluggedintent.putExtra("state", 0);
//                        headSetUnPluggedintent.putExtra("name", "Headset");
//                        try {
//                            sendOrderedBroadcast(headSetUnPluggedintent, null);
//                        } catch (Exception e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                            Log.e("Exception: Phone Api is", ""+ Build.VERSION.SDK_INT+" "+e.getMessage());
//                        }
//                    }
                }catch (Exception ex){
                    Log.e("Exception: ",""+ex.getMessage());
                }

                relativeLayout.postDelayed(Kill,9000);
            }
        }
    }

    Runnable Kill = new Runnable() {
        @Override
        public void run() {
            Log.d("In coming", " rejected!!");
            //new MainActivity().killCall(AutoRecieve.this);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(tm != null){
            tm = null;
        }
    }

    public boolean recieveCall(Context context) {
        Log.e("Kill call", "Called");
        // Get the boring old TelephonyManager
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);


        Class classTelephony = null;
        try {
            // Get the getITelephony() method
            classTelephony = Class.forName(telephonyManager.getClass().getName());

            Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");

            // Ignore that the method is supposed to be private
            methodGetITelephony.setAccessible(true);

            // Invoke getITelephony() to get the ITelephony interface
            Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);

            // Get the endCall method from ITelephony
            Class telephonyInterfaceClass =
                    Class.forName(telephonyInterface.getClass().getName());
            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("answerRingingCall");

            // Invoke endCall()
            methodEndCall.invoke(telephonyInterface);
            Log.e("Kill call", "end");

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Log.e("Exception","MethodException");
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e("Exception", "ClassNotFoundException");
            return false;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            Log.e("Exception", "InvocationTargetException");
            return false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.e("Exception", "IllegalAccessException");
            return false;
        }
        return true;
    }
}