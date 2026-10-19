public class Mascota {
    private int id;
    private String nombre;
    private String especie;
    private String raza;

    public Mascota(int id, String nombre, String especie, String raza) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public String getRaza() {
        return raza;
    }
}
