package com.gnt.samsung.app;

/**
 * Created by Shakawat on 11/2/2015.
 */
public class DataContainer {
    public String operation;
    public String result;
    public String start_time;
    public String end_time;
    public String start_mode;
    public String end_mode;
    public String start_strength;
    public String end_strength;
    public String start_position;
    public String end_position;
    public DataContainer(){}
    public DataContainer(String op,String res,String st,String et,String sm,String em,String ss,String es,String sp,String ep){
        operation = op;
        result = res;
        start_time = st;
        end_time = et;
        start_mode = sm;
        end_mode = em;
        start_strength = ss;
        end_strength = es;
        start_position = sp;
        end_position = ep;
    }


}
