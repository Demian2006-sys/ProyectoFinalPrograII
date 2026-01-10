package udla.adminus.mmunoz;

import java.util.ArrayList;
import java.util.List;

public class Estudiante extends Persona {
    private String nivelEducativo;
    private String paralelo;
    private String datosRepresentante;
    private List<String> asignaturas;

    public Estudiante(String cedula, String nombre, int edad, String genero,
                      String direccion, long telefono, String email,
                      String nivelEducativo, String paralelo, String datosRepresentante) {
        super(cedula, nombre, edad, genero, direccion, telefono, email);
        this.nivelEducativo = nivelEducativo;
        this.paralelo = paralelo;
        this.datosRepresentante = datosRepresentante;
        this.asignaturas = new ArrayList<>();
    }

    public void addAsignatura(String asignatura) {
        this.asignaturas.add(asignatura);
    }

    @Override
    public String mostrarInformacion() {
        return "=== INFORMACIÓN DEL ESTUDIANTE ===\n" +
                "Cédula: " + this.cedula + "\n" +
                "Nombre: " + this.nombre + "\n" +
                "Edad: " + this.edad + " años\n" +
                "Género: " + this.genero + "\n" +
                "Nivel Educativo: " + this.nivelEducativo + "\n" +
                "Paralelo: " + this.paralelo + "\n" +
                "Representante: " + this.datosRepresentante + "\n" +
                "Asignaturas: " + this.asignaturas + "\n";
    }

    public void setNivelEducativo(String nivelEducativo) {
        this.nivelEducativo = nivelEducativo;
    }

    public void setParalelo(String paralelo) {
        this.paralelo = paralelo;
    }

    public void setDatosRepresentante(String datosRepresentante) {
        this.datosRepresentante = datosRepresentante;
    }

    public String getNivelEducativo() {
        return nivelEducativo;
    }

    public String getParalelo() {
        return paralelo;
    }

    public String getDatosRepresentante() {
        return datosRepresentante;
    }

    // Método agregado para compatibilidad con SQL
    public String getRepresentante() {
        return datosRepresentante;
    }

    public List<String> getAsignaturas() {
        return asignaturas;
    }
}