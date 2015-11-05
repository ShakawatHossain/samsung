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
import android.view.*;
import android.widget.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;


public class AutoRecieve extends Activity {

    MyPhonestateListener listener = new MyPhonestateListener();
    TelephonyManager tm = null;
    EditText num_of__call,interval;
    RelativeLayout relativeLayout;
    TextView status;
    DataContainer dc;
    WritinReport write;
    Button recieve;
    int total_call,interval_btn_call,counter,result=0,connectionType;
    double lat1,long1,lat2,long2;
    MyLocation myLocation;
    String start_time,end_time,start_mode,end_mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_recieve); Calendar now = Calendar.getInstance();

        int date = now.get(Calendar.DAY_OF_MONTH);
        write = new WritinReport(AutoRecieve.this,"file"+date+".xls");

        myLocation = new MyLocation(this);

        relativeLayout = (RelativeLayout) findViewById(R.id.auto_relative);
        recieve = (Button) findViewById(R.id.auto_activate);
        num_of__call = (EditText) findViewById(R.id.total_call);
        interval = (EditText) findViewById(R.id.gap_duration);
        status = (TextView) findViewById(R.id.incomeing_call_no);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void recieve(View v){
        String cal = num_of__call.getText().toString();
        String inter = interval.getText().toString();
        if(cal.equals("") || inter.equals("")){
            Toast.makeText(this,"Can't Start Swevice<font color='red'> Value Required</font>",Toast.LENGTH_LONG).show();
            return;
        }
        total_call = Integer.parseInt(cal);
        interval_btn_call = Integer.parseInt(inter);
        if(total_call<=0 || interval_btn_call<=0){
            Toast.makeText(this,"Wrong <font color='red'> Value</font>",Toast.LENGTH_LONG).show();
            return;
        }
        if(tm == null){
            Toast.makeText(this,"Auto Recieve Servece: "+"Started",Toast.LENGTH_LONG).show();
            tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
            tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
            relativeLayout.postDelayed(Check, (interval_btn_call * 60 * 1000));
            status.setText("Waiting For call!");
//            connectionType = tm.getPhoneType();
        }
        else{
            Toast.makeText(this,"Auto Recieve Servece: "+"Stopped",Toast.LENGTH_LONG).show();
            tm.listen(listener, PhoneStateListener.LISTEN_NONE);
            tm = null;
        }
    }

    public class MyPhonestateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            if(state == TelephonyManager.CALL_STATE_RINGING){
                Toast.makeText(AutoRecieve.this, "Calls Come WOW!", Toast.LENGTH_SHORT).show();
                Log.d("In coming", " Listened!!");
                Calendar calendar = Calendar.getInstance();
                start_time = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+" : "+
                        String.valueOf(calendar.get(Calendar.MINUTE))+" : "+
                        String.valueOf(calendar.get(Calendar.SECOND));
                counter++;
                result = 1;
                lat1 = myLocation.getLatitude();
                long1 = myLocation.getLongitude();
                start_mode = check_mode();
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
            }
            else if(state == TelephonyManager.CALL_STATE_IDLE){
                Calendar calendar = Calendar.getInstance();
                end_time = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+" : "+
                        String.valueOf(calendar.get(Calendar.MINUTE))+" : "+
                        String.valueOf(calendar.get(Calendar.SECOND));
                lat2 = myLocation.getLatitude();
                long2 = myLocation.getLongitude();
                end_mode = check_mode();
            }
        }
    }

    Runnable Check = new Runnable() {
        @Override
        public void run() {
            if(result == 0){
                counter++;
                status.setText("Call Left: " + (total_call - counter));
                Calendar calendar = Calendar.getInstance();
                String time = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+" : "+
                        String.valueOf(calendar.get(Calendar.MINUTE))+" : "+
                        String.valueOf(calendar.get(Calendar.SECOND));
                dc = new DataContainer("IncomeingCall","Failed",end_time,time,end_mode,check_mode(),"SS","Es",lat2+","+long2,lat2+","+long2);
                write.writeOut(dc);
            }
            else if(result == 1){
                result =0;
                status.setText("Call Left: "+(total_call-counter));
                dc = new DataContainer("IncomeingCall","Success",start_time,end_time,start_mode,end_mode,"Unknown","Unknown",lat1+","+long1,lat2+","+long2);
                write.writeOut(dc);
            }

            if(counter<total_call) {
                relativeLayout.postDelayed(Check, (interval_btn_call * 60 * 1000));
            }
            else{
                finish();
            }
            //new MainActivity().killCall(AutoRecieve.this);
        }
    };

    public String check_mode(){
        connectionType = tm.getPhoneType();
        if(connectionType == TelephonyManager.PHONE_TYPE_CDMA){
            Toast.makeText(AutoRecieve.this,"Calls CDMA network",Toast.LENGTH_SHORT).show();
            return "CDMA";
        }else if(connectionType == TelephonyManager.PHONE_TYPE_GSM){
            Toast.makeText(AutoRecieve.this,"Calls GSM network",Toast.LENGTH_SHORT).show();
            return "GSM";
        }else if(connectionType == TelephonyManager.PHONE_TYPE_NONE){
            Toast.makeText(AutoRecieve.this,"Calls NONE network",Toast.LENGTH_SHORT).show();
            return "None";
        }else if(connectionType == TelephonyManager.PHONE_TYPE_SIP){
            Toast.makeText(AutoRecieve.this,"Calls SIP network",Toast.LENGTH_SHORT).show();
            return "SIP";
        }
        else{
            return "Unknown";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(tm != null){
            tm = null;
        }
    }

//    public boolean recieveCall(Context context) {
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
//            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("answerRingingCall");
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

    public View.OnTouchListener TouchListen = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    if(v.getId() == R.id.btn_call){
                        v.setBackgroundColor(getResources().getColor(R.color.dark_green_btn));
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if(v.getId() == R.id.btn_call){
                        v.setBackgroundColor(getResources().getColor(R.color.green_btn));
                    }
                    break;
            }
            return false;
        }
    };

}