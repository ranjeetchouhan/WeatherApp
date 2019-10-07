package com.guysapp.uniqolabel.Response;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Clouds implements Serializable {

	@SerializedName("all")
	private int all;

	public int getAll(){
		return all;
	}
}