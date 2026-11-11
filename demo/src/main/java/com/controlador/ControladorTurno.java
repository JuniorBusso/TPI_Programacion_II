package com.controlador;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.modelo.Cliente;
import com.modelo.Mascota;
import com.modelo.Profesional;
import com.modelo.Servicio;
import com.modelo.Turno;
import com.modelo.dao.ProfesionalDAO;
import com.modelo.dao.ServicioDAO;
import com.modelo.dao.TurnoDAO;
import com.vista.Vista;

public class ControladorTurno {

    // Horario de atención: de 08:00 a 16:00 (en minutos desde las 00:00)
    private static final int APERTURA_MIN = 8 * 60;
    private static final int CIERRE_MIN = 16 * 60;

    private Vista vista;
    private ControladorCliente controladorCliente;
    private TurnoDAO turnoDAO;
    private ServicioDAO servicioDAO;
    private ProfesionalDAO profesionalDAO;

    public ControladorTurno(Vista vista, Connection conexion, ControladorCliente controladorCliente) {
        this.vista = vista;
        this.controladorCliente = controladorCliente;
        this.turnoDAO = new TurnoDAO(conexion);
        this.servicioDAO = new ServicioDAO(conexion);
        this.profesionalDAO = new ProfesionalDAO(conexion);
    }

    // Opción 3 del menú
    public void agendarTurno() {
        try {
            // 1) Quién: cliente y mascota
            Cliente cliente = controladorCliente.elegirCliente();
            if (cliente == null) {
                return;
            }
            Mascota mascota = vista.elegir("Mascotas de " + cliente.getNombre() + ":", cliente.getMascotas());
            if (mascota == null) {
                return;
            }

            // 2) Qué: servicio, y profesionales con la especialidad que ese servicio requiere
            Servicio servicio = vista.elegir("Servicios disponibles:", servicioDAO.listarTodos());
            if (servicio == null) {
                return;
            }
            List<Profesional> profesionales =
                profesionalDAO.listarPorEspecialidad(servicio.getEspecialidadRequerida());
            Profesional profesional =
                vista.elegir("Profesionales para " + servicio.getNombre() + ":", profesionales);
            if (profesional == null) {
                return;
            }

            // 3) Cuándo
            int duracion = servicio.getDuracionMin(); // la duración la define el servicio
            LocalDate fecha = vista.pedirFecha();
            LocalTime hora = vista.pedirHora();

            // Si el horario no es válido se vuelve a pedir, pero el usuario puede abandonar
            String error = validarHorario(fecha, hora, duracion);
            while (error != null) {
                vista.mostrarError(error);
                if (!vista.confirmar("¿Desea ingresar otra fecha y hora?")) {
                    return;
                }
                fecha = vista.pedirFecha();
                hora = vista.pedirHora();
                error = validarHorario(fecha, hora, duracion);
            }

            // 4) Que no se superponga con otros turnos
            Turno turno = new Turno(mascota, profesional, servicio, fecha, hora, duracion);

            if (haySolapamiento(turnoDAO.listarProgramadosDeProfesional(profesional.getId(), fecha), turno)) {
                vista.mostrarError("El profesional ya tiene un turno que se superpone con ese horario");
                return;
            }
            if (haySolapamiento(turnoDAO.listarProgramadosDeMascota(mascota.getId(), fecha), turno)) {
                vista.mostrarError("La mascota ya tiene un turno que se superpone con ese horario");
                return;
            }

            // 5) Guardar
            turnoDAO.insertar(turno);
            vista.mostrarMensaje("Turno agendado correctamente:");
            vista.mostrarMensaje(turno.toString());

        } catch (SQLException e) {
            vista.mostrarError("No se pudo agendar el turno: " + e.getMessage());
        }
    }

    // Opción 5 del menú
    public void listarTurnos() {
        try {
            vista.mostrarLista("Turnos:", turnoDAO.listarTodos());
        } catch (SQLException e) {
            vista.mostrarError("No se pudieron listar los turnos: " + e.getMessage());
        }
    }

    // Opción 6 del menú
    public void cancelarTurno() {
        int id = vista.pedirIdTurno();
        try {
            Turno turno = turnoDAO.buscarPorId(id);
            if (turno == null) {
                vista.mostrarError("No existe un turno con ese id");
                return;
            }
            turno.cancelar(); // lanza IllegalStateException si no está PROGRAMADO
            turnoDAO.actualizarEstado(turno);
            vista.mostrarMensaje("Turno cancelado: " + turno);
        } catch (IllegalStateException e) {
            vista.mostrarError(e.getMessage());
        } catch (SQLException e) {
            vista.mostrarError("No se pudo cancelar el turno: " + e.getMessage());
        }
    }

    // Opción 7 del menú
    public void finalizarTurno() {
        int id = vista.pedirIdTurno();
        try {
            Turno turno = turnoDAO.buscarPorId(id);
            if (turno == null) {
                vista.mostrarError("No existe un turno con ese id");
                return;
            }
            turno.finalizar(); // lanza IllegalStateException si no está PROGRAMADO
            turnoDAO.actualizarEstado(turno);
            vista.mostrarMensaje("Turno finalizado: " + turno);
        } catch (IllegalStateException e) {
            vista.mostrarError(e.getMessage());
        } catch (SQLException e) {
            vista.mostrarError("No se pudo finalizar el turno: " + e.getMessage());
        }
    }

    // Devuelve el mensaje de error, o null si el horario es válido
    private String validarHorario(LocalDate fecha, LocalTime hora, int duracion) {
        // El turno completo (inicio y fin) debe estar dentro del horario de atención.
        int inicio = hora.getHour() * 60 + hora.getMinute();
        if (inicio < APERTURA_MIN || inicio + duracion > CIERRE_MIN) {
            return "El turno debe estar dentro del horario de atención (08:00 a 16:00)";
        }
        if (LocalDateTime.of(fecha, hora).isBefore(LocalDateTime.now())) {
            return "No se puede agendar un turno en el pasado";
        }
        return null;
    }

    private boolean haySolapamiento(List<Turno> existentes, Turno nuevo) {
        for (Turno existente : existentes) {
            if (nuevo.seSolapaCon(existente)) {
                return true;
            }
        }
        return false;
    }
}