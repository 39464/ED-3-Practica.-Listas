/**
 * @author Nombre y apellidos
 * */

package evaluador;

public class Lista {
		private Nodo inicio, fin;
		private int numElementos;

		// Inicializa una lista vac�a (sin elementos)
		public Lista() {
			inicio = null;
			fin = null;
			numElementos = 0;
		}

		// Determina si la lista est� vac�a o no (sin elementos)
		public boolean vacia() {
			return inicio == null;
		}

		// A�ade un nuevo dato al final de la lista
		public void insertar(int dato) {
			Nodo nuevo = new Nodo(dato, null);  // Crear un nodo nuevo
			if (inicio == null) {  // Insertar el nodo al final de la lista enlazada
				inicio = nuevo;
			} else {
				fin.setSiguiente(nuevo);
			}
			fin = nuevo;
			numElementos++;
		}


		// Devuelve el elemento que ocupa una posicion dada.
		// Si no existe la posici�n, devuelve -1
		public int getElemento(int posicion) {
			if (posicion < 0 || posicion >= numElementos) {
				return -1;
			} else {
				// Avanzar en la lista enlazada tantos nodos como indique posicion
				Nodo actual = inicio;
				for (int i = 0; i < posicion; i++) {
					actual = actual.getSiguiente();
				}
				return actual.getDato();
			}
		}

		// Almacena elemento en la posicion indicada por posicion
		// Si la posici�n es incorrecta, devuelve false
		public boolean setElemento(int dato, int posicion) {
			if (posicion < 0 || posicion >= numElementos) {
				return false;
			} else {
				Nodo actual = inicio;
				for (int i = 0; i < posicion; i++) {
					actual = actual.getSiguiente();
				}
				actual.setDato(dato);
				return true;
			}
		}

		// Borra la primera ocurrencia del parámetro dato (si existe)
		public boolean borrar(int dato) {
			Nodo actual = inicio;
			Nodo anterior = null;
			while (actual != null && actual.getDato() != dato) {
				anterior = actual;
				actual = actual.getSiguiente();
			}
			if (actual != null) {  // dato encontrado.
				if (actual == inicio) {   // Borrar el primero de la lista
					inicio = actual.getSiguiente();
				} else {  // Borrar nodo que no es el primero
					anterior.setSiguiente(actual.getSiguiente());
				}
				if (actual == fin) {  // Se ha borrado el �ltimo de la lista
					fin = anterior;
				}
				numElementos--;
				return true;
			} else {
				return false;
			}
		}

		// Devuelve la primera posición en la que se encuentra el parámetro dato (si existe)
		public int posicion(int dato) {
			Nodo actual = inicio;
			int posicion = 0;
			while (actual != null && actual.getDato() != dato) {
				actual = actual.getSiguiente();
				posicion++;
			}
			if (actual != null) {  // Dato encontrado
				return posicion;
			} else {
				return -1;
			}
		}

		// Determina si el parámetro dato existe en la lista.
		public boolean contiene(int dato) {
			return this.posicion(dato) >= 0;
		}

		// Devuelve el número de elementos que tiene la lista
		public int getNumElementos() {
			return numElementos;
		}

		// Devuelve un iterador para recorrer la lista desde el principio
		public Iterador getIterador() {
			return new Iterador(inicio);
		}

		// Devuelve una representación de la lista en forma de String.
		public String toString() {
			String resultado = "[";
			if (!this.vacia()) {
				resultado +=inicio.getDato();
				Nodo actual = inicio.getSiguiente();
				while (actual != null) {
					resultado += "," + actual.getDato();
					actual = actual.getSiguiente();
				}
			}
			resultado += "]";
			return resultado;
		}

	public int numConvocatorias(String nombreAsignatura) {
		return 0; // Eliminar esta líne al codificar el método
	}
}
