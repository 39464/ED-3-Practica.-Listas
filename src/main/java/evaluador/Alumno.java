/**
 * @author Nombre y apellidos
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
        Iterador it = this.expediente.getIterador();
        Evaluacion prueba;
        if(!it.hasNext()) {
            this.expediente.insertar(evaluacion);
            resultado= true;
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
                    this.expediente.insertar(prueba);
                    resultado = true;
                }
            }
        }
        return resultado;
	}

	public boolean estaAprobado(String nombreAsig) {
		Iterador it = this.expediente.getIterador();
        boolean resultado = false;
        while(it.hasNext()) {
            Evaluacion prueba = it.next();
            if(prueba.getNombreAsignatura().equals(nombreAsig))
                if(prueba.getNota() >= 5) resultado = true;
        }
        return resultado;
	}

	public Lista asignaturasAprobadas() {
		Lista resultado = new Lista();
        Iterador it = this.expediente.getIterador();
        while(it.hasNext()) {
            Evaluacion prueba = it.next();
            if(estaAprobado(prueba.getNombreAsignatura())) {
                resultado.insertar(prueba);
            }
        }
        return resultado;
	}

	public double mediaAprobadas() {
		Lista aprobadas = asignaturasAprobadas();
        double suma = 0.0;
        int cont = 0;
        double media;
        Iterador it = aprobadas.getIterador();
        if(aprobadas.getNumElementos() == 0) { media = 0.0; }
        else{
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
        Iterador it = this.expediente.getIterador();
        System.out.println(nombre + ". Matricula: " + matricula);
        if(this.expediente.getNumElementos()== 0){
            System.out.println("No se ha realizado ninguna evaluación");
        }else{
            while(it.hasNext()) {
                it.next().mostrar();
            }
            System.out.println(this.expediente.getNumElementos() + " evaluaciones y "+getNumAprobadas()+
                    " asignaturas aprobadas con calificación media "+mediaAprobadas());
        }
	}

}


