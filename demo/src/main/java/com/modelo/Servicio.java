package com.modelo;

public class Servicio {
    private int id;
    private String nombre;
    private Especialidad especialidadRequerida;

    public Servicio(int id, String nombre, Especialidad especialidadRequerida) {
        this.id = id;
        this.nombre = nombre;
        this.especialidadRequerida = especialidadRequerida;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Especialidad getEspecialidadRequerida() {
        return especialidadRequerida;
    }

    @Override
    public String toString() {
        return nombre + " [" + especialidadRequerida + "]";
    }
}