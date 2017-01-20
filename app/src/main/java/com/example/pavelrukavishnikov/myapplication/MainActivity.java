package com.example.pavelrukavishnikov.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;


public class MainActivity extends AppCompatActivity {

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            String action=intent.getAction();

            if(action.equals(MyIntentService.ACTION_GET_VALUE)) {
                if (intent.getBooleanExtra(MyIntentService.VALID, false)) {
                    updateInfo(new Currency(
                            intent.getStringExtra(MyIntentService.RESULT_VALUE),
                            intent.getStringExtra(MyIntentService.TO_TYPE),
                            intent.getStringExtra(MyIntentService.FROM_TYPE),
                            intent.getStringExtra(MyIntentService.FROM_VALUE)
                    ));
                } else {
                    lolToast();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button convertButton = (Button) findViewById(R.id.button1);
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText mEdit = (EditText)findViewById(R.id.editText3);
                String fromValue = mEdit.getText().toString();

                Intent intent = new Intent(MainActivity.this, MyIntentService.class);
                intent.setAction(MyIntentService.ACTION_NEED_FETCH);
                intent.putExtra(MyIntentService.FROM_VALUE, fromValue);
                startService(intent);
            }
        });
        Button settingButton = (Button) findViewById(R.id.button2);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSetting();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        final IntentFilter filter = new IntentFilter();
        filter.addAction(MyIntentService.ACTION_GET_VALUE);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
        updateTypeCurrency();
    }

    private void startSetting() {
        startActivity(new Intent(this, SecondActivity.class));
    }
    private void lolToast() {
        Toast.makeText(this, "Not valid currency", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume(){
        super.onResume();
        final IntentFilter filter = new IntentFilter();
        filter.addAction(MyIntentService.ACTION_GET_VALUE);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
        Log.d("Resume", " ");
        updateTypeCurrency();
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.d("onPause", "A");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }
    private void updateTypeCurrency() {
        SharedPreferences myPrefs = getApplicationContext().getSharedPreferences("app", 0);
        String toType = myPrefs.getString("toType", "EUR");
        String fromType = myPrefs.getString("fromType", "RUB");
        Log.d("lol", "choosed toType " + toType + " fromType " + fromType);
        TextView fromEdit = (TextView)findViewById(R.id.textView2);
        TextView toEdit = (TextView)findViewById(R.id.textView);
        fromEdit.setText(fromType);
        toEdit.setText(toType);
    }

    private void updateInfo(Currency cur) {
        EditText result = (EditText)findViewById(R.id.editText4);
        TextView fromType = (TextView)findViewById(R.id.textView2);
        TextView toType = (TextView)findViewById(R.id.textView);

        result.setText(cur.result);
        fromType.setText(cur.fromType);
        toType.setText(cur.toType);

        Log.d("lal", "update");
        Toast.makeText(this, "Currency updated", Toast.LENGTH_SHORT).show();
    }
}
