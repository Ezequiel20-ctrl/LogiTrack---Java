package com.logitrack.controlador;

import java.util.List;
import com.logitrack.modelo.Envio;
import com.logitrack.modelo.Trazabilidad;

public interface IEvidenceRepository {
	boolean registrarAdmision(Envio envio, int idUsuario, int idEstado, String ubicacionInicial);
	boolean registrarMovimiento(String numSeguimiento, int idUsuario, int idEstado, String ubicacion);
	List<Trazabilidad> consultarTrazabilidad(String numSeguimiento);
}
