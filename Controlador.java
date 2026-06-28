package com.logitrack.controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.logitrack.modelo.ConexionDB;
import com.logitrack.modelo.Envio;
import com.logitrack.modelo.Trazabilidad;

public class Controlador implements IEvidenceRepository {
	
	public boolean registrarAdmision (Envio envio, int idUsuario, int idEstado, String ubicacionInicial) {
		Connection conexion = null;
		PreparedStatement stmtEnvio = null;
		PreparedStatement stmtTrazabilidad = null;
		boolean exito = false;
		
		
		String sqlInsertEnvio = "INSERT INTO envios (num_seguimiento, remitente, destinatario, "
				+ "provincia_destino, localidad_destino, cp_destino, direccion_destino, "
				+ "peso_real, largo, ancho, alto, peso_volumetrico, tarifa)"
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		
		String sqlInsertTrazabilidad = "INSERT INTO trazabilidad (num_seguimiento, id_usuario, id_estado, ubicacion) "
				+ "VALUES (?, ?, ?, ?)";
		
		try {
			conexion = ConexionDB.abrirConexion();
			
			if (conexion != null) {
				conexion.setAutoCommit(false);
				
				stmtEnvio = conexion.prepareStatement(sqlInsertEnvio);
				stmtEnvio.setString(1, envio.getNumSeguimiento());
				stmtEnvio.setString(2, envio.getRemitente());
				stmtEnvio.setString(3, envio.getDestinatario());
				stmtEnvio.setString(4, envio.getProvinciaDestino());
				stmtEnvio.setString(5, envio.getLocalidadDestino());
				stmtEnvio.setString(6, envio.getCpDestino());
				stmtEnvio.setString(7, envio.getDireccionDestino());
				stmtEnvio.setDouble(8, envio.getPesoReal());
				stmtEnvio.setDouble(9, envio.getLargo());
				stmtEnvio.setDouble(10, envio.getAncho());
				stmtEnvio.setDouble(11, envio.getAlto());
				stmtEnvio.setDouble(12, envio.getPesoVolumetrico());
				stmtEnvio.setDouble(13, envio.getTarifa());
				
				stmtEnvio.executeUpdate();
				
				
				stmtTrazabilidad = conexion.prepareStatement(sqlInsertTrazabilidad);
				stmtTrazabilidad.setString(1, envio.getNumSeguimiento());
				stmtTrazabilidad.setInt(2, idUsuario);
				stmtTrazabilidad.setInt(3, idEstado);
				stmtTrazabilidad.setString(4, ubicacionInicial);
				
				stmtTrazabilidad.executeUpdate();
				
				conexion.commit();
				exito = true;
				System.out.println("[Controlador] Transacción confirmada con éxito (COMMIT). Envio y Trazabilidad guardados.");
			}
		} catch (SQLException e) {
			System.err.println("[Controlador] ERROR CRÍTICO durante la admisión. Se abortará la operación.");
			System.err.println("Detalle técnico del fallo: " + e.getMessage());
			
			if (conexion != null) {
				try {
					conexion.rollback();
					System.err.println("[Controlador] Rollback ejecutado correctamente. Ningún dato fue alterado");	
				} catch (SQLException ex) {
					System.err.println("[Controlador] Error fatal al intentar realizar Rollback: " + ex.getMessage());
				}
			}
		} finally {
			try {
				if (stmtEnvio != null) stmtEnvio.close();
				if (stmtTrazabilidad != null) stmtTrazabilidad.close();
				if (conexion != null) conexion.close();
			} catch (SQLException e) {
				System.err.println("Error al cerrar recursos JDBC: " + e.getMessage());
			}
		}
		return exito;
	}
	
	public boolean registrarMovimiento(String numSeguimiento, int idUsuario, int idEstadoNuevo, String ubicacion) {
	    String sqlUltimoEstado = "SELECT id_estado FROM trazabilidad WHERE num_seguimiento = ? ORDER BY fecha_hora DESC, id_movimiento DESC LIMIT 1";
	    String sqlInsertMovimiento = "INSERT INTO trazabilidad (num_seguimiento, id_usuario, id_estado, fecha_hora, ubicacion) VALUES (?, ?, ?, NOW(), ?)";
	    
	    Connection con = null;
	    PreparedStatement psCheck = null;
	    PreparedStatement psInsert = null;
	    ResultSet rs = null;
	    
	    try {
	        con = ConexionDB.abrirConexion();
	        con.setAutoCommit(false); 
	        
	        
	        psCheck = con.prepareStatement(sqlUltimoEstado);
	        psCheck.setString(1, numSeguimiento);
	        rs = psCheck.executeQuery();
	        
	        int estadoActual = 0;
	        if (rs.next()) {
	            estadoActual = rs.getInt("id_estado");
	        } else {
	            System.err.println("[Controlador] Error: El paquete no existe o no tiene un estado inicial de admisión.");
	            return false;
	        }
	        
	        
	        if (idEstadoNuevo != (estadoActual + 1)) {
	            System.err.println("\n[VALIDACIÓN RECHAZADA] Intento de salto de estado inválido.");
	            System.err.println("Estado actual en sistema: " + estadoActual);
	            System.err.println("Estado intentado: " + idEstadoNuevo);
	            System.err.println("El flujo postal exige que sea secuencial (Debe ser: " + (estadoActual + 1) + ").");
	            
	            con.rollback(); 
	            return false;   
	        }
	        
	        
	        psInsert = con.prepareStatement(sqlInsertMovimiento);
	        psInsert.setString(1, numSeguimiento);
	        psInsert.setInt(2, idUsuario);
	        psInsert.setInt(3, idEstadoNuevo);
	        psInsert.setString(4, ubicacion);
	        
	        int filasAfectadas = psInsert.executeUpdate();
	        
	        if (filasAfectadas > 0) {
	            con.commit();
	            return true;
	        } else {
	            con.rollback();
	            return false;
	        }
	        
	    } catch (Exception e) {
	        System.err.println("Error en la persistencia del movimiento: " + e.getMessage());
	        if (con != null) {
	            try { con.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
	        }
	        return false;
	    } finally {
	        
	        try { if (rs != null) rs.close(); } catch (Exception e) {}
	        try { if (psCheck != null) psCheck.close(); } catch (Exception e) {}
	        try { if (psInsert != null) psInsert.close(); } catch (Exception e) {}
	        try { if (con != null) con.close(); } catch (Exception e) {}
	    }
	}
	
	public List<Trazabilidad> consultarTrazabilidad(String numSeguimiento){
		List<Trazabilidad> historial = new ArrayList<>();
		Connection conexion = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		
		String sqlSelect = "SELECT t.id_movimiento, t.num_seguimiento, u.nombre AS responsable, "
				+ "e.nombre_estado AS estado, t.ubicacion, t.fecha_hora "
				+ "FROM trazabilidad t "
				+ "JOIN usuario u ON t.id_usuario = u.id_usuario "
				+ "JOIN estado e ON t.id_estado = e.id_estado "
				+ "WHERE t.num_seguimiento = ? "
				+ "ORDER BY t.fecha_hora ASC";
		
		try {
			conexion = ConexionDB.abrirConexion();
			if (conexion != null) {
				stmt = conexion.prepareStatement(sqlSelect);
				stmt.setString(1, numSeguimiento);
				
				rs = stmt.executeQuery();
				
				while (rs.next()) {
					Trazabilidad hito = new Trazabilidad();
					hito.setIdMovimiento(rs.getInt("id_movimiento"));
					hito.setNumSeguimiento(rs.getString("num_seguimiento"));
					hito.setNombreResponsable(rs.getString("responsable"));
					hito.setNombreEstado(rs.getString("estado"));
					hito.setUbicacion(rs.getString("ubicacion"));
					hito.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
					historial.add(hito);
				}
			}
		} catch (SQLException e) {
			System.err.println("[Controlador] Error al consultar historial de trazabilidad");
			System.err.println("Detalle técnico: " + e.getMessage());
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
				if (conexion != null) conexion.close();
			} catch (SQLException e) {
				System.err.println("Error al cerrar recursos JDBC: " + e.getMessage());
			}
		}
		return historial;
	}
}
