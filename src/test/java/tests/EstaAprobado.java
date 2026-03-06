package tests;

import evaluador.Alumno;
import evaluador.Evaluacion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EstaAprobado {

    @Test
    void estaAprobado_devuelve_true_si_hay_alguna_aprobada_de_la_asignatura() {
        Alumno a = new Alumno("Felipe", 1);

        a.nuevaEvaluacion(new Evaluacion("ED", "Junio 19", 4.5)); // suspensa
        a.nuevaEvaluacion(new Evaluacion("ED", "Julio 19", -1));  // NP
        a.nuevaEvaluacion(new Evaluacion("ED", "Junio 20", 7.4)); // aprobada

        assertTrue(
                a.estaAprobado("ED"),
                "Debe devolver true si existe al menos una evaluación aprobada de la asignatura"
        );
    }

    @Test
    void estaAprobado_devuelve_false_si_no_hay_aprobadas() {
        Alumno a = new Alumno("Alicia", 2);

        a.nuevaEvaluacion(new Evaluacion("ED", "Junio 19", 4.9)); // suspensa
        a.nuevaEvaluacion(new Evaluacion("ED", "Julio 19", -1));  // NP

        assertFalse(
                a.estaAprobado("ED"),
                "Si no hay ninguna evaluación aprobada debe devolver false"
        );
    }

    @Test
    void estaAprobado_devuelve_false_si_no_existe_la_asignatura() {
        Alumno a = new Alumno("Pedro", 3);

        a.nuevaEvaluacion(new Evaluacion("Algebra", "Junio 18", 6.4));

        assertFalse(
                a.estaAprobado("ED"),
                "Si el alumno no tiene evaluaciones de la asignatura debe devolver false"
        );
    }

    @Test
    void estaAprobado_distingue_mayusculas_y_minusculas_con_equals() {
        Alumno a = new Alumno("X", 4);

        a.nuevaEvaluacion(new Evaluacion("ED", "Junio 20", 8.0));

        assertFalse(
                a.estaAprobado("ed"),
                "Debe comparar Strings con equals (case-sensitive)"
        );
    }
    @Test
    void estaAprobado_debe_usar_iterador_para_recorrer_la_lista() {
        String cuerpo = ComprobarMetodos.leerMetodoDeClase(
                "estaAprobado",
                "../ED 3 Practica. Listas. Solucion/src/evaluador/Alumno.java",
                "src/evaluador/Alumno.java",
                "src/main/java/evaluador/Alumno.java"
        );

        assertFalse(cuerpo.isEmpty(),
                "No se pudo localizar el método estaAprobado en Alumno.java");

        assertTrue(
                ComprobarMetodos.contieneLlamadaFuncion(cuerpo, "getIterador", 1),
                "estaAprobado debe usar getIterador()"
        );
        assertTrue(
                ComprobarMetodos.contieneLlamadaFuncion(cuerpo, "hasNext", 1),
                "estaAprobado debe recorrer con hasNext()"
        );
        assertTrue(
                ComprobarMetodos.contieneLlamadaFuncion(cuerpo, "next", 1),
                "estaAprobado debe recorrer con next()"
        );
    }
}
