package udla.adminus.mmunoz;

import java.util.ArrayList;
import java.util.List;

public class Docente extends Empleado {
    private String especialidad;
    private String tituloAcademico;
    private int cargaHoraria;
    private List<String> cursosAsignados;
    private Materia materiaAsignada;

    public Docente(String cedula, String nombre, int edad, String genero,
                   String direccion, long telefono, String email,
                   double sueldoMensual, String jornadaLaboral, int horasTrabajadas,
                   String especialidad, String tituloAcademico, int cargaHoraria) {
        super(cedula, nombre, edad, genero, direccion, telefono, email,
                sueldoMensual, jornadaLaboral, horasTrabajadas);
        this.especialidad = especialidad;
        this.tituloAcademico = tituloAcademico;
        this.cargaHoraria = cargaHoraria;
        this.cursosAsignados = new ArrayList<>();
    }

    public void addCurso(String curso) {
        this.cursosAsignados.add(curso);
    }

    public void setMateriaAsignada(Materia materia) {
        this.materiaAsignada = materia;
    }

    public Materia getMateriaAsignada() {
        return materiaAsignada;
    }

    @Override
    public double calcularNomina() {
        return this.sueldoMensual;
    }

    @Override
    public String mostrarInformacion() {
        String info = "=== INFORMACIÓN DEL DOCENTE ===\n" +
                "Cédula: " + this.cedula + "\n" +
                "Nombre: " + this.nombre + "\n" +
                "Edad: " + this.edad + " años\n" +
                "Género: " + this.genero + "\n" +
                "Especialidad: " + this.especialidad + "\n" +
                "Título Académico: " + this.tituloAcademico + "\n" +
                "Jornada Laboral: " + this.jornadaLaboral + "\n" +
                "Carga Horaria: " + this.cargaHoraria + " horas\n" +
                "Sueldo Mensual: $" + this.sueldoMensual + "\n";

        if(materiaAsignada != null) {
            info += "Materia Asignada: " + materiaAsignada.name() + "\n";
        }

        info += "Cursos Asignados:\n";
        for(String curso : this.cursosAsignados) {
            info += "  - " + curso + "\n";
        }

        return info;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public void setTituloAcademico(String tituloAcademico) {
        this.tituloAcademico = tituloAcademico;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public String getTituloAcademico() {
        return tituloAcademico;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public List<String> getCursosAsignados() {
        return cursosAsignados;
    }

    // Métodos agregados para compatibilidad con SQL
    public String getJornadaLaboral() {
        return this.jornadaLaboral;
    }

    public double getSueldo() {
        return this.sueldoMensual;
    }
}