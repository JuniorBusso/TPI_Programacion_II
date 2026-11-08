package com.modelo.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.modelo.EstadoTurno;
import com.modelo.Turno;

public class TurnoDAO {
    private Connection conexion;
    private MascotaDAO mascotaDAO;
    private ProfesionalDAO profesionalDAO;
    private ServicioDAO servicioDAO;

    private static final String SELECT_TURNO =
        "SELECT id, mascota_id, profesional_id, servicio_id, fecha, horario, duracion_est, estado FROM turno";

    public TurnoDAO(Connection conexion) {
        this.conexion = conexion;
        this.mascotaDAO = new MascotaDAO(conexion);
        this.profesionalDAO = new ProfesionalDAO(conexion);
        this.servicioDAO = new ServicioDAO(conexion);
    }

    public void insertar(Turno turno) throws SQLException {
        String sql = "INSERT INTO turno (mascota_id, profesional_id, servicio_id, fecha, horario, duracion_est, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, turno.getMascota().getId());
            ps.setInt(2, turno.getProfesional().getId());
            ps.setInt(3, turno.getServicio().getId());
            ps.setDate(4, Date.valueOf(turno.getFecha()));
            ps.setTime(5, Time.valueOf(turno.getHorario()));
            ps.setInt(6, turno.getDuracionEst());
            ps.setString(7, turno.getEstado().name());
            ps.executeUpdate();

            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (claves.next()) {
                    turno.setId(claves.getInt(1));
                }
            }
        }
    }

    // Guarda el estado actual del turno (después de cancelar() o finalizar())
    public void actualizarEstado(Turno turno) throws SQLException {
        String sql = "UPDATE turno SET estado = ? WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, turno.getEstado().name());
            ps.setInt(2, turno.getId());
            ps.executeUpdate();
        }
    }

    // Devuelve null si no existe
    public Turno buscarPorId(int id) throws SQLException {
        String sql = SELECT_TURNO + " WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return armarTurno(rs);
                }
            }
        }
        return null;
    }

    public List<Turno> listarTodos() throws SQLException {
        List<Turno> turnos = new ArrayList<>();
        String sql = SELECT_TURNO + " ORDER BY fecha, horario";
        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                turnos.add(armarTurno(rs));
            }
        }
        return turnos;
    }

    // Turnos PROGRAMADOS de un profesional en un día (para controlar solapamientos)
    public List<Turno> listarProgramadosDeProfesional(int profesionalId, LocalDate fecha) throws SQLException {
        return listarProgramados("profesional_id", profesionalId, fecha);
    }

    // Turnos PROGRAMADOS de una mascota en un día (para controlar solapamientos)
    public List<Turno> listarProgramadosDeMascota(int mascotaId, LocalDate fecha) throws SQLException {
        return listarProgramados("mascota_id", mascotaId, fecha);
    }

    // "columna" siempre viene de las dos llamadas de arriba, nunca del usuario
    private List<Turno> listarProgramados(String columna, int id, LocalDate fecha) throws SQLException {
        List<Turno> turnos = new ArrayList<>();
        String sql = SELECT_TURNO + " WHERE " + columna + " = ? AND fecha = ? AND estado = ? ORDER BY horario";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setDate(2, Date.valueOf(fecha));
            ps.setString(3, EstadoTurno.PROGRAMADO.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    turnos.add(armarTurno(rs));
                }
            }
        }
        return turnos;
    }

    // Convierte los ids de la BD en objetos (mascota, profesional, servicio) usando los otros DAOs
    private Turno armarTurno(ResultSet rs) throws SQLException {
        return new Turno(
            rs.getInt("id"),
            mascotaDAO.buscarPorId(rs.getInt("mascota_id")),
            profesionalDAO.buscarPorId(rs.getInt("profesional_id")),
            servicioDAO.buscarPorId(rs.getInt("servicio_id")),
            rs.getDate("fecha").toLocalDate(),
            rs.getTime("horario").toLocalTime(),
            rs.getInt("duracion_est"),
            EstadoTurno.valueOf(rs.getString("estado"))
        );
    }
}