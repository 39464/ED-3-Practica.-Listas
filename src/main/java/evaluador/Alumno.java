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
		return false; // Eliminar esta línea al codificar el método
	}

	public boolean estaAprobado(String nombreAsig) {
		// Codificar
		return false; // Eliminar esta línea al codificar el método
	}

	public Lista asignaturasAprobadas() {
		return null;  // Eliminar esta línea al codificar el método
	}

	public double mediaAprobadas() {
		return 0.0;  // Eliminar esta línea al codificar el método
	}

	public int getNumAprobadas() {
		return 0;  // Eliminar esta línea al codificar el método
	}

	public void mostrar() {

	}

}


