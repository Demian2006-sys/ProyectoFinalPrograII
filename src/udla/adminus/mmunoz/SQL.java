package udla.adminus.mmunoz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQL {
    public Connection getConnection() {
        String url = "jdbc:mysql://localhost:3306/adminus";
        String user = "root";
        String passwd = "sasa";

        try {
            Connection conn = DriverManager.getConnection(url, user, passwd);
            return conn;
        } catch (SQLException ex) {
            System.out.println("Error estableciendo la conexión con la base de datos:");
            ex.printStackTrace();
        }
        return null;
    }

    public boolean verificarUsuarioExiste(String cedula, Connection conn) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT cedula FROM Usuario WHERE cedula = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, cedula);
            rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("Error al verificar usuario:");
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int verificarTipoUsuario(String cedula, Connection conn) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT cedula FROM Estudiante WHERE cedula = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, cedula);
            rs = ps.executeQuery();

            if (rs.next()) {
                return 1;
            }

            rs.close();
            ps.close();

            sql = "SELECT cedula FROM Docente WHERE cedula = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, cedula);
            rs = ps.executeQuery();

            if (rs.next()) {
                return 2;
            }

            rs.close();
            ps.close();

            sql = "SELECT cedula FROM Administrativo WHERE cedula = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, cedula);
            rs = ps.executeQuery();

            if (rs.next()) {
                return 3;
            }

            return 0;
        } catch (SQLException ex) {
            System.out.println("Error al verificar tipo de usuario:");
            ex.printStackTrace();
            return 0;
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void insertarDatos(Estudiante estudiante, Connection conn) {
        PreparedStatement psUsuario = null;
        PreparedStatement psEstudiante = null;

        try {
            conn.setAutoCommit(false);
            String sqlUsuario = """
                        INSERT INTO Usuario (cedula, nombre_completo, edad, genero, direccion, telefono, email)
                        VALUES (?, ?, ?, ?, ?, ?, ?)
                    """;
            psUsuario = conn.prepareStatement(sqlUsuario);
            psUsuario.setString(1, estudiante.getCedula());
            psUsuario.setString(2, estudiante.getNombre());
            psUsuario.setInt(3, estudiante.getEdad());
            psUsuario.setString(4, estudiante.getGenero());
            psUsuario.setString(5, estudiante.getDireccion());
            psUsuario.setLong(6, estudiante.getTelefono());
            psUsuario.setString(7, estudiante.getEmail());

            int usuarioResultado = psUsuario.executeUpdate();

            String sqlEstudiante = """
                        INSERT INTO Estudiante (cedula, nivel_educativo, paralelo, representante)
                        VALUES (?, ?, ?, ?)
                    """;
            psEstudiante = conn.prepareStatement(sqlEstudiante);
            psEstudiante.setString(1, estudiante.getCedula());
            psEstudiante.setString(2, estudiante.getNivelEducativo());
            psEstudiante.setString(3, estudiante.getParalelo());
            psEstudiante.setString(4, estudiante.getDatosRepresentante());

            int estudianteResultado = psEstudiante.executeUpdate();

            if (usuarioResultado > 0 && estudianteResultado > 0) {
                System.out.println("El estudiante se ha insertado correctamente.");
                conn.commit();
            } else {
                System.out.println("No se pudo ingresar al estudiante");
                conn.rollback();
            }
        } catch (SQLException ex) {
            System.out.println("Error al ingresar el estudiante");
            ex.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                if (psUsuario != null) psUsuario.close();
                if (psEstudiante != null) psEstudiante.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void insertarDocente(Docente docente, Connection conn, String horario) {
        PreparedStatement psUsuario = null;
        PreparedStatement psDocente = null;

        try {
            conn.setAutoCommit(false);
            String sqlUsuario = """
                        INSERT INTO Usuario (cedula, nombre_completo, edad, genero, direccion, telefono, email)
                        VALUES (?, ?, ?, ?, ?, ?, ?)
                    """;
            psUsuario = conn.prepareStatement(sqlUsuario);
            psUsuario.setString(1, docente.getCedula());
            psUsuario.setString(2, docente.getNombre());
            psUsuario.setInt(3, docente.getEdad());
            psUsuario.setString(4, docente.getGenero());
            psUsuario.setString(5, docente.getDireccion());
            psUsuario.setLong(6, docente.getTelefono());
            psUsuario.setString(7, docente.getEmail());

            int usuarioResultado = psUsuario.executeUpdate();

            String sqlDocente = """
                        INSERT INTO Docente (cedula, especialidad, titulo_academico, jornada_laboral, sueldo, carga_horaria, horario_clases)
                        VALUES (?, ?, ?, ?, ?, ?, ?)
                    """;
            psDocente = conn.prepareStatement(sqlDocente);
            psDocente.setString(1, docente.getCedula());
            psDocente.setString(2, docente.getEspecialidad());
            psDocente.setString(3, docente.getTituloAcademico());
            psDocente.setString(4, docente.getJornadaLaboral());
            psDocente.setDouble(5, docente.getSueldo());
            psDocente.setInt(6, docente.getCargaHoraria());
            psDocente.setString(7, horario);

            int docenteResultado = psDocente.executeUpdate();

            if (usuarioResultado > 0 && docenteResultado > 0) {
                System.out.println("El docente se ha insertado correctamente.");
                conn.commit();
            } else {
                System.out.println("No se pudo ingresar al docente");
                conn.rollback();
            }
        } catch (SQLException ex) {
            System.out.println("Error al ingresar el docente");
            ex.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                if (psUsuario != null) psUsuario.close();
                if (psDocente != null) psDocente.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void insertarAdministrativo(Administrativo admin, Connection conn) {
        PreparedStatement psUsuario = null;
        PreparedStatement psAdmin = null;

        try {
            conn.setAutoCommit(false);
            String sqlUsuario = """
                        INSERT INTO Usuario (cedula, nombre_completo, edad, genero, direccion, telefono, email)
                        VALUES (?, ?, ?, ?, ?, ?, ?)
                    """;
            psUsuario = conn.prepareStatement(sqlUsuario);
            psUsuario.setString(1, admin.getCedula());
            psUsuario.setString(2, admin.getNombre());
            psUsuario.setInt(3, admin.getEdad());
            psUsuario.setString(4, admin.getGenero());
            psUsuario.setString(5, admin.getDireccion());
            psUsuario.setLong(6, admin.getTelefono());
            psUsuario.setString(7, admin.getEmail());

            int usuarioResultado = psUsuario.executeUpdate();

            String sqlAdmin = """
                        INSERT INTO Administrativo (cedula, cargo, area, jornada_laboral, horas_trabajadas, sueldo)
                        VALUES (?, ?, ?, ?, ?, ?)
                    """;
            psAdmin = conn.prepareStatement(sqlAdmin);
            psAdmin.setString(1, admin.getCedula());
            psAdmin.setString(2, admin.getCargo());
            psAdmin.setString(3, admin.getArea());
            psAdmin.setString(4, admin.getJornadaLaboral());
            psAdmin.setInt(5, admin.getHorasTrabajadas());
            psAdmin.setDouble(6, admin.getSueldo());

            int adminResultado = psAdmin.executeUpdate();

            if (usuarioResultado > 0 && adminResultado > 0) {
                System.out.println("El administrativo se ha insertado correctamente.");
                conn.commit();
            } else {
                System.out.println("No se pudo ingresar al administrativo");
                conn.rollback();
            }
        } catch (SQLException ex) {
            System.out.println("Error al ingresar el administrativo");
            ex.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                if (psUsuario != null) psUsuario.close();
                if (psAdmin != null) psAdmin.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void mostrarNotasEstudiante(String cedula, Connection conn) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT asignatura_id, parcial, nota, fecha_registro FROM Notas WHERE estudiante_cedula = ? ORDER BY asignatura_id, parcial";
            ps = conn.prepareStatement(sql);
            ps.setString(1, cedula);
            rs = ps.executeQuery();

            boolean hayNotas = false;
            System.out.println("\n===================== MIS NOTAS =====================");
            System.out.printf("%-25s %-15s %-10s %-15s%n", "ASIGNATURA", "PARCIAL", "NOTA", "FECHA");
            System.out.println("======================================================");

            Materia[] materias = Materia.values();

            while (rs.next()) {
                hayNotas = true;
                int asignaturaId = rs.getInt("asignatura_id");
                String parcial = rs.getString("parcial");
                double nota = rs.getDouble("nota");
                String fecha = rs.getString("fecha_registro");

                String nombreMateria = materias[asignaturaId - 1].name();

                System.out.printf("%-25s %-15s %-10.2f %-15s%n", nombreMateria, parcial, nota, fecha);
            }

            if (!hayNotas) {
                System.out.println("No hay notas registradas.");
            }
            System.out.println("======================================================");
        } catch (SQLException ex) {
            System.out.println("Error al mostrar notas del estudiante:");
            ex.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Error: ID de asignatura invalido en la base de datos.");
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void actualizarNota(String cedulaEstudiante, int asignaturaId, String parcial, double nota, Connection conn) {
        PreparedStatement ps = null;

        try {
            String sql = "UPDATE Notas SET nota = ? WHERE estudiante_cedula = ? AND asignatura_id = ? AND parcial = ?";
            ps = conn.prepareStatement(sql);
            ps.setDouble(1, nota);
            ps.setString(2, cedulaEstudiante);
            ps.setInt(3, asignaturaId);
            ps.setString(4, parcial);

            int filasActualizadas = ps.executeUpdate();
            if (filasActualizadas > 0) {
                System.out.println("Nota actualizada exitosamente.");
            } else {
                System.out.println("No se encontro el registro a actualizar.");
            }
        } catch (SQLException ex) {
            System.out.println("Error al actualizar nota:");
            ex.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void mostrarAsistencias(String cedulaDocente, Connection conn) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT e.cedula, u.nombre_completo, a.fecha, a.estado FROM Estudiante e JOIN Usuario u ON e.cedula = u.cedula JOIN Asistencia a ON e.cedula = a.estudiante_cedula ORDER BY a.fecha DESC";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            System.out.println("\n--- ASISTENCIAS DE ESTUDIANTES ---");
            while (rs.next()) {
                String cedula = rs.getString("cedula");
                String nombre = rs.getString("nombre_completo");
                String fecha = rs.getString("fecha");
                String estado = rs.getString("estado");
                System.out.println("Cedula: " + cedula + " | Nombre: " + nombre + " | Fecha: " + fecha + " | Estado: " + estado);
            }

        } catch (SQLException ex) {
            System.out.println("Error al mostrar asistencias:");
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void registrarAsistencia(String cedulaEstudiante, String fecha, String estado, String cedulaDocente, Connection conn) {
        PreparedStatement ps = null;

        try {
            String sql = "INSERT INTO Asistencia (cedula_estudiante, fecha, estado, cedula_docente) VALUES (?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, cedulaEstudiante);
            ps.setString(2, fecha);
            ps.setString(3, estado);
            ps.setString(4, cedulaDocente);

            int resultado = ps.executeUpdate();
            if (resultado > 0) {
                System.out.println("Asistencia registrada correctamente.");
            } else {
                System.out.println("Error al registrar asistencia.");
            }
        } catch (SQLException ex) {
            System.out.println("Error al registrar asistencia:");
            ex.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void mostrarCursoDocente(String cedulaDocente, Connection conn) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT especialidad FROM Docente WHERE cedula = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, cedulaDocente);
            rs = ps.executeQuery();

            if (rs.next()) {
                String curso = rs.getString("especialidad");
                System.out.println("Curso asignado: " + curso);
            } else {
                System.out.println("No se encontro informacion del curso.");
            }
        } catch (SQLException ex) {
            System.out.println("Error al mostrar curso:");
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void mostrarHorarioDocente(String cedulaDocente, Connection conn) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT horario_clases FROM Docente WHERE cedula = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, cedulaDocente);
            rs = ps.executeQuery();

            if (rs.next()) {
                String horario = rs.getString("horario_clases");
                System.out.println("Horario: " + horario);
            } else {
                System.out.println("No se encontro informacion del horario.");
            }
        } catch (SQLException ex) {
            System.out.println("Error al mostrar horario:");
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void mostrarTodosLosDocentes(Connection conn) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT u.nombre_completo, d.especialidad, d.titulo_academico FROM Docente d JOIN Usuario u ON d.cedula = u.cedula";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            boolean hayDocentes = false;
            System.out.println("\n=== DOCENTES REGISTRADOS ===");
            System.out.println("MATERIA\t\t\t\tNOMBRE\t\t\t\tTITULO");
            System.out.println("================================================================================");

            while (rs.next()) {
                hayDocentes = true;
                String nombre = rs.getString("nombre_completo");
                String especialidad = rs.getString("especialidad");
                String titulo = rs.getString("titulo_academico");
                System.out.println(especialidad + "\t\t\t" + nombre + "\t\t\t" + titulo);
            }

            if (!hayDocentes) {
                System.out.println("No se han ingresado ningun docente.");
            }
        } catch (SQLException ex) {
            System.out.println("Error al mostrar docentes:");
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void mostrarTodasLasMaterias(Connection conn) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Materia[] todasLasMaterias = Materia.values();
            System.out.println("\n=== TODAS LAS MATERIAS ===");
            System.out.println("MATERIA\t\t\t\tDOCENTE ASIGNADO");
            System.out.println("================================================================================");

            for (int i = 0; i < todasLasMaterias.length; i++) {
                String nombreMateria = todasLasMaterias[i].name();

                String sql = "SELECT u.nombre_completo FROM Docente d JOIN Usuario u ON d.cedula = u.cedula WHERE d.especialidad = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, nombreMateria);
                rs = ps.executeQuery();

                String docenteAsignado = "Sin asignar";
                if (rs.next()) {
                    docenteAsignado = rs.getString("nombre_completo");
                }

                System.out.println(nombreMateria + "\t\t\t" + docenteAsignado);

                if (rs != null) rs.close();
                if (ps != null) ps.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error al mostrar materias:");
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void mostrarInformacionEstudiante(String cedula, Connection conn) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT u.cedula, u.nombre_completo, u.edad, u.genero, u.direccion, u.telefono, u.email, e.nivel_educativo, e.paralelo, e.representante FROM Usuario u JOIN Estudiante e ON u.cedula = e.cedula WHERE u.cedula = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, cedula);
            rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("\n=== MI INFORMACION PERSONAL ===");
                System.out.println("Cedula: " + rs.getString("cedula"));
                System.out.println("Nombre Completo: " + rs.getString("nombre_completo"));
                System.out.println("Edad: " + rs.getInt("edad"));
                System.out.println("Genero: " + rs.getString("genero"));
                System.out.println("Direccion: " + rs.getString("direccion"));
                System.out.println("Telefono: " + rs.getLong("telefono"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("\n=== INFORMACION ACADEMICA ===");
                System.out.println("Nivel Educativo: " + rs.getString("nivel_educativo"));
                System.out.println("Paralelo: " + rs.getString("paralelo"));
                System.out.println("Nombre del Representante: " + rs.getString("representante"));
            } else {
                System.out.println("No se encontro informacion del estudiante.");
            }
        } catch (SQLException ex) {
            System.out.println("Error al mostrar informacion del estudiante:");
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void insertarNota(String cedulaEstudiante, int asignaturaId, String parcial, double nota, Connection conn) {
        PreparedStatement ps = null;

        try {
            String sql = "INSERT INTO Notas (estudiante_cedula, asignatura_id, parcial, nota, fecha_registro) VALUES (?, ?, ?, ?, CURDATE())";
            ps = conn.prepareStatement(sql);
            ps.setString(1, cedulaEstudiante);
            ps.setInt(2, asignaturaId);
            ps.setString(3, parcial);
            ps.setDouble(4, nota);

            int filasInsertadas = ps.executeUpdate();
            if (filasInsertadas > 0) {
                System.out.println("Nota registrada exitosamente.");
            } else {
                System.out.println("No se pudo registrar la nota.");
            }
        } catch (SQLException ex) {
            System.out.println("Error al insertar nota:");
            ex.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void registrarAsistencia(String cedulaEstudiante, int asignaturaId, String fecha, String estado, Connection conn) {
        PreparedStatement ps = null;

        try {
            String sql = "INSERT INTO Asistencia (estudiante_cedula, asignatura_id, fecha, estado) VALUES (?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, cedulaEstudiante);
            ps.setInt(2, asignaturaId);
            ps.setString(3, fecha);
            ps.setString(4, estado);

            int filasInsertadas = ps.executeUpdate();
            if (filasInsertadas > 0) {
                System.out.println("Asistencia registrada exitosamente.");
            } else {
                System.out.println("No se pudo registrar la asistencia.");
            }
        } catch (SQLException ex) {
            System.out.println("Error al registrar asistencia:");
            ex.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void actualizarAsistencia(String cedulaEstudiante, int asignaturaId, String fecha, String estado, Connection conn) {
        PreparedStatement ps = null;

        try {
            String sql = "UPDATE Asistencia SET estado = ? WHERE estudiante_cedula = ? AND asignatura_id = ? AND fecha = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, estado);
            ps.setString(2, cedulaEstudiante);
            ps.setInt(3, asignaturaId);
            ps.setString(4, fecha);

            int filasActualizadas = ps.executeUpdate();
            if (filasActualizadas > 0) {
                System.out.println("Asistencia actualizada exitosamente.");
            } else {
                System.out.println("No se encontro el registro a actualizar.");
            }
        } catch (SQLException ex) {
            System.out.println("Error al actualizar asistencia:");
            ex.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void mostrarTodosLosEstudiantes(Connection conn) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT e.cedula, u.nombre_completo, e.nivel_educativo, e.paralelo " +
                    "FROM Estudiante e " +
                    "JOIN Usuario u ON e.cedula = u.cedula " +
                    "ORDER BY e.nivel_educativo, e.paralelo, u.nombre_completo";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            System.out.println("\n========================================================================================================");
            System.out.println("                                    LISTADO DE ESTUDIANTES");
            System.out.println("========================================================================================================");
            System.out.printf("%-15s %-35s %-25s %-15s%n", "CEDULA", "NOMBRE COMPLETO", "NIVEL EDUCATIVO", "PARALELO");
            System.out.println("--------------------------------------------------------------------------------------------------------");

            boolean hayEstudiantes = false;
            while (rs.next()) {
                hayEstudiantes = true;
                String cedula = rs.getString("cedula");
                String nombre = rs.getString("nombre_completo");
                String nivelEducativo = rs.getString("nivel_educativo");
                String paralelo = rs.getString("paralelo");

                System.out.printf("%-15s %-35s %-25s %-15s%n", cedula, nombre, nivelEducativo, paralelo);
            }

            if (!hayEstudiantes) {
                System.out.println("No hay estudiantes registrados en el sistema.");
            }

            System.out.println("========================================================================================================");

        } catch (SQLException ex) {
            System.out.println("Error al mostrar estudiantes:");
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void mostrarInformacionAdministrativo(String cedula, Connection conn) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT u.nombre_completo, u.edad, u.genero, u.direccion, u.telefono, u.email, " +
                    "a.sueldo, a.jornada_laboral, a.horas_trabajadas, a.cargo, a.area " +
                    "FROM Usuario u " +
                    "JOIN Administrativo a ON u.cedula = a.cedula " +
                    "WHERE u.cedula = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, cedula);
            rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("\n========================================");
                System.out.println("      MI INFORMACION");
                System.out.println("========================================");
                System.out.println("Cedula: " + cedula);
                System.out.println("Nombre: " + rs.getString("nombre_completo"));
                System.out.println("Edad: " + rs.getInt("edad"));
                System.out.println("Genero: " + rs.getString("genero"));
                System.out.println("Direccion: " + rs.getString("direccion"));
                System.out.println("Telefono: " + rs.getLong("telefono"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("\n--- DATOS LABORALES ---");
                System.out.println("Cargo: " + rs.getString("cargo"));
                System.out.println("Area: " + rs.getString("area"));
                System.out.println("Sueldo: $" + rs.getDouble("sueldo"));
                System.out.println("Jornada Laboral: " + rs.getString("jornada_laboral"));
                System.out.println("Horas Trabajadas: " + rs.getInt("horas_trabajadas"));
                System.out.println("========================================");
            } else {
                System.out.println("No se encontro informacion del administrativo.");
            }
        } catch (SQLException ex) {
            System.out.println("Error al mostrar informacion:");
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean verificarDocenteEnseñaMateria(String cedulaDocente, int asignaturaId, Connection conn) {
        String query = "SELECT COUNT(*) FROM docente_materia WHERE cedula_docente = ? AND id_asignatura = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, cedulaDocente);
            stmt.setInt(2, asignaturaId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        return false;
    }
}
