public class Horario {
    public static final int HORA_APERTURA = 8;
    public static final int HORA_CIERRE = 18;
    public static final int DURACION_MINUTOS = 30;

    // Convierte "09:30" en minutos desde las 00:00 (570)
    public static int aMinutos(String horario) {
        int hora = Integer.parseInt(horario.substring(0, 2));
        int minuto = Integer.parseInt(horario.substring(3, 5));
        return hora * 60 + minuto;
    }

    // Arma el texto "09:30" a partir de hora y minuto
    public static String formatear(int hora, int minuto) {
        String h = String.valueOf(hora);
        String m = String.valueOf(minuto);
        if (hora < 10) {
            h = "0" + h;
        }
        if (minuto < 10) {
            m = "0" + m;
        }
        return h + ":" + m;
    }

    // Un horario es válido si cae dentro de la atención y en un bloque de 30 min
    public static boolean esValido(int hora, int minuto) {
        if (hora < 0 || hora > 23 || minuto < 0 || minuto > 59) {
            return false;
        }
        int total = hora * 60 + minuto;
        int apertura = HORA_APERTURA * 60;
        int ultimoTurno = HORA_CIERRE * 60 - DURACION_MINUTOS;
        if (total < apertura || total > ultimoTurno) {
            return false;
        }
        return (total - apertura) % DURACION_MINUTOS == 0;
    }

    // Versión que recibe el texto "09:30"
    public static boolean esValido(String horario) {
        if (horario == null || horario.length() != 5 || horario.charAt(2) != ':') {
            return false;
        }
        if (!soloDigitos(horario.substring(0, 2)) || !soloDigitos(horario.substring(3, 5))) {
            return false;
        }
        int hora = Integer.parseInt(horario.substring(0, 2));
        int minuto = Integer.parseInt(horario.substring(3, 5));
        return esValido(hora, minuto);
    }

    // Devuelve todos los horarios posibles del día: 08:00, 08:30, ... 17:30
    public static String[] generarHorariosDelDia() {
        int apertura = HORA_APERTURA * 60;
        int ultimoTurno = HORA_CIERRE * 60 - DURACION_MINUTOS;
        int cantidad = (ultimoTurno - apertura) / DURACION_MINUTOS + 1;
        String[] horarios = new String[cantidad];
        for (int i = 0; i < cantidad; i++) {
            int total = apertura + i * DURACION_MINUTOS;
            horarios[i] = formatear(total / 60, total % 60);
        }
        return horarios;
    }

    // Valida una fecha escrita como "aaaa-mm-dd" (por ejemplo "2026-10-23")
    public static boolean esFechaValida(String fecha) {
        if (fecha == null || fecha.length() != 10 || fecha.charAt(4) != '-' || fecha.charAt(7) != '-') {
            return false;
        }
        if (!soloDigitos(fecha.substring(0, 4)) || !soloDigitos(fecha.substring(5, 7))
                || !soloDigitos(fecha.substring(8, 10))) {
            return false;
        }
        int anio = Integer.parseInt(fecha.substring(0, 4));
        int mes = Integer.parseInt(fecha.substring(5, 7));
        int dia = Integer.parseInt(fecha.substring(8, 10));
        if (mes < 1 || mes > 12 || dia < 1) {
            return false;
        }
        return dia <= diasDelMes(anio, mes);
    }

    private static int diasDelMes(int anio, int mes) {
        if (mes == 2) {
            boolean bisiesto = (anio % 4 == 0 && anio % 100 != 0) || anio % 400 == 0;
            if (bisiesto) {
                return 29;
            }
            return 28;
        }
        if (mes == 4 || mes == 6 || mes == 9 || mes == 11) {
            return 30;
        }
        return 31;
    }

    private static boolean soloDigitos(String texto) {
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
