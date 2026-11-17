package com.controlador;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

import com.modelo.Especialidad;
import com.modelo.Servicio;
import com.modelo.dao.ServicioDAO;
import com.vista.Vista;

public class ControladorServicio {

    // Largo máximo: tiene que coincidir con el VARCHAR de InicializadorBD
    private static final int MAX_NOMBRE = 60;

    // Un servicio más largo que el horario de atención no podría agendarse nunca
    private static final int DURACION_MAXIMA = ControladorTurno.CIERRE_MIN - ControladorTurno.APERTURA_MIN;

    private Vista vista;
    private ServicioDAO servicioDAO;

    public ControladorServicio(Vista vista, Connection conexion) {
        this.vista = vista;
        this.servicioDAO = new ServicioDAO(conexion);
    }

    // Opción 10 del menú
    public void listarServicios() {
        try {
            vista.mostrarLista("Servicios:", servicioDAO.listarTodos());
        } catch (SQLException e) {
            vista.mostrarError("No se pudieron listar los servicios: " + e.getMessage());
        }
    }

    // Opción 11 del menú
    public void agregarServicio() {
        String nombre = vista.pedirNombreServicio();
        int duracion = vista.pedirDuracionServicio();
        Especialidad especialidad = vista.elegir("Especialidad requerida:", Arrays.asList(Especialidad.values()));

        if (nombre.length() > MAX_NOMBRE) {
            vista.mostrarError("El nombre no puede superar los " + MAX_NOMBRE + " caracteres");
            return;
        }
        if (duracion < 1 || duracion > DURACION_MAXIMA) {
            vista.mostrarError("La duración debe estar entre 1 y " + DURACION_MAXIMA
                    + " minutos (el horario de atención es de 8 a 16)");
            return;
        }

        try {
            Servicio existente = servicioDAO.buscarPorNombre(nombre);
            if (existente != null) {
                vista.mostrarError("Ya existe un servicio con ese nombre: " + existente);
                return;
            }

            Servicio servicio = new Servicio(nombre, duracion, especialidad);
            servicioDAO.insertar(servicio);
            vista.mostrarMensaje("Servicio registrado: " + servicio);
        } catch (SQLException e) {
            vista.mostrarError("No se pudo guardar el servicio: " + e.getMessage());
        }
    }
}