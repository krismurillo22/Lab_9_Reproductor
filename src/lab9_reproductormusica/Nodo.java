/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab9_reproductormusica;

/**
 *
 * @author User
 */
public class Nodo {
    Cancion cancion;
    Nodo siguiente;

    public Nodo(Cancion cancion) {
        this.cancion = cancion;
        this.siguiente = null;
    }
}
