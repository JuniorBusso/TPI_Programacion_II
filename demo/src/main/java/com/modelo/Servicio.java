package com.modelo;

public class Servicio {
    private int id;
    private String nombre;
    private int duracionMin; // duración fija del servicio, en minutos
    private Especialidad especialidadRequerida;

    public Servicio(int id, String nombre, int duracionMin, Especialidad especialidadRequerida) {
        if (duracionMin <= 0) {
            throw new IllegalArgumentException("La duración del servicio debe ser mayor a 0");
        }
        this.id = id;
        this.nombre = nombre;
        this.duracionMin = duracionMin;
        this.especialidadRequerida = especialidadRequerida;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getDuracionMin() {
        return duracionMin;
    }

    public Especialidad getEspecialidadRequerida() {
        return especialidadRequerida;
    }

    @Override
    public String toString() {
        return nombre + " (" + duracionMin + " min) [" + especialidadRequerida + "]";
    }
}