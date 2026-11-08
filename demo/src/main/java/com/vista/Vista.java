package com.vista;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * La Vista solo se encarga de mostrar y pedir datos por consola.
 * No valida reglas de negocio (eso lo hace el Controlador / Modelo).
 */
public class Vista {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

    private final Scanner scanner = new Scanner(System.in);

    // ---------- Menú ----------

    public void mostrarMenu() {
        System.out.println();
        System.out.println("=== Sistema de gestión de la veterinaria ===");
        System.out.println("1. Agregar cliente");
        System.out.println("2. Agregar mascota a cliente");
        System.out.println("3. Agendar turno");
        System.out.println("4. Buscar cliente por nombre");
        System.out.println("5. Listar turnos");
        System.out.println("6. Cancelar turno");
        System.out.println("7. Finalizar turno");
        System.out.println("8. Listar profesionales");
        System.out.println("0. Salir");
    }

    public int leerOpcion() {
        return pedirEntero("Seleccione una opción: ");
    }

    // ---------- Datos de cliente ----------

    public String pedirNombreCliente() {
        return pedirTexto("Nombre del cliente: ");
    }

    public String pedirDni() {
        return pedirTexto("DNI del cliente: ");
    }

    public String pedirTelefono() {
        return pedirTexto("Teléfono del cliente: ");
    }

    // ---------- Datos de mascota ----------

    // Devuelve null si se deja vacío (mascota sin nombre)
    public String pedirNombreMascota() {
        System.out.print("Nombre de la mascota (Enter si no tiene): ");
        String nombre = scanner.nextLine().trim();
        if (nombre.isEmpty()) {
            return null;
        }
        return nombre;
    }

    public String pedirEspecie() {
        return pedirTexto("Especie de la mascota: ");
    }

    // ---------- Datos de turno ----------

    public LocalDate pedirFecha() {
        while (true) {
            String texto = pedirTexto("Fecha del turno (dd/MM/yyyy): ");
            try {
                return LocalDate.parse(texto, FORMATO_FECHA);
            } catch (DateTimeParseException e) {
                mostrarError("Fecha inválida. Ejemplo: 25/12/2026");
            }
        }
    }

    public LocalTime pedirHora() {
        while (true) {
            String texto = pedirTexto("Hora del turno (HH:mm): ");
            try {
                return LocalTime.parse(texto, FORMATO_HORA);
            } catch (DateTimeParseException e) {
                mostrarError("Hora inválida. Ejemplo: 09:30");
            }
        }
    }

    public int pedirDuracion() {
        return pedirEntero("Duración estimada en minutos: ");
    }

    public int pedirIdTurno() {
        return pedirEntero("Número (id) del turno: ");
    }

    // ---------- Selección de una lista ----------

    /**
     * Muestra los elementos numerados y devuelve el elegido.
     * Devuelve null si la lista está vacía.
     */
    public <T> T elegir(String titulo, List<T> elementos) {
        if (elementos == null || elementos.isEmpty()) {
            mostrarError("No hay opciones disponibles: " + titulo);
            return null;
        }
        System.out.println(titulo);
        for (int i = 0; i < elementos.size(); i++) {
            System.out.println((i + 1) + ". " + elementos.get(i));
        }
        while (true) {
            int opcion = pedirEntero("Elija una opción (1-" + elementos.size() + "): ");
            if (opcion >= 1 && opcion <= elementos.size()) {
                return elementos.get(opcion - 1);
            }
            mostrarError("Opción fuera de rango");
        }
    }

    // ---------- Mostrar ----------

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarError(String mensaje) {
        System.out.println("[ERROR] " + mensaje);
    }

    public <T> void mostrarLista(String titulo, List<T> elementos) {
        System.out.println(titulo);
        if (elementos == null || elementos.isEmpty()) {
            System.out.println("  (sin resultados)");
            return;
        }
        for (T elemento : elementos) {
            System.out.println("  - " + elemento);
        }
    }

    // ---------- Lectura básica (privada) ----------

    private String pedirTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            mostrarError("El valor no puede estar vacío");
        }
    }

    private int pedirEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                mostrarError("Ingrese un número entero");
            }
        }
    }
}