package team.co.kr.testproject.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.telephony.TelephonyManager;
import android.view.Surface;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public class DeviceUtil {

    public static PackageInfo getPackageInfo(Context context) throws Exception {
        PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        return pInfo;
    }

    public static int getVersionCode(Context context) {
        try {
            PackageInfo pInfo = getPackageInfo(context);
            return pInfo.versionCode;
        }
        catch (Exception e) {
            return 0;
        }
    }

    public static String getVersionName(Context context) {
        try {
            PackageInfo pInfo = getPackageInfo(context);
            return pInfo.versionName;
        }
        catch (Exception e) {
            return "";
        }
    }

    public static int getVersionNameToInt(Context context) {
        return Integer.parseInt(StringUtil.pickOutNumber(getVersionName(context)));
    }

    public static String getPhoneNumber(Context context) {
        String phoneNumber = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();

        if (phoneNumber != null) {
            if (phoneNumber.matches(".*82.*")) {
                phoneNumber = "0" + phoneNumber.substring(3, phoneNumber.length());
            }
        }

        return phoneNumber;
    }

    public static String getDeviceId(Context context) {

        String deviceId = "UNKNOWN";
        try {
            String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            deviceId = URLEncoder.encode(android_id, "utf-8");
        }
        catch (Exception e) {

        }

        if (deviceId.equals("UNKNOWN") || deviceId.equals("")) {
            TelephonyManager tm = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
            if (tm != null) {
                deviceId = tm.getDeviceId();
            }

            if (deviceId == null || deviceId.equals("UNKNOWN") || deviceId.length() == 0) {
                deviceId = getMacAddress(context);
            }
        }

        return deviceId;
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface ni = (NetworkInterface) en.nextElement();

                for (Enumeration<InetAddress> enumIpAddr = ni.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();

                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        }
        catch (SocketException se) {
            se.printStackTrace();
        }

        return null;
    }

    public static String getMacAddress(Context context) {
        String macAddress = "000000000000000";
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            String macAddress_temp = wifiManager.getConnectionInfo().getMacAddress();
            if (macAddress_temp == null) {
                macAddress_temp = "000000000000000";
            }

            macAddress = macAddress_temp.replace(":", "").replace("-", "");
        }
        catch (Exception e) {
        }

        return macAddress;
    }

    public static String getNetworkOperator(Context context) {
        TelephonyManager telephoneyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //		LogUtil.log("----- getNetworkOperator() - return : [" + telephoneyManager.getNetworkOperator() + "]");
        return telephoneyManager.getNetworkOperator();
    }

    public static String getNetworkOperatorName(Context context) {
        TelephonyManager telephoneyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //		LogUtil.log("----- getNetworkOperatorName() - return : [" + telephoneyManager.getNetworkOperatorName() + "]");
        return telephoneyManager.getNetworkOperatorName();
    }

    public static String getBuildDevice() {
        return Build.DEVICE;
    }

    public static String getBuildModel() {
        return Build.MODEL;
    }

    public static String getBuildId() {
        return Build.ID;
    }

    public static String getBuildVersionSdk() {
        return Build.ID;
    }

    public static String getBuildVersionCodeName() {
        return Build.ID;
    }

    public static String getBuildVersionRelease() {
        return Build.VERSION.RELEASE;
    }

    public static int getScreenOffTimeout(Context context) {
        return Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 0);
    }

    public static long getLockScreenTimeout(Context context) {
        return Settings.Secure.getLong(context.getContentResolver(), "lock_screen_lock_after_timeout", 0);
    }

    public static boolean getKeyguardLockScreenIsShowing(Context context) {
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        return keyguardManager.inKeyguardRestrictedInputMode();
    }

    @SuppressWarnings("deprecation")
    public static boolean getIsScreenLocked(Context context) {
        boolean flag = false;

        try {
            flag = Settings.Secure.getInt(context.getContentResolver(), Settings.System.LOCK_PATTERN_ENABLED) == 1;
        }
        catch (SettingNotFoundException e) {
            e.printStackTrace();
        }

        return flag;
    }

    public static boolean isInstalledApp(Context context, String packageName) {
        List<ApplicationInfo> appList = context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo appInfo : appList) {
            if (appInfo.packageName.equals(packageName)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isScreenOn(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return powerManager.isScreenOn();
    }

    public static boolean isLandscapeOrientation(Context context) {
        return context.getResources().getConfiguration().orientation == Surface.ROTATION_0 ||
               context.getResources().getConfiguration().orientation == Surface.ROTATION_180;
    }

    public static boolean isNetworkConnected(Context context) {
        return isWifiConnected(context) || isMobileConnected(context);
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        boolean isWifiConnected = ni.isConnected();
        return isWifiConnected;
    }

    public static boolean isMobileConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        boolean isMobileConnected = ni.isConnected();
        return ni.isConnected();
    }

    public static boolean isWifiConnectedOrConnecting(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return ni.isConnectedOrConnecting();
    }

    public static boolean isMobileConnectedOrConnecting(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return ni.isConnectedOrConnecting();
    }

    public static boolean isRunningProcess(Context context) {
        boolean isRunningPackage = false;

        if (context == null || context.getPackageName() == null) {
            return isRunningPackage;
        }

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningProcessList = activityManager.getRunningAppProcesses();

        for (RunningAppProcessInfo runningApp : runningProcessList) {
            if (context.getPackageName().equals(runningApp.processName)) {
                isRunningPackage = true;
                break;
            }
        }

        return isRunningPackage;
    }

    public static int getRunningActivityCount(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTaskList = activityManager.getRunningTasks(1);

        int count = 0;

        for (Iterator<RunningTaskInfo> iterator = runningTaskList.iterator(); iterator.hasNext(); ) {
            RunningTaskInfo info = (RunningTaskInfo) iterator.next();
            if (info.baseActivity.getPackageName().equals(context.getPackageName())) {
                count = info.numActivities;
            }
        }

        return count;
    }

    public static String getTopActivityName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTaskList = activityManager.getRunningTasks(1);

        String topActivityName = null;

        for (Iterator<RunningTaskInfo> iterator = runningTaskList.iterator(); iterator.hasNext(); ) {
            RunningTaskInfo info = (RunningTaskInfo) iterator.next();

            if (info.topActivity.getPackageName().equals(context.getPackageName())) {
                topActivityName = info.topActivity.getClassName();
                break;
            }
        }

        return topActivityName;
    }

    public static String getTopActivityName(Context context, int searchCount, String exceptActivityName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTaskList = activityManager.getRunningTasks(searchCount);

        String topActivityName = null;

        for (Iterator<RunningTaskInfo> iterator = runningTaskList.iterator(); iterator.hasNext(); ) {
            RunningTaskInfo info = (RunningTaskInfo) iterator.next();

            if (info.topActivity.getPackageName().equals(context.getPackageName()) && !info.topActivity.getClassName().contains(exceptActivityName)) {
                topActivityName = info.topActivity.getClassName();
                break;
            }
        }

        return topActivityName;
    }

    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTaskList = activityManager.getRunningTasks(1);

        if (runningTaskList == null || runningTaskList.size() == 0) {
            return false;
        }

        final String packageName = context.getPackageName();

        if (packageName.equals(runningTaskList.get(0).topActivity.getPackageName())) {
            return true;
        }
        else {
            return false;
        }
    }
}
