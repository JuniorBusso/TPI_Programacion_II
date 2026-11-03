public class Turno {
    private int id;
    private Mascota mascota;
    private Profesional profesional;
    private Servicio servicio;
    private String fecha;     // formato "aaaa-mm-dd"
    private String horario;   // formato "HH:mm"
    private EstadoTurno estado;

    // Todo turno nuevo arranca como PROGRAMADO
    public Turno(int id, Mascota mascota, Profesional profesional, Servicio servicio, String fecha, String horario) {
        this.id = id;
        this.mascota = mascota;
        this.profesional = profesional;
        this.servicio = servicio;
        this.fecha = fecha;
        this.horario = horario;
        this.estado = EstadoTurno.PROGRAMADO;
    }

    // Solo se puede cancelar un turno programado. Devuelve true si se pudo cancelar.
    public boolean cancelar() {
        if (estado != EstadoTurno.PROGRAMADO) {
            return false;
        }
        estado = EstadoTurno.CANCELADO;
        return true;
    }

    // Solo se puede finalizar un turno programado. Devuelve true si se pudo finalizar.
    public boolean finalizar() {
        if (estado != EstadoTurno.PROGRAMADO) {
            return false;
        }
        estado = EstadoTurno.FINALIZADO;
        return true;
    }

    public String descripcion() {
        return "Turno #" + id + " | " + mascota.getNombre() + " | " + profesional.getNombreCompleto()
                + " | " + servicio.getNombre() + " | " + fecha + " " + horario
                + " | " + estado.getDescripcion();
    }

    public int getId() {
        return id;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public Profesional getProfesional() {
        return profesional;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHorario() {
        return horario;
    }

    public EstadoTurno getEstado() {
        return estado;
    }

    public boolean isCancelado() {
        return estado == EstadoTurno.CANCELADO;
    }
}
