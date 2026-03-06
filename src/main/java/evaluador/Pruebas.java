/**
 * @author Nombre y apellidos
 * */

package evaluador;

public class Pruebas {

    public static void main(String[] args) {
        System.out.println("------------ EVALUACIONES EN LA LISTA ------------");
        Evaluacion ev1 = new Evaluacion("ED", "Junio 19", 4.5);
        Evaluacion ev2 = new Evaluacion("ED", "Julio 19", -1); //-1 == no presentado
        Evaluacion ev3 = new Evaluacion("ED", "Julio 20", 7.4);
        Evaluacion ev4 = new Evaluacion("Algebra", "Junio 18", 6.4);
        Lista lista = new Lista();
        lista.insertar(ev1); lista.insertar(ev2); lista.insertar(ev3); lista.insertar(ev4);
        Iterador it = lista.getIterador();
        if (lista.vacia()) {
            System.out.println("Lista vacia");
        } else {
            while(it.hasNext()) {
                it.next().mostrar();
            }
        }

        System.out.println("Convocatorias en ED: " + lista.numConvocatorias("ED"));
        System.out.println("Convocatorias en Algebra: " + lista.numConvocatorias("Algebra"));
        System.out.println("Convocatorias en Fundamentos de Programacion: " + lista.numConvocatorias("Fundamentos de Programacion"));
    }
}
    