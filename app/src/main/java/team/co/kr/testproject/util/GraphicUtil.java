package team.co.kr.testproject.util;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.ByteArrayOutputStream;

public class GraphicUtil {

    private static final int STANDARD_WIDTH = 720;
    private static final int STANDARD_HEIGHT = 1280;

    public static int getDisplayWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getDisplayHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getStatusBarHeight(Context context) {
        Rect rect = new Rect();
        Window window = ((Activity) context).getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    public static int getDisplayHeightWithoutStatusBar(Context context) {
        return getDisplayHeight(context) - getStatusBarHeight(context);
    }

    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static float getDensityDpi(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    public static float getScaledDensity(Context context) {
        return context.getResources().getDisplayMetrics().scaledDensity;
    }

    public static float getDpFromPixel(Context context, int pixel) {
        return pixel / getDensity(context);
    }

    public static int getPixelFromDp(Context context, float dp) {
        context.getResources().getDisplayMetrics();
        return (int) (dp * getDensityDpi(context) / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static byte[] getByteArrayFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap getBitmapFromByteArray(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    @SuppressWarnings("deprecation")
    public static int getHorizontalSize(Context context, int width) {
        if (width == ViewGroup.LayoutParams.MATCH_PARENT || width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            return width;
        }
        else {
            return (getDisplayWidth(context) * width) / STANDARD_WIDTH;
        }
    }

    @SuppressWarnings("deprecation")
    public static int getVerticalSize(Context context, int height) {
        if (height == ViewGroup.LayoutParams.MATCH_PARENT || height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            return height;
        }
        else {
            return (getDisplayHeight(context) * height) / STANDARD_HEIGHT;
        }
    }

    public static float getFontSize(Context context, int pt) {
        return pt / getScaledDensity(context);
    }
}
