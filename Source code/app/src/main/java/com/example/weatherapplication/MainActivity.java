package com.example.weatherapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RequestQueue queue;
    private String description ="Click to refresh";
    private String city="Click to refresh";
    private double temperature=0;
    private double wind=0;

    private Map<String, String> weatherImages = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Create a new web request queue
        queue = Volley.newRequestQueue(this);
        // Ready to sent request with the queue
        // Write the values on the UI
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(description);
        TextView temperatureTextView = findViewById(R.id.temperatureTextView);
        temperatureTextView.setText("" + temperature + "°C");
        TextView windTextView = findViewById(R.id.windTextView);
        windTextView.setText("" + wind + "m/s");
        TextView cityTextView = findViewById(R.id.cityTextView);
        cityTextView.setText(city);
        addWeatherImages();
        new DownloadImageTask((ImageView) findViewById(R.id.imageView2))
                .execute(weatherImages.get("Default"));
    }
    // Activity lifecycle method

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("start", "start");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState ){
        super.onSaveInstanceState(outState);
        outState.putString("CITY_NAME", city);
        outState.putString("WEATHER_DESCRIPTION",description);
        outState.putDouble("TEMPERATURE",temperature);
        outState.putDouble("WIND",wind);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
       super.onRestoreInstanceState(savedInstanceState);


       city = savedInstanceState.getString("CITY_NAME");
       if (city == null) {
           city = "Click to refresh";
       }
       description = savedInstanceState.getString("WEATHER_DESCRIPTION");
       if (description == null) {
           description = "Click to refresh";
       }
       temperature = savedInstanceState.getDouble("TEMPERATURE", 0);
       wind = savedInstanceState.getDouble("WIND", 0);

        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(description);
        TextView temperatureTextView = findViewById(R.id.temperatureTextView);
        temperatureTextView.setText("" + temperature + "°C");
        TextView windTextView = findViewById(R.id.windTextView);
        windTextView.setText("" + wind + "m/s");
        TextView cityTextView = findViewById(R.id.cityTextView);
        cityTextView.setText(city);

    }

    public void fetchWeatherData(View view){
        String url = "https://api.openweathermap.org/data/2.5/weather?q=tampere&units=metric&appid=3588187a0e1748af7403db42bc9a4485";

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

    private void parseJsonAndUpdateUI(String response) {
        try {
            JSONObject weatherResponse = new JSONObject(response);

            description=weatherResponse.getJSONArray("weather").getJSONObject(0).getString("description");
            temperature=weatherResponse.getJSONObject("main").getDouble("temp");
            wind=weatherResponse.getJSONObject("wind").getDouble("speed");
            city=weatherResponse.getString("name");
            TextView descriptionTextView = findViewById(R.id.descriptionTextView);
            descriptionTextView.setText(description);
            TextView temperatureTextView = findViewById(R.id.temperatureTextView);
            temperatureTextView.setText("" + temperature + "°C");
            TextView windTextView = findViewById(R.id.windTextView);
            windTextView.setText("" + wind + "m/s");
            TextView cityTextView = findViewById(R.id.cityTextView);
            cityTextView.setText(city);
            setWeatherImage(temperature);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void openForecastActivity(View view) {
        Intent openForecast= new Intent(this ,ForecastActivity.class);
        openForecast.putExtra("City_Name", "Enter City Name");
        startActivity(openForecast);
    }

    public void openWebPage (View view){
        // open www.tuni.fi
        String urlString = "https://www.tuni.fi";
        Uri uri = Uri.parse(urlString);
        //create the intent
        Intent openWebPage = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity (openWebPage);
        }
        catch (ActivityNotFoundException e){

        }
    }

    public void setTimer(View view){
        Intent intent = new Intent(AlarmClock.ACTION_SET_TIMER)
                .putExtra(AlarmClock.EXTRA_MESSAGE, "Timeout")
                .putExtra(AlarmClock.EXTRA_LENGTH, 20)
                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
       try{
            startActivity(intent);
        }
       catch ( ActivityNotFoundException e){

       }
    }

    public void showMap(View view) {
        Uri mapUri = Uri.parse("geo:23.8,61.5?q=" + Uri.encode("Tampere"));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        //startActivity(mapIntent);
        try {
            startActivity(mapIntent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
     private void addWeatherImages(){
         weatherImages.put("Normal", "https://s7d2.scene7.com/is/image/TWCNews/Mostly_Sunny_Cirrus_Orlando_FL_112822_UGC_KarenLary");
         weatherImages.put("Hot", "https://wallpaperaccess.com/full/1779003.jpg");
         weatherImages.put("Cold", "https://www.visitrovaniemi.fi/wp-content/uploads/visit-rovaniemi-when-to-go-ice-break-700x450.jpg");
         weatherImages.put("Default", "https://th.bing.com/th/id/OIP.ueV4jMS9VqVehmZ8gaH4XQHaHI?w=209&h=202&c=7&r=0&o=5&dpr=3&pid=1.7");
     }

    private void setWeatherImage(double temperature){
        if(temperature > 25){
            new DownloadImageTask((ImageView) findViewById(R.id.imageView2))
                    .execute(weatherImages.get("Hot"));
        } else if (temperature > 9 && temperature < 26) {
            new DownloadImageTask((ImageView) findViewById(R.id.imageView2))
                    .execute(weatherImages.get("Normal"));
        }
        else{
            new DownloadImageTask((ImageView) findViewById(R.id.imageView2))
                    .execute(weatherImages.get("Cold"));
        }
    }
}

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }


}