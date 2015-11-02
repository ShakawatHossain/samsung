package com.gnt.samsung.app;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;


public class OutGoingCallActivity extends Activity {

    TableLayout tableLayout;
    Button startCall;
    EditText phnNum,duration,interval,quantity;
    String num,du,in,qu;
    int counter=0;
    Context ctx;
    OutGoingCall out;
    TextView numofcal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_going_call);
        tableLayout = (TableLayout) findViewById(R.id.tableLout_relativeLout_outGoingCall);
        startCall = (Button) findViewById(R.id.btn_call);
        startCall.setOnClickListener(listener);
        startCall.setOnTouchListener(touchListener);
        ctx = OutGoingCallActivity.this;
        out = new OutGoingCall(ctx);
    }

    @Override
    protected void onResume() {
        super.onResume();
        phnNum = (EditText) findViewById(R.id.edt_phn_num);
        duration = (EditText) findViewById(R.id.edt_call_duration);
        interval = (EditText) findViewById(R.id.edt_call_interval);
        quantity = (EditText) findViewById(R.id.edt_num_call);
        numofcal = (TextView) findViewById(R.id.numberofcall);
    }

    public View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("Start Call","Clicked");
            num = phnNum.getText().toString();
            du = duration.getText().toString();
            in = interval.getText().toString();
            qu = quantity.getText().toString();
            int delay = Integer.parseInt(du);
            tableLayout.postDelayed(run,delay*1000*60);         //Duration of call
            out.startCall(num);
            counter++;
            int totalcall = Integer.parseInt(qu);
            numofcal.setText("Call Left: "+(totalcall-counter));
        }

    };

    public View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
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

    public Runnable run = new Runnable() {
        @Override
        public void run() {
            endcall();
            int totalcall = Integer.parseInt(qu);
            numofcal.setText("Call Left: "+(totalcall-counter));
            if(totalcall > counter){
                counter++;
                int delay = Integer.parseInt(in);       //interval Between call
                Log.d("Interval","call num"+counter);
                tableLayout.postDelayed(makecall,delay*1000*60);    //value*ms*min
            }
            else {
                Log.d("No Interval","call finished"+counter);
                finish();
                //finish();
            }
        }
    };
    public Runnable makecall = new Runnable() {
        @Override
        public void run() {
            int totalcall = Integer.parseInt(qu);
            numofcal.setText("Call Left: "+(totalcall-counter));
            int delay = Integer.parseInt(du);
            out.startCall(num);
            tableLayout.postDelayed(run,delay*1000*60);     //Duration of call //value*ms*min
        }
    };

    private void endcall(){
        out.killCall();
    }
}
