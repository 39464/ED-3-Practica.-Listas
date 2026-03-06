package tests;

import org.junit.jupiter.api.Test;
import java.lang.reflect.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para AlumnoBib usando reflexión para evitar errores de compilación si la clase no existe.
 */
public class AlumnoBibTest {

    private boolean clasesExisten() {
        try {
            Class.forName("evaluador.AlumnoBib");
            Class.forName("evaluador.Evaluacion");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Test
    void alumnoBib_nuevaEvaluacion_debe_insertar_correctamente() {
        if (!clasesExisten()) {
            org.junit.jupiter.api.Assumptions.assumeTrue(false,
                "Las clases requeridas no existen. Test omitido.");
            return;
        }

        try {
            Class<?> alumnioBibClass = Class.forName("evaluador.AlumnoBib");
            Class<?> evalClass = Class.forName("evaluador.Evaluacion");

            Constructor<?> alumnoBibConstr = alumnioBibClass.getConstructor(String.class, int.class);
            Constructor<?> evalConstr = evalClass.getConstructor(String.class, String.class, double.class);

            Object a = alumnoBibConstr.newInstance("Test", 1);
            Object ed = evalConstr.newInstance("ED", "Ordinaria", 6.0);

            Method nuevaEval = alumnioBibClass.getMethod("nuevaEvaluacion", evalClass);
            boolean r = (boolean) nuevaEval.invoke(a, ed);

            assertTrue(r, "nuevaEvaluacion debe devolver true al insertar");

            Method getNumAprobadas = alumnioBibClass.getMethod("getNumAprobadas");
            int numAprobadas = (int) getNumAprobadas.invoke(a);
            assertEquals(1, numAprobadas, "Debe tener 1 evaluación aprobada");

        } catch (Exception e) {
            fail("Error al ejecutar test: " + e.getMessage());
        }
    }

    @Test
    void alumnoBib_getNumAprobadas_cuenta_solo_aprobadas() {
        if (!clasesExisten()) {
            org.junit.jupiter.api.Assumptions.assumeTrue(false,
                "Las clases requeridas no existen. Test omitido.");
            return;
        }

        try {
            Class<?> alumnioBibClass = Class.forName("evaluador.AlumnoBib");
            Class<?> evalClass = Class.forName("evaluador.Evaluacion");

            Constructor<?> alumnoBibConstr = alumnioBibClass.getConstructor(String.class, int.class);
            Constructor<?> evalConstr = evalClass.getConstructor(String.class, String.class, double.class);

            Object a = alumnoBibConstr.newInstance("Test", 1);

            Method nuevaEval = alumnioBibClass.getMethod("nuevaEvaluacion", evalClass);
            nuevaEval.invoke(a, evalConstr.newInstance("ED", "Jun", 4.5));
            nuevaEval.invoke(a, evalConstr.newInstance("ED", "Jul", -1.0));
            nuevaEval.invoke(a, evalConstr.newInstance("ED", "Sep", 7.4));
            nuevaEval.invoke(a, evalConstr.newInstance("Algebra", "Jun", 6.4));

            Method getNumAprobadas = alumnioBibClass.getMethod("getNumAprobadas");
            int numAprobadas = (int) getNumAprobadas.invoke(a);
            assertEquals(2, numAprobadas, "Debe contar solo aprobadas (>= 5)");

        } catch (Exception e) {
            fail("Error al ejecutar test: " + e.getMessage());
        }
    }

    @Test
    void alumnoBib_mediaAprobadas_calcula_correctamente() {
        if (!clasesExisten()) {
            org.junit.jupiter.api.Assumptions.assumeTrue(false,
                "Las clases requeridas no existen. Test omitido.");
            return;
        }

        try {
            Class<?> alumnioBibClass = Class.forName("evaluador.AlumnoBib");
            Class<?> evalClass = Class.forName("evaluador.Evaluacion");

            Constructor<?> alumnoBibConstr = alumnioBibClass.getConstructor(String.class, int.class);
            Constructor<?> evalConstr = evalClass.getConstructor(String.class, String.class, double.class);

            Object a = alumnoBibConstr.newInstance("Test", 1);

            Method nuevaEval = alumnioBibClass.getMethod("nuevaEvaluacion", evalClass);
            nuevaEval.invoke(a, evalConstr.newInstance("ED", "Jun", 6.0));
            nuevaEval.invoke(a, evalConstr.newInstance("Algebra", "Jun", 8.0));

            Method getMedia = alumnioBibClass.getMethod("mediaAprobadas");
            double media = (double) getMedia.invoke(a);
            assertEquals(7.0, media, 0.01, "La media de 6 y 8 debe ser 7");

        } catch (Exception e) {
            fail("Error al ejecutar test: " + e.getMessage());
        }
    }

    @Test
    void alumnoBib_estaAprobado_detecta_aprobados() {
        if (!clasesExisten()) {
            org.junit.jupiter.api.Assumptions.assumeTrue(false,
                "Las clases requeridas no existen. Test omitido.");
            return;
        }

        try {
            Class<?> alumnioBibClass = Class.forName("evaluador.AlumnoBib");
            Class<?> evalClass = Class.forName("evaluador.Evaluacion");

            Constructor<?> alumnoBibConstr = alumnioBibClass.getConstructor(String.class, int.class);
            Constructor<?> evalConstr = evalClass.getConstructor(String.class, String.class, double.class);

            Object a = alumnoBibConstr.newInstance("Test", 1);

            Method nuevaEval = alumnioBibClass.getMethod("nuevaEvaluacion", evalClass);
            nuevaEval.invoke(a, evalConstr.newInstance("ED", "Jun", 7.0));
            nuevaEval.invoke(a, evalConstr.newInstance("Algebra", "Jun", 4.0));

            Method estaAprobado = alumnioBibClass.getMethod("estaAprobado", String.class);

            boolean edAprobada = (boolean) estaAprobado.invoke(a, "ED");
            boolean algebraAprobada = (boolean) estaAprobado.invoke(a, "Algebra");

            assertTrue(edAprobada, "ED debe estar aprobada");
            assertFalse(algebraAprobada, "Algebra NO debe estar aprobada");

        } catch (Exception e) {
            fail("Error al ejecutar test: " + e.getMessage());
        }
    }
}
