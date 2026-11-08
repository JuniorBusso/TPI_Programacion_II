package com.controlador;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.modelo.Cliente;
import com.modelo.dao.ClienteDAO;
import com.vista.Vista;

public class ControladorCliente {

    // Largos máximos: tienen que coincidir con los VARCHAR de InicializadorBD
    private static final int MAX_NOMBRE = 60;
    private static final int MAX_DNI = 20;
    private static final int MAX_TELEFONO = 30;

    private Vista vista;
    private ClienteDAO clienteDAO;

    public ControladorCliente(Vista vista, Connection conexion) {
        this.vista = vista;
        this.clienteDAO = new ClienteDAO(conexion);
    }

    // Opción 1 del menú
    public void agregarCliente() {
        String nombre = vista.pedirNombreCliente();
        String dni = vista.pedirDni();
        String telefono = vista.pedirTelefono();

        if (nombre.length() > MAX_NOMBRE) {
            vista.mostrarError("El nombre no puede superar los " + MAX_NOMBRE + " caracteres");
            return;
        }
        if (dni.length() > MAX_DNI) {
            vista.mostrarError("El DNI no puede superar los " + MAX_DNI + " caracteres");
            return;
        }
        if (telefono.length() > MAX_TELEFONO) {
            vista.mostrarError("El teléfono no puede superar los " + MAX_TELEFONO + " caracteres");
            return;
        }

        try {
            Cliente existente = clienteDAO.buscarPorDni(dni);
            if (existente != null) {
                vista.mostrarError("Ya existe un cliente con ese DNI: " + existente.getNombre());
                return;
            }

            Cliente cliente = new Cliente(nombre, dni, telefono);
            clienteDAO.insertar(cliente);
            vista.mostrarMensaje("Cliente registrado correctamente (id " + cliente.getId() + ")");
        } catch (SQLException e) {
            vista.mostrarError("No se pudo guardar el cliente: " + e.getMessage());
        }
    }

    // Opción 4 del menú
    public void buscarPorNombre() {
        String nombre = vista.pedirNombreCliente();
        try {
            List<Cliente> clientes = clienteDAO.buscarPorNombre(nombre);
            if (clientes.isEmpty()) {
                vista.mostrarError("No se encontraron clientes con ese nombre");
                return;
            }
            for (Cliente cliente : clientes) {
                vista.mostrarMensaje(cliente.toString());
                vista.mostrarLista("   Mascotas:", cliente.getMascotas());
            }
        } catch (SQLException e) {
            vista.mostrarError("No se pudo buscar el cliente: " + e.getMessage());
        }
    }

    // Lo usan ControladorMascota y ControladorTurno:
    // busca por nombre y deja elegir uno. Devuelve null si no se pudo elegir ninguno.
    public Cliente elegirCliente() {
        String nombre = vista.pedirNombreCliente();
        try {
            List<Cliente> clientes = clienteDAO.buscarPorNombre(nombre);
            if (clientes.isEmpty()) {
                vista.mostrarError("No se encontraron clientes con ese nombre");
                return null;
            }
            return vista.elegir("Clientes encontrados:", clientes);
        } catch (SQLException e) {
            vista.mostrarError("No se pudo buscar el cliente: " + e.getMessage());
            return null;
        }
    }
}