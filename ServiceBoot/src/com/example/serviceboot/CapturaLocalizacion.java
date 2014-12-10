package com.example.serviceboot;


/*
 * @Author: Ing William Medina Romero
 * @Date: 08 de Agosto, 2014
 * 
 */

public class CapturaLocalizacion {

	private double longitud;
	private double latitud;
	private String idDevide;
	
	
	public double getLongitud() {
		return longitud;
	}
	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
	public double getLatitud() {
		return latitud;
	}
	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}
	public String getIdDevide() {
		return idDevide;
	}
	public void setIdDevide(String idDevide) {
		this.idDevide = idDevide;
	}
	
	public CapturaLocalizacion(double longitud, double latitud, String idDevide) {
		super();
		this.longitud = longitud;
		this.latitud = latitud;
		this.idDevide = idDevide;
	}
	
	
	
	
}
