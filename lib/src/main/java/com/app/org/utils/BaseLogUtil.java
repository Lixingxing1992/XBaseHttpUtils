package com.app.org.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.apkfuns.logutils.LogUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lixing on 2018/1/24.
 */
public class BaseLogUtil {

    private final static SimpleDateFormat LOG_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 日志的输出格式
    private final static SimpleDateFormat FILE_SUFFIX = new SimpleDateFormat("yyyy-MM-dd");// 日志文件格式
    private static Boolean LOG_SWITCH = true; // 日志文件总开关
    private static Boolean LOG_TO_FILE = false; // 日志写入文件开关
    private static String LOG_TAG = "TAG"; // 默认的tag
    private static char LOG_TYPE = 'v';// 输入日志类型，v代表输出所有信息,w则只输出警告...
    private static int LOG_SAVE_DAYS = 7;// sd卡中日志文件的最多保存天数
    private static String LOG_FILE_PATH; // 日志文件保存路径
    private static String LOG_FILE_NAME;// 日志文件保存名称

    public static void init(Context context) { // 在Application中初始化
        LOG_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + context.getPackageName();
        LOG_FILE_NAME = "Log";
    }

    public static void init(Context context,boolean LOG_SWITCHs,boolean LOG_TO_FILEs) { // 在Application中初始化
        LOG_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + context.getPackageName();
        LOG_FILE_NAME = "Log";
        LOG_SWITCH = LOG_SWITCHs;
        LOG_TO_FILE = LOG_TO_FILEs;

//        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
//                .showThreadInfo(false)      //（可选）是否显示线程信息。 默认值为true
//                .methodCount(1)               // （可选）要显示的方法行数。 默认2
//                .methodOffset(4)               // （可选）设置调用堆栈的函数偏移值，0的话则从打印该Log的函数开始输出堆栈信息，默认是0
//                .tag("MY_LOGGER")                  //（可选）每个日志的全局标记。 默认PRETTY_LOGGER（如上图）
//                .build();
//        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
//            @Override public boolean isLoggable(int priority, String tag) {
//                return LOG_SWITCH;
//            }
//        });
        LogUtils.getLogConfig()
                .configAllowLog(LOG_SWITCH)
                .configTagPrefix("MY_LOGGER")
                .configMethodOffset(3)
                .configShowBorders(false);
    }

    /****************************
     * Warn
     *********************************/
    public static void w(Object msg) {
        w(LOG_TAG, msg);
    }

    public static void w(String tag, Object msg) {
        w(tag, msg, null);
    }

    public static void w(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'w');
    }

    /***************************
     * Error
     ********************************/
    public static void e(Object msg) {
        e(LOG_TAG, msg);
    }

    public static void e(String tag, Object msg) {
        e(tag, msg, null);
    }

    public static void e(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'e');
    }

    /***************************
     * Debug
     ********************************/
    public static void d(Object msg) {
        d(LOG_TAG, msg);
    }

    public static void d(String tag, Object msg) {// 调试信息
        d(tag, msg, null);
    }

    public static void d(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'd');
    }

    /****************************
     * Info
     *********************************/
    public static void i(Object msg) {
        i(LOG_TAG, msg);
    }

    public static void i(String tag, Object msg) {
        i(tag, msg, null);
    }

    public static void i(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'i');
    }

    /**************************
     * Verbose
     ********************************/
    public static void v(Object msg) {
        v(LOG_TAG, msg);
    }

    public static void v(String tag, Object msg) {
        v(tag, msg, null);
    }

    public static void v(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'v');
    }

    /**
     * 根据tag, msg和等级，输出日志
     *
     * @param tag
     * @param msg
     * @param level
     */
    private static void log(String tag, String msg, Throwable tr, char level) {
        if (LOG_SWITCH) {
            if ('e' == level && ('e' == LOG_TYPE || 'v' == LOG_TYPE)) { // 输出错误信息
//                Log.e(tag, msg, tr);
                if(tr == null){
                    LogUtils.tag(tag).e(msg);
                }else{
                    LogUtils.tag(tag).e(tr);
                }
            } else if ('w' == level && ('w' == LOG_TYPE || 'v' == LOG_TYPE)) {
//                Log.w(tag, msg, tr);
                LogUtils.tag(tag).w(msg);
            } else if ('d' == level && ('d' == LOG_TYPE || 'v' == LOG_TYPE)) {
//                Log.d(tag, msg, tr);
                LogUtils.tag(tag).d(msg);
            } else if ('i' == level && ('d' == LOG_TYPE || 'v' == LOG_TYPE)) {
//                Log.i(tag, msg, tr);
                LogUtils.tag(tag).i(msg);
            } else {
//                Log.v(tag, msg, tr);
                LogUtils.tag(tag).i(msg);
            }
            if (LOG_TO_FILE)
                log2File(String.valueOf(level), tag, msg + tr == null ? "" : "\n" + Log.getStackTraceString(tr));
        }
    }

    /**
     * 打开日志文件并写入日志
     *
     * @return
     **/
    private synchronized static void log2File(String mylogtype, String tag, String text) {
        Date nowtime = new Date();
        String date = FILE_SUFFIX.format(nowtime);
        String dateLogContent = LOG_FORMAT.format(nowtime) + ":" + mylogtype + ":" + tag + ":" + text; // 日志输出格式
        File destDir = new File(LOG_FILE_PATH);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File file = new File(LOG_FILE_PATH, LOG_FILE_NAME + date);
        try {
            FileWriter filerWriter = new FileWriter(file, true);
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(dateLogContent);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除指定的日志文件
     */
    public static void delFile() {// 删除日志文件
        String needDelFiel = FILE_SUFFIX.format(getDateBefore());
        File file = new File(LOG_FILE_PATH, needDelFiel + LOG_FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 得到LOG_SAVE_DAYS天前的日期
     *
     * @return
     */
    private static Date getDateBefore() {
        Date nowtime = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(nowtime);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - LOG_SAVE_DAYS);
        return now.getTime();
    }

    public static void saveLogFile(String message) {
        File fileDir = new File(BaseFileUtil.getRootPath() + File.separator + BaseUtils.getContext().getPackageName());
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        File file = new File(fileDir, BaseTimeUtil.getCurrentDateTime("yyyyMMdd") + ".txt");
        try {
            if (file.exists()) {
                PrintStream ps = new PrintStream(new FileOutputStream(file, true));
                ps.append(BaseTimeUtil.getCurrentDateTime("\n\n\nyyyy-MM-dd HH:mm:ss") + "\n" + message);// 往文件里写入字符串
            } else {
                PrintStream ps = new PrintStream(new FileOutputStream(file));
                file.createNewFile();
                ps.println(BaseTimeUtil.getCurrentDateTime("yyyy-MM-dd HH:mm:ss") + "\n" + message);// 往文件里写入字符串
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
