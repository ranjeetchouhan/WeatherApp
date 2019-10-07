package com.guysapp.uniqolabel.Response;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Wind implements Serializable {

	@SerializedName("deg")
	private double deg;

	@SerializedName("speed")
	private double speed;

	public double getDeg(){
		return deg;
	}

	public double getSpeed(){
		return speed;
	}
}