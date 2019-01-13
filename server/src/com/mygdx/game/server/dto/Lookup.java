package com.mygdx.game.server.dto;

import java.io.Serializable;

public class Lookup implements Serializable {

	private static final long serialVersionUID = 4945969020679102860L;
	private String countryName;
	private String cityName;
	private String postal;
	private String state;

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getPostal() {
		return postal;
	}

	public void setPostal(String postal) {
		this.postal = postal;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
