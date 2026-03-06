package evaluador;

public class Iterador {

	private Nodo actual;

	public Iterador(Nodo nodo) {
		actual = nodo;
	}

	public boolean hasNext() {
		return actual != null;
	}

	public int next() {
		int resultado = actual.getDato();
		actual = actual.getSiguiente();
		return resultado;
	}
}
