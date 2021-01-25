// Bismillah Hirrahman Nirrahim

package com.toptal.controller;

import java.time.ZoneOffset;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toptal.model.Jog;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import tk.plogitech.darksky.forecast.APIKey;
import tk.plogitech.darksky.forecast.ForecastException;
import tk.plogitech.darksky.forecast.ForecastRequest;
import tk.plogitech.darksky.forecast.ForecastRequestBuilder;
import tk.plogitech.darksky.forecast.GeoCoordinates;
import tk.plogitech.darksky.forecast.model.Forecast;
import tk.plogitech.darksky.forecast.model.Latitude;
import tk.plogitech.darksky.forecast.model.Longitude;

public class WeatherController {
	String getWeather(String location) {
		
		
        try {
        	OkHttpClient client = new OkHttpClient();
        	Request request = new Request.Builder()
        	  .url("http://api.openweathermap.org/data/2.5/weather?q="+location+"&units=metric&appid=e446147610a883ebb1f3672322305bb9")
        	  .build();
        	//System.out.println(request.toString());
        	Response response = client.newCall(request).execute();
        	String jsonData = response.body().string();
            JSONObject Jobject = new JSONObject(jsonData);
            JSONArray Jarray = Jobject.getJSONArray("weather");
            JSONObject Jarray1 = Jobject.getJSONObject("main");
            //System.out.println(Jobject.toString());
            //System.out.println(Jarray1.toString());

            String temp = ((JSONObject) Jarray.get(0)).get("description") + ", " + Jarray1.get("temp") + " C";
            
            return temp;
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            return null;
        }
    }
}
