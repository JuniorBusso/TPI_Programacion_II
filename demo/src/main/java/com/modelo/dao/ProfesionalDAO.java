package com.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.modelo.Especialidad;
import com.modelo.Profesional;

public class ProfesionalDAO {
    private Connection conexion;

    public ProfesionalDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public List<Profesional> listarTodos() throws SQLException {
        List<Profesional> profesionales = new ArrayList<>();
        String sql = "SELECT id, matricula, nombre, apellido, telefono, correo, especialidad " +
                     "FROM profesional ORDER BY apellido, nombre";
        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                profesionales.add(armarProfesional(rs));
            }
        }
        return profesionales;
    }

    // Profesionales que pueden atender un servicio que requiere cierta especialidad
    public List<Profesional> listarPorEspecialidad(Especialidad especialidad) throws SQLException {
        List<Profesional> profesionales = new ArrayList<>();
        String sql = "SELECT id, matricula, nombre, apellido, telefono, correo, especialidad " +
                     "FROM profesional WHERE especialidad = ? ORDER BY apellido, nombre";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, especialidad.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    profesionales.add(armarProfesional(rs));
                }
            }
        }
        return profesionales;
    }

    // Devuelve null si no existe
    public Profesional buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, matricula, nombre, apellido, telefono, correo, especialidad " +
                     "FROM profesional WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return armarProfesional(rs);
                }
            }
        }
        return null;
    }

    private Profesional armarProfesional(ResultSet rs) throws SQLException {
        return new Profesional(
            rs.getInt("id"),
            rs.getInt("matricula"),
            rs.getString("nombre"),
            rs.getString("apellido"),
            rs.getString("telefono"),
            rs.getString("correo"),
            Especialidad.valueOf(rs.getString("especialidad"))
        );
    }
}