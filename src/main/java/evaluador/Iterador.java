package evaluador;

public class Iterador {

	private Nodo actual;

	public Iterador(Nodo nodo) {
		actual = nodo;
	}

	public boolean hasNext() {
		return actual != null;
	}

	public Evaluacion next() {
		Evaluacion resultado = actual.getEv();
		actual = actual.getSiguiente();
		return resultado;
	}
}
