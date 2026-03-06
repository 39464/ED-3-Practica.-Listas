package tests;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.lang.reflect.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Tests para numConvocatorias usando reflexión.
 */
public class NumConvocatoriasBasicos {

    private boolean clasesExisten() {
        try {
            Class.forName("evaluador.Evaluacion");
            Class.forName("evaluador.Lista");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Test
    void numConvocatorias_debe_ignorar_no_presentados_y_dar_los_valores_del_enunciado() {
        if (!clasesExisten()) {
            org.junit.jupiter.api.Assumptions.assumeTrue(false,
                "Las clases requeridas no existen. Test omitido.");
            return;
        }

        try {
            Class<?> listaClass = Class.forName("evaluador.Lista");
            Class<?> evalClass = Class.forName("evaluador.Evaluacion");

            Constructor<?> listaConstr = listaClass.getConstructor();
            Constructor<?> evalConstr = evalClass.getConstructor(String.class, String.class, double.class);

            Object listaEv = listaConstr.newInstance();

            Method insertar = listaClass.getMethod("insertar", evalClass);
            Method numConvocatorias = listaClass.getMethod("numConvocatorias", String.class);

            Object ed1 = evalConstr.newInstance("ED", "Junio 19", 4.5);
            Object ed2 = evalConstr.newInstance("ED", "Julio 19", -1.0);
            Object ed3 = evalConstr.newInstance("ED", "Junio 20", 7.4);
            Object algebra1 = evalConstr.newInstance("Algebra", "Junio 18", 6.4);

            insertar.invoke(listaEv, ed1);
            insertar.invoke(listaEv, ed2);
            insertar.invoke(listaEv, ed3);
            insertar.invoke(listaEv, algebra1);

            int convED = (int) numConvocatorias.invoke(listaEv, "ED");
            int convAlgebra = (int) numConvocatorias.invoke(listaEv, "Algebra");
            int convFP = (int) numConvocatorias.invoke(listaEv, "Fundamentos de Programación");

            assertEquals(2, convED, "Convocatorias en ED incorrectas (debe ignorar NP)");
            assertEquals(1, convAlgebra, "Convocatorias en Algebra incorrectas");
            assertEquals(0, convFP, "Convocatorias en Fundamentos de Programación incorrectas");

        } catch (Exception e) {
            org.junit.jupiter.api.Assertions.fail("Error al ejecutar test: " + e.getMessage());
        }
    }

    @Test
    void numConvocatorias_no_debe_usar_iteradores_en_su_implementacion() {
        String codigo = leerCodigoDeListaSiExiste();
        assertFalse(codigo.isEmpty(),
                "No pude leer el código fuente de Lista.java para comprobar la restricción de iteradores. " +
                        "Asegúrate de que existe en src/evaluador/Lista.java (o ajusta la ruta en el test).");

        // Buscamos el cuerpo del método numConvocatorias(...) y comprobamos que no aparezcan llamadas típicas
        String cuerpo = extraerCuerpoMetodo(codigo, "numConvocatorias");
        assertFalse(cuerpo.isEmpty(),
                "No pude localizar el método numConvocatorias en Lista.java (revisa nombre y firma).");

        // Comprobación: dentro del método no debería aparecer getIterador/Iterador/hasNext/next
        assertFalse(ComprobarMetodos.contieneLlamadaFuncion(cuerpo, "getIterador", 1),
                () -> "numConvocatorias parece usar iterador (getIterador).\n" + Textos.diffDetallado("", cuerpo, 0, 200));
        assertFalse(ComprobarMetodos.contieneLlamadaFuncion(cuerpo, "hasNext", 1),
                () -> "numConvocatorias parece usar iterador (hasNext).\n" + Textos.diffDetallado("", cuerpo, 0, 200));
        assertFalse(ComprobarMetodos.contieneLlamadaFuncion(cuerpo, "next", 1),
                () -> "numConvocatorias parece usar iterador (next).\n" + Textos.diffDetallado("", cuerpo, 0, 200));

        // También detectamos el tipo IteradorLista si lo escriben en el método
        assertFalse(Textos.normalizar(cuerpo).contains("iteradorlista"),
                "numConvocatorias parece declarar/usar IteradorLista. No está permitido.");
        assertFalse(Textos.normalizar(cuerpo).contains("iterador "),
                "numConvocatorias parece usar una variable 'iterador'. No está permitido.");
    }

    // -------- helpers --------

    private static String leerCodigoDeListaSiExiste() {
        Path[] candidatos = new Path[] {
                Path.of("..", "ED 3 Practica. Listas. Solucion", "src", "evaluador", "Lista.java"),
                Path.of("src", "evaluador", "Lista.java"),
                Path.of("src", "main", "java", "evaluador", "Lista.java")
        };

        for (Path p : candidatos) {
            try {
                if (Files.exists(p)) return Files.readString(p);
            } catch (Exception ignored) { }
        }
        return "";
    }

    private static String extraerCuerpoMetodo(String codigo, String nombreMetodo) {
        String src = codigo;
        int idx = src.indexOf(nombreMetodo + "(");
        if (idx < 0) return "";

        int braceOpen = src.indexOf('{', idx);
        if (braceOpen < 0) return "";

        int depth = 0;
        for (int i = braceOpen; i < src.length(); i++) {
            char c = src.charAt(i);
            if (c == '{') depth++;
            else if (c == '}') {
                depth--;
                if (depth == 0) {
                    return src.substring(braceOpen, i + 1);
                }
            }
        }
        return "";
    }
}

