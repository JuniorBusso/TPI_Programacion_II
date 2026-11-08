package com.controlador;

import java.sql.Connection;
import java.sql.SQLException;

import com.modelo.dao.ProfesionalDAO;
import com.vista.Vista;

public class ControladorProfesional {

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
}