package udla.adminus.mmunoz;

public class Administrativo extends Empleado {
    private String cargo;
    private String area;

    public Administrativo(String cedula, String nombre, int edad, String genero,
                          String direccion, long telefono, String email,
                          double sueldoMensual, String jornadaLaboral, int horasTrabajadas,
                          String cargo, String area) {
        super(cedula, nombre, edad, genero, direccion, telefono, email,
                sueldoMensual, jornadaLaboral, horasTrabajadas);
        this.cargo = cargo;
        this.area = area;
    }

    @Override
    public double calcularNomina() {
        return this.sueldoMensual;
    }

    @Override
    public String mostrarInformacion() {
        return "=== INFORMACIÓN DEL ADMINISTRATIVO ===\n" +
                "Cédula: " + this.cedula + "\n" +
                "Nombre: " + this.nombre + "\n" +
                "Edad: " + this.edad + " años\n" +
                "Género: " + this.genero + "\n" +
                "Cargo: " + this.cargo + "\n" +
                "Área: " + this.area + "\n" +
                "Jornada Laboral: " + this.jornadaLaboral + "\n" +
                "Horas Trabajadas: " + this.horasTrabajadas + " horas\n" +
                "Sueldo Mensual: $" + this.sueldoMensual + "\n";
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCargo() {
        return cargo;
    }

    public String getArea() {
        return area;
    }

    // Métodos agregados para compatibilidad con SQL
    public String getJornadaLaboral() {
        return this.jornadaLaboral;
    }

    public int getHorasTrabajadas() {
        return this.horasTrabajadas;
    }

    public double getSueldo() {
        return this.sueldoMensual;
    }
}