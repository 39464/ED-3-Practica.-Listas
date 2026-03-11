package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para nuevaEvaluacion usando reflexión para evitar errores de compilación
 * cuando las clases aún no están completamente implementadas.
 */
public class NuevaEvaluacion {

    private PrintStream originalOut;
    private ByteArrayOutputStream out;

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    private boolean clasesExisten() {
        try {
            Class<?> alumnoClass = Class.forName("evaluador.Alumno");
            Class<?> evalClass = Class.forName("evaluador.Evaluacion");
            Class<?> listaClass = Class.forName("evaluador.Lista");

            // Verificar que los métodos existen
            alumnoClass.getConstructor(String.class, int.class);
            evalClass.getConstructor(String.class, String.class, double.class);
            alumnoClass.getMethod("nuevaEvaluacion", evalClass);
            alumnoClass.getMethod("asignaturasAprobadas");
            listaClass.getMethod("getIterador");

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Test
    void nuevaEvaluacion_evaluacion_nueva_se_inserta_y_devuelve_true() {
        if (!clasesExisten()) {
            org.junit.jupiter.api.Assumptions.assumeTrue(false,
                "Las clases requeridas no existen. Test omitido.");
            return;
        }

        try {
            Class<?> alumnoClass = Class.forName("evaluador.Alumno");
            Class<?> evalClass = Class.forName("evaluador.Evaluacion");

            Constructor<?> alumnoConstr = alumnoClass.getConstructor(String.class, int.class);
            Constructor<?> evalConstr = evalClass.getConstructor(String.class, String.class, double.class);

            Object a = alumnoConstr.newInstance("A", 1);
            Object ed = evalConstr.newInstance("ED", "Ordinaria", 6.0);

            Method nuevaEval = alumnoClass.getMethod("nuevaEvaluacion", evalClass);
            boolean r = (boolean) nuevaEval.invoke(a, ed);

            assertTrue(r, "Si la evaluación es nueva debe devolver true");

            Method asignaturasAprobadas = alumnoClass.getMethod("asignaturasAprobadas");
            Object lista = asignaturasAprobadas.invoke(a);

            assertEquals(1, contar(lista, "ED", "Ordinaria"),
                    "La evaluación nueva debe insertarse");
        } catch (Exception e) {
            fail("Error al ejecutar test: " + e.getMessage());
        }
    }

    @Test
    void nuevaEvaluacion_existe_misma_nota_devuelve_true_y_no_modifica() {
        if (!clasesExisten()) {
            org.junit.jupiter.api.Assumptions.assumeTrue(false,
                "Las clases requeridas no existen. Test omitido.");
            return;
        }

        try {
            Class<?> alumnoClass = Class.forName("evaluador.Alumno");
            Class<?> evalClass = Class.forName("evaluador.Evaluacion");

            Constructor<?> alumnoConstr = alumnoClass.getConstructor(String.class, int.class);
            Constructor<?> evalConstr = evalClass.getConstructor(String.class, String.class, double.class);

            Object a = alumnoConstr.newInstance("A", 1);

            Method nuevaEval = alumnoClass.getMethod("nuevaEvaluacion", evalClass);

            Object ed1 = evalConstr.newInstance("ED", "Ordinaria", 7.0);
            assertTrue((boolean) nuevaEval.invoke(a, ed1));

            Object edIgual = evalConstr.newInstance("ED", "Ordinaria", 7.0);
            boolean r = (boolean) nuevaEval.invoke(a, edIgual);

            assertTrue(r, "Si existe y la nota coincide debe devolver true");

            Method asignaturasAprobadas = alumnoClass.getMethod("asignaturasAprobadas");
            Object lista = asignaturasAprobadas.invoke(a);

            assertEquals(1, contar(lista, "ED", "Ordinaria"),
                    "No debe duplicar evaluaciones");
            assertEquals(7.0, obtenerNota(lista, "ED", "Ordinaria"), 1e-9,
                    "La nota debe mantenerse");
            assertEquals("", Textos.normalizar(out.toString()).trim(),
                    "No debe imprimir error si la nota coincide");
        } catch (Exception e) {
            fail("Error al ejecutar test: " + e.getMessage());
        }
    }

    @Test
    void nuevaEvaluacion_existe_nota_distinta_devuelve_false_y_no_modifica_y_muestra_error() {
        if (!clasesExisten()) {
            org.junit.jupiter.api.Assumptions.assumeTrue(false,
                "Las clases requeridas no existen. Test omitido.");
            return;
        }

        try {
            Class<?> alumnoClass = Class.forName("evaluador.Alumno");
            Class<?> evalClass = Class.forName("evaluador.Evaluacion");

            Constructor<?> alumnoConstr = alumnoClass.getConstructor(String.class, int.class);
            Constructor<?> evalConstr = evalClass.getConstructor(String.class, String.class, double.class);

            Object a = alumnoConstr.newInstance("A", 1);

            Method nuevaEval = alumnoClass.getMethod("nuevaEvaluacion", evalClass);

            Object ed1 = evalConstr.newInstance("ED", "Ordinaria", 6.0);
            assertTrue((boolean) nuevaEval.invoke(a, ed1));

            Object edDistinta = evalConstr.newInstance("ED", "Ordinaria", 9.0);
            boolean r = (boolean) nuevaEval.invoke(a, edDistinta);

            assertFalse(r, "Si existe y la nota es distinta debe devolver false");

            Method asignaturasAprobadas = alumnoClass.getMethod("asignaturasAprobadas");
            Object lista = asignaturasAprobadas.invoke(a);

            assertEquals(1, contar(lista, "ED", "Ordinaria"),
                    "No debe modificarse la lista");
            assertEquals(6.0, obtenerNota(lista, "ED", "Ordinaria"), 1e-9,
                    "La nota original debe mantenerse");
        } catch (Exception e) {
            fail("Error al ejecutar test: " + e.getMessage());
        }
    }

    @Test
    void nuevaEvaluacion_misma_asignatura_distinta_convocatoria_se_inserta() {
        if (!clasesExisten()) {
            org.junit.jupiter.api.Assumptions.assumeTrue(false,
                "Las clases requeridas no existen. Test omitido.");
            return;
        }

        try {
            Class<?> alumnoClass = Class.forName("evaluador.Alumno");
            Class<?> evalClass = Class.forName("evaluador.Evaluacion");

            Constructor<?> alumnoConstr = alumnoClass.getConstructor(String.class, int.class);
            Constructor<?> evalConstr = evalClass.getConstructor(String.class, String.class, double.class);

            Object a = alumnoConstr.newInstance("A", 1);

            Method nuevaEval = alumnoClass.getMethod("nuevaEvaluacion", evalClass);

            assertTrue((boolean) nuevaEval.invoke(a, evalConstr.newInstance("ED", "Ordinaria", 5.0)));
            assertTrue((boolean) nuevaEval.invoke(a, evalConstr.newInstance("ED", "Extraordinaria", 8.0)));

            Method asignaturasAprobadas = alumnoClass.getMethod("asignaturasAprobadas");
            Object lista = asignaturasAprobadas.invoke(a);

            assertEquals(2, contarAsignatura(lista, "ED"),
                    "Convocatorias distintas deben contarse como evaluaciones distintas");
        } catch (Exception e) {
            fail("Error al ejecutar test: " + e.getMessage());
        }
    }

    @Test
    void nuevaEvaluacion_debe_usar_iterador_para_recorrer_la_lista() {
        String cuerpo = ComprobarMetodos.leerMetodoDeClase(
            "nuevaEvaluacion",
            "../ED 3 Practica. Listas. Solucion/src/evaluador/Alumno.java",
            "src/evaluador/Alumno.java",
            "src/main/java/evaluador/Alumno.java"
        );

        assertFalse(cuerpo.isEmpty(),
            "No se pudo localizar el método nuevaEvaluacion en Alumno.java");

        assertTrue(
            ComprobarMetodos.contieneLlamadaFuncion(cuerpo, "getIterador", 1),
            "Debe usar getIterador() para recorrer la lista"
        );
        assertTrue(
            ComprobarMetodos.contieneLlamadaFuncion(cuerpo, "hasNext", 1),
            "Debe recorrer con hasNext()"
        );
        assertTrue(
            ComprobarMetodos.contieneLlamadaFuncion(cuerpo, "next", 1),
            "Debe recorrer con next()"
        );
    }

    // ---------- helpers (usando reflexión) ----------

    private int contar(Object lista, String asig, String conv) {
        try {
            Class<?> listaClass = lista.getClass();
            Class<?> iteradorClass = Class.forName("evaluador.Iterador");
            Class<?> evalClass = Class.forName("evaluador.Evaluacion");

            Method getIterador = listaClass.getMethod("getIterador");
            Object iterador = getIterador.invoke(lista);

            Method hasNext = iteradorClass.getMethod("hasNext");
            Method next = iteradorClass.getMethod("next");
            Method getNombreAsignatura = evalClass.getMethod("getNombreAsignatura");
            Method getConvocatoria = evalClass.getMethod("getConvocatoria");

            int c = 0;
            while ((boolean) hasNext.invoke(iterador)) {
                Object e = next.invoke(iterador);
                String nAsig = (String) getNombreAsignatura.invoke(e);
                String nConv = (String) getConvocatoria.invoke(e);
                if (asig.equals(nAsig) && conv.equals(nConv)) c++;
            }
            return c;
        } catch (Exception e) {
            fail("Error en contar: " + e.getMessage());
            return -1;
        }
    }

    private int contarAsignatura(Object lista, String asig) {
        try {
            Class<?> listaClass = lista.getClass();
            Class<?> iteradorClass = Class.forName("evaluador.Iterador");
            Class<?> evalClass = Class.forName("evaluador.Evaluacion");

            Method getIterador = listaClass.getMethod("getIterador");
            Object iterador = getIterador.invoke(lista);

            Method hasNext = iteradorClass.getMethod("hasNext");
            Method next = iteradorClass.getMethod("next");
            Method getNombreAsignatura = evalClass.getMethod("getNombreAsignatura");

            int c = 0;
            while ((boolean) hasNext.invoke(iterador)) {
                Object e = next.invoke(iterador);
                String nAsig = (String) getNombreAsignatura.invoke(e);
                if (asig.equals(nAsig)) c++;
            }
            return c;
        } catch (Exception e) {
            fail("Error en contarAsignatura: " + e.getMessage());
            return -1;
        }
    }

    private double obtenerNota(Object lista, String asig, String conv) {
        try {
            Class<?> listaClass = lista.getClass();
            Class<?> iteradorClass = Class.forName("evaluador.Iterador");
            Class<?> evalClass = Class.forName("evaluador.Evaluacion");

            Method getIterador = listaClass.getMethod("getIterador");
            Object iterador = getIterador.invoke(lista);

            Method hasNext = iteradorClass.getMethod("hasNext");
            Method next = iteradorClass.getMethod("next");
            Method getNombreAsignatura = evalClass.getMethod("getNombreAsignatura");
            Method getConvocatoria = evalClass.getMethod("getConvocatoria");
            Method getNota = evalClass.getMethod("getNota");

            while ((boolean) hasNext.invoke(iterador)) {
                Object e = next.invoke(iterador);
                String nAsig = (String) getNombreAsignatura.invoke(e);
                String nConv = (String) getConvocatoria.invoke(e);
                if (asig.equals(nAsig) && conv.equals(nConv)) {
                    return (double) getNota.invoke(e);
                }
            }
            fail("Evaluación no encontrada");
            return Double.NaN;
        } catch (Exception e) {
            fail("Error en obtenerNota: " + e.getMessage());
            return Double.NaN;
        }
    }
}

