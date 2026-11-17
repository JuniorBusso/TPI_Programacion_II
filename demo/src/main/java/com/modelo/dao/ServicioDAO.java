package com.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.modelo.Especialidad;
import com.modelo.Servicio;

public class ServicioDAO {
    private Connection conexion;

    public ServicioDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public void insertar(Servicio servicio) throws SQLException {
        String sql = "INSERT INTO servicio (nombre, duracion_min, especialidad_requerida) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, servicio.getNombre());
            ps.setInt(2, servicio.getDuracionMin());
            ps.setString(3, servicio.getEspecialidadRequerida().name());
            ps.executeUpdate();

            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (claves.next()) {
                    servicio.setId(claves.getInt(1));
                }
            }
        }
    }

    // Busca por nombre exacto (MySQL no distingue mayúsculas ni tildes). Devuelve null si no existe
    public Servicio buscarPorNombre(String nombre) throws SQLException {
        String sql = "SELECT id, nombre, duracion_min, especialidad_requerida FROM servicio WHERE nombre = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return armarServicio(rs);
                }
            }
        }
        return null;
    }

    public List<Servicio> listarTodos() throws SQLException {
        List<Servicio> servicios = new ArrayList<>();
        String sql = "SELECT id, nombre, duracion_min, especialidad_requerida FROM servicio ORDER BY nombre";
        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                servicios.add(armarServicio(rs));
            }
        }
        return servicios;
    }

    // Devuelve null si no existe
    public Servicio buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nombre, duracion_min, especialidad_requerida FROM servicio WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return armarServicio(rs);
                }
            }
        }
        return null;
    }

    private Servicio armarServicio(ResultSet rs) throws SQLException {
        return new Servicio(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getInt("duracion_min"),
            Especialidad.valueOf(rs.getString("especialidad_requerida"))
        );
    }
}