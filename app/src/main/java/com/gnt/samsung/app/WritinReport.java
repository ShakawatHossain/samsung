package com.gnt.samsung.app;

import java.io.*;
import java.util.Iterator;
import android.content.SharedPreferences;
import org.apache.poi.hssf.usermodel.*;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
/**
 * Created by Shakawat on 10/11/2015.
 */
public class WritinReport {

    String filename;
    Context ctx;
    SharedPreferences prefs;
    SharedPreferences.Editor edit;
    public WritinReport(){}
    public WritinReport(Context ctx,String filename){
        this.ctx = ctx;
        prefs = this.ctx.getSharedPreferences("myPref", Context.MODE_PRIVATE);
        edit = prefs.edit();
        this.filename = filename;
    }

    public void writeOut(DataContainer dc){  //writng Report for outGoingCall...
        Workbook wb = new HSSFWorkbook();
        Cell c = null;
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.GOLD.index);
        cs.setFillPattern(HSSFCellStyle.ALIGN_CENTER);

        Sheet sheet1 = null;
        int roo = 0;
        roo = prefs.getInt("value", 0);
        Log.d("Writing on Row: ", "" + roo);
        edit.putInt("value", roo + 1);
        edit.commit();
        if(roo > 0){
            FileInputStream fsIp = null;
            FileOutputStream fsOp = null;
            try{
                fsIp = new FileInputStream(new File(ctx.getExternalFilesDir(null), filename));
                wb = new HSSFWorkbook(fsIp);
//                HSSFSheet worksheet = (HSSFSheet) wb.getSheetAt(0);
                Sheet sheet = wb.getSheetAt(0);
                Row row = sheet.createRow(roo);
                c = row.createCell(0);
                c.setCellValue(roo-1);

                c = row.createCell(1);
                c.setCellValue(dc.operation);

                c = row.createCell(2);
                c.setCellValue(dc.result);

                c = row.createCell(3);
                c.setCellValue(dc.start_time);

                c = row.createCell(4);
                c.setCellValue(dc.end_time);

                c = row.createCell(5);
                c.setCellValue(dc.start_mode);

                c = row.createCell(6);
                c.setCellValue(dc.end_mode);

                c = row.createCell(7);
                c.setCellValue(dc.start_strength);

                c = row.createCell(8);
                c.setCellValue(dc.end_strength);

                c = row.createCell(9);
                c.setCellValue(dc.start_position);

                c = row.createCell(10);
                c.setCellValue(dc.end_position);
                fsOp = new FileOutputStream(new File(ctx.getExternalFilesDir(null), filename));
                wb.write(fsOp);
            }catch(FileNotFoundException ex){
                Log.e("FileNotFoundException","Due to override xls file");
            } catch (IOException e) {
                Log.e("FileUtils", "Error writing "+e.getMessage());
            }
            catch(Exception ex){
                Log.e("Exception","Due to override xls file " + ex.getMessage());
            }finally {
                try {
                    if (null != fsOp)
                        fsOp.close();
                } catch (Exception ex) {
                    Log.e("finally Error","Got error finally");
                }
            }
            return;
        }
        sheet1 = wb.createSheet("Call in");
        Row row = sheet1.createRow(roo);

        c = row.createCell(0);
        c.setCellValue("No");
        c.setCellStyle(cs);

        c = row.createCell(1);
        c.setCellValue("Operation");
        c.setCellStyle(cs);

        c = row.createCell(2);
        c.setCellValue("Result");
        c.setCellStyle(cs);

        c = row.createCell(3);
        c.setCellValue("Start_Time");
        c.setCellStyle(cs);

        c = row.createCell(4);
        c.setCellValue("End_Time");
        c.setCellStyle(cs);

        c = row.createCell(5);
        c.setCellValue("Start_Mode");
        c.setCellStyle(cs);

        c = row.createCell(6);
        c.setCellValue("End_Mode");
        c.setCellStyle(cs);

        c = row.createCell(7);
        c.setCellValue("Start_Strength");
        c.setCellStyle(cs);

        c = row.createCell(8);
        c.setCellValue("End_Strength");
        c.setCellStyle(cs);

        c = row.createCell(9);
        c.setCellValue("Start_Position");
        c.setCellStyle(cs);

        c = row.createCell(10);
        c.setCellValue("End_Position");
        c.setCellStyle(cs);

        sheet1.setColumnWidth(0, (15 * 500));
        sheet1.setColumnWidth(1, (15 * 500));
        sheet1.setColumnWidth(2, (15 * 500));
        sheet1.setColumnWidth(3, (15 * 500));
        sheet1.setColumnWidth(4, (15 * 500));
        sheet1.setColumnWidth(5, (15 * 500));
        sheet1.setColumnWidth(6, (15 * 500));
        sheet1.setColumnWidth(7, (15 * 500));
        sheet1.setColumnWidth(8, (15 * 500));
        sheet1.setColumnWidth(9, (15 * 500));
        sheet1.setColumnWidth(10, (15 * 500));

        roo = prefs.getInt("value", 0);
        edit.putInt("value", roo + 1);
        edit.commit();
        row = sheet1.createRow(roo);
        c = row.createCell(0);
        c.setCellValue(roo-1);

        c = row.createCell(1);
        c.setCellValue(dc.operation);

        c = row.createCell(2);
        c.setCellValue(dc.result);

        c = row.createCell(3);
        c.setCellValue(dc.start_time);

        c = row.createCell(4);
        c.setCellValue(dc.end_time);

        c = row.createCell(5);
        c.setCellValue(dc.start_mode);

        c = row.createCell(6);
        c.setCellValue(dc.end_mode);

        c = row.createCell(7);
        c.setCellValue(dc.start_strength);

        c = row.createCell(8);
        c.setCellValue(dc.end_strength);

        c = row.createCell(9);
        c.setCellValue(dc.start_position);

        c = row.createCell(10);
        c.setCellValue(dc.end_position);


        File file = new File(ctx.getExternalFilesDir(null), filename);
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
            Log.e("FileUtils", "Writing file" + file);
        } catch (IOException e) {
            Log.e("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.e("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
                Log.e("finally Error","Got error finally");
            }
        }
    }



    public static  boolean write(Context context,String fileName) {
        //String root = Environment.getExternalStorageDirectory().toString();
        //fileName = root+"/"+fileName;
        Log.d("Got call","WritinReport");
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e("File Open Error", "Storage not available or read only");
            return false;
        }
        boolean success = false;
        //new workbook
        Workbook wb = new HSSFWorkbook();
        Cell c = null;

        //Cell Style for header row
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.LIME.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        //New Sheet
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("Call in");

        Row row = sheet1.createRow(0);
        c = row.createCell(0);
        c.setCellValue("Item Number");
        c.setCellStyle(cs);

        c = row.createCell(1);
        c.setCellValue("Quantity");
        c.setCellStyle(cs);

        c = row.createCell(2);
        c.setCellValue("Price");
        c.setCellStyle(cs);
        row = sheet1.createRow(1);
        c = row.createCell(0);
        c.setCellValue("Hello!");
        c.setCellStyle(cs);

        c = row.createCell(1);
        c.setCellValue("Hi");
        c.setCellStyle(cs);

        c = row.createCell(2);
        c.setCellValue("Bye");
        c.setCellStyle(cs);

        sheet1.setColumnWidth(0, (15 * 500));
        sheet1.setColumnWidth(1, (15 * 500));
        sheet1.setColumnWidth(2, (15 * 500));

        // Create a path where we will place our List of objects on external storage
        File file = new File(context.getExternalFilesDir(null), fileName);
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
            Log.e("FileUtils", "Writing file" + file);
            success = true;
        } catch (IOException e) {
            Log.e("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.e("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
                Log.e("finally Error","Got error finally");
            }
        }
        return success;
    }
    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }
}
