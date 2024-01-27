package com.example.weatherapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ForecastActivity extends AppCompatActivity {

    private RequestQueue queue;
    private double temperature=0;
    EditText editCityText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        Intent intent = getIntent();
        String City = intent.getStringExtra("City_Name");

        TextView forecastHeader = findViewById(R.id.forecastTextView);

        if(City!= null){
            forecastHeader.setText(City);
        }
        else{
            forecastHeader.setText("Location_is_not_found");
        }
        queue = Volley.newRequestQueue(this);

        TextView textView2 = findViewById(R.id.textView2);
        textView2.setText("" + temperature + "°C");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState ){
        super.onSaveInstanceState(outState);
        outState.putDouble("TEMPERATURE",temperature);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        temperature = savedInstanceState.getDouble("TEMPERATURE", 0);
        TextView textView2 = findViewById(R.id.textView2);
        textView2.setText("" + temperature + "°C");
    }

    public void openMainActivity(View view) {
        Intent openMain = new Intent(this, MainActivity.class);
        startActivity(openMain);
    }

    public void otherCityTemp(View view) {

        editCityText = findViewById(R.id.editCityText);
        String cityString = String.valueOf(editCityText.getText());
        if (cityString != null){

        String url = "https://api.openweathermap.org/data/2.5/weather?q="+cityString+"&units=metric&appid=3588187a0e1748af7403db42bc9a4485";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.d("Wather_App", response);
                    parseJsonAndUpdateUI(response);
                }, error -> {
                    Log.d("Wather_App", error.toString());
        });
        queue.add(stringRequest);
        }
    }

    private void parseJsonAndUpdateUI(String response) {
        try {
            JSONObject weatherResponse = new JSONObject(response);

            temperature=weatherResponse.getJSONObject("main").getDouble("temp");

            TextView textView2 = findViewById(R.id.textView2);
            textView2.setText("" + temperature + "°C");

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}