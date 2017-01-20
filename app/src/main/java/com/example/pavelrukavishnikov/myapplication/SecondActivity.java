package com.example.pavelrukavishnikov.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * Created by pavelrukavishnikov on 19.01.17.
 */

public class SecondActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES_FROM = "fromType";
    public static final String APP_PREFERENCES_TO = "toType";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        final Button convertButton = (Button) findViewById(R.id.button);
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSettings();
            }
        });
    }
    void saveSettings() {
        final EditText fromType = (EditText)findViewById(R.id.editText2);
        final EditText toType = (EditText)findViewById(R.id.editText);
        String fromValue = fromType.getText().toString();
        String toValue = toType.getText().toString();

        Log.d("AAAA", "fromValue: " + fromValue + " toValue: " + toValue);

        SharedPreferences myPrefs = getApplicationContext().getSharedPreferences("app", 0);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putString(APP_PREFERENCES_FROM, fromValue);
        prefsEditor.putString(APP_PREFERENCES_TO, toValue);
        prefsEditor.commit();

        Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume(){
        super.onResume();
        updateTypeCurrency();
    }
    private void updateTypeCurrency() {
        SharedPreferences myPrefs = getApplicationContext().getSharedPreferences("app", 0);
        String toType = myPrefs.getString("toType", "EUR");
        String fromType = myPrefs.getString("fromType", "RUB");
        Log.d("lol", "choosed toType " + toType + " fromType " + fromType);
        EditText fromEdit = (EditText)findViewById(R.id.editText2);
        EditText toEdit = (EditText)findViewById(R.id.editText);
        fromEdit.setText(fromType);
        toEdit.setText(toType);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateTypeCurrency();
    }
}