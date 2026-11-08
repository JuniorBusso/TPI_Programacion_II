package com.modelo;

public class Mascota {
    private int id;
    private String nombre;
    private String especie;

    // Para mascotas nuevas (el id lo asigna la BD)
    public Mascota(String nombre, String especie) {
        this.nombre = nombre;
        this.especie = especie;
    }

    // Para mascotas leídas de la BD
    public Mascota(int id, String nombre, String especie) {
        this(nombre, especie);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEspecie() {
        return especie;
    }

    @Override
    public String toString() {
        if (nombre == null){
            return "Sin nombre (" + especie + ")";
        }
        else {
            return nombre + " (" + especie + ")";
        }
    }
}