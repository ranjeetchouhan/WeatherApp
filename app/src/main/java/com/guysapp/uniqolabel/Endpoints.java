package com.guysapp.uniqolabel;

import com.guysapp.uniqolabel.ForecastModel.ForecastResponse;
import com.guysapp.uniqolabel.Response.Response;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface Endpoints {


    @GET("weather")
    Call<Response> getWeatherResponse(@QueryMap Map<String,String> options);


    @GET("forecast")
    Call<ForecastResponse> getForecastResponse(@QueryMap Map<String,String> options);
}
