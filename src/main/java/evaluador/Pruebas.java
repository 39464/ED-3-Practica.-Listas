/**
 * @author Irene Lombardo Cabrera
 * */

package evaluador;

public class Pruebas {

    public static void main(String[] args) {
        System.out.println("------------ EVALUACIONES EN LA LISTA ------------");
        Evaluacion ev1 = new Evaluacion("ED", "Junio 19", 4.5);
        Evaluacion ev2 = new Evaluacion("ED", "Julio 19", -1); //-1 == no presentado
        Evaluacion ev3 = new Evaluacion("ED", "Julio 20", 7.4);
        Evaluacion ev4 = new Evaluacion("Algebra", "Junio 18", 6.4);
        Lista lista1 = new Lista();
        lista1.insertar(ev1); lista1.insertar(ev2); lista1.insertar(ev3); lista1.insertar(ev4);
        mostrar(lista1);

        System.out.println("Convocatorias en ED: " + lista1.numConvocatorias("ED"));
        System.out.println("Convocatorias en Algebra: " + lista1.numConvocatorias("Algebra"));
        System.out.println("Convocatorias en Fundamentos de Programacion: " + lista1.numConvocatorias("Fundamentos de Programacion"));

        Alumno a1 = new Alumno("Felipe Garcia Gomez", 1253);
        Alumno a2 = new Alumno("Alicia Blazquez Martin", 5622);
        for(int i=0; i < lista1.getNumElementos(); i++){
            a1.nuevaEvaluacion(lista1.getElemento(i));
        }

        Evaluacion ev5 = new Evaluacion("ED", "Junio 20", 3);
        a1.nuevaEvaluacion(ev5);

        System.out.println("---ASIGNATURAS APROBADAS POR "+ a1.getNombre()+ "---");
        if(!mostrar(a1.asignaturasAprobadas())) System.out.println("Ninguna asignatura aprobada");
        System.out.println();
        System.out.println("---ASIGNATURAS APROBADAS POR "+ a2.getNombre()+ "---");
        if(!mostrar(a2.asignaturasAprobadas())) System.out.println("Ninguna asignatura aprobada");

        a1.mostrar();
        a2.mostrar();
    }

    public static boolean mostrar(Lista lista){ //TODO PREGUNTAR SI PUEDO HACER ESTO
        boolean resultado = false;
        if(!lista.vacia()){
            Iterador it = lista.getIterador();
            while(it.hasNext()) {
                it.next().mostrar();
            }
        }
        return resultado;
    }
}
    