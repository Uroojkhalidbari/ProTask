package com.uktechians.protask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.uktechians.protask.model.Applicable;
import com.uktechians.protask.model.MainObject;

import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView tv_dummy;
    Button bt_fetch, bt_find;
    ProgressBar pb_loader;
    EditText et_query;
    CardView cv_main;
    MainObject dataObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        tv_dummy = findViewById(R.id.tv);
        bt_fetch = findViewById(R.id.fetch);
        bt_find = findViewById(R.id.find);
        pb_loader = findViewById(R.id.pb);
        et_query = findViewById(R.id.edit_query);
        cv_main = findViewById(R.id.cv);

        bt_fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInternetUseable()) {
                    syncData();
                } else {
                    Toast.makeText(MainActivity.this, "Internet not Connected!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bt_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_query.getText().length() > 0) {
                    String data = et_query.getText().toString();
                    Applicable applicable = null;
                    if (dataObject != null) {
                        for (Applicable applicable1 : dataObject.getNetworks().getApplicable()) {
                            if (data.equalsIgnoreCase(applicable1.getCode())) {
                                applicable = applicable1;
                                break;
                            }
                        }
                        if (applicable != null) {
                            Log.d("Tag", applicable.toString());
                            String dataStr = new Gson().toJson(applicable);
                            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                            intent.putExtra("dataClass", dataStr);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "Item not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    private void syncData() {
        tv_dummy.setVisibility(View.GONE);
        RequestURL requestURL = new RequestURL();
        requestURL.execute();
    }

    class RequestURL extends AsyncTask<String, Void, MainObject> {
        String urlStr = "https://raw.githubusercontent.com/optile/checkout-android/develop/shared-test/lists/listresult.json";
        boolean isSuccessfullyUpdate = false;

        @Override
        protected void onPreExecute() {
            pb_loader.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected MainObject doInBackground(String... strings) {

            try {
                URL url = new URL(urlStr);
                StringBuilder stringBuffer = new StringBuilder();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.d("URL Request", connection.toString());
                } else {
                    InputStream inputStream = connection.getInputStream();
                    int i = 0;
                    while ((i = inputStream.read()) != -1) {
                        stringBuffer.append((char) i);
                    }
                    dataObject = new Gson().fromJson(stringBuffer.toString(), MainObject.class);
                    Log.d("URL Request", dataObject.toString());
                    isSuccessfullyUpdate = true;
                }
                connection.disconnect();
            } catch (Exception e) {
                Log.d("Tag", e.getLocalizedMessage());
            }
            return dataObject;
        }

        @Override
        protected void onPostExecute(MainObject s) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isSuccessfullyUpdate) {
                        tv_dummy.setVisibility(View.GONE);
                        cv_main.setVisibility(View.VISIBLE);
                    } else {
                        cv_main.setVisibility(View.GONE);
                        tv_dummy.setVisibility(View.VISIBLE);
                    }
                    pb_loader.setVisibility(View.GONE);
                }
            }, 1000);
            super.onPostExecute(s);
        }
    }

    public boolean isInternetUseable() {
        boolean status = false;
        try {
            ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager != null) {
                status = (manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isAvailable());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return status;
    }
}