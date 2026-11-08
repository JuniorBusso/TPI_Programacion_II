package com.modelo.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // Los datos de conexión se leen de variables de entorno de cada computadora, así la clave no queda escrita en el código ni en el repositorio.
    //   VET_DB_USER     -> usuario de MySQL (si no existe, usa "root")
    //   VET_DB_PASSWORD -> clave de MySQL   (si no existe, usa clave vacía)
    private static final String USUARIO = obtenerVariable("VET_DB_USER", "root");
    private static final String CLAVE = obtenerVariable("VET_DB_PASSWORD", "");

    private static final String NOMBRE_BD = "veterinaria";

    // Conexión al servidor SIN elegir base (se usa solo para crear la base)
    private static final String URL_SERVIDOR =
        "jdbc:mysql://localhost:3306/?allowPublicKeyRetrieval=true&useSSL=false";

    // Conexión a la base de la veterinaria
    private static final String URL =
        "jdbc:mysql://localhost:3306/" + NOMBRE_BD + "?allowPublicKeyRetrieval=true&useSSL=false";

    private static String obtenerVariable(String nombre, String valorPorDefecto) {
        String valor = System.getenv(nombre);
        if (valor == null) {
            return valorPorDefecto;
        }
        return valor;
    }

    public static String getNombreBD() {
        return NOMBRE_BD;
    }

    public static Connection conectarServidor() throws SQLException {
        return DriverManager.getConnection(URL_SERVIDOR, USUARIO, CLAVE);
    }

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}