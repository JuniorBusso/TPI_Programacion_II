import java.util.ArrayList;
import java.util.List;

// Guarda los datos del sistema y aplica las reglas de negocio de los turnos
public class Agenda {
    private List<Cliente> clientes;
    private List<Profesional> profesionales;
    private List<Servicio> servicios;
    private List<Turno> turnos;
    private int proximoIdCliente;
    private int proximoIdMascota;
    private int proximoIdTurno;

    public Agenda() {
        clientes = new ArrayList<>();
        profesionales = new ArrayList<>();
        servicios = new ArrayList<>();
        turnos = new ArrayList<>();
        proximoIdCliente = 1;
        proximoIdMascota = 1;
        proximoIdTurno = 1;
    }

    // ---------- Altas ----------

    public Cliente registrarCliente(String nombre, String apellido, String telefono) {
        Cliente cliente = new Cliente(proximoIdCliente, nombre, apellido, telefono);
        proximoIdCliente++;
        clientes.add(cliente);
        return cliente;
    }

    public Mascota registrarMascota(Cliente cliente, String nombre, String especie, String raza) {
        Mascota mascota = new Mascota(proximoIdMascota, nombre, especie, raza);
        proximoIdMascota++;
        cliente.agregarMascota(mascota);
        return mascota;
    }

    public void agregarProfesional(Profesional profesional) {
        profesionales.add(profesional);
    }

    public void agregarServicio(Servicio servicio) {
        servicios.add(servicio);
    }

    // ---------- Búsquedas ----------

    public Cliente buscarCliente(int id) {
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getId() == id) {
                return clientes.get(i);
            }
        }
        return null;
    }

    public Mascota buscarMascota(Cliente cliente, int id) {
        for (int i = 0; i < cliente.getMascotas().size(); i++) {
            if (cliente.getMascotas().get(i).getId() == id) {
                return cliente.getMascotas().get(i);
            }
        }
        return null;
    }

    public Profesional buscarProfesional(int matricula) {
        for (int i = 0; i < profesionales.size(); i++) {
            if (profesionales.get(i).getMatricula() == matricula) {
                return profesionales.get(i);
            }
        }
        return null;
    }

    public Servicio buscarServicio(int id) {
        for (int i = 0; i < servicios.size(); i++) {
            if (servicios.get(i).getId() == id) {
                return servicios.get(i);
            }
        }
        return null;
    }

    public Turno buscarTurno(int id) {
        for (int i = 0; i < turnos.size(); i++) {
            if (turnos.get(i).getId() == id) {
                return turnos.get(i);
            }
        }
        return null;
    }

    // Profesionales cuya especialidad sirve para atender el servicio
    public List<Profesional> profesionalesParaServicio(Servicio servicio) {
        List<Profesional> compatibles = new ArrayList<>();
        for (int i = 0; i < profesionales.size(); i++) {
            if (servicio.admiteEspecialidad(profesionales.get(i).getEspecialidad())) {
                compatibles.add(profesionales.get(i));
            }
        }
        return compatibles;
    }

    // ---------- Lógica de horarios ----------

    // Un profesional está libre si no tiene un turno PROGRAMADO en esa fecha y horario.
    // Los turnos cancelados no se borran, pero liberan el horario.
    public boolean profesionalLibre(Profesional profesional, String fecha, String horario) {
        for (int i = 0; i < turnos.size(); i++) {
            Turno t = turnos.get(i);
            if (t.getProfesional().getMatricula() == profesional.getMatricula()
                    && t.getEstado() == EstadoTurno.PROGRAMADO
                    && t.getFecha().equals(fecha)
                    && t.getHorario().equals(horario)) {
                return false;
            }
        }
        return true;
    }

    // Horarios del día en los que el profesional todavía no tiene turno
    public List<String> horariosLibres(Profesional profesional, String fecha) {
        List<String> libres = new ArrayList<>();
        String[] todos = Horario.generarHorariosDelDia();
        for (int i = 0; i < todos.length; i++) {
            if (profesionalLibre(profesional, fecha, todos[i])) {
                libres.add(todos[i]);
            }
        }
        return libres;
    }

    // Devuelve "" si se puede dar el turno, o el motivo por el que no se puede
    public String validarTurno(Profesional profesional, Servicio servicio, String fecha, String horario) {
        if (!Horario.esFechaValida(fecha)) {
            return "La fecha no es válida. Usá el formato aaaa-mm-dd.";
        }
        if (!Horario.esValido(horario)) {
            return "El horario no es válido. Se atiende de " + Horario.HORA_APERTURA + ":00 a "
                    + Horario.HORA_CIERRE + ":00, en bloques de " + Horario.DURACION_MINUTOS + " minutos.";
        }
        if (!servicio.admiteEspecialidad(profesional.getEspecialidad())) {
            return "El profesional no tiene la especialidad que requiere el servicio.";
        }
        if (!profesionalLibre(profesional, fecha, horario)) {
            return "El profesional ya tiene un turno en ese horario.";
        }
        return "";
    }

    // ---------- Turnos ----------

    // Antes de llamarlo hay que usar validarTurno
    public Turno crearTurno(Mascota mascota, Profesional profesional, Servicio servicio, String fecha, String horario) {
        Turno turno = new Turno(proximoIdTurno, mascota, profesional, servicio, fecha, horario);
        proximoIdTurno++;
        turnos.add(turno);
        return turno;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public List<Profesional> getProfesionales() {
        return profesionales;
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public List<Turno> getTurnos() {
        return turnos;
    }
}
