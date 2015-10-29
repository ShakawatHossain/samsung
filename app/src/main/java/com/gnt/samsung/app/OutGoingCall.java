package com.gnt.samsung.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.*;
import android.telephony.PhoneStateListener;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;

/**
 * Created by Shakawat on 10/14/2015.
 */
public class OutGoingCall /*extends android.telephony.PhoneStateListener */{
    Intent callIntent;
    TelephonyManager tm;
    Context ctx;
    WritinReport write;

    public OutGoingCall(){}
    public OutGoingCall(Context ctx){
        this.ctx = ctx;
        tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        Calendar now = Calendar.getInstance();
        int date = now.get(Calendar.DAY_OF_MONTH);
        write = new WritinReport(ctx,"file"+date+".xls");
//        tm.listen(this, PhoneStateListener.LISTEN_CALL_STATE);
    }

    public void startCall(String phone){
        Log.e("Start Ongoing call", "Called");
        callIntent = new Intent(Intent.ACTION_CALL)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        callIntent.setData(Uri.parse("tel:" + phone));
        ctx.startActivity(callIntent);
    }

    public boolean killCall() {
        Log.e("Kill call", "Called");
        // Get the boring old TelephonyManager
        writeCallState();
        TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);


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
            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");

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

    public void writeCallState(){
        int state = tm.getCallState();
        if(state == TelephonyManager.CALL_STATE_OFFHOOK){
            Log.d("CallState","Offhook");
            write.writeOut("outGoing","Success");
        }
        else {
            Log.d("CallState",""+state);
            write.writeOut("outGoing", "Failed");
        }
    }

//    @Override
//    public void onCallStateChanged(int state, String incomingNumber) {
//        super.onCallStateChanged(state, incomingNumber);
//        if(state == TelephonyManager.CALL_STATE_OFFHOOK){
//            Log.d("CallState","Offhook");
//
//        }
//        else{
//
//        }
//    }
}
