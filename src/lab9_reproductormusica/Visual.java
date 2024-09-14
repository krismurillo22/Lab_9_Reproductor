/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab9_reproductormusica;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javazoom.jl.decoder.JavaLayerException;

/**
 *
 * @author User
 */
public class Visual extends JFrame {

    private Controles controles;
    private Funciones funciones;
    private JList<String> listado;
    private JLabel imagenLabel;
    private File carpetaMusica;
    private Cancion canActualPlay = null;

    public Visual() {
        controles = new Controles();
        funciones = new Funciones();
        /*
        Aqui estan todas las canciones guardadas y por cada cancion es una carpeta con la cancion adentro, la imagen
        y los datos agregados de dicha cancion.
         */
        carpetaMusica = new File("Musica");
        if (!carpetaMusica.exists()) {
            carpetaMusica.mkdir();
        }

        setTitle("Reproductor de Musica");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        JLabel cancionSeleccionada = new JLabel("Selecciona la cancion que deseas escuchar:", JLabel.CENTER);
        cancionSeleccionada.setFont(new Font("Arial", Font.BOLD, 14));
        cancionSeleccionada.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        listado = new JList<>();
        listado.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listado.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                ImagenCancionSeleccionada();
            }
        });

        imagenLabel = new JLabel();
        imagenLabel.setHorizontalAlignment(JLabel.CENTER);
        imagenLabel.setVerticalAlignment(JLabel.CENTER);

        Dimension buttonSize = new Dimension(50, 50);
        ImageIcon playIcon = new ImageIcon(new ImageIcon("src/lab9_reproductormusica/play.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
        ImageIcon pauseIcon = new ImageIcon(new ImageIcon("src/lab9_reproductormusica/pause.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
        ImageIcon stopIcon = new ImageIcon(new ImageIcon("src/lab9_reproductormusica/stop.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));

        JButton playButton = new JButton(playIcon);
        playButton.setPreferredSize(buttonSize);
        playButton.setBackground(new Color(190, 37, 174));
        playButton.setFocusPainted(false);

        JButton pauseButton = new JButton(pauseIcon);
        pauseButton.setPreferredSize(buttonSize);
        pauseButton.setBackground(new Color(197, 128, 190));
        pauseButton.setFocusPainted(false);

        JButton stopButton = new JButton(stopIcon);
        stopButton.setPreferredSize(buttonSize);
        stopButton.setBackground(new Color(190, 37, 174));
        stopButton.setFocusPainted(false);

        JButton addButton = new JButton("Agregar cancion");
        addButton.setPreferredSize(new Dimension(150, 50));
        addButton.setBackground(new Color(197, 128, 190));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setFocusPainted(false);

        playButton.addActionListener(e -> playCancionSelec());
        pauseButton.addActionListener(e -> controles.pause());
        stopButton.addActionListener(e -> controles.stop());
        addButton.addActionListener(e -> panelAgregarCancion());

        JPanel controlPanel = new JPanel();
        controlPanel.add(playButton);
        controlPanel.add(pauseButton);
        controlPanel.add(stopButton);
        controlPanel.add(addButton);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        JPanel listadoCan = new JPanel(new BorderLayout());
        listadoCan.add(cancionSeleccionada, BorderLayout.NORTH);
        listadoCan.add(new JScrollPane(listado), BorderLayout.CENTER);
        mainPanel.add(listadoCan);
        mainPanel.add(imagenLabel);
        add(mainPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        cargarCancionesGuard();
        setVisible(true);
    }

    private void ImagenCancionSeleccionada() {
        int index = listado.getSelectedIndex();
        if (index != -1) {
            Cancion cancionSeleccionada = funciones.getCancion(index);
            ImageIcon originalIcon = cancionSeleccionada.getImagen();
            Image scaledImage = originalIcon.getImage().getScaledInstance(imagenLabel.getWidth(), imagenLabel.getWidth(), Image.SCALE_SMOOTH);
            imagenLabel.setIcon(new ImageIcon(scaledImage));
            canActualPlay = cancionSeleccionada;
        }
    }

    private void playCancionSelec() {
        int index = listado.getSelectedIndex();
        if (index != -1) {
            try {
                Cancion seleccionada = funciones.getCancion(index);
                if (!controles.estaSonando()) {
                    controles.play(seleccionada);
                    ImageIcon originalIcon = seleccionada.getImagen();
                    Image scaledImage = originalIcon.getImage().getScaledInstance(imagenLabel.getWidth(), imagenLabel.getWidth(), Image.SCALE_SMOOTH);
                    imagenLabel.setIcon(new ImageIcon(scaledImage));
                }
            } catch (JavaLayerException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void panelAgregarCancion() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String fileName = selectedFile.getName();
            if (!fileName.toLowerCase().endsWith(".mp3")) {
                JOptionPane.showMessageDialog(this, "Por favor selecciona un archivo de música en formato MP3.", "Formato no válido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                Cancion nuevaCancion = panelAgregarDatosCancion(selectedFile);
                if (nuevaCancion != null) {
                    File songFolder = new File(carpetaMusica, nuevaCancion.getNombre());
                    if (!songFolder.exists()) {
                        songFolder.mkdir();
                    }
                    File musicDestination = new File(songFolder, nuevaCancion.getNombre() + ".mp3");
                    Files.copy(selectedFile.toPath(), musicDestination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    File imageDestination = new File(songFolder, "cover.jpg");
                    File imageFile = new File(nuevaCancion.getImagen().getDescription());
                    Files.copy(imageFile.toPath(), imageDestination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    guardarDatosCancion(nuevaCancion);
                    funciones.agregarCancion(nuevaCancion);
                    actualizarLista();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void actualizarLista() {
        DefaultListModel<String> model = new DefaultListModel<>();
        for (int i = 0; i < funciones.getPlaylistSize(); i++) {
            Cancion cancion = funciones.getCancion(i);
            model.addElement(cancion.getNombre() + " - " + cancion.getArtista());
        }
        listado.setModel(model);
    }

    private Cancion panelAgregarDatosCancion(File musicFile) {
        JDialog dialog = new JDialog(this, "Agregar Cancion", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(6, 2));

        JTextField nombreField = new JTextField();
        JTextField artistaField = new JTextField();
        JTextField duracionField = new JTextField();
        JTextField generoField = new JTextField();
        JLabel imageLabel = new JLabel("Selecciona una imagen");
        JButton selectImageButton = new JButton("Seleccionar Imagen");
        final ImageIcon[] imageIcon = {null};
        selectImageButton.addActionListener(e -> {
            JFileChooser imageChooser = new JFileChooser();
            int result = imageChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedImageFile = imageChooser.getSelectedFile();
                String imageName = selectedImageFile.getName();
                if (!imageName.toLowerCase().endsWith(".jpg") && !imageName.toLowerCase().endsWith(".jpeg") && !imageName.toLowerCase().endsWith(".png") && !imageName.toLowerCase().endsWith(".gif")) {
                    JOptionPane.showMessageDialog(dialog, "Por favor selecciona un archivo de imagen válido (jpg, jpeg, png, gif).", "Formato de imagen no válido", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                imageIcon[0] = new ImageIcon(selectedImageFile.getPath());
                imageIcon[0].setDescription(selectedImageFile.getAbsolutePath());
                imageLabel.setText(selectedImageFile.getName());
            }
        });

        JButton saveButton = new JButton("Guardar");
        dialog.add(new JLabel("Nombre de la Cancion:"));
        dialog.add(nombreField);
        dialog.add(new JLabel("Artista:"));
        dialog.add(artistaField);
        dialog.add(new JLabel("Duracion (mm:ss):"));
        dialog.add(duracionField);
        dialog.add(new JLabel("Genero:"));
        dialog.add(generoField);
        dialog.add(imageLabel);
        dialog.add(selectImageButton);
        dialog.add(new JLabel(""));
        dialog.add(saveButton);
        final Cancion[] newTrack = {null};
        saveButton.addActionListener(e -> {
            String nombre = nombreField.getText();
            String artista = artistaField.getText();
            String duracion = duracionField.getText();
            String genero = generoField.getText();
            if (!nombre.isEmpty() && !artista.isEmpty() && !duracion.isEmpty() && imageIcon[0] != null) {
                newTrack[0] = new Cancion(nombre, artista, duracion, genero, imageIcon[0], musicFile);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Completa todos los campos y selecciona una imagen válida", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        return newTrack[0];
    }

    private void cargarCancionesGuard() {
        File[] songFolders = carpetaMusica.listFiles(File::isDirectory);
        if (songFolders != null) {
            for (File songFolder : songFolders) {
                try {
                    File datos = new File(songFolder, "datosCancion.can");
                    if (datos.exists()) {
                        try (RandomAccessFile raf = new RandomAccessFile(datos, "r")) {
                            String nombre = raf.readUTF();
                            String artista = raf.readUTF();
                            String duracion = raf.readUTF();
                            String genero = raf.readUTF();
                            String ImagenPath = raf.readUTF();
                            String musicFilePath = raf.readUTF();
                            ImageIcon coverImage = new ImageIcon(ImagenPath);
                            File musicFile = new File(musicFilePath);
                            Cancion track = new Cancion(nombre, artista, duracion, genero, coverImage, musicFile);
                            funciones.agregarCancion(track);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            actualizarLista();
        }
    }

    private void guardarDatosCancion(Cancion newTrack) {
        File folder = new File(carpetaMusica, newTrack.getNombre());
        if (!folder.exists()) {
            folder.mkdir();
        }
        File dataFile = new File(folder, "datosCancion.can");
        try (RandomAccessFile raf = new RandomAccessFile(dataFile, "rw")) {
            raf.writeUTF(newTrack.getNombre());
            raf.writeUTF(newTrack.getArtista());
            raf.writeUTF(newTrack.getDuracion());
            raf.writeUTF(newTrack.getGenero());
            raf.writeUTF(newTrack.getImagen().getDescription());
            raf.writeUTF(newTrack.getFile().getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Visual();
    }
}
