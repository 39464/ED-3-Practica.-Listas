package tests;

import evaluador.Pruebas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class MainCompleto {

    private PrintStream originalOut;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void main_de_Pruebas_debe_imprimir_la_salida_esperada() {
        // 1) Comprobaciones estructurales con tolerancia a typos
        assertTrue(
                ComprobarMetodos.claseTieneMetodo(Pruebas.class, "main", 1),
                () -> "No se encontró el método main en evaluador.Pruebas. Más parecido: "
                        + ComprobarMetodos.metodoMasParecido(Pruebas.class, "main")
        );

        assertTrue(
                ComprobarMetodos.claseTieneMetodo(Pruebas.class, "main", 1),
                () -> "No se encontró el método main en evaluador.Pruebas. Más parecido: "
                        + ComprobarMetodos.metodoMasParecido(Pruebas.class, "main")
        );

        // 2) Ejecutar el main capturando salida
        assertDoesNotThrow(() -> Pruebas.main(new String[0]), "Pruebas.main() lanzó una excepción");

        String salidaReal = outContent.toString(StandardCharsets.UTF_8);

        // 3) Salida esperada (text block: JDK 15+; tú estás en JDK 17)
        String salidaEsperada = """
                ---------- EVALUACIONES EN LA LISTA -----------
                    ED (Junio 19): 4.5
                    ED (Julio 19): NP
                    ED (Junio 20): 7.4
                    Algebra (Junio 18): 6.4
                ------------------------------
                Convocatorias en ED: 2
                Convocatorias en Algebra: 1
                Convocatorias en Fundamentos de Programación: 0
                Calificación previamente insertada con nota: 7.4
                ---------- Asignaturas aprobadas por Felipe García Gómez ------------
                    ED (Junio 20): 7.4
                    Algebra (Junio 18): 6.4

                ----------- Asignaturas aprobadas por Alicia Blázquez Martín -----------

                ----------- MOSTRAR LOS ALUMNOS -----------
                Felipe García Gómez. Matricula: 1253
                    ED (Junio 19): 4.5
                    ED (Julio 19): NP
                    ED (Junio 20): 7.4
                    Algebra (Junio 18): 6.4
                4 evaluaciones y 2 asignaturas aprobadas con calificación media 6.9
                ---------------------------------
                Alicia Blázquez Martín. Matricula: 5622
                No ha realizado ninguna evaluación
                ---------------------------------

                ----------- MOSTRAR LOS ALUMNOS BIBLIOTECA -----------
                Eduardo Parra Martín. Matricula: 8765
                    ED (Junio 19): 4.5
                    ED (Julio 19): NP
                    ED (Junio 20): 7.4
                    Algebra (Junio 18): 6.4
                4 evaluaciones y 2 asignaturas aprobadas con calificación media 6.9
                ---------------------------------
                Sonia Torres Pardo. Matricula: 2345
                No ha realizado ninguna evaluación
                ---------------------------------
                """;

        // 4) Comparación robusta con Textos
        String esperadoNorm = Textos.normalizar(salidaEsperada);
        String obtenidoNorm = Textos.normalizar(salidaReal);
        assertEquals(
                esperadoNorm,
                obtenidoNorm,
                () -> Textos.diffDetallado(salidaEsperada, salidaReal, 3, 15)
        );
    }
}