public enum EstadoTurno {
    PROGRAMADO("Programado"),
    CANCELADO("Cancelado"),
    FINALIZADO("Finalizado");

    private String descripcion;

    EstadoTurno(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
