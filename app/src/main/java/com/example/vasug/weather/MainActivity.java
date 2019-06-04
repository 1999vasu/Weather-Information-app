package com.example.vasug.weather;

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
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView resultText;

    public class DownloadTask extends AsyncTask<String , Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection connection;
            StringBuilder result = new StringBuilder();
            try {
                url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                {
                    while (data != -1) {
                        result.append((char) data);
                        data = reader.read();
                    }
                }

                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();

                Toast.makeText(getApplicationContext(),"Could not find weather", Toast.LENGTH_LONG).show();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject json = new JSONObject(s);
                String weatherinfo = json.getString("weather");
                Log.i("Weather content", weatherinfo);
                JSONArray arr = new JSONArray(weatherinfo);
                String message = "";
                for (int i = 0; i < arr.length(); ++i) {
                    JSONObject jsonpart = arr.getJSONObject(i);
                    String main = jsonpart.getString("main");
                    String description = jsonpart.getString("description");

                    if(!main.equals("")&&!description.equals("")){
                        message += main +": "+ description;
                    }

                }

                if(!message.equals("")){
                    resultText.setText(message);
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Could not find weather", Toast.LENGTH_LONG).show();
            }
            Log.i("JSON", s);
        }
    }

    public void ButtonClicked(View view){
        Log.i("Button Clicked","Getting Information");
        try {
            String encodedcityname = URLEncoder.encode(editText.getText().toString(), "UTF-8");
            DownloadTask task = new DownloadTask();
            task.execute("https://openweathermap.org/data/2.5/weather?q=" + encodedcityname + "&appid=b6907d289e10d714a6e88b30761fae22");

            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(editText.getWindowToken(), 0);

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Could not find weather", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        resultText = findViewById(R.id.textView3);


    }
}
