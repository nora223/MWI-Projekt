/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package osmfxml;

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
    public void alg1copy(){
        showAlgMap();
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
