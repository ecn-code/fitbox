/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fitbox.controller;

/*import com.google.gdata.client.*;
 import com.google.gdata.client.youtube.*;
 import com.google.gdata.data.*;
 import com.google.gdata.data.geo.impl.*;
 import com.google.gdata.data.media.*;
 import com.google.gdata.data.media.mediarss.*;
 import com.google.gdata.data.youtube.*;
 import com.google.gdata.data.extensions.*;
 import com.google.gdata.util.*;
 import java.io.IOException;
 import java.io.File;
 import java.net.URL;*/
import fitbox.Cronometro;
import fitbox.ObjetoWebCam;
import fitbox.controller.dao.Dal;
import fitbox.model.Actividad;
import fitbox.model.Jugador;
import fitbox.model.Usuario;
import fitbox.view.ControlledScreen;
import fitbox.view.Recurso;
import fitbox.view.ScreensFramework;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import jfx.messagebox.MessageBox;
import javax.swing.JOptionPane;

public class RealizarActividadController implements Initializable, ControlledScreen {

    private ScreensController myController;
    @FXML
    private WebView videoMuestra;
    @FXML
    private Label crono1;
    @FXML
    private Label crono2;
    @FXML
    private Pane zonaVideo;
    private Cronometro crono;
    double puntos = 0;
    double puntosAct;
    private Recurso recurso;
    private String URL;// = "UNvAy1N6jvU";
    private Usuario usuario;
    private Actividad actividad;
    private Dal dal;
    private Jugador j;
    private ObjetoWebCam webcam;
    private String MEDIA_URL;
    private String nombreVideo;

    @FXML
    public void grabar() {
        if (crono == null) {
            int answer = MessageBox.show(ScreensFramework.stage,
                    "¿Desea activar la webcam?",
                    "Information dialog",
                    MessageBox.ICON_INFORMATION | MessageBox.OK | MessageBox.CANCEL);
            if (answer == MessageBox.OK) {
                nombreVideo = JOptionPane.showInputDialog(
                        null,
                        "¿Qué nombre quiere para el archivo de video?",
                        JOptionPane.QUESTION_MESSAGE);
                try {
                    webcam = new ObjetoWebCam(nombreVideo);
                    Thread t1 = new Thread(webcam);
                    t1.start();
                } catch (Exception e) {
                    nombreVideo = JOptionPane.showInputDialog(
                            null,
                            "Error inesperado",
                            JOptionPane.ERROR_MESSAGE);
                }

                crono = new Cronometro(this);
                Thread t = new Thread(crono);
                t.start();

            }
        }
    }

    @FXML
    public void pausaGrabacion() {
        crono.finalize();
        int answer = MessageBox.show(ScreensFramework.stage,
                "¿Desea finalizar el ejercicio?",
                "Information dialog",
                MessageBox.ICON_INFORMATION | MessageBox.OK | MessageBox.CANCEL);
        /*if (webcam != null) {
            int answer2 = MessageBox.show(ScreensFramework.stage,
                    "¿Desea subir el ejercicio a Youtube?",
                    "Information dialog",
                    MessageBox.ICON_INFORMATION | MessageBox.OK | MessageBox.CANCEL);
        }*/
        if (answer == MessageBox.OK) {
            webcam.finalize();
            puntos = crono.getPuntos();

            // Subida de video
            //if(answer2 == MessageBox.OK){}
            /*YouTubeService service = new YouTubeService("xze.411@gmail.com", "AI39si73OlWoHwDEA5CmVkNeqLGsw5sQawk6T_Odf32LWSwXuIItZC2AV5XGCNeLFFpDmRbBQv-pDbz1TF1j--R_YegUbq2gWQ");
             VideoEntry newEntry = new VideoEntry();
             newEntry.setLocation("Valencia, SPA");

             YouTubeMediaGroup mg = newEntry.getOrCreateMediaGroup();

             mg.addCategory(new MediaCategory(YouTubeNamespace.CATEGORY_SCHEME, "Sport"));
             mg.addCategory(new MediaCategory(YouTubeNamespace.DEVELOPER_TAG_SCHEME, j.getId()+""));
             mg.setPrivate(true);
             mg.setTitle(new MediaTitle());
             mg.getTitle().setPlainTextContent(nombreVideo);
             mg.setKeywords(new MediaKeywords());
             mg.getKeywords().addKeyword("fitbox");
             mg.setDescription(new MediaDescription());
             //mg.getDescription().setPlainTextContent("");
             MediaFileSource ms = new MediaFileSource(new File(webcam.getRuta()), "video/quicktime");
             newEntry.setMediaSource(ms);

             String uploadUrl = "http://uploads.gdata.youtube.com/feeds/api/users/default/uploads";
             try {
             VideoEntry createdEntry = service.insert(new URL(uploadUrl), newEntry);
             } catch (IOException ex) {
             Logger.getLogger(RealizarActividadController.class.getName()).log(Level.SEVERE, null, ex);
             } catch (ServiceException ex) {
             Logger.getLogger(RealizarActividadController.class.getName()).log(Level.SEVERE, null, ex);
             }*/

            answer = MessageBox.show(ScreensFramework.stage,
                    "Genial!! Has acumulado " + puntos + "\nAcumula puntos y gana puestos en el ranking!!",
                    "Information dialog",
                    MessageBox.ICON_INFORMATION | MessageBox.OK);

            if (answer == MessageBox.OK) {
                ScreensFramework.stage.setWidth(921);
                ScreensFramework.stage.setHeight(590);
                puntos = puntos + j.getPuntos();
                j.getValores()[6] = puntos;
                dal.update(j);

                myController.loadScreen(ScreensFramework.PANTALLA_PRINCIPAL, ScreensFramework.PANTALLA_PRINCIPAL_FXML, recurso);
                myController.setScreen(ScreensFramework.PANTALLA_PRINCIPAL);

                //UPDATE PARA ACTUALIZAR LOS PUNTOS DEL USUARIO (PUNTOS ACT+PUNTOS)
            } else if (answer == MessageBox.CANCEL) {
            }

        } else if (answer == MessageBox.CANCEL) {
            crono.resume();
        }

    }

    public void actualizaLabels(final int h, final int m, final int s, final int c) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                crono1.setText(h + ":" + m + ":" + s + ":" + c);
                crono2.setText(h + ":" + m + ":" + s + ":" + c);
            }
        });
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dal = Dal.getDal();
        this.recurso = (Recurso) rb;

        usuario = (Usuario) recurso.getObject("usuario");
        List<Jugador> jugadores = dal.find(Jugador.JUGADORBYUSUARIO, new Object[]{usuario.getId()}, Jugador.class);
        j = jugadores.get(0);
        actividad = (Actividad) recurso.getObject("actividad");
        URL = actividad.getVideo();

        ScreensFramework.stage.setWidth(791);
        ScreensFramework.stage.setHeight(578);
        MEDIA_URL = "<iframe width=\"508\" height=\"419\" src=\"http://www.youtube.com/embed/" + URL + "\" frameborder=\"0\" allowfullscreen></iframe>";
        WebEngine webEngine = videoMuestra.getEngine();
        webEngine.loadContent(MEDIA_URL);
        videoMuestra.setLayoutX(-5);
        videoMuestra.setLayoutY(-4);

    }

    @FXML
    public void home() {

        int answer = MessageBox.show(ScreensFramework.stage,
                "¿Desea volver al menú principal?",
                "Information dialog",
                MessageBox.ICON_INFORMATION | MessageBox.OK | MessageBox.CANCEL);

        if (answer == MessageBox.OK) {
            ScreensFramework.stage.setWidth(921);
            ScreensFramework.stage.setHeight(590);
            myController.setScreen(ScreensFramework.PANTALLA_PRINCIPAL);
        } else if (answer == MessageBox.CANCEL) {
        }
    }
}
