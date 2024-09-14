/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab9_reproductormusica;

/**
 *
 * @author User
 */
public class Funciones {
    private ListaEnlazada listado;

    public Funciones() {
        listado = new ListaEnlazada();
    }

    public void agregarCancion(Cancion cancion) {
        listado.agregar(cancion);
    }

    public Cancion getCancion(int index) {
        return listado.get(index);
    }

    public void eliminarCancion(int index) {
        listado.eliminar(index);
    }

    public int getPlaylistSize() {
        return listado.size();
    }
}
