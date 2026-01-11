package udla.adminus.mmunoz;

import java.sql.Connection;
/**
 * Interfaz para mostrar información de una entidad.
 */
public interface Informacion {
    /**
     * Método para mostrar información básica.
     */
    String mostrarInformacion();
    /**
     * Método para mostrar información completa desde la base de datos.
     */
    void mostrarInformacionCompleta(String cedula, Connection conn);
}