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
import java.awt.Image;
import javax.imageio.ImageIO;

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
       menu.Menu.readKML();
   }
   
    @FXML
    public void readSUR() throws FileNotFoundException, IOException {

        try {
            //String test = null;
            String koordinaten[][] = menu.Menu.readSUR();
            int z = menu.Menu.getAnzahlSUR();
            /*for (int i = 0; i < z; i++) {
             System.out.println("1. Schleife: " + i);
             for (int j = 0; j <= 3; j++) {
             System.out.println("Durchlauf " + j);
             System.out.println(koordinaten[i][j]);
             }
             }*/
            String Ausgabe = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\n"
                    + "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"de\" lang=\"de-de\">\n"
                    + "    <head>\n"
                    + "        <title>Map | Testanwendung</title>\n"
                    + "        <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" />\n"
                    + "        <meta http-equiv=\"content-script-type\" content=\"text/javascript\" />\n"
                    + "        <meta http-equiv=\"content-style-type\" content=\"text/css\" />\n"
                    + "        <meta http-equiv=\"content-language\" content=\"de\" />\n"
                    + "        <link rel=\"stylesheet\" type=\"text/css\" href=\"map.css\"></link>\n"
                    + "        <!--[if IE]>\n"
                    + "        <link rel=\"stylesheet\" type=\"text/css\" href=\"ie_map.css\"></link>\n"
                    + "        <![endif]-->\n"
                    + "        <script type=\"text/javascript\" src=\"http://www.openlayers.org/api/OpenLayers.js\"></script>\n"
                    + "        <script type=\"text/javascript\" src=\"http://www.openstreetmap.org/openlayers/OpenStreetMap.js\"></script>\n"
                    + "        <script type=\"text/javascript\" src=\"tom.js\"></script>\n"
                    + "        <script type=\"text/javascript\" src=\"test.js\"></script>\n"
                    + "        <script type=\"text/javascript\" src=\"menu/showCoordinate.js\"></script>\n"
                    + "        <script type=\"text/javascript\">\n"
                    + "            //<![CDATA[\n"
                    + "\n"
                    + "            var map;\n"
                    + "            var layer_mapnik;\n"
                    + "            var layer_tah;\n"
                    + "            var layer_markers;\n"
                    + "            var getLon = null;\n"
                    + "\n"
                    + "//            function timedRefresh(timeoutPeriod) {\n"
                    + "//                setTimeout(\"location.reload(true);\", timeoutPeriod);\n"
                    + "//            }\n"
                    + "            function drawmap() {\n"
                    + "                // Popup und Popuptext mit evtl. Grafik\n"
                    + "                var popuptext = \"<font color=\\\"black\\\"><b>DHBW Karlsruhe<br>Erzbergerstra&szlig;e 121<br>76133 Karlsruhe</b><p><img src=\\\"dhbw.jpg\\\" width=\\\"180\\\" height=\\\"113\\\"></p></font>\";\n"
                    + "\n"
                    + "                OpenLayers.Lang.setCode('de');\n"
                    + "\n"
                    + "                // Position und Zoomstufe der Karte\n"
                    + "                var lon = "+ koordinaten[0][1] + ";\n"
                    + "                var lat = "+ koordinaten[0][2] + ";\n"
                    + "                var zoom = 15;\n"
                    + "                \n"
                    + "                map = new OpenLayers.Map('map', {\n"
                    + "                    projection: new OpenLayers.Projection(\"EPSG:900913\"),\n"
                    + "                    displayProjection: new OpenLayers.Projection(\"EPSG:4326\"),\n"
                    + "                    controls: [\n"
                    + "                        new OpenLayers.Control.Navigation(),\n"
                    + "                        new OpenLayers.Control.LayerSwitcher(),\n"
                    + "                        new OpenLayers.Control.PanZoomBar()],\n"
                    + "                    maxExtent:\n"
                    + "                            new OpenLayers.Bounds(-20037508.34, -20037508.34,\n"
                    + "                                    20037508.34, 20037508.34),\n"
                    + "                    numZoomLevels: 18,\n"
                    + "                    maxResolution: 156543,\n"
                    + "                    units: 'meters'\n"
                    + "                });\n"
                    + "\n"
                    + "                layer_mapnik = new OpenLayers.Layer.OSM.Mapnik(\"Mapnik\");\n"
                    + "                layer_markers = new OpenLayers.Layer.Markers(\"Address\", {projection: new OpenLayers.Projection(\"EPSG:4326\"),\n"
                    + "                    visibility: true, displayInLayerSwitcher: false});\n"
                    + "\n"
                    + "                //getLon = getLongitude();\n"
                    + "                //getLat = coordinates[1];\n"
                    + "                //coordinates[1] = showcoordinate.getLatitude();\n"
                    + "\n"
                    + "                //if (getLon != null) {\n"
                    + "                //popuptext = getLon + \"<font color=\\\"black\\\"><b>WOOOP WOOP<br>Erzbergerstra&szlig;e 121<br>76133 Karlsruhe</b><p><img src=\\\"dhbw.jpg\\\" width=\\\"180\\\" height=\\\"113\\\"></p></font>\";\n"
                    + "                map.addLayers([layer_mapnik, layer_markers]);\n"
                    + "                jumpTo(lon, lat, zoom);\n"
                    + "                // Position des Markers\n"
                    + "                addMarker(layer_markers, 8.38573, 49.02640, popuptext);\n"
                    + "\n"
                    + "                //} else {\n"
                    + "                //    popuptext = \"<font color=\\\"black\\\"><b>Test<br>Erzbergerstra&szlig;e 121<br>76133 Karlsruhe</b><p><img src=\\\"dhbw.jpg\\\" width=\\\"180\\\" height=\\\"113\\\"></p></font>\";\n"
                    + "                //    map.addLayers([layer_mapnik, layer_markers]);\n"
                    + "                //    jumpTo(lon, lat, zoom);\n"
                    + "\n"
                    + "                // Position des Markers\n"
                    + "                //    addMarker(layer_markers, 8.38573, 49.02640, popuptext);\n"
                    + "\n"
                    + "                //}\n"
                    + "            }\n"
                    + "\n"
                    + "            //]]>\n"
                    + "        </script>\n"
                    + "\n"
                    + "    </head>\n"
                    + "    <body onload=\"drawmap();\"><!-- Refresh der Seite -->\n"
                    + "\n"
                    + "        <div id=\"map\" id=\"box\">\n"
                    + "        </div>\n"
                    + "\n"
                    + "    </body>\n"
                    + "</html>";
            String dateiName = "C:\\Users\\CaReich\\Documents\\NetBeansProjects\\MWI-Projekt\\build\\classes\\osmfxml\\SURAusgabe.html";
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
    
    @FXML
    public void showImage(){
       Image image = null; try {
    URL url = new URL(
        "http://www.personal.psu.edu/acr117/blogs/audrey/images/image-2.jpg");
    image = ImageIO.read(url);
    } catch (IOException e) {
    }
    }


}
