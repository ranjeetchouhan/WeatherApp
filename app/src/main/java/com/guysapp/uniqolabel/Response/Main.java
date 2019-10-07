package com.guysapp.uniqolabel.Response;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Main implements Serializable {

	@SerializedName("temp")
	private double temp;

	@SerializedName("temp_min")
	private double tempMin;

	@SerializedName("humidity")
	private int humidity;

	@SerializedName("pressure")
	private double pressure;

	@SerializedName("temp_max")
	private double tempMax;

	public double getTemp(){
		return temp;
	}

	public double getTempMin(){
		return tempMin;
	}

	public int getHumidity(){
		return humidity;
	}

	public double getPressure(){
		return pressure;
	}

	public double getTempMax(){
		return tempMax;
	}
}