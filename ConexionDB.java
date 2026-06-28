package com.logitrack.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
	private static final String URL = "jdbc:mysql://localhost:3306/logitrack_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
	private static final String USER = "root";
	private static final String PASSWORD = "";
	
	public static Connection abrirConexion() {
		Connection conexion = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			conexion = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (ClassNotFoundException e) {
			System.err.println("ERROR CRÍTICO: No se encontró el controlador JDBC de MySQL en el proyeto.");
			System.err.println("Detalle técnico: " + e.getMessage());			
		} catch (SQLException e) {
			System.err.println("ERROR DE CONEXIÓN: No se pudo conectar a MySQL Server en el puerto 3306. ");
			System.err.println("Asegúrese de que el motor esté encendio y la base 'logitrack_db' exista.");
			System.err.println("Detalle técnico: " + e.getMessage());
		}
		return conexion;
	}
	
}
