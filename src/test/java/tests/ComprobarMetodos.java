package tests;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class ComprobarMetodos {
    private ComprobarMetodos() {}

    /**
     * Comprueba si una clase tiene un método (por nombre) exactamente o "casi" (typos).
     * @param clazz Clase a inspeccionar
     * @param nombreMetodo Nombre esperado (ej: "ejercicio4_1")
     * @param maxDistanciaLevenshtein tolerancia a typos (0 = exacto, 1-2 recomendado)
     */
    public static boolean claseTieneMetodo(Class<?> clazz, String nombreMetodo, int maxDistanciaLevenshtein) {
        if (clazz == null) return false;

        String objetivo = Textos.normalizar(nombreMetodo);
        Method[] methods = clazz.getDeclaredMethods();

        for (Method m : methods) {
            String candidato = Textos.normalizar(m.getName());
            if (candidato.equals(objetivo)) return true;
            if (Textos.distanciaLevenshtein(candidato, objetivo) <= maxDistanciaLevenshtein) return true;
        }
        return false;
    }

    /**
     * Devuelve el nombre REAL del método más parecido (útil para mensajes de error).
     */
    public static String metodoMasParecido(Class<?> clazz, String nombreMetodo) {
        if (clazz == null) return null;
        List<String> nombres = Arrays.stream(clazz.getDeclaredMethods())
                .map(Method::getName)
                .collect(Collectors.toList());
        return Textos.masParecido(nombreMetodo, nombres);
    }

    /**
     * Comprueba si el "código fuente" de un método contiene una llamada a una función.
     * Esto funciona SI tienes el texto (String) del método o del archivo.
     *
     * Ejemplo: contieneLlamada(codigo, "getIterador", 1) -> true si aparece algo tipo getIterador(...)
     *
     * @param codigoMetodo texto del método o del archivo .java
     * @param nombreFuncion nombre de la función buscada
     * @param maxDistanciaLevenshtein tolerancia a typos (0 exacto; 1-2 recomendado)
     */
    public static boolean contieneLlamadaFuncion(String codigoMetodo, String nombreFuncion, int maxDistanciaLevenshtein) {
        String codigo = Textos.normalizar(codigoMetodo);
        String objetivo = Textos.normalizar(nombreFuncion);

        // 1) Intento exacto: patrón "nombreFuncion ("
        Pattern pExacto = Pattern.compile("\\b" + Pattern.quote(objetivo) + "\\s*\\(");
        if (pExacto.matcher(codigo).find()) return true;

        // 2) Intento "fuzzy": buscar identificadores + "(" y comparar por distancia
        // Captura cosas tipo foo(   bar(   getiterador(
        var m = Pattern.compile("\\b([a-z_][a-z0-9_]*)\\s*\\(").matcher(codigo);
        while (m.find()) {
            String candidato = m.group(1);
            int d = Textos.distanciaLevenshtein(candidato, objetivo);
            if (d <= maxDistanciaLevenshtein) return true;
        }

        return false;
    }

    /**
     * Variante: devuelve cuál fue la llamada más parecida encontrada (para depurar).
     */
    public static String llamadaMasParecidaEnCodigo(String codigoMetodo, String nombreFuncion) {
        String codigo = Textos.normalizar(codigoMetodo);
        String objetivo = Textos.normalizar(nombreFuncion);

        String best = null;
        int bestDist = Integer.MAX_VALUE;

        var m = Pattern.compile("\\b([a-z_][a-z0-9_]*)\\s*\\(").matcher(codigo);
        while (m.find()) {
            String candidato = m.group(1);
            int d = Textos.distanciaLevenshtein(candidato, objetivo);
            if (d < bestDist) {
                bestDist = d;
                best = candidato;
            }
        }
        return best;
    }
    public static String leerArchivoSiExiste(String... rutas) {
        for (String r : rutas) {
            try {
                java.nio.file.Path p = java.nio.file.Path.of(r);
                if (java.nio.file.Files.exists(p)) {
                    return java.nio.file.Files.readString(p);
                }
            } catch (Exception ignored) {}
        }
        return "";
    }
    public static String leerCodigoDeAlumnoSiExiste() {
        Path[] candidatos = {
                Path.of("..", "ED 3 Practica. Listas. Solucion", "src", "evaluador", "Alumno.java"),
                Path.of("src", "evaluador", "Alumno.java"),
                Path.of("src", "main", "java", "evaluador", "Alumno.java")
        };
        for (Path p : candidatos) {
            try {
                if (Files.exists(p)) return Files.readString(p);
            } catch (Exception ignored) {}
        }
        return "";
    }

    public static String extraerCuerpoMetodo(String codigo, String nombreMetodo) {
        int idx = codigo.indexOf(nombreMetodo + "(");
        if (idx < 0) return "";

        int braceOpen = codigo.indexOf('{', idx);
        if (braceOpen < 0) return "";

        int depth = 0;
        for (int i = braceOpen; i < codigo.length(); i++) {
            char c = codigo.charAt(i);
            if (c == '{') depth++;
            else if (c == '}') {
                depth--;
                if (depth == 0) return codigo.substring(braceOpen, i + 1);
            }
        }
        return "";
    }
    public static String leerMetodoDeClase(String nombreMetodo, String... posiblesRutas) {
        String codigo = leerArchivoSiExiste(posiblesRutas);
        if (codigo == null || codigo.isEmpty()) return "";
        return extraerCuerpoMetodo(codigo, nombreMetodo);
    }

}
