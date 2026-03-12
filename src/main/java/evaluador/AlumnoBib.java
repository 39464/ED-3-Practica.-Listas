/**
 * @author Irene Lombardo Cabrera
 * */

package evaluador;

import java.util.Iterator;
import java.util.LinkedList;

public class AlumnoBib {

	private String nombre;
	private int matricula;
	private LinkedList<Evaluacion> expediente;

	public AlumnoBib(String nombre, int matricula) {
		this.nombre = nombre;
		this.matricula = matricula;
		expediente = new LinkedList<>();
	}

	public int getMatricula() {
		return matricula;
	}

	public void setMatricula(int matricula) {
		this.matricula = matricula;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public boolean nuevaEvaluacion(Evaluacion evaluacion) {
        boolean resultado = false;
        boolean insertar = true;
        if (evaluacion != null) {
            Iterator<Evaluacion> it = this.expediente.iterator();
            Evaluacion prueba;
            if (!it.hasNext()) {
                resultado = true;
            } else {
                while (it.hasNext()) {
                    prueba = it.next();
                    if (prueba.mismaEvaluacion(evaluacion)) {
                        if (prueba.getNota() == evaluacion.getNota()) {
                            resultado = true; insertar = false;
                        } else {
                            System.out.println("Calificacion previamente insertada con nota: " + prueba.getNota());
                            resultado = false; insertar = false;
                        }
                    }
                }
            }
            if (insertar) { this.expediente.addLast(evaluacion); resultado = true; }
        }
        return resultado;
    }

	public boolean estaAprobado(String nombreAsig) {
        boolean resul = false;
        if(!nombreAsig.isEmpty()) {
            Iterator<Evaluacion> it = this.expediente.iterator();
            Evaluacion prueba;
            while (it.hasNext()) {
                prueba = it.next();
                if ((prueba.getNombreAsignatura()).equals(nombreAsig) && prueba.getNota() >= 5) {
                    resul = true;
                }
            }
        }
        return resul;
	} //busca en toda la lista todas las posibilidades

	public LinkedList<Evaluacion> asignaturasAprobadas() {
		LinkedList<Evaluacion> resultado = new LinkedList<>();
        if(!this.expediente.isEmpty()) {
            Iterator<Evaluacion> it = this.expediente.iterator();
            Evaluacion prueba;
            while(it.hasNext()) {
                prueba = it.next();
                if(this.estaAprobado(prueba.getNombreAsignatura())) {
                    if(prueba.getNota() >= 5 && !resultado.contains(prueba)) {
                        resultado.addLast(prueba);
                    }
                }
            }
        }
        return resultado;
	}

	public double mediaAprobadas() {
		LinkedList<Evaluacion> aprobadas = asignaturasAprobadas();
        double media= 0.0;
        if(!aprobadas.isEmpty()) {
            Iterator<Evaluacion> it = aprobadas.iterator();
            double suma = 0.0;
            int cont = 0;
            while (it.hasNext()) {
                Evaluacion prueba = it.next();
                suma += prueba.getNota();
                cont++;
            }
            media = suma / cont;
        }
        return media;
	}

	public int getNumAprobadas() { return asignaturasAprobadas().size();	}

	public void mostrar() {
        System.out.println(nombre + ". Matricula: " + matricula);
        if (this.expediente.isEmpty()) {
            System.out.println("No ha realizado ninguna evaluación");
        } else {
            Iterator<Evaluacion> it = this.expediente.iterator();
            Evaluacion prueba;
            while (it.hasNext()) {
                prueba = it.next();
                prueba.mostrar();
            }
            System.out.println(this.expediente.size() + " evaluaciones y " + getNumAprobadas() +
                    " asignaturas aprobadas con calificación media " + mediaAprobadas());
        }
	}
}


