package com.charlie.wheather.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Log工具类
 * Created by charlie on 2017/2/23.
 */
public class LogUtil {

    public static final int PLACE_NONE = 0;
    public static final int PLACE_LOGCAT = 1;
    public static final int PLACE_FILE = 2;
    public static final int PLACE_LOGCAT_AND_FILE = 3;

    private static Context mContext;
    private static int mLogPlace = PLACE_NONE;
    private static int mLogLevel = android.util.Log.DEBUG;
    private static String mFileName = "charlie.log";
    private static SimpleDateFormat mDateFormat;

    /**
     * 相关配置 (在Application中全局配置一次就行)
     * @param ctx
     * @param place PLACE_NONE(不打印) or PLACE_LOGCAT(输出到Logcat) or PLACE_FILE(输出到SD卡文件) or PLACE_LOGCAT_AND_FILE(both)
     * @param level android.util.Log.DEBUG or android.util.Log.INFO or android.util.Log.WARN or android.util.Log.ERROR
     */
    public static void configue(Context ctx, int place, int level){
        mContext = ctx;
        mLogPlace = place;
        mLogLevel = level;

        if(mLogPlace >= PLACE_FILE){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String format = sdf.format(new Date());
            mFileName = format.concat(".log");
        }
    }

    /**
     * debug信息
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (PLACE_NONE == mLogPlace || mLogLevel>android.util.Log.DEBUG) return;

        switch (mLogPlace) {
            case PLACE_LOGCAT:
                android.util.Log.d(tag, msg);
                break;
            case PLACE_FILE:
                writeToFile(android.util.Log.DEBUG, tag, msg);
                break;
            case PLACE_LOGCAT_AND_FILE:
                android.util.Log.d(tag, msg);
                writeToFile(android.util.Log.DEBUG, tag, msg);
                break;
        }
    }

    /**
     * info信息
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (PLACE_NONE == mLogPlace || mLogLevel>android.util.Log.INFO) return;

        switch (mLogPlace) {
            case PLACE_LOGCAT:
                android.util.Log.i(tag, msg);
                break;
            case PLACE_FILE:
                writeToFile(android.util.Log.INFO, tag, msg);
                break;
            case PLACE_LOGCAT_AND_FILE:
                android.util.Log.i(tag, msg);
                writeToFile(android.util.Log.INFO, tag, msg);
                break;
        }
    }

    /**
     * wran信息
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
        if (PLACE_NONE == mLogPlace || mLogLevel>android.util.Log.WARN) return;

        switch (mLogPlace) {
            case PLACE_LOGCAT:
                android.util.Log.w(tag, msg);
                break;
            case PLACE_FILE:
                writeToFile(android.util.Log.WARN, tag, msg);
                break;
            case PLACE_LOGCAT_AND_FILE:
                android.util.Log.w(tag, msg);
                writeToFile(android.util.Log.WARN, tag, msg);
                break;
        }
    }

    /**
     * error信息
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (PLACE_NONE == mLogPlace || mLogLevel>android.util.Log.ERROR) return;

        switch (mLogPlace) {
            case PLACE_LOGCAT:
                android.util.Log.e(tag, msg);
                break;
            case PLACE_FILE:
                writeToFile(android.util.Log.ERROR, tag, msg);
                break;
            case PLACE_LOGCAT_AND_FILE:
                android.util.Log.e(tag, msg);
                writeToFile(android.util.Log.ERROR, tag, msg);
                break;
        }
    }

    // 写到应用的外部存储Cache文件下
    private static void writeToFile(int level, String tag, String msg){
        if(null == mContext || !Environment.MEDIA_MOUNTED.equalsIgnoreCase(Environment.getExternalStorageState())){
            return;
        }

        if (null == mDateFormat) {
            mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        }

        String strLevel = null;
        switch (level) {
            case android.util.Log.DEBUG:
                strLevel = "DEBUG";
                break;
            case android.util.Log.INFO:
                strLevel = "INFO";
                break;
            case android.util.Log.WARN:
                strLevel = "WARN";
                break;
            case android.util.Log.ERROR:
                strLevel = "ERROR";
                break;
            default:
                strLevel = "CHARLIE";
        }

        FileWriter fw = null;
        try {
            File logFile = new File(mContext.getExternalCacheDir(), mFileName);
            fw = new FileWriter(logFile, true);
            StringBuffer sb = new StringBuffer();
            sb.append(mDateFormat.format(new Date())).append('\t')
                    .append(strLevel).append('\t')
                    .append(tag).append('\t')
                    .append(msg).append('\r').append('\n');
            fw.write(sb.toString());
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
