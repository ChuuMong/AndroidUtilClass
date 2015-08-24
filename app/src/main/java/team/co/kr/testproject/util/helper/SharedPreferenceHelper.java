package team.co.kr.testproject.util.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceHelper {

    public static void setValue(final Context context, final String key, final String value) {
        if (value == null) {
            return;
        }
        if ("null".equals(value)) {
            return;
        }

        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor ed = sharedPreferences.edit();
            ed.putString(key, value).apply();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getValue(final Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static int getInt(final Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, 0);
    }

    public static boolean getBoolean(final Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public static void removeValue(final Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.remove(key);
        ed.apply();
    }

    public static void allRemove(final Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.clear();
        ed.apply();
    }
}
