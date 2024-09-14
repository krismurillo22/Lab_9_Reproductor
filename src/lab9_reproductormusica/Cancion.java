/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab9_reproductormusica;

import java.io.File;
import javax.swing.ImageIcon;

/**
 *
 * @author User
 */
public class Cancion {

    private String nombre;
    private String artista;
    private String duracion;
    private String genero;
    private ImageIcon imagen;
    private File archivo;

    public Cancion(String nombre, String artista, String duracion, String genero, ImageIcon imagen, File archivo) {
        this.nombre = nombre;
        this.artista = artista;
        this.duracion = duracion;
        this.genero = genero;
        this.imagen = imagen;
        this.archivo = archivo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getArtista() {
        return artista;
    }

    public String getDuracion() {
        return duracion;
    }

    public String getGenero() {
        return genero;
    }

    public ImageIcon getImagen() {
        return imagen;
    }

    public File getFile() {
        return archivo;
    }
}
