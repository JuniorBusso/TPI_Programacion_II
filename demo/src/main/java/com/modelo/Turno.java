package com.modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Turno {
    private int id;
    private Mascota mascota;
    private Profesional profesional;
    private Servicio servicio;
    private LocalDate fecha;
    private LocalTime horario;
    private int duracionEst; // duración estimada en minutos
    private EstadoTurno estado;

    // Para turnos nuevos: nace PROGRAMADO y sin id (lo asigna la BD)
    public Turno(Mascota mascota, Profesional profesional, Servicio servicio,
                 LocalDate fecha, LocalTime horario, int duracionEst) {
        this(0, mascota, profesional, servicio, fecha, horario, duracionEst, EstadoTurno.PROGRAMADO);
    }

    // Para turnos leídos de la BD
    public Turno(int id, Mascota mascota, Profesional profesional, Servicio servicio,
                 LocalDate fecha, LocalTime horario, int duracionEst, EstadoTurno estado) {
        if (mascota == null || profesional == null || servicio == null || fecha == null || horario == null) {
            throw new IllegalArgumentException("El turno necesita mascota, profesional, servicio, fecha y horario");
        }
        if (duracionEst <= 0) {
            throw new IllegalArgumentException("La duración estimada debe ser mayor a 0");
        }
        this.id = id;
        this.mascota = mascota;
        this.profesional = profesional;
        this.servicio = servicio;
        this.fecha = fecha;
        this.horario = horario;
        this.duracionEst = duracionEst;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public int getDuracionEst() {
        return duracionEst;
    }

    public EstadoTurno getEstado() {
        return estado;
    }

    public LocalTime getHoraFin() {
        return horario.plusMinutes(duracionEst);
    }

    // Solo un turno PROGRAMADO puede cancelarse o finalizarse
    public void cancelar() {
        validarProgramado();
        estado = EstadoTurno.CANCELADO;
    }

    // Solo se puede finalizar un turno de hoy o de una fecha pasada
    public void finalizar() {
        validarProgramado();
        if (fecha.isAfter(LocalDate.now())) {
            throw new IllegalStateException("No se puede finalizar un turno de una fecha futura (" + fecha + ")");
        }
        estado = EstadoTurno.FINALIZADO;
    }

    private void validarProgramado() {
        if (estado != EstadoTurno.PROGRAMADO) {
            throw new IllegalStateException("Solo se puede modificar un turno PROGRAMADO (estado actual: " + estado + ")");
        }
    }

    // Dos turnos se solapan si son del mismo día, ambos PROGRAMADOS,
    // y los intervalos [inicio, fin) se cruzan
    public boolean seSolapaCon(Turno otro) {
        if (estado != EstadoTurno.PROGRAMADO || otro.estado != EstadoTurno.PROGRAMADO) {
            return false;
        }
        if (!fecha.equals(otro.fecha)) {
            return false;
        }
        return horario.isBefore(otro.getHoraFin()) && otro.horario.isBefore(getHoraFin());
    }

    @Override
    public String toString() {
        return "Turno #" + id + " | " + fecha + " " + horario + " (" + duracionEst + " min) | "
                + mascota + " | " + servicio.getNombre() + " | Dr/a. "
                + profesional.getApellido() + " | " + estado;
    }
}