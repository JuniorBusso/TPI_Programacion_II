package com.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.modelo.Cliente;
import com.modelo.Mascota;

public class ClienteDAO {
    private Connection conexion;
    private MascotaDAO mascotaDAO;

    public ClienteDAO(Connection conexion) {
        this.conexion = conexion;
        this.mascotaDAO = new MascotaDAO(conexion);
    }

    public void insertar(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (nombre, dni, telefono) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getDni());
            ps.setString(3, cliente.getTelefono());
            ps.executeUpdate();

            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (claves.next()) {
                    cliente.setId(claves.getInt(1));
                }
            }
        }
    }

    // Devuelve null si no existe
    public Cliente buscarPorDni(String dni) throws SQLException {
        String sql = "SELECT id, nombre, dni, telefono FROM cliente WHERE dni = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return armarCliente(rs);
                }
            }
        }
        return null;
    }

    // Busca clientes cuyo nombre contenga el texto (sin distinguir mayúsculas)
    public List<Cliente> buscarPorNombre(String nombre) throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT id, nombre, dni, telefono FROM cliente WHERE nombre LIKE ? ORDER BY nombre";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, "%" + nombre + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    clientes.add(armarCliente(rs));
                }
            }
        }
        return clientes;
    }

    public List<Cliente> listarTodos() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT id, nombre, dni, telefono FROM cliente ORDER BY nombre";
        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                clientes.add(armarCliente(rs));
            }
        }
        return clientes;
    }

    // Arma el Cliente y le carga su lista de mascotas (la BD solo guarda cliente_id en mascota)
    private Cliente armarCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente(rs.getString("nombre"), rs.getString("dni"), rs.getString("telefono"));
        cliente.setId(rs.getInt("id"));
        for (Mascota mascota : mascotaDAO.listarPorCliente(cliente.getId())) {
            cliente.agregarMascota(mascota);
        }
        return cliente;
    }
}