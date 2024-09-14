/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab9_reproductormusica;

/**
 *
 * @author User
 */
public class ListaEnlazada {

    private Nodo head;

    public ListaEnlazada() {
        this.head = null;
    }

    public void agregar(Cancion cancion) {
        Nodo temp = new Nodo(cancion);
        if (head == null) {
            head = temp;
        } else {
            Nodo actual = head;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = temp;
        }
    }

    public Cancion get(int index) {
        Nodo actual = head;
        int contador = 0;
        while (actual != null) {
            if (contador == index) {
                return actual.cancion;
            }
            contador++;
            actual = actual.siguiente;
        }
        return null;
    }

    public void eliminar(int index) {
        if (head == null) {
            return;
        }
        Nodo actual = head;
        if (index == 0) {
            head = actual.siguiente;
            return;
        }
        for (int i = 0; actual != null && i < index - 1; i++) {
            actual = actual.siguiente;
        }
        if (actual == null || actual.siguiente == null) {
            return;
        }
        Nodo next = actual.siguiente.siguiente;
        actual.siguiente = next;
    }

    public int size() {
        int size = 0;
        Nodo actual = head;
        while (actual != null) {
            size++;
            actual = actual.siguiente;
        }
        return size;
    }
}
