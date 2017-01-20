package com.example.pavelrukavishnikov.myapplication;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyIntentService extends IntentService {

    private static final String PREFIX = "com.example.pavelrukavishnikov.myapplication.";
    private static final String ACTION_PREFIX = "action.";
    private static final String EXTRA_PREFIX = "extra.";

    public static final String ACTION_NEED_FETCH = PREFIX + ACTION_PREFIX + "ACTION_NEED_FETCH";

    public static final String ACTION_GET_VALUE = PREFIX + ACTION_PREFIX + "action.ACTION_GET_VALUE";

    public static final String FROM_VALUE = PREFIX + EXTRA_PREFIX + "FROM_VALUE";
    public static final String RESULT_VALUE = PREFIX + EXTRA_PREFIX + "RESULT_VALUE";
    public static final String TO_TYPE = PREFIX + EXTRA_PREFIX + "TO_TYPE";
    public static final String FROM_TYPE = PREFIX + EXTRA_PREFIX + "FROM_TYPE";
    public static final String VALID = PREFIX + EXTRA_PREFIX + "VALID";


    public MyIntentService() {
        super("MyIntentService");
    }

    final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_NEED_FETCH.equals(action)) {
                final String from_value = intent.getStringExtra(FROM_VALUE);
                handleActionNeedFetch(from_value);
            }
        }
    }

    private void handleActionNeedFetch(String from_value) {
        Log.d("from_value", from_value);
        SharedPreferences myPrefs = getApplicationContext().getSharedPreferences("app", 0);
        String toType = myPrefs.getString("toType", "EUR");
        String fromType = myPrefs.getString("fromType", "RUB");
        convertFoo(from_value, toType, fromType);
    }

    private void receiveResult(Currency value) {
        final Intent outIntent = new Intent(ACTION_GET_VALUE);
        outIntent.putExtra(RESULT_VALUE, value.result);
        outIntent.putExtra(TO_TYPE, value.toType);
        outIntent.putExtra(FROM_TYPE, value.fromType);
        outIntent.putExtra(FROM_VALUE, value.fromValue);
        outIntent.putExtra(VALID, value.valid);
        LocalBroadcastManager.getInstance(this).sendBroadcast(outIntent);
    }

    private void convertFoo(String value, String toType, String fromType) {
        RequestBody formBody = new FormBody.Builder()
                .add("from-type", fromType)
                .add("from-value", value)
                .add("to-type", toType)
                .build();

        Request request = new Request.Builder()
                .url("https://community-neutrino-currency-conversion.p.mashape.com/convert")
                .addHeader("X-Mashape-Key", "YMPi21OEXlmsh0jfJu7gaOFKJIikp1uYy7PjsnEK1JbZkXLHsi")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept", "application/json")
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            Currency res = new Currency();
                try {
                    String jsonData = response.body().string();
                    if (response.isSuccessful()) {
                        res = parseResponse(jsonData);
                    } else {
                        throw new IOException("Unexpected code " + response);
                    }

                } catch (IOException e) {
                    Log.d("lal", "Exception caught : ", e);
                } catch (JSONException e) {
                    Log.d("lal", "Exception caught : ", e);
                }
                receiveResult(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Currency parseResponse(String jsonData) throws JSONException {
        JSONObject json = new JSONObject(jsonData);
        Currency res = new Currency();
        res.valid = Boolean.parseBoolean(json.getString("valid"));
        if (res.valid) {
            res.result = json.getString("result");
            res.fromType = json.getString("from-type");
            res.fromValue = json.getString("from-value");
            res.toType = json.getString("to-type");
        }
        return res;
    }
}