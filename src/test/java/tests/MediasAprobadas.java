package tests;

import evaluador.Alumno;
import evaluador.Evaluacion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MediasAprobadas {

    @Test
    void mediaAprobadas_debe_calcular_media_solo_de_aprobadas_y_devolver_0_si_no_hay() {
        // Felipe (según el main de Pruebas)
        Alumno felipe = new Alumno("Felipe García Gómez", 1253);
        Evaluacion ed1 = new Evaluacion("ED", "Junio 19", 4.5);     // suspensa
        Evaluacion ed2 = new Evaluacion("ED", "Julio 19", -1);      // NP
        Evaluacion ed3 = new Evaluacion("ED", "Junio 20", 7.4);     // aprobada
        Evaluacion algebra1 = new Evaluacion("Algebra", "Junio 18", 6.4); // aprobada

        felipe.nuevaEvaluacion(ed1);
        felipe.nuevaEvaluacion(ed2);
        felipe.nuevaEvaluacion(ed3);
        felipe.nuevaEvaluacion(algebra1);

        double esperado = (7.4 + 6.4) / 2.0; // 6.9
        assertEquals(esperado, felipe.mediaAprobadas(), 1e-9,
                "Media de aprobadas incorrecta (debe considerar solo notas >= 5 y excluir NP/negativas y suspensos).");

        // Alicia (sin evaluaciones)
        Alumno alicia = new Alumno("Alicia Blázquez Martín", 5622);
        assertEquals(0.0, alicia.mediaAprobadas(), 1e-9,
                "Si no hay aprobadas (ni evaluaciones), debe devolver 0.0");
    }
    @Test
    void mediaAprobadas_debe_usar_iterador() {
        String codigo = ComprobarMetodos.leerCodigoDeAlumnoSiExiste();
        assertFalse(codigo.isEmpty(),
                "No pude leer Alumno.java para comprobar el uso de iterador.");

        String cuerpo = ComprobarMetodos.extraerCuerpoMetodo(codigo, "mediaAprobadas");
        assertFalse(cuerpo.isEmpty(),
                "No se encontró el método mediaAprobadas en Alumno.java.");

        assertTrue(
                ComprobarMetodos.contieneLlamadaFuncion(cuerpo, "getIterador", 1),
                "mediaAprobadas debe usar un iterador (getIterador)."
        );

        assertTrue(
                ComprobarMetodos.contieneLlamadaFuncion(cuerpo, "hasNext", 1),
                "mediaAprobadas debe recorrer con hasNext()."
        );

        assertTrue(
                ComprobarMetodos.contieneLlamadaFuncion(cuerpo, "next", 1),
                "mediaAprobadas debe recorrer con next()."
        );
    }

    @Test
    void mediaAprobadas_sin_aprobadas_debe_devolver_0() {
        Alumno a = new Alumno("X", 1);
        a.nuevaEvaluacion(new Evaluacion("ED", "A", 4.9));
        a.nuevaEvaluacion(new Evaluacion("ED", "B", 0.0));
        a.nuevaEvaluacion(new Evaluacion("ED", "C", -1)); // NP
        assertEquals(0.0, a.mediaAprobadas(), 1e-9, "Sin aprobadas (>=5), debe devolver 0.0");
    }

    @Test
    void mediaAprobadas_con_una_sola_aprobada_debe_ser_esa_nota() {
        Alumno a = new Alumno("Y", 2);
        a.nuevaEvaluacion(new Evaluacion("ED", "A", 5.0)); // aprobada
        a.nuevaEvaluacion(new Evaluacion("ED", "B", 4.99));
        a.nuevaEvaluacion(new Evaluacion("ED", "C", -1));
        assertEquals(5.0, a.mediaAprobadas(), 1e-9, "Con una aprobada, la media debe ser esa nota");
    }
}
