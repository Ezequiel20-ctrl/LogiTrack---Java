package com.logitrack.modelo;

public class Envio {
	private String numSeguimiento;
	private String remitente;
	private String destinatario;
	private String provinciaDestino;
	private String localidadDestino;
	private String cpDestino;
	private String direccionDestino;
	private double pesoReal;
	private double largo;
	private double ancho;
	private double alto;
	private double pesoVolumetrico;
	private double tarifa;
	
	public Envio() {}
	
	public Envio(String numSeguimiento, String remitente, String destinatario, String provinciaDestino,
			String localidadDestino, String cpDestino, String direccionDestino, double pesoReal,
			double largo, double ancho, double alto) {
		this.numSeguimiento = numSeguimiento;
		this.remitente = remitente;
		this.destinatario = destinatario;
		this.provinciaDestino = provinciaDestino;
		this.localidadDestino = localidadDestino;
		this.cpDestino = cpDestino;
		this.direccionDestino = direccionDestino;
		this.pesoReal = pesoReal;
		this.largo = largo;
		this.ancho = ancho;
		this.alto = alto;
		this.calcularPesoVolumetrico();
		this.asignarTarifa();
	}
	public void calcularPesoVolumetrico() {
		this.pesoVolumetrico = (this.largo * this.ancho * this.alto) / 5000.0;
	}
	public void asignarTarifa() {
		double pesoA_Cobrar = Math.max(this.pesoReal, this.pesoVolumetrico);
		this.tarifa = pesoA_Cobrar * 500.0;		
	}
	
	public String getNumSeguimiento() { return numSeguimiento; }
    public void setNumSeguimiento(String numSeguimiento) { this.numSeguimiento = numSeguimiento; }
    public String getRemitente() { return remitente; }
    public void setRemitente(String remitente) {this.remitente = remitente; }
    public String getDestinatario() { return destinatario; }
    public void setDestinatario(String destinatario) {this.destinatario = destinatario; }
	public String getProvinciaDestino() { return provinciaDestino; }
	public void setProvinciaDestino(String provinciaDestino) {this.provinciaDestino = provinciaDestino; }
	public String getLocalidadDestino() { return localidadDestino; }
    public void setLocalidadDestino(String localidadDestino) { this.localidadDestino = localidadDestino; }
    public String getCpDestino() { return cpDestino; }
    public void setCpDestino(String cpDestino) { this.cpDestino = cpDestino; }
    public String getDireccionDestino() { return direccionDestino; }
    public void setDireccionDestino(String direccionDestino) { this.direccionDestino = direccionDestino; }
    public double getPesoReal() { return pesoReal; }
    public void setPesoReal(double pesoReal) { this.pesoReal = pesoReal; }
    public double getLargo() { return largo; }
    public void setLargo(double largo) { this.largo = largo; }
    public double getAncho() { return ancho; }
    public void setAncho(double ancho) { this.ancho = ancho; }
    public double getAlto() { return alto; }
    public void setAlto(double alto) { this.alto = alto; }
    public double getPesoVolumetrico() { return pesoVolumetrico; }
    public double getTarifa() { return tarifa; }
}
