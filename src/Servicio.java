import java.util.ArrayList;
import java.util.List;

public class Servicio {
    private int id;
    private String nombre;
    private List<Especialidad> especialidadRequerida;

    public Servicio(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.especialidadRequerida = new ArrayList<>();
    }

    public void agregarEspecialidad(Especialidad especialidad) {
        especialidadRequerida.add(especialidad);
    }

    // Si el servicio no pide ninguna especialidad, lo puede atender cualquier profesional
    public boolean admiteEspecialidad(Especialidad especialidad) {
        if (especialidadRequerida.isEmpty()) {
            return true;
        }
        return especialidadRequerida.contains(especialidad);
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Especialidad> getEspecialidadRequerida() {
        return especialidadRequerida;
    }
}
