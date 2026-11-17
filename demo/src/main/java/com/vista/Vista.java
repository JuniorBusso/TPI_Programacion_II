package com.vista;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Scanner;

/**
 * La Vista solo se encarga de mostrar y pedir datos por consola.
 * No valida reglas de negocio (eso lo hace el Controlador / Modelo).
 */
public class Vista {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

    //Se configura el scanner para que lea bien desde la consola, sino tildes y ñ se cambian por otros caracteres
    private final Scanner scanner = new Scanner(System.in, codificacionConsola());

    private static Charset codificacionConsola() {
        if (System.console() != null) {
            return System.console().charset();
        }
        return Charset.defaultCharset();
    }

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
        System.out.println("9. Agregar profesional");
        System.out.println("10. Listar servicios");
        System.out.println("11. Agregar servicio");
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

    // ---------- Datos de profesional ----------

    public int pedirMatricula() {
        return pedirEntero("Matrícula del profesional: ");
    }

    public String pedirNombreProfesional() {
        return pedirTexto("Nombre del profesional: ");
    }

    public String pedirApellidoProfesional() {
        return pedirTexto("Apellido del profesional: ");
    }

    // Devuelve null si se deja vacío (dato opcional)
    public String pedirTelefonoProfesional() {
        System.out.print("Teléfono del profesional (Enter si no tiene): ");
        String telefono = scanner.nextLine().trim();
        if (telefono.isEmpty()) {
            return null;
        }
        return telefono;
    }

    // Devuelve null si se deja vacío (dato opcional)
    public String pedirCorreoProfesional() {
        System.out.print("Correo del profesional (Enter si no tiene): ");
        String correo = scanner.nextLine().trim();
        if (correo.isEmpty()) {
            return null;
        }
        return correo;
    }

    // ---------- Datos de servicio ----------

    public String pedirNombreServicio() {
        return pedirTexto("Nombre del servicio: ");
    }

    public int pedirDuracionServicio() {
        return pedirEntero("Duración del servicio en minutos: ");
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

    // Pregunta s/n y devuelve true si la respuesta es "s"
    public boolean confirmar(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (s/n): ");
            String respuesta = scanner.nextLine().trim().toLowerCase();
            if (respuesta.equals("s")) {
                return true;
            }
            if (respuesta.equals("n")) {
                return false;
            }
            mostrarError("Responda s o n");
        }
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