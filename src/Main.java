import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Agenda agenda = new Agenda();

    public static void main(String[] args) {
        cargarDatosIniciales();

        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Elegí una opción: ");
            System.out.println();

            switch (opcion) {
                case 1:
                    registrarCliente();
                    break;
                case 2:
                    registrarMascota();
                    break;
                case 3:
                    pedirTurno();
                    break;
                case 4:
                    cancelarTurno();
                    break;
                case 5:
                    verHorariosLibres();
                    break;
                case 6:
                    listarTurnos();
                    break;
                case 0:
                    System.out.println("Hasta luego.");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    // ---------- Menú ----------

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("===== SISTEMA DE TURNOS VETERINARIOS =====");
        System.out.println("1. Registrar cliente");
        System.out.println("2. Registrar mascota");
        System.out.println("3. Pedir turno");
        System.out.println("4. Cancelar turno");
        System.out.println("5. Ver horarios libres de un profesional");
        System.out.println("6. Listar turnos");
        System.out.println("0. Salir");
    }

    // ---------- Datos de prueba ----------

    private static void cargarDatosIniciales() {
        agenda.agregarProfesional(new Profesional(12345, "Juan", "Pérez", "123456789", "juan.perez@email.com", Especialidad.CLINICA_GENERAL));
        agenda.agregarProfesional(new Profesional(23456, "Ana", "Gómez", "111222333", "ana.gomez@email.com", Especialidad.CIRUGIA));
        agenda.agregarProfesional(new Profesional(34567, "Luis", "Torres", "444555666", "luis.torres@email.com", Especialidad.DERMATOLOGIA));

        Servicio consulta = new Servicio(1, "Consulta General");
        consulta.agregarEspecialidad(Especialidad.CLINICA_GENERAL);
        agenda.agregarServicio(consulta);

        Servicio vacunacion = new Servicio(2, "Vacunación");
        vacunacion.agregarEspecialidad(Especialidad.CLINICA_GENERAL);
        agenda.agregarServicio(vacunacion);

        Servicio cirugia = new Servicio(3, "Cirugía");
        cirugia.agregarEspecialidad(Especialidad.CIRUGIA);
        agenda.agregarServicio(cirugia);

        Servicio dermatologia = new Servicio(4, "Consulta dermatológica");
        dermatologia.agregarEspecialidad(Especialidad.DERMATOLOGIA);
        agenda.agregarServicio(dermatologia);
    }

    // ---------- Opciones del menú ----------

    private static void registrarCliente() {
        System.out.println("--- Registrar cliente ---");
        String nombre = leerTexto("Nombre: ");
        String apellido = leerTexto("Apellido: ");
        String telefono = leerTexto("Teléfono: ");

        if (nombre.equals("") || apellido.equals("") || telefono.equals("")) {
            System.out.println("Todos los datos son obligatorios.");
            return;
        }

        Cliente cliente = agenda.registrarCliente(nombre, apellido, telefono);
        System.out.println("Cliente registrado con el número " + cliente.getId() + ".");
    }

    private static void registrarMascota() {
        System.out.println("--- Registrar mascota ---");
        Cliente cliente = elegirCliente();
        if (cliente == null) {
            return;
        }

        String nombre = leerTexto("Nombre de la mascota: ");
        String especie = leerTexto("Especie: ");
        String raza = leerTexto("Raza: ");

        if (nombre.equals("") || especie.equals("") || raza.equals("")) {
            System.out.println("Todos los datos son obligatorios.");
            return;
        }

        Mascota mascota = agenda.registrarMascota(cliente, nombre, especie, raza);
        System.out.println("Mascota registrada con el número " + mascota.getId() + ".");
    }

    private static void pedirTurno() {
        System.out.println("--- Pedir turno ---");

        Cliente cliente = elegirCliente();
        if (cliente == null) {
            return;
        }

        Mascota mascota = elegirMascota(cliente);
        if (mascota == null) {
            return;
        }

        Servicio servicio = elegirServicio();
        if (servicio == null) {
            return;
        }

        Profesional profesional = elegirProfesional(servicio);
        if (profesional == null) {
            return;
        }

        String fecha = leerTexto("Fecha (aaaa-mm-dd): ");
        if (!Horario.esFechaValida(fecha)) {
            System.out.println("La fecha no es válida. Usá el formato aaaa-mm-dd, por ejemplo 2026-10-23.");
            return;
        }

        List<String> libres = agenda.horariosLibres(profesional, fecha);
        if (libres.isEmpty()) {
            System.out.println("No quedan horarios libres para ese día.");
            return;
        }

        System.out.println("Horarios libres:");
        for (int i = 0; i < libres.size(); i++) {
            System.out.println((i + 1) + ". " + libres.get(i));
        }

        int numero = leerEntero("Elegí un horario (número): ");
        if (numero < 1 || numero > libres.size()) {
            System.out.println("Horario inválido.");
            return;
        }
        String horario = libres.get(numero - 1);

        String error = agenda.validarTurno(profesional, servicio, fecha, horario);
        if (!error.equals("")) {
            System.out.println("No se pudo dar el turno: " + error);
            return;
        }

        Turno turno = agenda.crearTurno(mascota, profesional, servicio, fecha, horario);
        System.out.println("Turno confirmado:");
        System.out.println(turno.descripcion());
    }

    private static void cancelarTurno() {
        System.out.println("--- Cancelar turno ---");

        boolean hayProgramados = false;
        for (int i = 0; i < agenda.getTurnos().size(); i++) {
            Turno t = agenda.getTurnos().get(i);
            if (t.getEstado() == EstadoTurno.PROGRAMADO) {
                System.out.println(t.descripcion());
                hayProgramados = true;
            }
        }
        if (!hayProgramados) {
            System.out.println("No hay turnos programados para cancelar.");
            return;
        }

        int id = leerEntero("Número del turno a cancelar: ");
        Turno turno = agenda.buscarTurno(id);
        if (turno == null) {
            System.out.println("No existe un turno con ese número.");
            return;
        }

        if (turno.cancelar()) {
            System.out.println("Turno cancelado. El horario quedó libre.");
        } else {
            System.out.println("Ese turno no se puede cancelar (estado: " + turno.getEstado().getDescripcion() + ").");
        }
    }

    private static void verHorariosLibres() {
        System.out.println("--- Horarios libres ---");

        for (int i = 0; i < agenda.getProfesionales().size(); i++) {
            Profesional p = agenda.getProfesionales().get(i);
            System.out.println(p.getMatricula() + " - " + p.getNombreCompleto()
                    + " (" + p.getEspecialidad().getDescripcion() + ")");
        }
        int matricula = leerEntero("Matrícula del profesional: ");
        Profesional profesional = agenda.buscarProfesional(matricula);
        if (profesional == null) {
            System.out.println("No existe un profesional con esa matrícula.");
            return;
        }

        String fecha = leerTexto("Fecha (aaaa-mm-dd): ");
        if (!Horario.esFechaValida(fecha)) {
            System.out.println("La fecha no es válida. Usá el formato aaaa-mm-dd, por ejemplo 2026-10-23.");
            return;
        }

        List<String> libres = agenda.horariosLibres(profesional, fecha);
        if (libres.isEmpty()) {
            System.out.println("No quedan horarios libres para ese día.");
            return;
        }
        System.out.println("Horarios libres de " + profesional.getNombreCompleto() + " el " + fecha + ":");
        for (int i = 0; i < libres.size(); i++) {
            System.out.println("  " + libres.get(i));
        }
    }

    private static void listarTurnos() {
        System.out.println("--- Turnos ---");
        if (agenda.getTurnos().isEmpty()) {
            System.out.println("Todavía no hay turnos.");
            return;
        }
        for (int i = 0; i < agenda.getTurnos().size(); i++) {
            System.out.println(agenda.getTurnos().get(i).descripcion());
        }
    }

    // ---------- Elegir datos de una lista ----------

    private static Cliente elegirCliente() {
        if (agenda.getClientes().isEmpty()) {
            System.out.println("Primero tenés que registrar un cliente (opción 1).");
            return null;
        }
        for (int i = 0; i < agenda.getClientes().size(); i++) {
            Cliente c = agenda.getClientes().get(i);
            System.out.println(c.getId() + " - " + c.getNombre() + " " + c.getApellido());
        }
        int id = leerEntero("Número de cliente: ");
        Cliente cliente = agenda.buscarCliente(id);
        if (cliente == null) {
            System.out.println("No existe un cliente con ese número.");
        }
        return cliente;
    }

    private static Mascota elegirMascota(Cliente cliente) {
        if (cliente.getMascotas().isEmpty()) {
            System.out.println("Ese cliente no tiene mascotas. Registrala primero (opción 2).");
            return null;
        }
        for (int i = 0; i < cliente.getMascotas().size(); i++) {
            Mascota m = cliente.getMascotas().get(i);
            System.out.println(m.getId() + " - " + m.getNombre() + " (" + m.getEspecie() + ")");
        }
        int id = leerEntero("Número de mascota: ");
        Mascota mascota = agenda.buscarMascota(cliente, id);
        if (mascota == null) {
            System.out.println("Ese cliente no tiene una mascota con ese número.");
        }
        return mascota;
    }

    private static Servicio elegirServicio() {
        for (int i = 0; i < agenda.getServicios().size(); i++) {
            Servicio s = agenda.getServicios().get(i);
            System.out.println(s.getId() + " - " + s.getNombre());
        }
        int id = leerEntero("Número de servicio: ");
        Servicio servicio = agenda.buscarServicio(id);
        if (servicio == null) {
            System.out.println("No existe un servicio con ese número.");
        }
        return servicio;
    }

    private static Profesional elegirProfesional(Servicio servicio) {
        List<Profesional> compatibles = agenda.profesionalesParaServicio(servicio);
        if (compatibles.isEmpty()) {
            System.out.println("No hay profesionales para ese servicio.");
            return null;
        }
        for (int i = 0; i < compatibles.size(); i++) {
            Profesional p = compatibles.get(i);
            System.out.println(p.getMatricula() + " - " + p.getNombreCompleto()
                    + " (" + p.getEspecialidad().getDescripcion() + ")");
        }
        int matricula = leerEntero("Matrícula del profesional: ");
        Profesional profesional = agenda.buscarProfesional(matricula);
        if (profesional == null || !servicio.admiteEspecialidad(profesional.getEspecialidad())) {
            System.out.println("Esa matrícula no corresponde a un profesional de la lista.");
            return null;
        }
        return profesional;
    }

    // ---------- Lectura por teclado ----------

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    // Devuelve -1 si lo que se escribió no es un número
    private static int leerEntero(String mensaje) {
        String texto = leerTexto(mensaje);
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
