package com.gnt.samsung.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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
    double lat1,lat2,long1,long2;
    String start_time,end_time;
    String start_mode,end_mode;
    String start_strength,end_strength;
    MyLocation myLocation;

    public OutGoingCall(){}
    public OutGoingCall(Context ctx){
        this.ctx = ctx;
        tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        Calendar now = Calendar.getInstance();
        int date = now.get(Calendar.DAY_OF_MONTH);
        write = new WritinReport(ctx,"file"+date+".xls");
        myLocation = new MyLocation(this.ctx);
//        tm.listen(this, PhoneStateListener.LISTEN_CALL_STATE);
    }
    public String phoneType(){
        int connectionType = tm.getPhoneType();
        if(connectionType == TelephonyManager.PHONE_TYPE_CDMA){
            return "CDMA";
        }else if(connectionType == TelephonyManager.PHONE_TYPE_GSM){
            return "GSM";
        }else if(connectionType == TelephonyManager.PHONE_TYPE_NONE){
            return "None";
        }else if(connectionType == TelephonyManager.PHONE_TYPE_SIP){
            return "SIP";
        }
        return "Unknown";
    }

    public String strength(String mode){
//        if(mode.equals("GSM")){
//            if(Build.VERSION.SDK_INT>=17  && tm != null){
//            CellInfoGsm cellInfoGsm = (CellInfoGsm)tm.getAllCellInfo().get(0);
//            CellSignalStrengthGsm cellSignalStrengthGsm = cellInfoGsm.getCellSignalStrength();
//            String s = String.valueOf(cellSignalStrengthGsm.getDbm());
//            return s;
//            }
//        }
//        else if(mode.equals("CDMA")){
//            if(Build.VERSION.SDK_INT>=17 && tm != null){
//                CellInfoCdma cellInfoCdma = (CellInfoCdma)tm.getAllCellInfo().get(0);
//                CellSignalStrengthCdma cellSignalStrengthCdma = cellInfoCdma.getCellSignalStrength();
//                String s = String.valueOf(cellSignalStrengthCdma.getDbm());
//                return s;
//            }
//        }
        return "Unknown";
    }
    public void startCall(String phone){
        lat1 = myLocation.getLatitude();
        long1 = myLocation.getLongitude();
        Calendar calendar = Calendar.getInstance();
        start_time = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+" : "+String.valueOf(calendar.get(Calendar.MINUTE))+" : "+String.valueOf(calendar.get(Calendar.SECOND));
        start_mode = phoneType();
        start_strength = strength(start_mode);
        Log.e("Start Ongoing call", "Called @ "+start_time+" Place: "+lat1+" , "+long1+" mode: "+start_mode+" Strength: "+start_strength);
        callIntent = new Intent(Intent.ACTION_CALL)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        callIntent.setData(Uri.parse("tel:" + phone));
        ctx.startActivity(callIntent);
    }

    public boolean killCall() {
        lat2 = myLocation.getLatitude();
        long2 = myLocation.getLongitude();
        Calendar calendar = Calendar.getInstance();
        end_time = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+" : "+String.valueOf(calendar.get(Calendar.MINUTE))+" : "+String.valueOf(calendar.get(Calendar.SECOND));
        end_mode = phoneType();
        end_strength = strength(end_mode);
        Log.e("Start Ongoing call", "Called @ "+end_time+" Place: "+lat2+" , "+long2+" mode: "+end_mode+" Strength: "+end_strength);
        // Get the boring old TelephonyManager
        writeCallState();
        TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);

        Class classTelephony =null;
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
            Log.d("CallState", "Offhook");
            DataContainer dc = new DataContainer("OutGoing","success",start_time,end_time,start_mode,end_mode,
                    start_strength,end_strength,lat1+","+long1,lat2+","+long2);
            write.writeOut(dc);
//
        }
        else {
            Log.d("CallState", "" + state);
            DataContainer dc = new DataContainer("outGoing", "Failed",start_time,end_time,start_mode,end_mode,
                    start_strength,end_strength,lat1+","+long1,lat2+","+long2);
            write.writeOut(dc);
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
