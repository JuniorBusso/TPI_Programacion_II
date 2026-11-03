public enum Especialidad {
    CLINICA_GENERAL("Clínica general"),
    CIRUGIA("Cirugía"),
    DERMATOLOGIA("Dermatología");

    private String descripcion;

    Especialidad(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
