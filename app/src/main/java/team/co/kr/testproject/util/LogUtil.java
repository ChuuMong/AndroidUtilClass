package team.co.kr.testproject.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.util.Calendar;

public class LogUtil {

    private static boolean isLog = true;
    private static boolean isLogFile = false;

    private static final String TAG = "Test Log";

    private static final String LOG_PATH = "test/log";
    private static final String LOG_FILENAME = "log.txt";

    public static final int ERROR = Log.ERROR;
    public static final int WARN = Log.WARN;
    public static final int INFO = Log.INFO;
    public static final int VERBOSE = Log.VERBOSE;
    public static final int DEBUG = Log.DEBUG;

    /**
     * 로그용 현재 시간 추출([YYYY/MM/DD hh:mm/ss])
     */
    @SuppressLint("DefaultLocale")
    public static String getLogTime() {
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.DAY_OF_MONTH) + 1;
        int date = cal.get(Calendar.DATE);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);

        return String.format("[%04d/%02d/%02d %02d:%02d:%02d] ", year, month, date, hour, min, sec);
    }

    public static void log(String tag, int logLevel, String text) {
        if (isLog) {
            switch (logLevel) {
                case Log.ERROR:
                    Log.e(tag, getLogTime() + text);
                    break;
                case Log.INFO:
                    Log.i(tag, getLogTime() + text);
                    break;
                case Log.VERBOSE:
                    Log.v(tag, getLogTime() + text);
                    break;
                case Log.WARN:
                    Log.w(tag, getLogTime() + text);
                    break;
                default:
                    Log.d(tag, getLogTime() + text);
                    break;
            }
        }
    }

    public static void log(String tag, String text) {
        log(tag, Log.DEBUG, text);
    }

    public static void log(int logLevel, String text) {
        log(TAG, logLevel, text);
    }

    public static void log(String text) {
        log(TAG, Log.DEBUG, text);
    }

    public static void fileLog(Context context, String text) {
        if (isLogFile) {
            String path = FileUtil.makeDir(LOG_PATH);
            FileUtil.writeToFile(context, path, LOG_FILENAME, getLogTime() + " " + text + "\n");
        }
    }

    public static void deleteLog(Context context) {
        FileUtil.deleteDir(LOG_PATH);
    }
}
