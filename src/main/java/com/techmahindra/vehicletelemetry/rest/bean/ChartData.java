package com.techmahindra.vehicletelemetry.rest.bean;

public class ChartData {

	
	private Integer jan;
	private Integer feb;
	private Integer mar;
	private Integer apr;
	private Integer may;
	private Integer jun;
	private Integer jul;
	
	
	public ChartData(Integer jan, Integer feb, Integer mar, Integer apr,
			Integer may, Integer jun, Integer juL) {
		super();
		this.jan = jan;
		this.feb = feb;
		this.mar = mar;
		this.apr = apr;
		this.may = may;
		this.jun = jun;
		this.jul = juL;
	}
	
	public Integer getJan() {
		return jan;
	}
	public void setJan(Integer jan) {
		this.jan = jan;
	}
	public Integer getFeb() {
		return feb;
	}
	public void setFeb(Integer feb) {
		this.feb = feb;
	}
	public Integer getMar() {
		return mar;
	}
	public void setMar(Integer mar) {
		this.mar = mar;
	}
	public Integer getApr() {
		return apr;
	}
	public void setApr(Integer apr) {
		this.apr = apr;
	}
	public Integer getMay() {
		return may;
	}
	public void setMay(Integer may) {
		this.may = may;
	}
	public Integer getJun() {
		return jun;
	}
	public void setJun(Integer jun) {
		this.jun = jun;
	}
	public Integer getJuL() {
		return jul;
	}
	public void setJuL(Integer juL) {
		this.jul = juL;
	}
	
	
}
