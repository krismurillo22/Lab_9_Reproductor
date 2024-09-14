/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab9_reproductormusica;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author User
 */
public class Controles {
    private AdvancedPlayer player;
    private Thread playThread;
    private FileInputStream fileInputStream;
    private int FramePausado = 0;
    private boolean estaPausado = false;
    private int tiempoPausado = 0;
    private boolean estaSonando = false;

    public void play(Cancion cancion) throws JavaLayerException, IOException {
        if (estaSonando) {
            return;
        }
        if (estaPausado) {
            resume(cancion);
        } else {
            empezarPlay(cancion, 0);
        }
    }

    private void empezarPlay(Cancion cancion, int startFrame) throws IOException, JavaLayerException {
        fileInputStream = new FileInputStream(cancion.getFile());
        player = new AdvancedPlayer(fileInputStream);
        playThread = new Thread(() -> {
            try {
                estaSonando = true;
                player.play(startFrame, Integer.MAX_VALUE);
                estaSonando = false;
            } catch (JavaLayerException e) {
                e.printStackTrace();
            }
        });
        playThread.start();
        estaPausado = false;
    }

    private void resume(Cancion cancion) throws JavaLayerException, IOException {
        empezarPlay(cancion, FramePausado);
    }

    public void pause() {
        if (player != null) {
            try {
                FramePausado = tiempoPausado;
                stopPlayback();
                estaPausado = true;
                estaSonando = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        if (player != null) {
            stopPlayback();
            FramePausado = 0;
            estaSonando = false;
        }
    }

    private void stopPlayback() {
        if (player != null) {
            player.close();
            player = null;
        }
        if (playThread != null) {
            playThread.interrupt();
        }
    }
    
    public boolean estaSonando() {
        return estaSonando;
    }
}
