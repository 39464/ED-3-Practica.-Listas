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
        if (evaluacion != null) {
            Iterador it = this.expediente.getIterador();
            Evaluacion prueba; //actua sobre this.expediente
            if (!it.hasNext()) {
                this.expediente.insertar(evaluacion);
                resultado = true;
            } else {
                while (it.hasNext()) {
                    prueba = it.next();
                    if (prueba.mismaEvaluacion(evaluacion)) {
                        if (prueba.getNota() == evaluacion.getNota()) {
                            resultado = true;
                        } else {
                            System.out.println("Calificacion previamente insertada con nota: " + prueba.getNota());
                        }
                    } else {
                        this.expediente.insertar(evaluacion);
                        resultado = true;
                    }
                }
            }
        }
        return resultado;
    }

	public boolean estaAprobado(String nombreAsig) {
		Iterador it = expediente.getIterador();
        boolean resultado = false;
        if(!nombreAsig.isEmpty()) {
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
        Iterador it = this.expediente.getIterador();
        if(this.expediente!= null){
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
        Iterador it = aprobadas.getIterador();
        double media;
        if(aprobadas.getNumElementos() == 0) { media = 0.0; }
        else{
            double suma = 0.0;
            int cont = 0;
            while(it.hasNext()) {
                Evaluacion prueba = it.next();
                suma += prueba.getNota();
                cont++;
            }
            media = suma/cont;
        }
        return media;
	}

	public int getNumAprobadas() { return asignaturasAprobadas().getNumElementos();	}

	public void mostrar() {
        System.out.println(nombre + ". Matricula: " + matricula);
        if (this.expediente.getNumElementos() == 0) {
            System.out.println("No se ha realizado ninguna evaluación");
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


