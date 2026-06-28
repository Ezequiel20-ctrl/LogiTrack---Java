package com.logitrack.vista;

import java.util.List;
import java.util.Scanner;
import com.logitrack.controlador.Controlador;
import com.logitrack.modelo.Envio;
import com.logitrack.modelo.Trazabilidad;

public class MenuPrincipal {

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        Controlador controlador = new Controlador();
        int opcion = 0;

        System.out.println("=================================================");
        System.out.println("   SISTEMA LOGITRACK JAVA - PROTOTIPO OPERACIONAL");
        System.out.println("   Autor: Ezequiel Palma | Legajo: VINF013026");
        System.out.println("=================================================");

        do {
            System.out.println("\n--- MENÚ PRINCIPAL DE OPERACIONES ---");
            System.out.println("1. Registrar Admisión de Nuevo Envío (Estado 1)");
            System.out.println("2. Registrar Movimiento / Cambiar Estado (Estados 2-5)");
            System.out.println("3. Consultar Trazabilidad Completa (Tracking)");
            System.out.println("4. Salir del Sistema");
            System.out.print("Seleccione una opción (1-4): ");
            
            try {
                opcion = Integer.parseInt(teclado.nextLine());
                
                switch (opcion) {
                    case 1:
                        System.out.println("\n--- REGISTRO DE ADMISIÓN POSTAL ---");
                        System.out.print("Ingrese número de seguimiento único (ej. TRK10025): ");
                        String numSeguimiento = teclado.nextLine();
                        System.out.print("Nombre del Remitente: ");
                        String remitente = teclado.nextLine();
                        System.out.print("Nombre del Destinatario: ");
                        String destinatario = teclado.nextLine();
                        System.out.print("Provincia de Destino: ");
                        String provincia = teclado.nextLine();
                        System.out.print("Localidad de Destino: ");
                        String localidad = teclado.nextLine();
                        System.out.print("Código Postal (CP): ");
                        String cp = teclado.nextLine();
                        System.out.print("Dirección de Destino: ");
                        String direccion = teclado.nextLine();
                        System.out.print("Peso Real del bulto (en kg): ");
                        double pesoReal = Double.parseDouble(teclado.nextLine());
                        System.out.print("Largo del paquete (en cm): ");
                        double largo = Double.parseDouble(teclado.nextLine());
                        System.out.print("Ancho del paquete (en cm): ");
                        double ancho = Double.parseDouble(teclado.nextLine());
                        System.out.print("Alto del paquete (en cm): ");
                        double alto = Double.parseDouble(teclado.nextLine());

                        Envio nuevoEnvio = new Envio(numSeguimiento, remitente, destinatario, 
                                provincia, localidad, cp, direccion, pesoReal, largo, ancho, alto);

                        System.out.println("\n--- CÁLCULOS TÉCNICOS AUTOMATIZADOS ---");
                        System.out.printf("-> Peso Volumétrico Calculado: %.2f kg\n", nuevoEnvio.getPesoVolumetrico());
                        System.out.printf("-> Tarifa Final Comercial Asignada: $%.2f\n", nuevoEnvio.getTarifa());
                        
                        System.out.print("\n¿Confirmar el guardado transaccional en MySQL? (Si/No): ");
                        String confirmar = teclado.nextLine().trim();
                        
                        if (confirmar.equalsIgnoreCase("Si") || confirmar.equalsIgnoreCase("S")) {
                            
                            boolean exito = controlador.registrarAdmision(nuevoEnvio, 1, 1, "Sucursal Frías");
                            if (exito) {
                                System.out.println("¡Operación Exitosa! Paquete admitido en 'Sucursal Frías'.");
                            }
                        } else {
                            System.out.println("Admisión cancelada por el usuario.");
                        }
                        break;

                    case 2:
                        System.out.println("\n--- REGISTRAR MOVIMIENTO (CAMBIO DE ESTADO) ---");
                        System.out.print("Ingrese el número de seguimiento del paquete: ");
                        String nSeg = teclado.nextLine();
                        
                        
                        String[] nombresEstados = {
                            "No válido",                        
                            "Admisión Postal",                  
                            "En Tránsito Troncal",              
                            "Recibido en Sucursal de Destino",   
                            "En Distribución Final",            
                            "Entregado con Éxito"               
                        };
                        
                        
                        System.out.println("Seleccione el nuevo Estado Logístico:");
                        for (int i = 2; i < nombresEstados.length; i++) {
                            System.out.println(i + ". " + nombresEstados[i]);
                        }
                        
                        System.out.print("Seleccione una opción (2-5): ");
                        int nuevoEstado = Integer.parseInt(teclado.nextLine());
                        
                        
                        if (nuevoEstado < 2 || nuevoEstado >= nombresEstados.length) {
                            System.out.println("\n[Error] Opción de estado fuera de rango permitido.");
                            break;
                        }
                        
                        
                        int idUsuarioAsignado = 2;
                        String datosUbicacionYEntrega = "";

                        if (nuevoEstado == 4) {
                            idUsuarioAsignado = 3; 
                            System.out.print("Ingrese la Sucursal desde donde sale a distribución (ej. Sucursal Frías): ");
                            datosUbicacionYEntrega = "Hacia calle desde " + teclado.nextLine();
                            
                        } else if (nuevoEstado == 5) {
                            idUsuarioAsignado = 3; 
                            System.out.print("Ingrese el Domicilio Real de entrega: ");
                            String domicilio = teclado.nextLine();
                            System.out.print("Ingrese Nombre y DNI de quién recibe el paquete: ");
                            String receptor = teclado.nextLine();
                            
                            datosUbicacionYEntrega = domicilio + " - Recibió: " + receptor;
                            
                        } else {
                            System.out.print("Ingrese la ubicación geográfica del evento (ej. Centro Logístico Tucumán): ");
                            datosUbicacionYEntrega = teclado.nextLine();
                        }
                        
                        
                        boolean movExito = controlador.registrarMovimiento(nSeg, idUsuarioAsignado, nuevoEstado, datosUbicacionYEntrega);
                        if (movExito) {
                            System.out.println("¡Estado actualizado e historial de auditoría guardado correctamente!");
                        } else {
                            System.err.println("No se pudo registrar el movimiento. Verifique el código del paquete o la secuencia de estados.");
                        }
                        break;

                    case 3:
                        System.out.println("\n--- CONSULTA DE TRAZABILIDAD ---");
                        System.out.print("Ingrese el número de seguimiento a buscar: ");
                        String busqueda = teclado.nextLine();
                        
                        List<Trazabilidad> historial = controlador.consultarTrazabilidad(busqueda);
                        
                        if (historial.isEmpty()) {
                            System.out.println("[Alerta] No se encontraron movimientos para el código: " + busqueda);
                        } else {
                            System.out.println("\n=================================================================================================================");
                            System.out.printf("%-10s | %-32s | %-18s | %-40s\n", "MOVIMIENTO", "ESTADO LOGÍSTICO", "RESPONSABLE", "UBICACIÓN / DETALLE DE ENTREGA");
                            System.out.println("=================================================================================================================");
                            for (Trazabilidad hito : historial) {
                                System.out.printf("#%-9d | %-32s | %-18s | %-40s\n", 
                                        hito.getIdMovimiento(),
                                        hito.getNombreEstado(),
                                        hito.getNombreResponsable(),
                                        hito.getUbicacion());
                            }
                            System.out.println("=================================================================================================================");
                        }
                        break;

                    case 4:
                        System.out.println("\nCerrando conexiones... Gracias por utilizar LogiTrack.");
                        break;

                    default:
                        System.err.println("Opción inválida. Intente del 1 al 4.");
                }
            } catch (NumberFormatException e) {
                System.err.println("Error: Ingrese un valor numérico válido.");
            }
        } while (opcion != 4);

        teclado.close();
    }
}
