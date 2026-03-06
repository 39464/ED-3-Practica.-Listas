/**
 * @author Nombre y apellidos
 * */

package evaluador;

public class Lista {
		private Nodo inicio, fin;
		private int numElementos;

		// Inicializa una lista vacia (sin elementos)
		public Lista() {
			inicio = null;
			fin = null;
			numElementos = 0;
		}

		// Determina si la lista esta vacia o no (sin elementos)
		public boolean vacia() {
			return inicio == null;
		}

		// Añade una nueva ev al final de la lista
		public void insertar(Evaluacion ev) {
			Nodo nuevo = new Nodo(ev, null);  // Crear un nodo nuevo
			if (inicio == null) {  // Insertar el nodo al final de la lista enlazada
				inicio = nuevo;
			} else {
				fin.setSiguiente(nuevo);
			}
			fin = nuevo;
			numElementos++;
		}


		// Devuelve la evaluacion que ocupa una posicion dada.
		// Si no existe la posicion, devuelve null
		public Evaluacion getElemento(int posicion) {
			if (posicion < 0 || posicion >= numElementos) {
				return null;
			} else {
				// Avanzar en la lista enlazada tantos nodos como indique posicion
				Nodo actual = inicio;
				for (int i = 0; i < posicion; i++) {
					actual = actual.getSiguiente();
				}
				return actual.getEv();
			}
		}

		// Almacena ev en la posicion indicada por posicion
		// Si la posicion es incorrecta, devuelve false
		public boolean setElemento(Evaluacion ev, int posicion) {
			if (posicion < 0 || posicion >= numElementos) {
				return false;
			} else {
				Nodo actual = inicio;
				for (int i = 0; i < posicion; i++) {
					actual = actual.getSiguiente();
				}
				actual.setEv(ev);
				return true;
			}
		}

		// Borra la primera ocurrencia del parámetro ev (si existe)
		public boolean borrar(Evaluacion ev) {
			Nodo actual = inicio;
			Nodo anterior = null;
			while (actual != null && actual.getEv() != ev) {
				anterior = actual;
				actual = actual.getSiguiente();
			}
			if (actual != null) {  // ev encontrada.
				if (actual == inicio) {   // Borrar el primero de la lista
					inicio = actual.getSiguiente();
				} else {  // Borrar nodo que no es el primero
					anterior.setSiguiente(actual.getSiguiente());
				}
				if (actual == fin) {  // Se ha borrado el ultimo de la lista
					fin = anterior;
				}
				numElementos--;
				return true;
			} else {
				return false;
			}
		}

		// Devuelve la primera posición en la que se encuentra el parámetro ev (si existe)
		public int posicion(Evaluacion ev) {
			Nodo actual = inicio;
			int posicion = 0;
			while (actual != null && actual.getEv() != ev) {
				actual = actual.getSiguiente();
				posicion++;
			}
			if (actual != null) {  // Evaluacion encontrada
				return posicion;
			} else {
				return -1;
			}
		}

		// Determina si el parámetro ev existe en la lista.
		public boolean contiene(Evaluacion ev) {
			return this.posicion(ev) >= 0;
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
			StringBuilder resultado = new StringBuilder("[");
			if (!this.vacia()) {
				resultado.append(inicio.getEv());
				Nodo actual = inicio.getSiguiente();
				while (actual != null) {
					resultado.append(",").append(actual.getEv());
					actual = actual.getSiguiente();
				}
			}
			resultado.append("]");
			return resultado.toString();
		}

	public int numConvocatorias(String nombreAsignatura) {
        Nodo actual = inicio;
        int cont = 0;
        if(numElementos == 0) {
        	return 0;
        }else{
            while(actual != null) {
                Evaluacion ev = actual.getEv();
                if(ev.getNombreAsignatura().equals(nombreAsignatura)) {
                    if(ev.getNota() != -1){
                        cont++;
                    }
                }
            	actual = actual.getSiguiente();
            }
        }
        return cont;
	}
}
