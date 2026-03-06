package tests;

import org.junit.jupiter.api.Test;
import java.lang.reflect.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para getNumAprobadas usando reflexión.
 * Verifica que el método tenga implementación real (no solo return directo).
 */
public class GetNumAprobadas {

    private boolean clasesYMetodosCompatibles() {
        try {
            Class<?> alumnoClass = Class.forName("evaluador.Alumno");
            Class<?> evalClass = Class.forName("evaluador.Evaluacion");

            alumnoClass.getConstructor(String.class, int.class);
            evalClass.getConstructor(String.class, String.class, double.class);
            alumnoClass.getMethod("nuevaEvaluacion", evalClass);
            alumnoClass.getMethod("getNumAprobadas");

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Test
    void getNumAprobadas_debe_contar_solo_aprobadas() {
        if (!clasesYMetodosCompatibles()) {
            org.junit.jupiter.api.Assumptions.assumeTrue(false,
                "Las clases o métodos requeridos no existen o son incompatibles. Test omitido.");
            return;
        }

        try {
            Class<?> alumnoClass = Class.forName("evaluador.Alumno");
            Class<?> evalClass = Class.forName("evaluador.Evaluacion");

            Constructor<?> alumnoConstructor = alumnoClass.getConstructor(String.class, int.class);
            Constructor<?> evalConstructor = evalClass.getConstructor(String.class, String.class, double.class);

            Object felipe = alumnoConstructor.newInstance("Felipe García Gómez", 1253);

            Method nuevaEval = alumnoClass.getMethod("nuevaEvaluacion", evalClass);
            nuevaEval.invoke(felipe, evalConstructor.newInstance("ED", "Junio 19", 4.5));
            nuevaEval.invoke(felipe, evalConstructor.newInstance("ED", "Julio 19", -1.0));
            nuevaEval.invoke(felipe, evalConstructor.newInstance("ED", "Junio 20", 7.4));
            nuevaEval.invoke(felipe, evalConstructor.newInstance("Algebra", "Junio 18", 6.4));

            Method getNumAprobadas = alumnoClass.getMethod("getNumAprobadas");
            int resultado = (int) getNumAprobadas.invoke(felipe);

            assertEquals(2, resultado,
                    "Debe contar solo las asignaturas aprobadas (nota >= 5)");
        } catch (Exception e) {
            fail("Error al ejecutar test: " + e.getMessage());
        }
    }

    @Test
    void getNumAprobadas_sin_evaluaciones_debe_devolver_0() {
        if (!clasesYMetodosCompatibles()) {
            org.junit.jupiter.api.Assumptions.assumeTrue(false,
                "Las clases o métodos requeridos no existen o son incompatibles. Test omitido.");
            return;
        }

        try {
            Class<?> alumnoClass = Class.forName("evaluador.Alumno");

            Constructor<?> alumnoConstructor = alumnoClass.getConstructor(String.class, int.class);
            Object alicia = alumnoConstructor.newInstance("Alicia Blázquez Martín", 5622);

            Method getNumAprobadas = alumnoClass.getMethod("getNumAprobadas");
            int resultado = (int) getNumAprobadas.invoke(alicia);

            assertEquals(0, resultado,
                    "Si no hay evaluaciones, debe devolver 0");
        } catch (Exception e) {
            fail("Error al ejecutar test: " + e.getMessage());
        }
    }

    @Test
    void getNumAprobadas_todas_suspendidas_o_np_debe_devolver_0() {
        if (!clasesYMetodosCompatibles()) {
            org.junit.jupiter.api.Assumptions.assumeTrue(false,
                "Las clases o métodos requeridos no existen o son incompatibles. Test omitido.");
            return;
        }

        try {
            Class<?> alumnoClass = Class.forName("evaluador.Alumno");
            Class<?> evalClass = Class.forName("evaluador.Evaluacion");

            Constructor<?> alumnoConstructor = alumnoClass.getConstructor(String.class, int.class);
            Constructor<?> evalConstructor = evalClass.getConstructor(String.class, String.class, double.class);

            Object a = alumnoConstructor.newInstance("X", 1);

            Method nuevaEval = alumnoClass.getMethod("nuevaEvaluacion", evalClass);
            nuevaEval.invoke(a, evalConstructor.newInstance("ED", "A", 4.9));
            nuevaEval.invoke(a, evalConstructor.newInstance("ED", "B", -1.0));
            nuevaEval.invoke(a, evalConstructor.newInstance("ED", "C", 0.0));

            Method getNumAprobadas = alumnoClass.getMethod("getNumAprobadas");
            int resultado = (int) getNumAprobadas.invoke(a);

            assertEquals(0, resultado,
                    "Si todas son suspensas o NP, debe devolver 0");
        } catch (Exception e) {
            fail("Error al ejecutar test: " + e.getMessage());
        }
    }

    @Test
    void getNumAprobadas_con_una_sola_aprobada() {
        if (!clasesYMetodosCompatibles()) {
            org.junit.jupiter.api.Assumptions.assumeTrue(false,
                "Las clases o métodos requeridos no existen o son incompatibles. Test omitido.");
            return;
        }

        try {
            Class<?> alumnoClass = Class.forName("evaluador.Alumno");
            Class<?> evalClass = Class.forName("evaluador.Evaluacion");

            Constructor<?> alumnoConstructor = alumnoClass.getConstructor(String.class, int.class);
            Constructor<?> evalConstructor = evalClass.getConstructor(String.class, String.class, double.class);

            Object a = alumnoConstructor.newInstance("Y", 2);

            Method nuevaEval = alumnoClass.getMethod("nuevaEvaluacion", evalClass);
            nuevaEval.invoke(a, evalConstructor.newInstance("ED", "A", 5.0));
            nuevaEval.invoke(a, evalConstructor.newInstance("ED", "B", 3.0));

            Method getNumAprobadas = alumnoClass.getMethod("getNumAprobadas");
            int resultado = (int) getNumAprobadas.invoke(a);

            assertEquals(1, resultado,
                    "Con una sola aprobada, debe devolver 1");
        } catch (Exception e) {
            fail("Error al ejecutar test: " + e.getMessage());
        }
    }

    @Test
    void getNumAprobadas_tiene_implementacion_real_no_solo_return() {
        String codigo = ComprobarMetodos.leerCodigoDeAlumnoSiExiste();
        assertFalse(codigo.isEmpty(),
                "No pude leer Alumno.java para comprobar la implementación");

        String cuerpo = ComprobarMetodos.extraerCuerpoMetodo(codigo, "getNumAprobadas");
        assertFalse(cuerpo.isEmpty(),
                "No se encontró el método getNumAprobadas en Alumno.java");

        // Normalizamos para búsqueda insensible a espacios
        String cuerpoNorm = Textos.normalizar(cuerpo);

        // Comprobación 1: No debe ser solo "return 0;" o "return -1;" o similar
        assertFalse(cuerpoNorm.matches(".*\\{\\s*return\\s+[0-9-]+\\s*;\\s*\\}.*"),
                () -> "getNumAprobadas no debe ser solo 'return <número>'. Debe tener lógica implementada.\n"
                        + Textos.diffDetallado("", cuerpo, 0, 150));

        // Comprobación 2: No debe ser solo "return null;"
        assertFalse(cuerpoNorm.contains("return null"),
                () -> "getNumAprobadas no debe ser solo 'return null'. Debe tener lógica implementada.\n"
                        + Textos.diffDetallado("", cuerpo, 0, 150));

        // Comprobación 3: Debe tener ALGO más que un return directo
        // Verificar que hay al menos un if, for, while, o una variable de control
        /*boolean tieneLogica = cuerpoNorm.contains("if") || cuerpoNorm.contains("for") ||
                              cuerpoNorm.contains("while") || cuerpoNorm.contains("++") ||
                              cuerpoNorm.contains("--") || cuerpoNorm.contains("+=");

        assertTrue(tieneLogica,
                () -> "getNumAprobadas debe tener lógica implementada (if, for, while, o incremento de variables).\n"
                        + Textos.diffDetallado("", cuerpo, 0, 150));
         */
    }

    @Test
    void getNumAprobadas_debe_usar_iterador_si_recorre_lista_ordinal() {
        String codigo = ComprobarMetodos.leerCodigoDeAlumnoSiExiste();
        assertFalse(codigo.isEmpty(),
                "No pude leer Alumno.java para comprobar el uso de iteradores");

        String cuerpo = ComprobarMetodos.extraerCuerpoMetodo(codigo, "getNumAprobadas");
        assertFalse(cuerpo.isEmpty(),
                "No se encontró el método getNumAprobadas en Alumno.java");

        boolean usaGetIterador = ComprobarMetodos.contieneLlamadaFuncion(cuerpo, "getIterador", 1);
        boolean usaHasNext = ComprobarMetodos.contieneLlamadaFuncion(cuerpo, "hasNext", 1);
        boolean usaNext = ComprobarMetodos.contieneLlamadaFuncion(cuerpo, "next", 1);

        // Detectar si usa bucle for con getElemento (acceso ordinal directo)
        String cuerpoNorm = Textos.normalizar(cuerpo);
        boolean usaBucleFor = cuerpoNorm.contains("for(") || cuerpoNorm.contains("for (");
        boolean usaGetElemento = ComprobarMetodos.contieneLlamadaFuncion(cuerpo, "getElemento", 1);
        
        // Si usa bucle for con getElemento, está mal (debe usar iterador o delegar)
        assertFalse(usaBucleFor && usaGetElemento,
                () -> "getNumAprobadas no debe usar bucle for con getElemento. Usa iteradores o delega en asignaturasAprobadas().\n"
                        + Textos.diffDetallado("", cuerpo, 2, 200));

        // Si recorre la lista con otros patrones, debe hacerlo con iterador
        if (cuerpoNorm.contains("lista")
                || cuerpoNorm.contains("evaluaciones")
                || cuerpoNorm.contains("expediente")) {

            // Si no usa for con getElemento, y está recorriendo, debe usar iterador O delegar
            boolean delegaCorrectamente = cuerpoNorm.contains("asignaturasaprobadas");
            boolean usaIteradorCorrectamente = usaGetIterador && usaHasNext && usaNext;
            
            assertTrue(
                    delegaCorrectamente || usaIteradorCorrectamente,
                    () -> "Si recorres una lista en getNumAprobadas, debes usar iterador o delegar en asignaturasAprobadas().\n"
                            + Textos.diffDetallado("", cuerpo, 2, 200)
            );
        }
    }
}
