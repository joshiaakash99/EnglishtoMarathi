package com.example.weatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
n
public class MainActivity extends AppCompatActivity {

    EditText editText ;
    TextView textView;
    public class Downloadtasks extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection httpURLConnection;
            String result = "";
            try {
                url = new URL(strings[0]);
                httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                InputStream inputStream= httpURLConnection.getInputStream();
                InputStreamReader reader= new InputStreamReader(inputStream);
                int data = reader.read();
                 while(data!=-1)
                 {
                     char current = (char) data;
                     result += current;
                     data = reader.read();
                 }
                 return result;
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Could find the weather", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                Log.i("weather info is",weatherInfo);
                JSONArray jsonArray = new JSONArray(weatherInfo);
                JSONObject weatherInfoObject = new JSONObject();
                for(int i =0; i< jsonArray.length();i++) {
                    weatherInfoObject = jsonArray.getJSONObject(i);
                    Log.i("main",weatherInfoObject.getString("main"));
                    Log.i("description",weatherInfoObject.getString("description"));


                }
                String main = weatherInfoObject.getString("main");
                String decsription = weatherInfoObject.getString("description");
                textView.setText("Main:"+main+"\nDescripton:"+decsription);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Could find the weather", Toast.LENGTH_SHORT).show();
            }


        }
    }


    public void getWeather(View view)
    {
        String cityName= editText.getText().toString();
        Downloadtasks downloadtasks= new Downloadtasks();
        String results = "";
        try
        {
            results = downloadtasks.execute("https://openweathermap.org/data/2.5/weather?q="+cityName+"&appid=b6907d289e10d714a6e88b30761fae22").get();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Could find the weather", Toast.LENGTH_SHORT).show();
        }
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(editText.getWindowToken(),0);
        Log.i("info","button tapped");


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      editText =(EditText) findViewById(R.id.cityEditText);
        textView = (TextView) findViewById(R.id.infoTextView);
}
}
