package com.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.modelo.Mascota;

public class MascotaDAO {
    private Connection conexion;

    public MascotaDAO(Connection conexion) {
        this.conexion = conexion;
    }

    // La Mascota no conoce a su cliente, así que el id del cliente se pasa por parámetro
    public void insertar(Mascota mascota, int clienteId) throws SQLException {
        String sql = "INSERT INTO mascota (nombre, especie, cliente_id) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (mascota.getNombre() == null) {
                ps.setNull(1, Types.VARCHAR);
            } else {
                ps.setString(1, mascota.getNombre());
            }
            ps.setString(2, mascota.getEspecie());
            ps.setInt(3, clienteId);
            ps.executeUpdate();

            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (claves.next()) {
                    mascota.setId(claves.getInt(1));
                }
            }
        }
    }

    public List<Mascota> listarPorCliente(int clienteId) throws SQLException {
        List<Mascota> mascotas = new ArrayList<>();
        String sql = "SELECT id, nombre, especie FROM mascota WHERE cliente_id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mascotas.add(armarMascota(rs));
                }
            }
        }
        return mascotas;
    }

    // Devuelve null si no existe
    public Mascota buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nombre, especie FROM mascota WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return armarMascota(rs);
                }
            }
        }
        return null;
    }

    private Mascota armarMascota(ResultSet rs) throws SQLException {
        return new Mascota(rs.getInt("id"), rs.getString("nombre"), rs.getString("especie"));
    }
}