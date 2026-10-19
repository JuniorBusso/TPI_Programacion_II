import java.util.ArrayList;
import java.util.List;

public class Servicio {
    private int id;
    private String nombre;
    private List<String> especialidadRequerida;

    public Servicio(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.especialidadRequerida = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public List<String> getEspecialidadRequerida() {
        return especialidadRequerida;
    }
}
 