package com.modelo;

public class Profesional {
    private int id;
    private int matricula;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;
    private Especialidad especialidad;

    public Profesional(int matricula, String nombre, String apellido, String telefono, String correo, Especialidad especialidad) {
        this.matricula = matricula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.correo = correo;
        this.especialidad = especialidad;
    }

    public Profesional(int id, int matricula, String nombre, String apellido, String telefono, String correo, Especialidad especialidad) {
        this(matricula, nombre, apellido, telefono, correo, especialidad);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMatricula() {
        return matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    @Override
    public String toString() {
        return apellido + ", " + nombre + " (Mat. " + matricula + ", " + especialidad + ")";
    }
}