import java.sql.Time;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        // Crear instancias de Mascota, Profesional y Servicio
        Mascota mascota = new Mascota(1, "Firulais", "Perro", "Labrador");
        Profesional profesional = new Profesional(12345, "Juan", "Pérez", "123456789", "juan.perez@email.com", "Veterinario");
        Servicio servicio = new Servicio(1, "Consulta General");
        Turno turno = new Turno(1, mascota, profesional, servicio, new Date(), new Time(System.currentTimeMillis()), false);
    }
}
