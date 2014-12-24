/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package osmfxml;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author Tino
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Label label;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    @FXML
    WebView WebViewMap;

    @FXML
    WebView WebViewEarth;
    Scene scene;

    @FXML
    WebView WebViewSatellit;
    
    @FXML
    WebView WebViewAlg;

    @FXML
    public void showMap() {

        WebEngine webEngine = WebViewMap.getEngine();
        URL url = getClass().getResource("index.html");
        webEngine.load(url.toExternalForm());

    }

    @FXML
    public void showEarth() {

        WebEngine webEngine = WebViewEarth.getEngine();
        URL url = getClass().getResource("google.html");
        webEngine.load(url.toExternalForm());

    }

    @FXML
    public void showgoogleSatellit() {

        WebEngine webEngine = WebViewSatellit.getEngine();
        URL url = getClass().getResource("googleSatellit.html");
        //webEngine.executeScript("document.setMapTypeSatellit");
        webEngine.load(url.toExternalForm());

    }
    @FXML
    public void showAlgMap() {

        WebEngine webEngine = WebViewAlg.getEngine();
        URL url = getClass().getResource("algMap.html");
        //webEngine.executeScript("document.setMapTypeSatellit");
        webEngine.load(url.toExternalForm());

    }
    @FXML
    public void showAlg2Map() {

        WebEngine webEngine = WebViewAlg.getEngine();
        URL url = getClass().getResource("alg2Map.html");
        //webEngine.executeScript("document.setMapTypeSatellit");
        webEngine.load(url.toExternalForm());

    }
    @FXML
    public void showAlg3Map() {

        WebEngine webEngine = WebViewAlg.getEngine();
        URL url = getClass().getResource("alg3Map.html");
        //webEngine.executeScript("document.setMapTypeSatellit");
        webEngine.load(url.toExternalForm());

    }
    
      @FXML
    public void showAlg4Map() {

        WebEngine webEngine = WebViewAlg.getEngine();
        URL url = getClass().getResource("alg4Map.html");
        //webEngine.executeScript("document.setMapTypeSatellit");
        webEngine.load(url.toExternalForm());

    }
    
    @FXML
    public void alg1copy(){
        showAlgMap();
    }

    
   @FXML
   public void readKML()throws FileNotFoundException{
       
       try{
        String[] koordinaten = menu.Menu.readKML();
        String Ausgabe = menu.HTML_text.generatekmlHTML(koordinaten);

        String dateiName = "src\\osmfxml\\KmlAusgabe.html";
        FileOutputStream schreibeStrom = new FileOutputStream(dateiName);
        for (int i = 0; i < Ausgabe.length(); i++) {
            schreibeStrom.write((byte) Ausgabe.charAt(i));
        }
        schreibeStrom.close();

        System.out.println("Datei ist geschrieben!");
        WebEngine webEngine = WebViewMap.getEngine();
        URL url = getClass().getResource("KmlAusgabe.html");
        //webEngine.executeScript("document.setMapTypeSatellit");
        
        webEngine.load(url.toExternalForm());

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Datei könnte nicht gelesen werden. " + e);
        }
   }
   
    @FXML
    public void readSUR() throws FileNotFoundException, IOException {

        try {
            String koordinaten[][] = menu.Menu.readSUR();
            int z = menu.Menu.getAnzahlSUR();
            /*for (int i = 0; i < z; i++) {
             System.out.println("1. Schleife: " + i);
             for (int j = 0; j <= 3; j++) {
             System.out.println("Durchlauf " + j);
             System.out.println(koordinaten[i][j]);
             }
             }*/
            String Ausgabe = menu.Menu.generateHTML();
            String dateiName = "C:\\Users\\CaReich\\Documents\\NetBeansProjects\\MWI-Projekt\\src\\osmfxml\\SURAusgabe.html";
                FileOutputStream schreibeStrom = new FileOutputStream(dateiName);
                for (int i = 0; i < Ausgabe.length(); i++) {
                    schreibeStrom.write((byte) Ausgabe.charAt(i));
                }
            schreibeStrom.close();
            
            System.out.println("Datei ist geschrieben!");
            WebEngine webEngine = WebViewMap.getEngine();
            URL url = getClass().getResource("SURAusgabe.html");
            //webEngine.executeScript("document.setMapTypeSatellit");
            webEngine.load(url.toExternalForm());

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Datei könnte nicht gelesen werden. " + e);
        }

    }
   
   public void saveKML(){
       menu.Menu.saveKML();
   }
   
    @FXML
    public void drawPolygon() throws ScriptException, NoSuchMethodException{
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("osmfxml.algMap.html");
        String st = "drawPolygonHTML";
        engine.eval(st);
        Invocable inv = (Invocable)engine;
        inv.invokeFunction("drawPolygonHTML");
        
        
    }


}
