package udla.adminus.mmunoz.usuario;

/**
 * Creacion de Clase abstracta que representa a un empleado de la institución.
 * Hereda de Persona e incluye información laboral básica.
 */

public abstract class Empleado extends Persona {
    protected double sueldoMensual;
    protected String jornadaLaboral;
    protected int horasTrabajadas;

    /**
     * Creacion de Constructor de la clase Empleado.
     */
    public Empleado(String cedula, String nombre, int edad, String genero,
                    String direccion, long telefono, String email,
                    double sueldoMensual, String jornadaLaboral, int horasTrabajadas) {
        super(cedula, nombre, edad, genero, direccion, telefono, email);
        this.sueldoMensual = sueldoMensual;
        this.jornadaLaboral = jornadaLaboral;
        this.horasTrabajadas = horasTrabajadas;
    }


    /**
     * Muestra la información básica del empleado
     */
    @Override
    public String mostrarInformacion() {
        return super.mostrarInformacion() +
                "Sueldo Mensual: $" + sueldoMensual + "\n" +
                "Jornada Laboral: " + jornadaLaboral + "\n" +
                "Horas Trabajadas: " + horasTrabajadas + "\n";
    }

    /** Métodos Get y Set */
    public abstract double calcularNomina();

    public void setSueldoMensual(double sueldoMensual) {
        this.sueldoMensual = sueldoMensual;
    }

    public void setJornadaLaboral(String jornadaLaboral) {
        this.jornadaLaboral = jornadaLaboral;
    }

    public void setHorasTrabajadas(int horasTrabajadas) {
        this.horasTrabajadas = horasTrabajadas;
    }

    public double getSueldoMensual() {
        return sueldoMensual;
    }

    public String getJornadaLaboral() {
        return jornadaLaboral;
    }

    public int getHorasTrabajadas() {
        return horasTrabajadas;
    }
}