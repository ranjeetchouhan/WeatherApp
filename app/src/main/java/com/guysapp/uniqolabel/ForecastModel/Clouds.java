package com.guysapp.uniqolabel.ForecastModel;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Clouds implements Serializable {

	@SerializedName("all")
	private int all;

	public void setAll(int all){
		this.all = all;
	}

	public int getAll(){
		return all;
	}
}