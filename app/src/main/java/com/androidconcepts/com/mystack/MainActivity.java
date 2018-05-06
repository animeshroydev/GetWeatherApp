

package com.androidconcepts.com.mystack;

import android.app.VoiceInteractor;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    Button button;
    EditText city;
    TextView result;
    TextView resultKelvin;
    TextView humidity;
    TextView kelvin;
    Double a = (Double) 273.15;
    String baseURL = "http://api.openweathermap.org/data/2.5/weather?q=";
    String API = "&appid=96ea4e4ec9190a3efd9dc19704b6f797";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        button = (Button) findViewById(R.id.button);
        city = (EditText) findViewById(R.id.getCity);
        result = (TextView) findViewById(R.id.result);
        resultKelvin = (TextView) findViewById(R.id.resultKelvin);
kelvin = (TextView) findViewById(R.id.kelvin);
humidity = (TextView) findViewById(R.id.humidity);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myURL = baseURL + city.getText().toString() + API;
                //Log.i("URL", "URL: " + myURL);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null,
                        new Response.Listener<JSONObject>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                Log.i("JSON", "JSON: " + jsonObject);

                                try {
                                    String info = jsonObject.getString("weather");
//                                   String pressure = jsonObject.getString("main");
                                    Log.i("INFO", "INFO: "+ info);

                                    JSONArray ar = new JSONArray(info);
//                                    JSONObject ar1 = new JSONObject(pressure);

                                    for (int i = 0; i < ar.length(); i++){
                                        JSONObject parObj = ar.getJSONObject(i);

                                        String myWeather = parObj.getString("main");
//                                        String myWeather2 = parObj.getString("temp");
                                        result.setText(myWeather);
//                                        resultKelvin.setText(myWeather2);

                                        Log.i("ID", "ID: " + parObj.getString("id"));
                                        Log.i("MAIN", "MAIN: " + parObj.getString("main"));
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                try {
                                    String pressure = jsonObject.getString("main");
                                    Log.i("MAIN", "Main: " + pressure);
                                    JSONObject co = new JSONObject(pressure);
                                   Double temp = co.getDouble("temp");
                                      resultKelvin.setText(new DecimalFormat("##.##").format(temp-a)+" \u2103");
                                      kelvin.setText(new DecimalFormat("##.##").format(temp)+" \u212A");
                                      String hum = co.getString("humidity");
                                      humidity.setText("Humidity: "+hum);
                                    Log.i("LON", "LON: " + temp);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Log.i("Error", "Something went wrong" + volleyError);

                            }
                        }


                );
                MySingleton.getInstance(MainActivity.this).addToRequestQue(jsonObjectRequest);



            }
        });






    }
}
