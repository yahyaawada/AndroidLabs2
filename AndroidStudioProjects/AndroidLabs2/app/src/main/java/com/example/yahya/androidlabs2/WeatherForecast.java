package com.example.yahya.androidlabs2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherForecast extends Activity {

    ProgressBar progress;
    ImageView imageView;
    TextView currentTemp;
    TextView minTemp;
    TextView maxTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        progress = (ProgressBar)findViewById(R.id.progressBar);
        progress.setVisibility(View.VISIBLE);

        imageView = (ImageView)findViewById(R.id.imageView2);

        currentTemp = (TextView)findViewById(R.id.textView3);
        minTemp = (TextView)findViewById(R.id.textView4);
        maxTemp = (TextView)findViewById(R.id.textView5);

        ForecastQuery forcast = new ForecastQuery();
        forcast.execute("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");
    }



    public class ForecastQuery extends AsyncTask<String, Integer, String>{
        String min, max, current, iconName;
        Bitmap image;

        @Override
        protected String doInBackground(String...args){

            int i = 0;
            InputStream is;

            try{
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();

                is = conn.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser parser = factory.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(is, null);


                int event = parser.getEventType();
                while (event != XmlPullParser.END_DOCUMENT)  {
                    String name=parser.getName();
                    switch (event){
                        case XmlPullParser.START_TAG:
                            if(name.equals("temperature")){
                                current=parser.getAttributeValue(null,"value");
                                publishProgress(25,50,75);
                                min=parser.getAttributeValue(null,"min");
                                publishProgress(25,50,75);
                                max=parser.getAttributeValue(null,"max");
                                publishProgress(25,50,75);
                            }
                            if(name.equals("weather")){
                                iconName=parser.getAttributeValue(null,"icon");
                                publishProgress(25,50,75);
                            }
                            break;

                        case XmlPullParser.END_TAG:
                            name=parser.getName();
                            Log.i("End tag", name);
                            break;
                    }
                    event = parser.next();
                }

            } catch (Exception e) {
                    e.printStackTrace();
            }


            if(fileExistance(iconName+ ".png")){
                FileInputStream fis = null;
                try {    fis = openFileInput(iconName+ ".png");   }
                catch (FileNotFoundException e) {    e.printStackTrace();  }
                image = BitmapFactory.decodeStream(fis);
                Log.i("WeatherForecast","Looking for "+iconName+ ".png image");
                Log.i("Weather Forecast","Image found locally");
            }else{
                URL ImageURL = null;
                try {
                    ImageURL = new URL("http://openweathermap.org/img/w/" + iconName+ ".png image");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                image  = getImage(ImageURL);
                FileOutputStream outputStream = null;
                try {
                    outputStream = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                Log.i("WeatherForecast","Looking for "+iconName+" image");
                Log.i("Weather Forecast","Image needs to be downloaded");
                try {
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            publishProgress(100);


            return "Go to OnPostExecute";
        }

        public void onProgressUpdate(Integer...value){
            progress.setVisibility(View.VISIBLE);
            progress.setProgress(value[0]);
        }

        public void onPostExecute(String s ) {
            currentTemp.setText(current);
            minTemp.setText(min);
            maxTemp.setText(max);
            imageView.setImageBitmap(image);
            progress.setVisibility(View.INVISIBLE);
        }

        public Bitmap getImage(URL url) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } else
                    return null;
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

    }
}
