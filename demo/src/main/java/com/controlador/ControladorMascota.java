package com.controlador;

import java.sql.Connection;
import java.sql.SQLException;

import com.modelo.Cliente;
import com.modelo.Mascota;
import com.modelo.dao.MascotaDAO;
import com.vista.Vista;

public class ControladorMascota {

    // Largos máximos: tienen que coincidir con los VARCHAR de InicializadorBD
    private static final int MAX_NOMBRE = 60;
    private static final int MAX_ESPECIE = 50;

    private Vista vista;
    private ControladorCliente controladorCliente;
    private MascotaDAO mascotaDAO;

    public ControladorMascota(Vista vista, Connection conexion, ControladorCliente controladorCliente) {
        this.vista = vista;
        this.controladorCliente = controladorCliente;
        this.mascotaDAO = new MascotaDAO(conexion);
    }

    // Opción 2 del menú
    public void agregarMascota() {
        Cliente cliente = controladorCliente.elegirCliente();
        if (cliente == null) {
            return;
        }

        String nombre = vista.pedirNombreMascota(); // puede ser null (mascota sin nombre)
        String especie = vista.pedirEspecie();

        if (nombre != null && nombre.length() > MAX_NOMBRE) {
            vista.mostrarError("El nombre no puede superar los " + MAX_NOMBRE + " caracteres");
            return;
        }
        if (especie.length() > MAX_ESPECIE) {
            vista.mostrarError("La especie no puede superar los " + MAX_ESPECIE + " caracteres");
            return;
        }

        try {
            Mascota mascota = new Mascota(nombre, especie);
            mascotaDAO.insertar(mascota, cliente.getId());
            cliente.agregarMascota(mascota);
            vista.mostrarMensaje("Mascota registrada: " + mascota + " - Dueño: " + cliente.getNombre());
        } catch (SQLException e) {
            vista.mostrarError("No se pudo guardar la mascota: " + e.getMessage());
        }
    }
}