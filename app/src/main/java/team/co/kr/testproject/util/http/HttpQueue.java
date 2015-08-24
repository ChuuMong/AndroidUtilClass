package team.co.kr.testproject.util.http;

import android.content.Context;
import android.support.v4.BuildConfig;
import android.util.Log;
import android.util.TimeUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.navercorp.volleyextensions.volleyer.builder.PostBuilder;
import com.navercorp.volleyextensions.volleyer.builder.PutBuilder;
import com.navercorp.volleyextensions.volleyer.factory.DefaultRequestQueueFactory;
import com.navercorp.volleyextensions.volleyer.http.ContentType;
import com.navercorp.volleyextensions.volleyer.multipart.Part;

import static com.navercorp.volleyextensions.volleyer.Volleyer.volleyer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import team.co.kr.testproject.util.helper.SharedPreferenceHelper;

/**
 * Created by JongHunLee on 2015-04-27.
 */
public class HttpQueue implements Response.Listener<String>, Response.ErrorListener {

    private static final int DEFAULT_CACHE_SIZE = 10 * 1024 * 1024;

    //Constants
    public static final int GET = 0;
    public static final int POST = 1;
    public static final int PUT = 2;
    public static final int DELETE = 3;

    //Request Parameters
    private final Context context;
    private final Object obj;
    private final HttpQueueListener listener;
    private final int returnCode;
    private final int methodType;
    private final String url;
    private final Map<String, String> paramsMap;
    private final Map<String, File> fileMap;

    private static RequestQueue requestQueue;

    private HttpQueue(Builder builder) {
        this.context = builder.context;
        this.obj = builder.obj;
        this.returnCode = builder.returnCode;
        this.methodType = builder.methodType;
        this.url = builder.url;
        this.paramsMap = builder.paramsMap;
        this.fileMap = builder.fileMap;
        this.listener = builder.listener;

        if (requestQueue == null) {
            initVolley();
            initVolleyer();
        }
    }

    private void initVolley() {
        requestQueue = DefaultRequestQueueFactory.create(context);
        requestQueue.start();
    }

    private void initVolleyer() {
        volleyer(requestQueue).settings().setAsDefault().done();
    }

    public void getResponseCookie() {
        StringRequest request = new StringRequest(context, methodType, url, paramsMap, this, this);
        //request.setRetryPolicy()
        requestQueue.add(request);
    }

    public static class Builder {

        private Context context;
        private int returnCode;
        private int methodType;
        private String url;
        private Map<String, String> paramsMap;
        private Map<String, File> fileMap;
        private HttpQueueListener listener;
        private Object obj;

        public Builder() {
            paramsMap = new HashMap<>();
            fileMap = new HashMap<>();
        }

        public Builder contxt(Context context) {
            this.context = context;
            return this;
        }

        public Builder setObj(Object obj) {
            this.obj = obj;
            return this;
        }

        public Builder returnCode(int returnCode) {
            this.returnCode = returnCode;
            return this;
        }

        public Builder methodType(int value) {
            methodType = value;
            return this;
        }

        public Builder url(String value) {
            url = value;
            return this;
        }

        public Builder addParameter(String key, String value) {
            paramsMap.put(key, value);
            return this;
        }

        public Builder setParameter(Map<String, String> value) {
            paramsMap.clear();
            paramsMap.putAll(value);
            return this;
        }

        public Builder addFile(String key, File value) {
            fileMap.put(key, value);
            return this;
        }

        public Builder listener(HttpQueueListener listener) {
            this.listener = listener;
            return this;
        }

        public HttpQueue build() {
            return new HttpQueue(this);
        }
    }

    public synchronized HttpQueue execute() {
        //        if (url.equals(HttpUrl.USER_JOIN) || url.equals(HttpUrl.USER_JOIN_NEW) || url.equals(HttpUrl.USER_LOGIN) || url.equals(HttpUrl.USER_LOGIN_NEW)) {
        //            getResponseCookie();
        //            return this;
        //        }

        if (methodType == GET) {
            get();
        }
        else if (methodType == POST) {
            post();
        }
        else if (methodType == PUT) {
            put();
        }
        else if (methodType == DELETE) {
            delete();
        }

        return this;
    }

    private static final String STR_ACCEPT = "Accept";
    public static final String APPLICATION_JSON = "application/json";
    private static final String COOKIE_KEY = "Cookie";
    public static final String SESSION_TAG = "_interpay_session_";

    private void get() {
        volleyer().get(url).addHeader(STR_ACCEPT, APPLICATION_JSON).addHeader(COOKIE_KEY, getCookie()).withListener(this).withErrorListener(this).execute();
    }

    private void post() {
        PostBuilder postBuilder = volleyer().post(url);
        postBuilder.addHeader(STR_ACCEPT, APPLICATION_JSON).addHeader(COOKIE_KEY, getCookie());

        if (!paramsMap.isEmpty()) {
            for (String key : paramsMap.keySet()) {
                postBuilder.addStringPart(key, paramsMap.get(key));
            }
        }

        if (!fileMap.isEmpty()) {
            for (String key : fileMap.keySet()) {
                postBuilder.addFilePart(key, fileMap.get(key));
            }
        }

        postBuilder.withListener(this).withErrorListener(this).execute();
    }

    private void put() {
        PutBuilder putBuilder = volleyer().put(url);
        putBuilder.addHeader(STR_ACCEPT, APPLICATION_JSON).addHeader(COOKIE_KEY, getCookie());

        if (!paramsMap.isEmpty()) {
            for (String key : paramsMap.keySet()) {
                putBuilder.addStringPart(key, paramsMap.get(key));
            }
        }

        if (!fileMap.isEmpty()) {
            for (String key : fileMap.keySet()) {
                putBuilder.addFilePart(key, fileMap.get(key));
            }
        }

        putBuilder.withListener(this).withErrorListener(this).execute();
    }

    private void delete() {
        volleyer().delete(url).addHeader(STR_ACCEPT, APPLICATION_JSON).addHeader(COOKIE_KEY, getCookie()).withListener(this).withErrorListener(this).execute();
    }

    private String getCookie() {
        String sessionId = SharedPreferenceHelper.getValue(context, SESSION_TAG);

        StringBuilder builder = new StringBuilder();
        builder.append(SESSION_TAG);
        builder.append("=");
        builder.append(sessionId);

        return builder.toString();
    }

    private int timeOutRetry = 0;

    @Override
    public void onErrorResponse(VolleyError error) {
        if (obj != null) {
            if (BuildConfig.DEBUG) {
                Log.d("HttpQueue", "API " + String.valueOf(returnCode) + "번 Error " + url + "(" +
                                   obj.getClass().getSimpleName() + ")" + " >> " + error.getMessage());
            }
        }
        else {
            if (BuildConfig.DEBUG) {
                Log.d("HttpQueue", "API " + String.valueOf(returnCode) + "번 Error " + url + " >> " +
                                   error.getMessage());
            }
        }

        if (error instanceof NoConnectionError) {
            if (BuildConfig.DEBUG) {
                Log.d("HttpQueue", "API " + String.valueOf(returnCode) + "번 NoConnectionError");
            }

            this.execute();
        }
        else if (error instanceof NetworkError) {
            if (BuildConfig.DEBUG) {
                Log.d("HttpQueue", "API " + String.valueOf(returnCode) + "번 NetworkError");
            }
            try {
                TimeUnit.SECONDS.sleep(2);
                this.execute();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (error instanceof TimeoutError) {
            if (BuildConfig.DEBUG) {
                Log.d("HttpQueue", "API " + String.valueOf(returnCode) + "번 TimeoutError");
            }

            if (timeOutRetry >= 0 && timeOutRetry < 4) {
                timeOutRetry++;
                this.execute();
            }
            else {
                timeOutRetry = -1;
                if (listener != null) {
                    listener.request_failed(this.returnCode, "서버와 연결 시간이 초과하였습니다(TimeoutError)");
                }
            }
        }
        else if (error instanceof AuthFailureError) {
            if (BuildConfig.DEBUG) {
                Log.d("HttpQueue", "API " + String.valueOf(returnCode) + "번 AuthFailureError");
            }

            if (listener != null) {
                listener.request_failed(this.returnCode, "인증에 실패 하였습니다(AuthFailureError)");
            }
        }
        else if (error instanceof ServerError) {
            if (BuildConfig.DEBUG) {
                Log.d("HttpQueue", "API " + String.valueOf(returnCode) + "번 ServerError");
            }

            if (listener != null) {
                listener.request_failed(this.returnCode, "서버에서 에러가 발생하였습니다.\n관리자에게 문의하세요.");
            }
        }
        else {
            if (listener != null) {
                listener.request_failed(this.returnCode, null);
            }
        }
    }

    @Override
    public void onResponse(String response) {
        if (obj != null) {
            if (BuildConfig.DEBUG) {
                Log.d("HttpQueue", "API " + String.valueOf(returnCode) + "번 " + url + "(" +
                                   obj.getClass().getSimpleName() + ") >> " + response);
            }
        }
        else {
            if (BuildConfig.DEBUG) {
                Log.d("HttpQueue", "API " + String.valueOf(returnCode) + "번 " + url + " >> " + response);
            }
        }

        if (listener != null) {
            listener.request_finished(this.returnCode, response);
        }
    }
}