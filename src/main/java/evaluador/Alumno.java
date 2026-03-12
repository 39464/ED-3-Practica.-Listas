/**
 * @author Irene Lombardo Cabrera
 * */

package evaluador;

public class Alumno {

	private String nombre;
	private int matricula;
	private Lista expediente;

	public Alumno(String nombre, int matricula) {
		this.nombre = nombre;
		this.matricula = matricula;
		expediente = new Lista();
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
            Iterador it = this.expediente.getIterador();
            Evaluacion prueba; //actua sobre this.expediente
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
                            resultado = false; insertar=false;
                        }
                    }
                }
            }
            if (insertar) { this.expediente.insertar(evaluacion); resultado = true; }
        }
        return resultado;
    }

	public boolean estaAprobado(String nombreAsig) {
        boolean resultado = false;
        if(!nombreAsig.isEmpty()) {
            Iterador it = expediente.getIterador();
            Evaluacion prueba;
            while (it.hasNext()) {
                prueba = it.next();
                if ((prueba.getNombreAsignatura()).equals(nombreAsig) && prueba.getNota() >= 5) {
                    resultado = true;
                }
            }
        }
        return resultado;
	} //busca en toda la lista todas las posibilidades

	public Lista asignaturasAprobadas() {
        Lista resultado = new Lista();
        if(this.expediente!= null){
            Iterador it = this.expediente.getIterador();
            Evaluacion prueba;
            while(it.hasNext()) {
                prueba = it.next();
                if(this.estaAprobado(prueba.getNombreAsignatura())) {
                    if(prueba.getNota() >= 5 && !resultado.contiene(prueba)) {
                        resultado.insertar(prueba);
                    }
                }
            }
        }
        return resultado;
	}

	public double mediaAprobadas() {
		Lista aprobadas = asignaturasAprobadas();
        double media=0.0;
        if(!aprobadas.vacia()) {
            Iterador it = aprobadas.getIterador();
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

	public int getNumAprobadas() { return asignaturasAprobadas().getNumElementos();	}

	public void mostrar() {
        System.out.println(nombre + ". Matricula: " + matricula);
        if (this.expediente.getNumElementos() == 0) {
            System.out.println("No ha realizado ninguna evaluación");
        } else {
            Iterador it = this.expediente.getIterador();
            Evaluacion prueba;
            while (it.hasNext()) {
                prueba = it.next();
                prueba.mostrar();
            }
            System.out.println(this.expediente.getNumElementos() + " evaluaciones y " + getNumAprobadas() +
                    " asignaturas aprobadas con calificación media " + mediaAprobadas());
        }
	}
}


