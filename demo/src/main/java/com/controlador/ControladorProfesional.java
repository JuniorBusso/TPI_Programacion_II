package com.controlador;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

import com.modelo.Especialidad;
import com.modelo.Profesional;
import com.modelo.dao.ProfesionalDAO;
import com.vista.Vista;

public class ControladorProfesional {

    // Largos máximos: tienen que coincidir con los VARCHAR de InicializadorBD
    private static final int MAX_NOMBRE = 40;
    private static final int MAX_APELLIDO = 40;
    private static final int MAX_TELEFONO = 30;
    private static final int MAX_CORREO = 60;

    private Vista vista;
    private ProfesionalDAO profesionalDAO;

    public ControladorProfesional(Vista vista, Connection conexion) {
        this.vista = vista;
        this.profesionalDAO = new ProfesionalDAO(conexion);
    }

    // Opción 8 del menú
    public void listarProfesionales() {
        try {
            vista.mostrarLista("Profesionales:", profesionalDAO.listarTodos());
        } catch (SQLException e) {
            vista.mostrarError("No se pudieron listar los profesionales: " + e.getMessage());
        }
    }

    // Opción 9 del menú
    public void agregarProfesional() {
        int matricula = vista.pedirMatricula();
        if (matricula <= 0) {
            vista.mostrarError("La matrícula debe ser un número mayor a 0");
            return;
        }

        try {
            // Se controla la matrícula enseguida, antes de pedir el resto de los datos
            Profesional existente = profesionalDAO.buscarPorMatricula(matricula);
            if (existente != null) {
                vista.mostrarError("Ya existe un profesional con esa matrícula: " + existente);
                return;
            }

            String nombre = vista.pedirNombreProfesional();
            String apellido = vista.pedirApellidoProfesional();
            String telefono = vista.pedirTelefonoProfesional(); // puede ser null
            String correo = vista.pedirCorreoProfesional();     // puede ser null
            Especialidad especialidad = vista.elegir("Especialidad:", Arrays.asList(Especialidad.values()));

            if (nombre.length() > MAX_NOMBRE) {
                vista.mostrarError("El nombre no puede superar los " + MAX_NOMBRE + " caracteres");
                return;
            }
            if (apellido.length() > MAX_APELLIDO) {
                vista.mostrarError("El apellido no puede superar los " + MAX_APELLIDO + " caracteres");
                return;
            }
            if (telefono != null && telefono.length() > MAX_TELEFONO) {
                vista.mostrarError("El teléfono no puede superar los " + MAX_TELEFONO + " caracteres");
                return;
            }
            if (correo != null && (correo.length() > MAX_CORREO || !correo.contains("@"))) {
                vista.mostrarError("El correo debe contener '@' y no superar los " + MAX_CORREO + " caracteres");
                return;
            }

            Profesional profesional = new Profesional(matricula, nombre, apellido, telefono, correo, especialidad);
            profesionalDAO.insertar(profesional);
            vista.mostrarMensaje("Profesional registrado: " + profesional);
        } catch (SQLException e) {
            vista.mostrarError("No se pudo guardar el profesional: " + e.getMessage());
        }
    }
}