package team.co.kr.testproject.util.http;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import interpolar.co.kr.android.helper.SharedPreferenceHelper;
import interpolar.co.kr.tallyby.BuildConfig;

/**
 * Created by JongHunLee on 2015-04-27.
 */
public class StringRequest extends com.android.volley.toolbox.StringRequest {

    private static final String TAG = StringRequest.class.getSimpleName();
    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    public static final String SESSION_TAG = "_interpay_session_";

    private final Context context;
    private final Map<String, String> params;

    public StringRequest(Context context, int method, String url, Map<String, String> params, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.context = context;
        this.params = params;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        checkSessionCookie(response.headers);

        return super.parseNetworkResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();

        if (headers == null || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<>();
        }

        this.addSessionCookie(headers);

        return headers;
    }

    private void checkSessionCookie(Map<String, String> headers) {
        if (headers.containsKey(SET_COOKIE_KEY) && headers.get(SET_COOKIE_KEY).startsWith(SESSION_TAG)) {
            String cookie = headers.get(SET_COOKIE_KEY);
            if (cookie.length() > 0) {
                String[] splitCookie = cookie.split(";");
                String[] splitSessionId = splitCookie[0].split("=");
                cookie = splitSessionId[1];

                SharedPreferenceHelper.setValue(SESSION_TAG, cookie);
            }
        }
    }

    public final void addSessionCookie(Map<String, String> headers) {
        String sessionId = SharedPreferenceHelper.getValue(SESSION_TAG);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, sessionId);
        }

        if (sessionId.length() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(SESSION_TAG);
            builder.append("=");
            builder.append(sessionId);
            if (headers.containsKey(COOKIE_KEY)) {
                builder.append("; ");
                builder.append(headers.get(COOKIE_KEY));
            }
            headers.put(COOKIE_KEY, builder.toString());
        }
    }
}
