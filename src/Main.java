import java.sql.Connection;
import java.util.Scanner;
import udla.adminus.mmunoz.*;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static SQL util = new SQL();

    private static String cedulaActual = "";
    private static int tipoUsuarioActual = 0;
    private static boolean sesionIniciada = false;

    public static void main(String[] args) {
        int opcion = 0;

        while(opcion != 3) {
            mostrarMenuInicial();
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch(opcion) {
                case 1:
                    iniciarSesion();
                    if(sesionIniciada) {
                        menuPrincipal();
                        sesionIniciada = false;
                    }
                    break;
                case 2:
                    registrarse();
                    break;
                case 3:
                    System.out.println("\nGracias por usar ADMINUS! Hasta pronto.");
                    break;
                default:
                    System.out.println("Opcion invalida. Intente nuevamente.");
            }
        }

        scanner.close();
    }

    private static void mostrarMenuInicial() {
        System.out.println("\n========================================");
        System.out.println("      BIENVENIDO A ADMINUS");
        System.out.println("========================================");
        System.out.println("1. Iniciar Sesion");
        System.out.println("2. Registrarse");
        System.out.println("3. Salir");
        System.out.print("Seleccione una opcion: ");
    }

    private static void registrarse() {
        System.out.println("\n=== REGISTRO DE NUEVO USUARIO ===");
        System.out.println("Seleccione el tipo de usuario:");
        System.out.println("1. Estudiante");
        System.out.println("2. Docente");
        System.out.println("3. Personal Administrativo");
        System.out.print("Opcion: ");

        int tipo = scanner.nextInt();
        scanner.nextLine();

        if(tipo < 1 || tipo > 3) {
            System.out.println("ERROR: Opcion invalida. Seleccione entre 1 y 3.");
            return;
        }

        System.out.println("\n--- DATOS PERSONALES ---");

        System.out.print("Cedula: ");
        String cedula = scanner.nextLine();
        if(cedula.isEmpty()) {
            System.out.println("ERROR: La cedula no puede estar vacia. Registro cancelado.");
            return;
        }

        Connection conn = util.getConnection();
        if (conn != null) {
            boolean existe = util.verificarUsuarioExiste(cedula, conn);
            if (existe) {
                System.out.println("ERROR: Esta cedula ya esta registrada en el sistema.");
                System.out.println("Si es su cuenta, puede iniciar sesion directamente.");
                return;
            }
        } else {
            System.out.println("ERROR: No se pudo conectar a la base de datos.");
            return;
        }

        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine();
        if(nombre.isEmpty()) {
            System.out.println("ERROR: El nombre no puede estar vacio. Registro cancelado.");
            return;
        }

        int edad = obtenerEdadValida();

        System.out.print("Genero: ");
        String genero = scanner.nextLine();
        if(genero.isEmpty()) {
            System.out.println("ERROR: El genero no puede estar vacio. Registro cancelado.");
            return;
        }

        System.out.print("Direccion: ");
        String direccion = scanner.nextLine();
        if(direccion.isEmpty()) {
            System.out.println("ERROR: La direccion no puede estar vacia. Registro cancelado.");
            return;
        }

        System.out.print("Telefono: ");
        long telefono = scanner.nextLong();
        scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();
        if(email.isEmpty()) {
            System.out.println("ERROR: El email no puede estar vacio. Registro cancelado.");
            return;
        }

        switch(tipo) {
            case 1:
                registrarNuevoEstudiante(cedula, nombre, edad, genero, direccion, telefono, email);
                break;
            case 2:
                registrarNuevoDocente(cedula, nombre, edad, genero, direccion, telefono, email);
                break;
            case 3:
                registrarNuevoAdministrativo(cedula, nombre, edad, genero, direccion, telefono, email);
                break;
            default:
                System.out.println("Opcion invalida.");
        }

    }

    private static void registrarNuevoEstudiante(String cedula, String nombre, int edad,
                                                 String genero, String direccion, long telefono, String email) {
        System.out.println("\n--- DATOS ACADEMICOS DEL ESTUDIANTE ---");

        System.out.println("Seleccione el nivel educativo:");
        System.out.println("1. Bachillerato");
        System.out.println("2. Basica");
        System.out.print("Seleccione una opcion: ");
        int opcionNivel = scanner.nextInt();
        scanner.nextLine();

        String nivel;
        if(opcionNivel == 1) {
            nivel = "Bachillerato";
        } else if(opcionNivel == 2) {
            nivel = "Basica";
        } else {
            System.out.println("ERROR: Opcion invalida. Registro cancelado.");
            return;
        }

        System.out.print("Paralelo: ");
        String paralelo = scanner.nextLine();
        if(paralelo.isEmpty()) {
            System.out.println("ERROR: El paralelo no puede estar vacio. Registro cancelado.");
            return;
        }

        System.out.print("Nombre del Representante: ");
        String representante = scanner.nextLine();
        if(representante.isEmpty()) {
            System.out.println("ERROR: Los datos del representante no pueden estar vacios. Registro cancelado.");
            return;
        }

        Estudiante estudiante = new Estudiante(cedula, nombre, edad, genero, direccion,
                telefono, email, nivel, paralelo, representante);

        Connection conn = util.getConnection();
        if (conn != null) {
            System.out.println("¡¡Conectados!!");
            util.insertarDatos(estudiante, conn);
        } else {
            System.out.println("NO conectado...");
            return;
        }

        System.out.println("\n--- MATERIAS PREDEFINIDAS ---");
        Materia[] todasLasMaterias = Materia.values();
        for(int i = 0; i < todasLasMaterias.length; i++) {
            estudiante.addAsignatura(todasLasMaterias[i].name());
            System.out.println((i+1) + ". " + todasLasMaterias[i].name());
        }

        System.out.println("\nEstudiante registrado exitosamente con todas las materias!");
        System.out.println(estudiante.mostrarInformacion());
    }

    private static void registrarNuevoDocente(String cedula, String nombre, int edad,
                                              String genero, String direccion, long telefono, String email) {
        System.out.println("\n--- DATOS LABORALES DEL DOCENTE ---");

        double sueldo = obtenerSueldoValido();

        System.out.print("Jornada Laboral: ");
        String jornada = scanner.nextLine();
        if(jornada.isEmpty()) {
            System.out.println("ERROR: La jornada no puede estar vacia. Registro cancelado.");
            return;
        }

        int horas = obtenerHorasTrabajadasValidas();

        System.out.print("Especialidad: ");
        String especialidad = scanner.nextLine();
        if(especialidad.isEmpty()) {
            System.out.println("ERROR: La especialidad no puede estar vacia. Registro cancelado.");
            return;
        }

        System.out.print("Titulo Academico: ");
        String titulo = scanner.nextLine();
        if(titulo.isEmpty()) {
            System.out.println("ERROR: El titulo no puede estar vacio. Registro cancelado.");
            return;
        }

        System.out.print("Carga Horaria: ");
        int carga = obtenerHorasTrabajadasValidas();

        System.out.print("Horario de Clases (Ej: Lunes-Viernes 8:00-12:00): ");
        String horario = scanner.nextLine();
        if(horario.isEmpty()) {
            System.out.println("ERROR: El horario no puede estar vacio. Registro cancelado.");
            return;
        }

        Docente docente = new Docente(cedula, nombre, edad, genero, direccion, telefono,
                email, sueldo, jornada, horas, especialidad, titulo, carga);

        Connection conn = util.getConnection();
        if (conn != null) {
            System.out.println("¡¡Conectados!!");
            util.insertarDocente(docente, conn, horario);
        } else {
            System.out.println("NO conectado...");
            return;
        }

        System.out.println("\n--- SELECCION DE MATERIA ---");
        System.out.println("Seleccione la materia que impartira:");
        Materia[] todasLasMaterias = Materia.values();
        for(int i = 0; i < todasLasMaterias.length; i++) {
            System.out.println((i+1) + ". " + todasLasMaterias[i].name());
        }
        System.out.print("Opcion: ");
        int opcionMateria = scanner.nextInt();
        scanner.nextLine();

        if(opcionMateria > 0 && opcionMateria <= todasLasMaterias.length) {
            docente.setMateriaAsignada(todasLasMaterias[opcionMateria - 1]);
            docente.addCurso(todasLasMaterias[opcionMateria - 1].name());
            System.out.println("Materia asignada: " + todasLasMaterias[opcionMateria - 1].name());
        } else {
            System.out.println("Opcion invalida. No se asigno materia.");
            return;
        }

        System.out.println("\nDocente registrado exitosamente!");
        System.out.println(docente.mostrarInformacion());
    }

    private static void registrarNuevoAdministrativo(String cedula, String nombre, int edad,
                                                     String genero, String direccion, long telefono, String email) {
        System.out.println("\n--- DATOS LABORALES DEL ADMINISTRATIVO ---");

        double sueldo = obtenerSueldoValido();

        System.out.print("Jornada Laboral: ");
        String jornada = scanner.nextLine();
        if(jornada.isEmpty()) {
            System.out.println("ERROR: La jornada no puede estar vacia. Registro cancelado.");
            return;
        }

        int horas = obtenerHorasTrabajadasValidas();

        System.out.print("Cargo: ");
        String cargo = scanner.nextLine();
        if(cargo.isEmpty()) {
            System.out.println("ERROR: El cargo no puede estar vacio. Registro cancelado.");
            return;
        }

        System.out.print("Area: ");
        String area = scanner.nextLine();
        if(area.isEmpty()) {
            System.out.println("ERROR: El area no puede estar vacia. Registro cancelado.");
            return;
        }

        Administrativo admin = new Administrativo(cedula, nombre, edad, genero, direccion,
                telefono, email, sueldo, jornada, horas, cargo, area);

        Connection conn = util.getConnection();
        if (conn != null) {
            System.out.println("¡¡Conectados!!");
            util.insertarAdministrativo(admin, conn);
        } else {
            System.out.println("NO conectado...");
            return;
        }

        System.out.println("\nAdministrativo registrado exitosamente!");
        System.out.println(admin.mostrarInformacion());
    }


    private static void iniciarSesion() {
        System.out.println("\n=== INICIAR SESION ===");
        System.out.print("Ingrese su cedula: ");
        String cedula = scanner.nextLine();

        if(cedula.isEmpty()) {
            System.out.println("ERROR: Debe ingresar una cedula valida.");
            System.out.println("Si no tiene cuenta, debe registrarse primero.");
            return;
        }

        Connection conn = util.getConnection();
        if (conn != null) {
            int tipoUsuario = util.verificarTipoUsuario(cedula, conn);
            if (tipoUsuario == 0) {
                System.out.println("ERROR: Usuario no registrado.");
                System.out.println("Debe registrarse primero antes de iniciar sesion.");
                return;
            }

            cedulaActual = cedula;
            tipoUsuarioActual = tipoUsuario;
            sesionIniciada = true;
            System.out.println("\nBienvenido/a");
        } else {
            System.out.println("ERROR: No se pudo conectar a la base de datos.");
            return;
        }
    }

private static void menuPrincipal() {
    int opcion = 0;

    if(tipoUsuarioActual == 1) {
        while(opcion != 6) {
            mostrarMenuEstudiante();
            opcion = scanner.nextInt();
            scanner.nextLine();
            menuEstudiante(opcion);
        }
    } else if(tipoUsuarioActual == 2) {
        while(opcion != 5) {
            mostrarMenuDocente();
            opcion = scanner.nextInt();
            scanner.nextLine();
            menuDocente(opcion);
        }
    } else if(tipoUsuarioActual == 3) {
        while(opcion != 6) {
            mostrarMenuAdministrativo();
            opcion = scanner.nextInt();
            scanner.nextLine();
            menuAdministrativo(opcion);
        }
    }
}

    private static void mostrarMenuEstudiante() {
        System.out.println("\n========================================");
        System.out.println("      MENU ESTUDIANTE");
        System.out.println("========================================");
        System.out.println("1. Ver mi informacion");
        System.out.println("2. Ver mis Notas");
        System.out.println("3. Ver mi Registro de Asistencia");
        System.out.println("4. Ver mis Docentes");
        System.out.println("5. Ver mis Materias");
        System.out.println("6. Cerrar Sesion");
        System.out.print("Seleccione una opcion: ");
    }

    private static void mostrarMenuDocente() {
        System.out.println("\n========================================");
        System.out.println("      MENU DOCENTE");
        System.out.println("========================================");
        System.out.println("1. Ver y Editar Notas de Estudiantes");
        System.out.println("2. Ver y Editar Asistencias");
        System.out.println("3. Ver mi Curso Asignado");
        System.out.println("4. Ver mi Horario de Clases");
        System.out.println("5. Cerrar Sesion");
        System.out.print("Seleccione una opcion: ");
    }

    private static void menuEstudiante(int opcion) {
        switch(opcion) {
            case 1:
                Connection connInfo = util.getConnection();
                if(connInfo != null) {
                    util.mostrarInformacionEstudiante(cedulaActual, connInfo);
                } else {
                    System.out.println("ERROR: No se pudo conectar a la base de datos.");
                }
                break;
            case 2:
                System.out.println("\n=== MIS NOTAS ===");
                Connection connNotas = util.getConnection();
                if(connNotas != null) {
                    util.mostrarNotasEstudiante(cedulaActual, connNotas);
                } else {
                    System.out.println("ERROR: No se pudo conectar a la base de datos.");
                }
                break;
            case 3:
                System.out.println("\n=== MI ASISTENCIA ===");
                Connection connAsistencia = util.getConnection();
                if(connAsistencia != null) {
                    util.mostrarAsistencias(cedulaActual, connAsistencia);
                } else {
                    System.out.println("ERROR: No se pudo conectar a la base de datos.");
                }
                break;
            case 4:
                Connection conn = util.getConnection();
                if(conn != null) {
                    util.mostrarTodosLosDocentes(conn);
                } else {
                    System.out.println("ERROR: No se pudo conectar a la base de datos.");
                }
                break;
            case 5:
                Connection conn2 = util.getConnection();
                if(conn2 != null) {
                    util.mostrarTodasLasMaterias(conn2);
                } else {
                    System.out.println("ERROR: No se pudo conectar a la base de datos.");
                }
                break;
            case 6:
                cerrarSesion();
                break;
            default:
                System.out.println("Opcion invalida.");
        }
    }


    private static void menuDocente(int opcion) {
        switch(opcion) {
            case 1:
                gestionarNotasEstudiantes();
                break;
            case 2:
                gestionarAsistencias();
                break;
            case 3:
                verCursoAsignado();
                break;
            case 4:
                verHorarioDocente();
                break;
            case 5:
                cerrarSesion();
                break;
            default:
                System.out.println("Opcion invalida.");
        }
    }

    private static void gestionarNotasEstudiantes() {
        System.out.println("\n=== GESTION DE NOTAS ===");
        System.out.println("1. Ingresar notas de estudiantes");
        System.out.println("2. Ver notas de estudiantes");
        System.out.println("3. Editar notas de estudiantes");
        System.out.print("Seleccione una opcion: ");
        int opcion = scanner.nextInt();
        scanner.nextLine();

        Connection conn = util.getConnection();
        if(conn == null) {
            System.out.println("ERROR: No se pudo conectar a la base de datos.");
            return;
        }

        if(opcion == 1) {
            System.out.print("Ingrese la cedula del estudiante: ");
            String cedulaEstudiante = scanner.nextLine();

            if(!verificarEstudianteExiste(cedulaEstudiante, conn)) {
                System.out.println("ERROR: El estudiante con cedula " + cedulaEstudiante + " no existe.");
                return;
            }

            System.out.println("\n--- MATERIAS DISPONIBLES ---");
            Materia[] materias = Materia.values();
            for(int i = 0; i < materias.length; i++) {
                System.out.println((i+1) + ". " + materias[i].name());
            }

            System.out.print("Ingrese el ID de la asignatura (1-7): ");
            int asignaturaId = scanner.nextInt();
            scanner.nextLine();

            if(asignaturaId < 1 || asignaturaId > 7) {
                System.out.println("ERROR: El ID debe estar entre 1 y 7.");
                return;
            }

            if(!verificarDocenteEnseñaMateria(cedulaActual, asignaturaId, conn)) {
                System.out.println("ERROR: Usted no enseña esta materia.");
                return;
            }

            System.out.print("Ingrese el parcial: ");
            String parcial = scanner.nextLine();

            System.out.print("Ingrese la nota (0-10): ");
            double nota = scanner.nextDouble();
            scanner.nextLine();

            if(nota >= 0 && nota <= 10) {
                util.insertarNota(cedulaEstudiante, asignaturaId, parcial, nota, conn);
            } else {
                System.out.println("ERROR: La nota debe estar entre 0 y 10.");
            }

        } else if(opcion == 2) {
            System.out.print("Ingrese la cedula del estudiante: ");
            String cedulaEstudiante = scanner.nextLine();

            if(!verificarEstudianteExiste(cedulaEstudiante, conn)) {
                System.out.println("ERROR: El estudiante con cedula " + cedulaEstudiante + " no existe.");
                return;
            }

            util.mostrarNotasEstudiante(cedulaEstudiante, conn);
        } else if(opcion == 3) {
            System.out.print("Ingrese la cedula del estudiante: ");
            String cedulaEstudiante = scanner.nextLine();

            if(!verificarEstudianteExiste(cedulaEstudiante, conn)) {
                System.out.println("ERROR: El estudiante con cedula " + cedulaEstudiante + " no existe.");
                return;
            }

            System.out.println("\n--- MATERIAS DISPONIBLES ---");
            Materia[] materias = Materia.values();
            for(int i = 0; i < materias.length; i++) {
                System.out.println((i+1) + ". " + materias[i].name());
            }

            System.out.print("Ingrese el ID de la asignatura (1-7): ");
            int asignaturaId = scanner.nextInt();
            scanner.nextLine();

            if(asignaturaId < 1 || asignaturaId > 7) {
                System.out.println("ERROR: El ID debe estar entre 1 y 7.");
                return;
            }

            if(!verificarDocenteEnseñaMateria(cedulaActual, asignaturaId, conn)) {
                System.out.println("ERROR: Usted no enseña esta materia.");
                return;
            }

            System.out.print("Ingrese el parcial: ");
            String parcial = scanner.nextLine();

            System.out.print("Ingrese la nueva nota (0-10): ");
            double nota = scanner.nextDouble();
            scanner.nextLine();

            if(nota >= 0 && nota <= 10) {
                util.actualizarNota(cedulaEstudiante, asignaturaId, parcial, nota, conn);
            } else {
                System.out.println("ERROR: La nota debe estar entre 0 y 10.");
            }
        } else {
            System.out.println("Opcion invalida.");
        }
    }


   private static void gestionarAsistencias() {
       System.out.println("\n=== GESTION DE ASISTENCIAS ===");
       System.out.println("1. Registrar asistencia de estudiantes");
       System.out.println("2. Ver asistencias de estudiantes");
       System.out.println("3. Editar asistencia de estudiantes");
       System.out.print("Seleccione una opcion: ");
       int opcion = scanner.nextInt();
       scanner.nextLine();

       Connection conn = util.getConnection();
       if(conn == null) {
           System.out.println("ERROR: No se pudo conectar a la base de datos.");
           return;
       }

       if(opcion == 1) {
           System.out.print("Ingrese la cedula del estudiante: ");
           String cedulaEstudiante = scanner.nextLine();

           System.out.println("\n--- MATERIAS DISPONIBLES ---");
           Materia[] materias = Materia.values();
           for(int i = 0; i < materias.length; i++) {
               System.out.println((i+1) + ". " + materias[i].name());
           }

           System.out.print("Ingrese el ID de la asignatura (1-7): ");
           int asignaturaId = scanner.nextInt();
           scanner.nextLine();

           if(asignaturaId < 1 || asignaturaId > 7) {
               System.out.println("ERROR: El ID debe estar entre 1 y 7.");
               return;
           }

           System.out.print("Ingrese la fecha (YYYY-MM-DD): ");
           String fecha = scanner.nextLine();

           System.out.println("\nEstado:");
           System.out.println("1. Presente");
           System.out.println("2. Ausente");
           System.out.println("3. Tarde");
           System.out.print("Seleccione una opcion: ");
           int estadoOpcion = scanner.nextInt();
           scanner.nextLine();

           String estado = "";
           if(estadoOpcion == 1) {
               estado = "Presente";
           } else if(estadoOpcion == 2) {
               estado = "Ausente";
           } else if(estadoOpcion == 3) {
               estado = "Tarde";
           } else {
               System.out.println("Opcion invalida.");
               return;
           }

           util.registrarAsistencia(cedulaEstudiante, asignaturaId, fecha, estado, conn);
       } else if(opcion == 2) {
           System.out.print("Ingrese la cedula del estudiante: ");
           String cedulaEstudiante = scanner.nextLine();
           util.mostrarAsistencias(cedulaEstudiante, conn);
       } else if(opcion == 3) {
           System.out.print("Ingrese la cedula del estudiante: ");
           String cedulaEstudiante = scanner.nextLine();

           System.out.println("\n--- MATERIAS DISPONIBLES ---");
           Materia[] materias = Materia.values();
           for(int i = 0; i < materias.length; i++) {
               System.out.println((i+1) + ". " + materias[i].name());
           }

           System.out.print("Ingrese el ID de la asignatura (1-7): ");
           int asignaturaId = scanner.nextInt();
           scanner.nextLine();

           if(asignaturaId < 1 || asignaturaId > 7) {
               System.out.println("ERROR: El ID debe estar entre 1 y 7.");
               return;
           }

           System.out.print("Ingrese la fecha (YYYY-MM-DD): ");
           String fecha = scanner.nextLine();

           System.out.println("\nNuevo estado:");
           System.out.println("1. Presente");
           System.out.println("2. Ausente");
           System.out.println("3. Tarde");
           System.out.print("Seleccione una opcion: ");
           int estadoOpcion = scanner.nextInt();
           scanner.nextLine();

           String estado = "";
           if(estadoOpcion == 1) {
               estado = "Presente";
           } else if(estadoOpcion == 2) {
               estado = "Ausente";
           } else if(estadoOpcion == 3) {
               estado = "Tarde";
           } else {
               System.out.println("Opcion invalida.");
               return;
           }

           util.actualizarAsistencia(cedulaEstudiante, asignaturaId, fecha, estado, conn);
       } else {
           System.out.println("Opcion invalida.");
       }
   }

    private static void verCursoAsignado() {
        System.out.println("\n=== MI CURSO ASIGNADO ===");
        Connection conn = util.getConnection();
        if(conn != null) {
            util.mostrarCursoDocente(cedulaActual, conn);
        } else {
            System.out.println("ERROR: No se pudo conectar a la base de datos.");
        }
    }

    private static void verHorarioDocente() {
        System.out.println("\n=== MI HORARIO DE CLASES ===");
        Connection conn = util.getConnection();
        if(conn != null) {
            util.mostrarHorarioDocente(cedulaActual, conn);
        } else {
            System.out.println("ERROR: No se pudo conectar a la base de datos.");
        }
    }

    private static void cerrarSesion() {
        System.out.println("\nSesion cerrada exitosamente.");
        cedulaActual = "";
        tipoUsuarioActual = 0;
    }
    private static void mostrarMenuAdministrativo() {
        System.out.println("1. Ver mi informacion");
        System.out.println("2. Ver Estudiantes");
        System.out.println("3. Ver y Editar Notas");
        System.out.println("4. Ver y Editar Asistencia");
        System.out.println("5. Ver y Editar Docentes");
        System.out.println("6. Cerrar Sesion");
    }
    private static void menuAdministrativo(int opcion) {
        switch(opcion) {
            case 1:
                Connection connInfo = util.getConnection();
                if(connInfo != null) {
                    util.mostrarInformacionAdministrativo(cedulaActual, connInfo);
                } else {
                    System.out.println("ERROR: No se pudo conectar a la base de datos.");
                }
                break;

            case 2:
           System.out.println("\n=== VER ESTUDIANTES ===");
           Connection connEstudiantes = util.getConnection();
           if(connEstudiantes != null) {
               util.mostrarTodosLosEstudiantes(connEstudiantes);
           } else {
               System.out.println("ERROR: No se pudo conectar a la base de datos.");
           }
           break;
            case 3:
                System.out.println("\n=== VER Y EDITAR NOTAS ===");
                gestionarNotasEstudiantes();

                break;
            case 4:
                System.out.println("\n=== VER Y EDITAR ASISTENCIA ===");
                gestionarAsistencias();

                break;
            case 5:
                System.out.println("\n=== VER Y EDITAR DOCENTES ===");

                break;
            case 6:
                cerrarSesion();
                break;
            default:
                System.out.println("Opcion invalida.");
        }
    }
    private static int obtenerEdadValida() {
        int edad = 0;
        boolean valido = false;
        while (!valido) {
            try {
                System.out.print("Edad: ");
                edad = scanner.nextInt();
                scanner.nextLine();
                if (edad >= 5 && edad <= 100) {
                    valido = true;
                } else {
                    System.out.println("ERROR: La edad debe estar entre 5 y 100 años.");
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("ERROR: Ingrese un numero valido.");
                scanner.nextLine();
            }
        }
        return edad;
    }

    private static double obtenerSueldoValido() {
        double sueldo = 0;
        boolean valido = false;
        while (!valido) {
            try {
                System.out.print("Sueldo Mensual: ");
                sueldo = scanner.nextDouble();
                scanner.nextLine();
                if (sueldo > 0) {
                    valido = true;
                } else {
                    System.out.println("ERROR: El sueldo debe ser mayor a 0.");
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("ERROR: Ingrese un numero valido.");
                scanner.nextLine();
            }
        }
        return sueldo;
    }

    private static int obtenerHorasTrabajadasValidas() {
        int horas = 0;
        boolean valido = false;
        while (!valido) {
            try {
                System.out.print("Horas Trabajadas: ");
                horas = scanner.nextInt();
                scanner.nextLine();
                if (horas > 0 && horas <= 24) {
                    valido = true;
                } else {
                    System.out.println("ERROR: Las horas trabajadas deben estar entre 1 y 24.");
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("ERROR: Ingrese un numero valido.");
                scanner.nextLine();
            }
        }
        return horas;
    }

    private static boolean verificarEstudianteExiste(String cedula, Connection conn) {
        return util.verificarUsuarioExiste(cedula, conn);
    }

    private static boolean verificarDocenteEnseñaMateria(String cedulaDocente, int asignaturaId, Connection conn) {
        return util.verificarDocenteEnseñaMateria(cedulaDocente, asignaturaId, conn);
    }

}