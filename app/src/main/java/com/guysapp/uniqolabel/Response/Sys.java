package com.guysapp.uniqolabel.Response;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Sys implements Serializable {

	@SerializedName("country")
	private String country;

	@SerializedName("sunrise")
	private int sunrise;

	@SerializedName("sunset")
	private int sunset;

	@SerializedName("id")
	private int id;

	@SerializedName("type")
	private int type;

	@SerializedName("message")
	private double message;

	public String getCountry(){
		return country;
	}

	public int getSunrise(){
		return sunrise;
	}

	public int getSunset(){
		return sunset;
	}

	public int getId(){
		return id;
	}

	public int getType(){
		return type;
	}

	public double getMessage(){
		return message;
	}
}