package com.reckonix;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import mssoni.reckonix.R;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText etUrl;
    private Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getSupportActionBar().hide();

        etUrl = findViewById(R.id.activity_setting_etUrl);
        btnDone = findViewById(R.id.activity_setting_btnDone);

        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            etUrl.setText(extras.getString(Constants.KEY_URL));
            etUrl.setSelection(etUrl.getText().length());
        }

        btnDone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_setting_btnDone:
                if (!etUrl.getText().toString().isEmpty()) {

                    //Saving URL into shared preference.
                    final String url = etUrl.getText().toString().trim();
                    SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString(Constants.MY_PREFS_KEY_URL, url);
                    editor.apply();

                    final Intent resultIntent = new Intent();
                    resultIntent.putExtra(Constants.KEY_URL, etUrl.getText().toString().trim());
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();


                }

        }
    }
}
