package com;

import java.sql.Connection;
import java.sql.SQLException;

import com.controlador.ControladorCliente;
import com.controlador.ControladorMascota;
import com.controlador.ControladorProfesional;
import com.controlador.ControladorTurno;
import com.modelo.database.ConexionBD;
import com.modelo.database.InicializadorBD;
import com.vista.Vista;

public class Main {
    public static void main(String[] args) {
        Vista vista = new Vista();

        // Crea la base y las tablas si hace falta
        try {
            InicializadorBD.inicializar();
        } catch (SQLException e) {
            vista.mostrarError("No se pudo inicializar la base de datos: " + e.getMessage());
            return;
        }

        // Una sola conexión para toda la ejecución; se cierra sola al salir del try
        try (Connection conexion = ConexionBD.conectar()) {

            ControladorCliente controladorCliente = new ControladorCliente(vista, conexion);
            ControladorMascota controladorMascota = new ControladorMascota(vista, conexion, controladorCliente);
            ControladorProfesional controladorProfesional = new ControladorProfesional(vista, conexion);
            ControladorTurno controladorTurno = new ControladorTurno(vista, conexion, controladorCliente);

            int opcion;
            do {
                vista.mostrarMenu();
                opcion = vista.leerOpcion();

                switch (opcion) {
                    case 1:
                        controladorCliente.agregarCliente();
                        break;
                    case 2:
                        controladorMascota.agregarMascota();
                        break;
                    case 3:
                        controladorTurno.agendarTurno();
                        break;
                    case 4:
                        controladorCliente.buscarPorNombre();
                        break;
                    case 5:
                        controladorTurno.listarTurnos();
                        break;
                    case 6:
                        controladorTurno.cancelarTurno();
                        break;
                    case 7:
                        controladorTurno.finalizarTurno();
                        break;
                    case 8:
                        controladorProfesional.listarProfesionales();
                        break;
                    case 0:
                        vista.mostrarMensaje("Hasta luego");
                        break;
                    default:
                        vista.mostrarError("Opción inválida");
                }
            } while (opcion != 0);

        } catch (SQLException e) {
            vista.mostrarError("No se pudo conectar con la base de datos: " + e.getMessage());
        }
    }
}