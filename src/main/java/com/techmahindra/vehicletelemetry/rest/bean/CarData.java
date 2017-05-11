package com.techmahindra.vehicletelemetry.rest.bean;

public class CarData {

	private String city;
	private String model;
	
	private String count;
	
	private String aggressiveDriversCount;
	private String fuelEffDriversCount;
	private String carCount;
	private String alertCount;
	private String averageSpeed;
	private String averageEngTemp;
	private String averageEngOil;
	
	
	
	public CarData() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CarData(String city, String model, String aggressiveDriversCount) {
		super();
		this.city = city;
		this.model = model;
		this.aggressiveDriversCount = aggressiveDriversCount;
	}
	
	public CarData(String city, String model, String averageSpeed,String averageEngTemp,String averageEngOil) {
		super();
		this.city = city;
		this.model = model;
		this.averageSpeed = averageSpeed;
		this.averageEngTemp = averageEngTemp;
		this.averageEngOil = averageEngOil;
	}
	
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getAggressiveDriversCount() {
		return aggressiveDriversCount;
	}
	public void setAggressiveDriversCount(String aggressiveDriversCount) {
		this.aggressiveDriversCount = aggressiveDriversCount;
	}
	public String getFuelEffDriversCount() {
		return fuelEffDriversCount;
	}
	public void setFuelEffDriversCount(String fuelEffDriversCount) {
		this.fuelEffDriversCount = fuelEffDriversCount;
	}
	public String getCarCount() {
		return carCount;
	}
	public void setCarCount(String carCount) {
		this.carCount = carCount;
	}
	public String getAlertCount() {
		return alertCount;
	}
	public void setAlertCount(String alertCount) {
		this.alertCount = alertCount;
	}
	public String getAverageSpeed() {
		return averageSpeed;
	}
	public void setAverageSpeed(String averageSpeed) {
		this.averageSpeed = averageSpeed;
	}
	public String getAverageEngTemp() {
		return averageEngTemp;
	}
	public void setAverageEngTemp(String averageEngTemp) {
		this.averageEngTemp = averageEngTemp;
	}
	public String getAverageEngOil() {
		return averageEngOil;
	}
	public void setAverageEngOil(String averageEngOil) {
		this.averageEngOil = averageEngOil;
	}
	
	
		
	}
