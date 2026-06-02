package com.logitrack.modelo;

import java.time.LocalDateTime;

public class Trazabilidad {
	private int idMovimiento;
	private String numSeguimiento;
	private String nombreResponsable;
	private String nombreEstado;
	private String ubicacion;
	private LocalDateTime fechaHora;
	
	public Trazabilidad () {}
	
	public Trazabilidad(String numSeguimiento, String nombreResponsable, String nombreEstado, String ubicacion, LocalDateTime fechaHora) {
		this.numSeguimiento = numSeguimiento;
		this.nombreResponsable = nombreResponsable;
		this.nombreEstado = nombreEstado;
		this.ubicacion = ubicacion;
		this.fechaHora = fechaHora;
	}
	
	public int getIdMovimiento() { return idMovimiento; }
    public void setIdMovimiento(int idMovimiento) { this.idMovimiento = idMovimiento; }
    public String getNumSeguimiento() { return numSeguimiento; }
    public void setNumSeguimiento(String numSeguimiento) { this.numSeguimiento = numSeguimiento; }
    public String getNombreResponsable() { return nombreResponsable; }
    public void setNombreResponsable(String nombreResponsable) { this.nombreResponsable = nombreResponsable; }
    public String getNombreEstado() { return nombreEstado; }
    public void setNombreEstado(String nombreEstado) { this.nombreEstado = nombreEstado; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
	
	

}
