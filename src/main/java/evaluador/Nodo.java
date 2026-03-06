/**
 * @author Irene Lombardo Cabrera
 * */
package evaluador;

public class Nodo {

	private Evaluacion ev;
	private Nodo siguiente;

	public Nodo(Evaluacion ev, Nodo siguiente) {
		this.ev = ev;
		this.siguiente = siguiente;
	}

	public Nodo getSiguiente() {
		return siguiente;
	}

	public void setSiguiente(Nodo siguiente) {
		this.siguiente = siguiente;
	}

	public Evaluacion getEv() {
		return ev;
	}

	public void setEv(Evaluacion ev) {
		this.ev = ev;
	}
}
