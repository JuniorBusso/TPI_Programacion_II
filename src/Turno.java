import java.sql.Time;
import java.util.Date;

public class Turno {
    private int id;
    private Mascota mascota;
    private Profesional profesional;
    private Servicio servicio;
    private Date fecha;
    private Time horario;
    private boolean cancelado;

    public Turno(int id, Mascota mascota, Profesional profesional, Servicio servicio, Date fecha, Time horario, boolean cancelado) {
        this.id = id;
        this.mascota = mascota;
        this.profesional = profesional;
        this.servicio = servicio;
        this.fecha = fecha;
        this.horario = horario;
        this.cancelado = cancelado;
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

    public Date getFecha() {
        return fecha;
    }

    public Time getHorario() {
        return horario;
    }

    public boolean isCancelado() {
        return cancelado;
    }
}
