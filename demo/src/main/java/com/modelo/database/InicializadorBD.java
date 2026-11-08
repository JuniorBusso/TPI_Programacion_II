package com.modelo.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class InicializadorBD {

    public static void inicializar() throws SQLException {
        crearBase();
        crearTablas();
        cargarDatosIniciales();
        System.out.println("Base de datos verificada o creada correctamente.");
    }

    // 1) Crea la base de datos si no existe
    private static void crearBase() throws SQLException {
        String crearBase =
            "CREATE DATABASE IF NOT EXISTS " + ConexionBD.getNombreBD() +
            " CHARACTER SET utf8mb4";

        try (Connection conexion = ConexionBD.conectarServidor();
             Statement sentencia = conexion.createStatement()) {
            sentencia.executeUpdate(crearBase);
        }
    }

    // 2) Crea las tablas
    private static void crearTablas() throws SQLException {

        String crearCliente =
            "CREATE TABLE IF NOT EXISTS cliente (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "nombre VARCHAR(60) NOT NULL, " +
            "dni VARCHAR(20) NOT NULL UNIQUE, " +
            "telefono VARCHAR(30) NOT NULL" +
            ")";

        String crearMascota =
            "CREATE TABLE IF NOT EXISTS mascota (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "nombre VARCHAR(60) NULL, " +
            "especie VARCHAR(50) NOT NULL, " +
            "cliente_id INT NOT NULL, " +
            "FOREIGN KEY (cliente_id) REFERENCES cliente(id)" +
            ")";

        String crearProfesional =
            "CREATE TABLE IF NOT EXISTS profesional (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "matricula INT NOT NULL UNIQUE, " +
            "nombre VARCHAR(40) NOT NULL, " +
            "apellido VARCHAR(40) NOT NULL, " +
            "telefono VARCHAR(30), " +
            "correo VARCHAR(60), " +
            "especialidad VARCHAR(20) NOT NULL" +
            ")";

        String crearServicio =
            "CREATE TABLE IF NOT EXISTS servicio (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "nombre VARCHAR(60) NOT NULL UNIQUE, " +
            "especialidad_requerida VARCHAR(20) NOT NULL" +
            ")";

        String crearTurno =
            "CREATE TABLE IF NOT EXISTS turno (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "mascota_id INT NOT NULL, " +
            "profesional_id INT NOT NULL, " +
            "servicio_id INT NOT NULL, " +
            "fecha DATE NOT NULL, " +
            "horario TIME NOT NULL, " +
            "duracion_est INT NOT NULL, " +
            "estado VARCHAR(15) NOT NULL DEFAULT 'PROGRAMADO', " +
            "FOREIGN KEY (mascota_id) REFERENCES mascota(id), " +
            "FOREIGN KEY (profesional_id) REFERENCES profesional(id), " +
            "FOREIGN KEY (servicio_id) REFERENCES servicio(id)" +
            ")";

        try (Connection conexion = ConexionBD.conectar();
             Statement sentencia = conexion.createStatement()) {

            sentencia.executeUpdate(crearCliente);
            sentencia.executeUpdate(crearMascota);
            sentencia.executeUpdate(crearProfesional);
            sentencia.executeUpdate(crearServicio);
            sentencia.executeUpdate(crearTurno);
        }
    }

    private static void cargarDatosIniciales() throws SQLException {

        String[] profesionales = {
            "INSERT IGNORE INTO profesional (matricula, nombre, apellido, telefono, correo, especialidad) " +
                "VALUES (1001, 'Ana', 'Pérez', '3534000001', 'ana.perez@vet.com', 'GENERAL')",
            "INSERT IGNORE INTO profesional (matricula, nombre, apellido, telefono, correo, especialidad) " +
                "VALUES (1002, 'Luis', 'Gómez', '3534000002', 'luis.gomez@vet.com', 'CIRUGIA')",
            "INSERT IGNORE INTO profesional (matricula, nombre, apellido, telefono, correo, especialidad) " +
                "VALUES (1003, 'Marta', 'Díaz', '3534000003', 'marta.diaz@vet.com', 'VACUNACION')"
        };

        String[] servicios = {
            "INSERT IGNORE INTO servicio (nombre, especialidad_requerida) VALUES ('Consulta general', 'GENERAL')",
            "INSERT IGNORE INTO servicio (nombre, especialidad_requerida) VALUES ('Cirugía', 'CIRUGIA')",
            "INSERT IGNORE INTO servicio (nombre, especialidad_requerida) VALUES ('Vacunación', 'VACUNACION')"
        };

        try (Connection conexion = ConexionBD.conectar();
             Statement sentencia = conexion.createStatement()) {

            for (String sql : profesionales) {
                sentencia.executeUpdate(sql);
            }
            for (String sql : servicios) {
                sentencia.executeUpdate(sql);
            }
        }
    }
}