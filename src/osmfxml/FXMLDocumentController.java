package osmfxml;

import com.sun.org.apache.bcel.internal.generic.TargetLostException;
import com.sun.prism.BufferedImageTools;
import com.sun.webpane.platform.ContextMenu;
import java.awt.Graphics2D;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
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
import java.io.File;
import static java.lang.Thread.sleep;
import java.util.regex.Pattern;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javax.imageio.ImageIO;
import javafx.scene.image.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javax.imageio.ImageWriter;

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

    String zahlKML = "0001";
    String zahl2;

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
    public void readKML() throws FileNotFoundException {
        WebEngine webEngineTest = WebViewMap.getEngine();

        try {
            String[] koordinaten = menu.Menu.readKML();
            int counter = koordinaten.length;
            System.out.println("Counter " + counter);
            //System.out.println("Länge Koordinaten: " + koordinaten.length);
            //for (int j = 0; j < koordinaten.length; j++){
            //    System.out.println("Inhalt: " + koordinaten[j]);
            //}
            //erste Koordinate = Längengrad und zweite Koordinate = Breitengrad
            String[] AllLon = new String[counter]; //alle Längengrad Angaben
            String[] AllLat = new String[counter];  //alle Breitengrad Angaben
            String firstLon = null; //Längengrad
            String firstLat = null; //Breitengrad

            for (int i = 0; i < koordinaten.length; i++) {
                //Zeile 0 des Array koordinaten ist immer leer
                //System.out.println("zeile " + i + ":" + koordinaten[i]);
                if (i == 0) {
                    //In der schleife werden die ersten koordinaten abgespeichert in firstLon und Lat um die Karte zu zentrieren
                    //und in AllLon und -Lat 
                    String[] firstLine = koordinaten[i].split(",");
                    //System.out.println(firstLine[0] +"    " + firstLine[1]);
                    firstLon = firstLine[0];
                    firstLat = firstLine[1];
                    AllLon[i] = firstLon;
                    AllLat[i] = firstLat;
                }
                String[] helpLine = koordinaten[i].split(",");
                //System.out.println(helpLine[0] + "     " + helpLine[1]);

                AllLon[i] = helpLine[0];
                AllLat[i] = helpLine[1];
                //System.out.println("Längengrad: " + AllLon[i] + " Breitengrad: " + AllLat[i]);
            }

            /*AllLon[0] = firstLon;
             AllLat[0] = firstLat;*/
            webEngineTest.executeScript("createArrayLonLat(" + (counter) + ")");

            Object helpLongtitude = null;
            Object helpLatitude = null;
            Object temp = null;
            //String[] lon = new String[counter];
            //String[] lat = new String[counter];
            for (int count = 0; count < counter; count++) {
                helpLongtitude = AllLon[count];
                helpLatitude = AllLat[count];
                //System.out.println("test" + helpLatitude);
                temp = webEngineTest.executeScript("setLonLatArrays(" + helpLongtitude + "," + helpLatitude + "," + count + ")");
                //System.out.println(temp);
            }
            //System.out.println("Latitude" + temp);
            webEngineTest.executeScript("goTo(" + firstLon + "," + firstLat + ")");
            webEngineTest.executeScript("pintarZonas()");

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Datei könnte nicht gelesen werden. " + e);
        }
    }

    String[][] ergSURGlobal;

    @FXML
    public void readAllSUR() throws FileNotFoundException, IOException, InterruptedException {
        System.out.println("KML Datei wird eingelesen");
        String[][] ergSUR;
        ergSUR = menu.Menu.readAllSUR();
        ergSURGlobal = new String[ergSUR.length][4];
        for (int i = 0; i < ergSUR.length; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.println(ergSUR[i][j]);
                ergSURGlobal[i][j] = ergSUR[i][j];
            }

        }

        int countSURS = ergSUR.length;
        for (int i = 0; i < countSURS; i++) {
            System.out.println("CreatePolygons wird aufgerufen");
            zahlKML = ergSUR[i][0];
            createPolygons(ergSUR[i]);

        }

    }

    public void findNextSUR() throws IOException, InterruptedException {
        int zahl = Integer.parseInt(zahl2);
        for (int i = zahl; i < ergSURGlobal.length; i++) {
            createPolygons(ergSURGlobal[zahl]);
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

    //public void saveKML() {
    //    menu.Menu.saveKML();
    //}
    @FXML
    public void drawPolygon() throws ScriptException, NoSuchMethodException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("osmfxml.algMap.html");
        String st = "drawPolygonHTML";
        engine.eval(st);
        Invocable inv = (Invocable) engine;
        inv.invokeFunction("drawPolygonHTML");
    }
    @FXML
    ImageView imageView;

    @FXML
    public void showImage() {
        Image image;
        
        image = WebViewMap.snapshot(null, null);
        imageView.setImage(image);
    }
    @FXML
    ImageView imageViewSelectColor;
    //Gibt die Farbwerte auf der Konsole für jeden Pixel aus, VORSICHT!!! Dauert lang

    @FXML
    public void selectColor() {
        Image image;
        image = WebViewMap.snapshot(null, null);
        imageViewSelectColor.setImage(image);
        // Obtain PixelReader
        PixelReader pixelReader = image.getPixelReader();
        System.out.println("Image Width: " + image.getWidth());
        System.out.println("Image Height: " + image.getHeight());
        System.out.println("Pixel Format: " + pixelReader.getPixelFormat());
        // Determine the color of each pixel in the image
        for (int readY = 0; readY < image.getHeight(); readY++) {
            for (int readX = 0; readX < image.getWidth(); readX++) {
                Color color = pixelReader.getColor(readX, readY);
                System.out.println(/*"\nPixel color at coordinates ("
                         + readX + "," + readY + ") "
                         + */color.toString());
                /*System.out.println("R = " + color.getRed());
                 System.out.println("G = " + color.getGreen());
                 System.out.println("B = " + color.getBlue());
                 System.out.println("Opacity = " + color.getOpacity());
                 System.out.println("Saturation = " + color.getSaturation()); */
            }
        }
    }
    @FXML
    ImageView imageViewChangeColor;

    public void changeColor() throws IOException {

        //findLake(sur);
    }

    public void createPolygons(String[] sur) throws IOException, InterruptedException {
        System.out.println("Create Polygons wird ausgeführt");
        if (sur[3].contains("swimming") || sur[3].contains("fishing")) {
            System.out.println("nun wird zu findLake gesprungen");
            findLake(sur);
            zahl2 = zahlKML;
        }
    }

    public void findLake(String[] sur) throws IOException, InterruptedException {
        System.out.println("FindLake wird ausgeführt");
        String latSUR = sur[1];
        latSUR = latSUR.substring(1);
        String lonSUR = sur[2];
        lonSUR = lonSUR.substring(1);
        String zoom = "19";
        Object waitForImage;

        Object rückgabe;

        System.out.println("gleich wird der Punkt angezeigt");
        WebEngine webEngineGoToCoordinate = WebViewMap.getEngine();
        webEngineGoToCoordinate.executeScript("goTo(" + lonSUR + "," + latSUR + "," + zoom + ")");
        //webEngineGoToCoordinate.loadContent("goTo(" + lonSUR + "," + latSUR + "," + zoom + ")");

    }

    public void starttofindPolygonLake() throws FileNotFoundException, IOException, InterruptedException {
        Image image;
        image = WebViewMap.snapshot(null, null);

        // erstellt PixelReader
        PixelReader pixelReader = image.getPixelReader();

        // erstellt WritableImage
        WritableImage wImage = new WritableImage(
                (int) image.getWidth(),
                (int) image.getHeight());

        //erstellt einen PixelWriter
        PixelWriter pixelWriter = wImage.getPixelWriter();

        //Schleife die jedes Pixel durchläuft und die Farbe auf Rot für die gewählten Farben setzt
        //die restlichen Pixel werden auf Transparent gesetzt
        for (int readY = 0;
                readY < image.getHeight();
                readY++) {
            for (int readX = 0; readX < image.getWidth(); readX++) {

                Color color = pixelReader.getColor(readX, readY);
                Color red = Color.RED;
                Color transparent = Color.TRANSPARENT;
                if (color.toString().equals("0xb5d0d0ff")) {
                    pixelWriter.setColor(readX, readY, red);

                } else {
                    pixelWriter.setColor(readX, readY, transparent);
                }
            }
        }
        //sucht und speichert das zentrale Pixel
        double middleY = wImage.getHeight() / 2;
        double middleX = wImage.getWidth() / 2;
        double[] zentralPixel = {middleX, middleY};

        //variable für das nächstgelegene Pixel mit roter Farbe zum zentralen Pixel
        int[] nearestPixel = {0, 0};

        //Hilfsvariablen
        double distance = 100000;
        double helpDistance;
        double[] helpPixel;

        //PixelReader für das erzeugte Wirteable Image
        PixelReader wPixelReader = wImage.getPixelReader();

        //Schleife zur Bestimmung des nächsten roten Pixel zum zentralen Pixel
        for (int y = 0; y < wImage.getHeight(); y++) {
            for (int x = 0; x < wImage.getWidth(); x++) {
                Color color = wPixelReader.getColor(x, y);
                //System.out.println("Color Pixel:"+ color.toString());

                if (color.toString().equals(Color.RED.toString())) {
                    helpDistance = Math.sqrt(Math.pow(Math.abs(zentralPixel[0] - y), 2) + Math.pow(Math.abs(zentralPixel[1] - x), 2));
                    System.out.println("Berechnung Distance");
                    if (helpDistance < distance) {
                        distance = helpDistance;
                        nearestPixel[0] = x;
                        nearestPixel[1] = y;
                    }
                }
            }

        }

        //setzt hilfsvariable auf den nächsten Punkt
        int checkX = nearestPixel[0];
        int checkY = nearestPixel[1];

        //Hilfsvariablen
        int helpX = 0;
        int helpY = 0;
        boolean redpixel = true;
        int feld = 0;
        int zähler = 0;
        Color blue = Color.BLUE;
        int pixelCounter = 0;

        //Array zum speichern der gefilterten Randpixel
        int[][] pixelArray = new int[10000][2];

        //while schleife die solange offen bleibt, bis der boolean redpixel auf false ist, dieser wird false, wenn kein RAndpixel mehr gefunden wird
        while (redpixel) {
            //zähler für momentane prüfung, damit while schleife abbricht, da code für blau nicht gefunden
            zähler++;
            //setze redpixel auf false damit nur weiter durch dei schleife gelaufen wird wenn in einer if schleife gegangen wird
            redpixel = false;
            //erste if schleife prüft, ob der nächste pixel rot oder blau ist (blau noch nciht implementiert da farbcode nicht vorhanden, momentaner farbcode leider nicht richtig)
            if (!(wPixelReader.getColor(checkX + 1, checkY).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX + 1, checkY).toString().equals("0x0000ffff"))) {
                //Prüfung ob danach ein Roter Pixel kommt, weil dann ist dieser Pixel ein RandPixel
                if (wPixelReader.getColor(checkX + 1, checkY + 1).toString().equals("0xff0000ff")) {
                    //wie ebenbeschrieben wird boolean auf true gesetzt
                    redpixel = true;
                    feld = 1;
                    //Randpixel wird blau gemacht
                    pixelWriter.setColor(checkX + 1, checkY + 1, blue);
                    //neuer Randpunkt wird als neuer Startpunkt gewählt
                    checkX = checkX + 1;
                    checkY = checkY + 1;
                    //Randpixel wird gespeichert
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;

                    pixelCounter = pixelCounter + 1;

                }
            }
            //diese Schleifen wieder holen sich 8 mal wo immer ein Pixel weiter gegangen wird //Start punkt für den Rundgang um den Pixel ist der 3 Uhr Pixel
            if (!(wPixelReader.getColor(checkX + 1, checkY + 1).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX + 1, checkY + 1).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX, checkY + 1).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 2;
                    pixelWriter.setColor(checkX, checkY + 1, blue);
                    checkY = checkY + 1;
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;
                    pixelCounter = pixelCounter + 1;

                }
            }
            if (!(wPixelReader.getColor(checkX, checkY + 1).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX, checkY + 1).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX - 1, checkY + 1).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 3;
                    pixelWriter.setColor(checkX - 1, checkY + 1, blue);
                    checkX = checkX - 1;
                    checkY = checkY + 1;
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;
                    pixelCounter = pixelCounter + 1;

                }
            }
            if (!(wPixelReader.getColor(checkX - 1, checkY + 1).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX - 1, checkY + 1).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX - 1, checkY).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 4;
                    pixelWriter.setColor(checkX - 1, checkY, blue);
                    checkX = checkX - 1;
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;
                    pixelCounter = pixelCounter + 1;

                }
            }
            if (!(wPixelReader.getColor(checkX - 1, checkY).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX - 1, checkY).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX - 1, checkY - 1).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 5;
                    pixelWriter.setColor(checkX - 1, checkY - 1, blue);
                    checkX = checkX - 1;
                    checkY = checkY - 1;
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;
                    pixelCounter = pixelCounter + 1;

                }
            }

            if (!(wPixelReader.getColor(checkX - 1, checkY - 1).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX - 1, checkY - 1).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX, checkY - 1).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 6;
                    pixelWriter.setColor(checkX, checkY - 1, blue);
                    checkY = checkY - 1;
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;
                    pixelCounter = pixelCounter + 1;

                }

            }

            if (!(wPixelReader.getColor(checkX, checkY - 1).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX, checkY - 1).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX + 1, checkY - 1).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 7;
                    pixelWriter.setColor(checkX + 1, checkY - 1, blue);
                    checkX = checkX + 1;
                    checkY = checkY - 1;
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;
                    pixelCounter = pixelCounter + 1;

                }
            }
            if (!(wPixelReader.getColor(checkX + 1, checkY - 1).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX + 1, checkY - 1).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX + 1, checkY).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 8;
                    pixelWriter.setColor(checkX + 1, checkY, blue);
                    checkX = checkX + 1;
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;
                    pixelCounter = pixelCounter + 1;

                }
            }

            //Wenn eine Runde um das Gebäude geangen wurde sind die hilfsvariablen wieder auf dem Anfangs punkt, hier wird dann die schleife beendet
            if (checkX == nearestPixel[0] && checkY == nearestPixel[1]) {
                redpixel = false;
            }
            // Zähler damit momentan beendet wird, da farbcode für blau nicht vorhanden, so wird nach 10000 Pixeln abgebrochen
            //if (zähler == 20000) {
            //   break;
            //}
            //Schleife läuft zum Randpixel falls Koordinate auf Objekt
            if (zähler < 2 && redpixel == false) {
                while (wPixelReader.getColor(checkX, checkY).toString().equals("0xff0000ff")) {
                    checkX++;
                }
                checkX = checkX - 1;
                redpixel = true;
            }
            //Hilfsvariablen
            int xhelp = 0;
            int yhelp = 0;
            int zähler1 = 0;

            //Schleife um einzelne FehlerPixel zu Filtern
            if (redpixel == false) {
                if (wPixelReader.getColor(checkX + 1, checkY).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    xhelp = xhelp + 1;
                }
                if (wPixelReader.getColor(checkX + 1, checkY + 1).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    xhelp = xhelp + 1;
                    yhelp = yhelp + 1;

                }
                if (wPixelReader.getColor(checkX + 0, checkY + 1).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    yhelp = yhelp + 1;

                }
                if (wPixelReader.getColor(checkX - 1, checkY + 1).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    xhelp = xhelp - 1;
                    yhelp = yhelp + 1;

                }
                if (wPixelReader.getColor(checkX - 1, checkY + 0).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    xhelp = xhelp - 1;

                }
                if (wPixelReader.getColor(checkX - 1, checkY - 1).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    xhelp = xhelp - 1;
                    yhelp = yhelp - 1;

                }
                if (wPixelReader.getColor(checkX + 0, checkY - 1).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    yhelp = yhelp - 1;

                }
                if (wPixelReader.getColor(checkX + 1, checkY - 1).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    xhelp = xhelp + 1;
                    yhelp = yhelp - 1;
                }
                if (zähler1 == 1) {
                    pixelWriter.setColor(checkX, checkY, Color.TRANSPARENT);
                    checkX = checkX + xhelp;
                    checkY = checkY + yhelp;
                    redpixel = true;
                }
            }
        }

        //Zeigt das gefilterte Image im rechten Fenster
        imageViewChangeColor.setImage(wImage);

        //Bestimmt den zentralen Pixel der index.html
        WebEngine webEngineTest = WebViewMap.getEngine();
        Object centerWebView;
        centerWebView = webEngineTest.executeScript("test()");
        //System.out.println(centerWebView);

        //Hilfsvariablen
        String test;
        String[] lon = new String[pixelCounter];
        String[] lat = new String[pixelCounter];
        String lonText = "lon=";
        String empty = "";
        String help = "";
        String help2 = "";
        String[][] longlat = new String[pixelCounter][2];
        String[] lonLatForSave = new String[pixelCounter];
        //Schleife zur Anpassung der Pixel Aufgrung ungleicher Größe WebView ImageView
        //Bearbeiten String Koordinaten für einzeichnen Polygon in Map
        for (int count = 0;
                count < pixelCounter;
                count++) {
            //*********************************************
            //für -10 die Variable aus Höhe/Breite zentralger Pixel Image - Höhe/Breite WebView ersetzen
            //***********************************************
            int x = pixelArray[count][0] - 9;
            int y = pixelArray[count][1] - 10;
            Object[] coordinate = new Object[pixelCounter];
            coordinate[count] = webEngineTest.executeScript("getCoordinate(" + x + ", " + y + ")");
            test = coordinate[count].toString();
            help = test.replaceAll(lonText, empty);
            help2 = help.replaceAll("lat=", empty);
            longlat[count] = help2.split(Pattern.quote(","));
            lon[count] = longlat[count][0];
            lat[count] = longlat[count][1];
            lonLatForSave[count] = lon[count] + "," + lat[count];
        }

        System.out.println(
                "SaveKML Methode wird aufgerufen");

        String[] test2;
        test2 = new String[5];
        test2[0] = "5.9999999,50.93210560";
        test2[1] = "5.34239840,50.93214990";
        test2[2] = "5.34245170,50.93209770";
        test2[3] = "5.34234260,50.93205340";
        test2[4] = "5.34228930,50.99999990";

       // menu.Menu.saveKML(test2);
        //Erstellt Array's für Längen- und Breiten-Koordinaten in JavaScript (index.html)
        webEngineTest.executeScript(
                "createArrayLonLat(" + pixelCounter + ")");

        //Hilfsvariablen
        Object helpLongtitude;
        Object helpLatitude;

        //Speichern der Koordinaten für Polygone in JavaScript(index.html)
        for (int count = 0;
                count < pixelCounter;
                count++) {
            helpLongtitude = lon[count];
            helpLatitude = lat[count];
            helpLatitude = webEngineTest.executeScript("setLonLatArrays(" + helpLongtitude + "," + helpLatitude + "," + count + ")");
        }

        //Ausgabe Polygon auf Map
        webEngineTest.executeScript(
                "pintarZonas()");

        System.out.println(zahlKML);
        findNextSUR();

    }

    public void findForestAndGreenField() throws IOException {

        //Erstellt ein Image der OSM Map
        Image image;
        image = WebViewMap.snapshot(null, null);

        // erstellt PixelReader
        PixelReader pixelReader = image.getPixelReader();

        // erstellt WritableImage
        WritableImage wImage = new WritableImage(
                (int) image.getWidth(),
                (int) image.getHeight());

        //erstellt einen PixelWriter
        PixelWriter pixelWriter = wImage.getPixelWriter();

        //Schleife die jedes Pixel durchläuft und die Farbe auf Rot für die gewählten Farben setzt
        //die restlichen Pixel werden auf Transparent gesetzt
        for (int readY = 0; readY < image.getHeight(); readY++) {
            for (int readX = 0; readX < image.getWidth(); readX++) {

                Color color = pixelReader.getColor(readX, readY);
                Color red = Color.RED;
                Color transparent = Color.TRANSPARENT;
                if (color.toString().equals("0xceeca8ff")//Wiese
                        || color.toString().equals("0xcfeca8ff")//Wiese
                        || color.toString().equals("0xccf6c9ff") //Wiese
                        || color.toString().equals("0xa0ce85ff")
                        || color.toString().equals("0xaacaaeff")
                        || color.toString().equals("0xaacbaeff")) {

                    pixelWriter.setColor(readX, readY, red);

                } else {
                    pixelWriter.setColor(readX, readY, transparent);
                }
            }
        }
        //sucht und speichert das zentrale Pixel
        double middleY = wImage.getHeight() / 2;
        double middleX = wImage.getWidth() / 2;
        double[] zentralPixel = {middleX, middleY};

        //variable für das nächstgelegene Pixel mit roter Farbe zum zentralen Pixel
        int[] nearestPixel = {0, 0};

        //Hilfsvariablen
        double distance = 100000;
        double helpDistance;
        double[] helpPixel;

        //PixelReader für das erzeugte Wirteable Image
        PixelReader wPixelReader = wImage.getPixelReader();

        //Schleife zur Bestimmung des nächsten roten Pixel zum zentralen Pixel
        for (int y = 0; y < wImage.getHeight(); y++) {
            for (int x = 0; x < wImage.getWidth(); x++) {
                Color color = wPixelReader.getColor(x, y);
                //System.out.println("Color Pixel:"+ color.toString());

                if (color.toString().equals(Color.RED.toString())) {
                    helpDistance = Math.sqrt(Math.pow(Math.abs(zentralPixel[0] - y), 2) + Math.pow(Math.abs(zentralPixel[1] - x), 2));
                    //System.out.println("Berechnung Distance");
                    if (helpDistance < distance) {
                        distance = helpDistance;
                        nearestPixel[0] = x;
                        nearestPixel[1] = y;
                    }
                }
            }

        }

        //setzt hilfsvariable auf den nächsten Punkt
        int checkX = nearestPixel[0];
        int checkY = nearestPixel[1];

        //Hilfsvariablen
        int helpX = 0;
        int helpY = 0;
        boolean redpixel = true;
        int feld = 0;
        int zähler = 0;
        Color blue = Color.BLUE;
        int pixelCounter = 0;

        //Array zum speichern der gefilterten Randpixel
        int[][] pixelArray = new int[10000][2];

        //while schleife die solange offen bleibt, bis der boolean redpixel auf false ist, dieser wird false, wenn kein RAndpixel mehr gefunden wird
        while (redpixel) {
            //zähler für momentane prüfung, damit while schleife abbricht, da code für blau nicht gefunden
            zähler++;
            //setze redpixel auf false damit nur weiter durch dei schleife gelaufen wird wenn in einer if schleife gegangen wird
            redpixel = false;
            //erste if schleife prüft, ob der nächste pixel rot oder blau ist (blau noch nciht implementiert da farbcode nicht vorhanden, momentaner farbcode leider nicht richtig)
            if (!(wPixelReader.getColor(checkX + 1, checkY).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX + 1, checkY).toString().equals("0x0000ffff"))) {
                //Prüfung ob danach ein Roter Pixel kommt, weil dann ist dieser Pixel ein RandPixel
                if (wPixelReader.getColor(checkX + 1, checkY + 1).toString().equals("0xff0000ff")) {
                    //wie ebenbeschrieben wird boolean auf true gesetzt
                    redpixel = true;
                    feld = 1;
                    //Randpixel wird blau gemacht
                    pixelWriter.setColor(checkX + 1, checkY + 1, blue);
                    //neuer Randpunkt wird als neuer Startpunkt gewählt
                    checkX = checkX + 1;
                    checkY = checkY + 1;
                    //Randpixel wird gespeichert
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;

                    pixelCounter = pixelCounter + 1;

                }
            }
            //diese Schleifen wieder holen sich 8 mal wo immer ein Pixel weiter gegangen wird //Start punkt für den Rundgang um den Pixel ist der 3 Uhr Pixel
            if (!(wPixelReader.getColor(checkX + 1, checkY + 1).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX + 1, checkY + 1).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX, checkY + 1).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 2;
                    pixelWriter.setColor(checkX, checkY + 1, blue);
                    checkY = checkY + 1;
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;
                    pixelCounter = pixelCounter + 1;

                }
            }
            if (!(wPixelReader.getColor(checkX, checkY + 1).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX, checkY + 1).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX - 1, checkY + 1).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 3;
                    pixelWriter.setColor(checkX - 1, checkY + 1, blue);
                    checkX = checkX - 1;
                    checkY = checkY + 1;
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;
                    pixelCounter = pixelCounter + 1;

                }
            }
            if (!(wPixelReader.getColor(checkX - 1, checkY + 1).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX - 1, checkY + 1).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX - 1, checkY).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 4;
                    pixelWriter.setColor(checkX - 1, checkY, blue);
                    checkX = checkX - 1;
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;
                    pixelCounter = pixelCounter + 1;

                }
            }
            if (!(wPixelReader.getColor(checkX - 1, checkY).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX - 1, checkY).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX - 1, checkY - 1).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 5;
                    pixelWriter.setColor(checkX - 1, checkY - 1, blue);
                    checkX = checkX - 1;
                    checkY = checkY - 1;
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;
                    pixelCounter = pixelCounter + 1;

                }
            }

            if (!(wPixelReader.getColor(checkX - 1, checkY - 1).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX - 1, checkY - 1).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX, checkY - 1).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 6;
                    pixelWriter.setColor(checkX, checkY - 1, blue);
                    checkY = checkY - 1;
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;
                    pixelCounter = pixelCounter + 1;

                }

            }

            if (!(wPixelReader.getColor(checkX, checkY - 1).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX, checkY - 1).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX + 1, checkY - 1).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 7;
                    pixelWriter.setColor(checkX + 1, checkY - 1, blue);
                    checkX = checkX + 1;
                    checkY = checkY - 1;
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;
                    pixelCounter = pixelCounter + 1;

                }
            }
            if (!(wPixelReader.getColor(checkX + 1, checkY - 1).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX + 1, checkY - 1).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX + 1, checkY).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 8;
                    pixelWriter.setColor(checkX + 1, checkY, blue);
                    checkX = checkX + 1;
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;
                    pixelCounter = pixelCounter + 1;

                }
            }

            //Wenn eine Runde um das Gebäude geangen wurde sind die hilfsvariablen wieder auf dem Anfangs punkt, hier wird dann die schleife beendet
            if (checkX == nearestPixel[0] && checkY == nearestPixel[1]) {
                redpixel = false;
            }
            // Zähler damit momentan beendet wird, da farbcode für blau nicht vorhanden, so wird nach 10000 Pixeln abgebrochen
            //if (zähler == 20000) {
            //   break;
            //}
            //Schleife läuft zum Randpixel falls Koordinate auf Objekt
            if (zähler < 2 && redpixel == false) {
                while (wPixelReader.getColor(checkX, checkY).toString().equals("0xff0000ff")) {
                    checkX++;
                }
                checkX = checkX - 1;
                redpixel = true;
            }
            //Hilfsvariablen
            int xhelp = 0;
            int yhelp = 0;
            int zähler1 = 0;

            //Schleife um einzelne FehlerPixel zu Filtern
            if (redpixel == false) {
                if (wPixelReader.getColor(checkX + 1, checkY).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    xhelp = xhelp + 1;
                }
                if (wPixelReader.getColor(checkX + 1, checkY + 1).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    xhelp = xhelp + 1;
                    yhelp = yhelp + 1;

                }
                if (wPixelReader.getColor(checkX + 0, checkY + 1).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    yhelp = yhelp + 1;

                }
                if (wPixelReader.getColor(checkX - 1, checkY + 1).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    xhelp = xhelp - 1;
                    yhelp = yhelp + 1;

                }
                if (wPixelReader.getColor(checkX - 1, checkY + 0).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    xhelp = xhelp - 1;

                }
                if (wPixelReader.getColor(checkX - 1, checkY - 1).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    xhelp = xhelp - 1;
                    yhelp = yhelp - 1;

                }
                if (wPixelReader.getColor(checkX + 0, checkY - 1).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    yhelp = yhelp - 1;

                }
                if (wPixelReader.getColor(checkX + 1, checkY - 1).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    xhelp = xhelp + 1;
                    yhelp = yhelp - 1;
                }
                if (zähler1 == 1) {
                    pixelWriter.setColor(checkX, checkY, Color.TRANSPARENT);
                    checkX = checkX + xhelp;
                    checkY = checkY + yhelp;
                    redpixel = true;
                }
            }
        }
        //Zeigt das gefilterte Image im rechten Fenster
        imageViewChangeColor.setImage(wImage);

        //Bestimmt den zentralen Pixel der index.html
        WebEngine webEngineTest = WebViewMap.getEngine();
        Object centerWebView;
        centerWebView = webEngineTest.executeScript("test()");
        //System.out.println(centerWebView);

        //Hilfsvariablen
        String test;
        String[] lon = new String[pixelCounter];
        String[] lat = new String[pixelCounter];
        String lonText = "lon=";
        String empty = "";
        String help = "";
        String help2 = "";
        String[][] longlat = new String[pixelCounter][2];

        //Schleife zur Anpassung der Pixel Aufgrung ungleicher Größe WebView ImageView
        //Bearbeiten String Koordinaten für einzeichnen Polygon in Map
        for (int count = 0; count < pixelCounter; count++) {
            //*********************************************
            //für -10 die Variable aus Höhe/Breite zentralger Pixel Image - Höhe/Breite WebView ersetzen
            //***********************************************
            int x = pixelArray[count][0] - 9;
            int y = pixelArray[count][1] - 10;
            Object[] coordinate = new Object[pixelCounter];
            coordinate[count] = webEngineTest.executeScript("getCoordinate(" + x + ", " + y + ")");
            test = coordinate[count].toString();
            help = test.replaceAll(lonText, empty);
            help2 = help.replaceAll("lat=", empty);
            longlat[count] = help2.split(Pattern.quote(","));
            lon[count] = longlat[count][0];
            lat[count] = longlat[count][1];
        }

        //Erstellt Array's für Längen- und Breiten-Koordinaten in JavaScript (index.html)
        webEngineTest.executeScript("createArrayLonLat(" + pixelCounter + ")");

        //Hilfsvariablen
        Object helpLongtitude;
        Object helpLatitude;

        //Speichern der Koordinaten für Polygone in JavaScript(index.html)
        for (int count = 0; count < pixelCounter; count++) {
            helpLongtitude = lon[count];
            helpLatitude = lat[count];
            helpLatitude = webEngineTest.executeScript("setLonLatArrays(" + helpLongtitude + "," + helpLatitude + "," + count + ")");
        }
        //Ausgabe Polygon auf Map
        webEngineTest.executeScript("pintarZonas()");

    }

    public void findAreaArroundBuilding() throws IOException {

        //Erstellt ein Image der OSM Map
        Image image;
        image = WebViewMap.snapshot(null, null);

        // erstellt PixelReader
        PixelReader pixelReader = image.getPixelReader();

        // erstellt WritableImage
        WritableImage wImage = new WritableImage(
                (int) image.getWidth(),
                (int) image.getHeight());

        //erstellt einen PixelWriter
        PixelWriter pixelWriter = wImage.getPixelWriter();

        //Schleife die jedes Pixel durchläuft und die Farbe auf Rot für die gewählten Farben setzt
        //die restlichen Pixel werden auf Transparent gesetzt
        for (int readY = 0; readY < image.getHeight(); readY++) {
            for (int readX = 0; readX < image.getWidth(); readX++) {

                Color color = pixelReader.getColor(readX, readY);
                Color red = Color.RED;
                Color transparent = Color.TRANSPARENT;
                if (color.toString().equals("0xe1e1e1ff")
                        || color.toString().equals("0xe0e0e0ff")) {
                    pixelWriter.setColor(readX, readY, red);

                } else {
                    pixelWriter.setColor(readX, readY, transparent);
                }
            }
        }
        //sucht und speichert das zentrale Pixel
        double middleY = wImage.getHeight() / 2;
        double middleX = wImage.getWidth() / 2;
        double[] zentralPixel = {middleX, middleY};

        //variable für das nächstgelegene Pixel mit roter Farbe zum zentralen Pixel
        int[] nearestPixel = {0, 0};

        //Hilfsvariablen
        double distance = 100000;
        double helpDistance;
        double[] helpPixel;

        //PixelReader für das erzeugte Wirteable Image
        PixelReader wPixelReader = wImage.getPixelReader();

        //Schleife zur Bestimmung des nächsten roten Pixel zum zentralen Pixel
        for (int y = 0; y < wImage.getHeight(); y++) {
            for (int x = 0; x < wImage.getWidth(); x++) {
                Color color = wPixelReader.getColor(x, y);
                //System.out.println("Color Pixel:"+ color.toString());

                if (color.toString().equals(Color.RED.toString())) {
                    helpDistance = Math.sqrt(Math.pow(Math.abs(zentralPixel[0] - y), 2) + Math.pow(Math.abs(zentralPixel[1] - x), 2));
                    //System.out.println("Berechnung Distance");
                    if (helpDistance < distance) {
                        distance = helpDistance;
                        nearestPixel[0] = x;
                        nearestPixel[1] = y;
                    }
                }
            }

        }

        //setzt hilfsvariable auf den nächsten Punkt
        int checkX = nearestPixel[0];
        int checkY = nearestPixel[1];

        //Hilfsvariablen
        int helpX = 0;
        int helpY = 0;
        boolean redpixel = true;
        int feld = 0;
        int zähler = 0;
        Color blue = Color.BLUE;
        int pixelCounter = 0;

        //Array zum speichern der gefilterten Randpixel
        int[][] pixelArray = new int[10000][2];

        //while schleife die solange offen bleibt, bis der boolean redpixel auf false ist, dieser wird false, wenn kein RAndpixel mehr gefunden wird
        while (redpixel) {
            //zähler für momentane prüfung, damit while schleife abbricht, da code für blau nicht gefunden
            zähler++;
            //setze redpixel auf false damit nur weiter durch dei schleife gelaufen wird wenn in einer if schleife gegangen wird
            redpixel = false;
            //erste if schleife prüft, ob der nächste pixel rot oder blau ist (blau noch nciht implementiert da farbcode nicht vorhanden, momentaner farbcode leider nicht richtig)
            if (!(wPixelReader.getColor(checkX + 1, checkY).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX + 1, checkY).toString().equals("0x0000ffff"))) {
                //Prüfung ob danach ein Roter Pixel kommt, weil dann ist dieser Pixel ein RandPixel
                if (wPixelReader.getColor(checkX + 1, checkY + 1).toString().equals("0xff0000ff")) {
                    //wie ebenbeschrieben wird boolean auf true gesetzt
                    redpixel = true;
                    feld = 1;
                    //Randpixel wird blau gemacht
                    pixelWriter.setColor(checkX + 1, checkY + 1, blue);
                    //neuer Randpunkt wird als neuer Startpunkt gewählt
                    checkX = checkX + 1;
                    checkY = checkY + 1;
                    //Randpixel wird gespeichert
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;

                    pixelCounter = pixelCounter + 1;

                }
            }
            //diese Schleifen wieder holen sich 8 mal wo immer ein Pixel weiter gegangen wird //Start punkt für den Rundgang um den Pixel ist der 3 Uhr Pixel
            if (!(wPixelReader.getColor(checkX + 1, checkY + 1).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX + 1, checkY + 1).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX, checkY + 1).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 2;
                    pixelWriter.setColor(checkX, checkY + 1, blue);
                    checkY = checkY + 1;
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;
                    pixelCounter = pixelCounter + 1;

                }
            }
            if (!(wPixelReader.getColor(checkX, checkY + 1).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX, checkY + 1).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX - 1, checkY + 1).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 3;
                    pixelWriter.setColor(checkX - 1, checkY + 1, blue);
                    checkX = checkX - 1;
                    checkY = checkY + 1;
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;
                    pixelCounter = pixelCounter + 1;

                }
            }
            if (!(wPixelReader.getColor(checkX - 1, checkY + 1).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX - 1, checkY + 1).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX - 1, checkY).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 4;
                    pixelWriter.setColor(checkX - 1, checkY, blue);
                    checkX = checkX - 1;
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;
                    pixelCounter = pixelCounter + 1;

                }
            }
            if (!(wPixelReader.getColor(checkX - 1, checkY).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX - 1, checkY).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX - 1, checkY - 1).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 5;
                    pixelWriter.setColor(checkX - 1, checkY - 1, blue);
                    checkX = checkX - 1;
                    checkY = checkY - 1;
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;
                    pixelCounter = pixelCounter + 1;

                }
            }

            if (!(wPixelReader.getColor(checkX - 1, checkY - 1).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX - 1, checkY - 1).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX, checkY - 1).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 6;
                    pixelWriter.setColor(checkX, checkY - 1, blue);
                    checkY = checkY - 1;
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;
                    pixelCounter = pixelCounter + 1;

                }

            }

            if (!(wPixelReader.getColor(checkX, checkY - 1).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX, checkY - 1).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX + 1, checkY - 1).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 7;
                    pixelWriter.setColor(checkX + 1, checkY - 1, blue);
                    checkX = checkX + 1;
                    checkY = checkY - 1;
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;
                    pixelCounter = pixelCounter + 1;

                }
            }
            if (!(wPixelReader.getColor(checkX + 1, checkY - 1).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX + 1, checkY - 1).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX + 1, checkY).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 8;
                    pixelWriter.setColor(checkX + 1, checkY, blue);
                    checkX = checkX + 1;
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;
                    pixelCounter = pixelCounter + 1;

                }
            }

            //Wenn eine Runde um das Gebäude geangen wurde sind die hilfsvariablen wieder auf dem Anfangs punkt, hier wird dann die schleife beendet
            if (checkX == nearestPixel[0] && checkY == nearestPixel[1]) {
                redpixel = false;
            }
            // Zähler damit momentan beendet wird, da farbcode für blau nicht vorhanden, so wird nach 10000 Pixeln abgebrochen
            //if (zähler == 20000) {
            //   break;
            //}
            //Schleife läuft zum Randpixel falls Koordinate auf Objekt
            if (zähler < 2 && redpixel == false) {
                while (wPixelReader.getColor(checkX, checkY).toString().equals("0xff0000ff")) {
                    checkX++;
                }
                checkX = checkX - 1;
                redpixel = true;
            }
            //Hilfsvariablen
            int xhelp = 0;
            int yhelp = 0;
            int zähler1 = 0;

            //Schleife um einzelne FehlerPixel zu Filtern
            if (redpixel == false) {
                if (wPixelReader.getColor(checkX + 1, checkY).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    xhelp = xhelp + 1;
                }
                if (wPixelReader.getColor(checkX + 1, checkY + 1).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    xhelp = xhelp + 1;
                    yhelp = yhelp + 1;

                }
                if (wPixelReader.getColor(checkX + 0, checkY + 1).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    yhelp = yhelp + 1;

                }
                if (wPixelReader.getColor(checkX - 1, checkY + 1).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    xhelp = xhelp - 1;
                    yhelp = yhelp + 1;

                }
                if (wPixelReader.getColor(checkX - 1, checkY + 0).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    xhelp = xhelp - 1;

                }
                if (wPixelReader.getColor(checkX - 1, checkY - 1).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    xhelp = xhelp - 1;
                    yhelp = yhelp - 1;

                }
                if (wPixelReader.getColor(checkX + 0, checkY - 1).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    yhelp = yhelp - 1;

                }
                if (wPixelReader.getColor(checkX + 1, checkY - 1).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    xhelp = xhelp + 1;
                    yhelp = yhelp - 1;
                }
                if (zähler1 == 1) {
                    pixelWriter.setColor(checkX, checkY, Color.TRANSPARENT);
                    checkX = checkX + xhelp;
                    checkY = checkY + yhelp;
                    redpixel = true;
                }
            }
        }
        //Zeigt das gefilterte Image im rechten Fenster
        imageViewChangeColor.setImage(wImage);

        //Bestimmt den zentralen Pixel der index.html
        WebEngine webEngineTest = WebViewMap.getEngine();
        Object centerWebView;
        centerWebView = webEngineTest.executeScript("test()");
        //System.out.println(centerWebView);

        //Hilfsvariablen
        String test;
        String[] lon = new String[pixelCounter];
        String[] lat = new String[pixelCounter];
        String lonText = "lon=";
        String empty = "";
        String help = "";
        String help2 = "";
        String[][] longlat = new String[pixelCounter][2];

        //Schleife zur Anpassung der Pixel Aufgrung ungleicher Größe WebView ImageView
        //Bearbeiten String Koordinaten für einzeichnen Polygon in Map
        for (int count = 0; count < pixelCounter; count++) {
            //*********************************************
            //für -10 die Variable aus Höhe/Breite zentralger Pixel Image - Höhe/Breite WebView ersetzen
            //***********************************************
            int x = pixelArray[count][0] - 9;
            int y = pixelArray[count][1] - 10;
            Object[] coordinate = new Object[pixelCounter];
            coordinate[count] = webEngineTest.executeScript("getCoordinate(" + x + ", " + y + ")");
            test = coordinate[count].toString();
            help = test.replaceAll(lonText, empty);
            help2 = help.replaceAll("lat=", empty);
            longlat[count] = help2.split(Pattern.quote(","));
            lon[count] = longlat[count][0];
            lat[count] = longlat[count][1];
        }

        //Erstellt Array's für Längen- und Breiten-Koordinaten in JavaScript (index.html)
        webEngineTest.executeScript("createArrayLonLat(" + pixelCounter + ")");

        //Hilfsvariablen
        Object helpLongtitude;
        Object helpLatitude;

        //Speichern der Koordinaten für Polygone in JavaScript(index.html)
        for (int count = 0; count < pixelCounter; count++) {
            helpLongtitude = lon[count];
            helpLatitude = lat[count];
            helpLatitude = webEngineTest.executeScript("setLonLatArrays(" + helpLongtitude + "," + helpLatitude + "," + count + ")");
        }
        //Ausgabe Polygon auf Map
        webEngineTest.executeScript("pintarZonas()");

    }

    public void findBuilding() throws IOException {

        //Erstellt ein Image der OSM Map
        Image image;
        image = WebViewMap.snapshot(null, null);

        // erstellt PixelReader
        PixelReader pixelReader = image.getPixelReader();

        // erstellt WritableImage
        WritableImage wImage = new WritableImage(
                (int) image.getWidth(),
                (int) image.getHeight());

        //erstellt einen PixelWriter
        PixelWriter pixelWriter = wImage.getPixelWriter();

        //Schleife die jedes Pixel durchläuft und die Farbe auf Rot für die gewählten Farben setzt
        //die restlichen Pixel werden auf Transparent gesetzt
        for (int readY = 0; readY < image.getHeight(); readY++) {
            for (int readX = 0; readX < image.getWidth(); readX++) {

                Color color = pixelReader.getColor(readX, readY);
                Color red = Color.RED;
                Color transparent = Color.TRANSPARENT;
                if (color.toString().equals("0xc0b0aeff")
                        || color.toString().equals("0xbeadadff")
                        || color.toString().equals("0xd6d1c8ff")
                        || color.toString().equals("0xd5d1c8ff")
                        || color.toString().equals("0xc1b0afff")
                        || color.toString().equals("0xd5d0c8ff")
                        || color.toString().equals("0xc1b0adff")) {
                    pixelWriter.setColor(readX, readY, red);

                } else {
                    pixelWriter.setColor(readX, readY, transparent);
                }
            }
        }
        //sucht und speichert das zentrale Pixel
        double middleY = wImage.getHeight() / 2;
        double middleX = wImage.getWidth() / 2;
        double[] zentralPixel = {middleX, middleY};

        //variable für das nächstgelegene Pixel mit roter Farbe zum zentralen Pixel
        int[] nearestPixel = {0, 0};

        //Hilfsvariablen
        double distance = 100000;
        double helpDistance;
        double[] helpPixel;

        //PixelReader für das erzeugte Wirteable Image
        PixelReader wPixelReader = wImage.getPixelReader();

        //Schleife zur Bestimmung des nächsten roten Pixel zum zentralen Pixel
        for (int y = 0; y < wImage.getHeight(); y++) {
            for (int x = 0; x < wImage.getWidth(); x++) {
                Color color = wPixelReader.getColor(x, y);
                //System.out.println("Color Pixel:"+ color.toString());

                if (color.toString().equals(Color.RED.toString())) {
                    helpDistance = Math.sqrt(Math.pow(Math.abs(zentralPixel[0] - y), 2) + Math.pow(Math.abs(zentralPixel[1] - x), 2));
                    //System.out.println("Berechnung Distance");
                    if (helpDistance < distance) {
                        distance = helpDistance;
                        nearestPixel[0] = x;
                        nearestPixel[1] = y;
                    }
                }
            }

        }

        //setzt hilfsvariable auf den nächsten Punkt
        int checkX = nearestPixel[0];
        int checkY = nearestPixel[1];

        //Hilfsvariablen
        int helpX = 0;
        int helpY = 0;
        boolean redpixel = true;
        int feld = 0;
        int zähler = 0;
        Color blue = Color.BLUE;
        int pixelCounter = 0;

        //Array zum speichern der gefilterten Randpixel
        int[][] pixelArray = new int[10000][2];

        //while schleife die solange offen bleibt, bis der boolean redpixel auf false ist, dieser wird false, wenn kein RAndpixel mehr gefunden wird
        while (redpixel) {
            //zähler für momentane prüfung, damit while schleife abbricht, da code für blau nicht gefunden
            zähler++;
            //setze redpixel auf false damit nur weiter durch dei schleife gelaufen wird wenn in einer if schleife gegangen wird
            redpixel = false;
            //erste if schleife prüft, ob der nächste pixel rot oder blau ist (blau noch nciht implementiert da farbcode nicht vorhanden, momentaner farbcode leider nicht richtig)
            if (!(wPixelReader.getColor(checkX + 1, checkY).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX + 1, checkY).toString().equals("0x0000ffff"))) {
                //Prüfung ob danach ein Roter Pixel kommt, weil dann ist dieser Pixel ein RandPixel
                if (wPixelReader.getColor(checkX + 1, checkY + 1).toString().equals("0xff0000ff")) {
                    //wie ebenbeschrieben wird boolean auf true gesetzt
                    redpixel = true;
                    feld = 1;
                    //Randpixel wird blau gemacht
                    pixelWriter.setColor(checkX + 1, checkY + 1, blue);
                    //neuer Randpunkt wird als neuer Startpunkt gewählt
                    checkX = checkX + 1;
                    checkY = checkY + 1;
                    //Randpixel wird gespeichert
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;

                    pixelCounter = pixelCounter + 1;

                }
            }
            //diese Schleifen wieder holen sich 8 mal wo immer ein Pixel weiter gegangen wird //Start punkt für den Rundgang um den Pixel ist der 3 Uhr Pixel
            if (!(wPixelReader.getColor(checkX + 1, checkY + 1).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX + 1, checkY + 1).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX, checkY + 1).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 2;
                    pixelWriter.setColor(checkX, checkY + 1, blue);
                    checkY = checkY + 1;
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;
                    pixelCounter = pixelCounter + 1;

                }
            }
            if (!(wPixelReader.getColor(checkX, checkY + 1).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX, checkY + 1).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX - 1, checkY + 1).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 3;
                    pixelWriter.setColor(checkX - 1, checkY + 1, blue);
                    checkX = checkX - 1;
                    checkY = checkY + 1;
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;
                    pixelCounter = pixelCounter + 1;

                }
            }
            if (!(wPixelReader.getColor(checkX - 1, checkY + 1).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX - 1, checkY + 1).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX - 1, checkY).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 4;
                    pixelWriter.setColor(checkX - 1, checkY, blue);
                    checkX = checkX - 1;
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;
                    pixelCounter = pixelCounter + 1;

                }
            }
            if (!(wPixelReader.getColor(checkX - 1, checkY).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX - 1, checkY).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX - 1, checkY - 1).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 5;
                    pixelWriter.setColor(checkX - 1, checkY - 1, blue);
                    checkX = checkX - 1;
                    checkY = checkY - 1;
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;
                    pixelCounter = pixelCounter + 1;

                }
            }

            if (!(wPixelReader.getColor(checkX - 1, checkY - 1).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX - 1, checkY - 1).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX, checkY - 1).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 6;
                    pixelWriter.setColor(checkX, checkY - 1, blue);
                    checkY = checkY - 1;
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;
                    pixelCounter = pixelCounter + 1;

                }

            }

            if (!(wPixelReader.getColor(checkX, checkY - 1).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX, checkY - 1).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX + 1, checkY - 1).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 7;
                    pixelWriter.setColor(checkX + 1, checkY - 1, blue);
                    checkX = checkX + 1;
                    checkY = checkY - 1;
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;
                    pixelCounter = pixelCounter + 1;

                }
            }
            if (!(wPixelReader.getColor(checkX + 1, checkY - 1).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX + 1, checkY - 1).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX + 1, checkY).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 8;
                    pixelWriter.setColor(checkX + 1, checkY, blue);
                    checkX = checkX + 1;
                    pixelArray[pixelCounter][0] = checkX;
                    pixelArray[pixelCounter][1] = checkY;
                    pixelCounter = pixelCounter + 1;

                }
            }

            //Wenn eine Runde um das Gebäude geangen wurde sind die hilfsvariablen wieder auf dem Anfangs punkt, hier wird dann die schleife beendet
            if (checkX == nearestPixel[0] && checkY == nearestPixel[1]) {
                redpixel = false;
            }
            // Zähler damit momentan beendet wird, da farbcode für blau nicht vorhanden, so wird nach 10000 Pixeln abgebrochen
            //if (zähler == 20000) {
            //   break;
            //}
            //Schleife läuft zum Randpixel falls Koordinate auf Objekt
            if (zähler < 2 && redpixel == false) {
                while (wPixelReader.getColor(checkX, checkY).toString().equals("0xff0000ff")) {
                    checkX++;
                }
                checkX = checkX - 1;
                redpixel = true;
            }
            //Hilfsvariablen
            int xhelp = 0;
            int yhelp = 0;
            int zähler1 = 0;

            //Schleife um einzelne FehlerPixel zu Filtern
            if (redpixel == false) {
                if (wPixelReader.getColor(checkX + 1, checkY).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    xhelp = xhelp + 1;
                }
                if (wPixelReader.getColor(checkX + 1, checkY + 1).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    xhelp = xhelp + 1;
                    yhelp = yhelp + 1;

                }
                if (wPixelReader.getColor(checkX + 0, checkY + 1).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    yhelp = yhelp + 1;

                }
                if (wPixelReader.getColor(checkX - 1, checkY + 1).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    xhelp = xhelp - 1;
                    yhelp = yhelp + 1;

                }
                if (wPixelReader.getColor(checkX - 1, checkY + 0).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    xhelp = xhelp - 1;

                }
                if (wPixelReader.getColor(checkX - 1, checkY - 1).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    xhelp = xhelp - 1;
                    yhelp = yhelp - 1;

                }
                if (wPixelReader.getColor(checkX + 0, checkY - 1).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    yhelp = yhelp - 1;

                }
                if (wPixelReader.getColor(checkX + 1, checkY - 1).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    xhelp = xhelp + 1;
                    yhelp = yhelp - 1;
                }
                if (zähler1 == 1) {
                    pixelWriter.setColor(checkX, checkY, Color.TRANSPARENT);
                    checkX = checkX + xhelp;
                    checkY = checkY + yhelp;
                    redpixel = true;
                }
            }
        }
        //Zeigt das gefilterte Image im rechten Fenster
        imageViewChangeColor.setImage(wImage);

        //Bestimmt den zentralen Pixel der index.html
        WebEngine webEngineTest = WebViewMap.getEngine();
        Object centerWebView;
        centerWebView = webEngineTest.executeScript("test()");
        //System.out.println(centerWebView);

        //Hilfsvariablen
        String test;
        String[] lon = new String[pixelCounter];
        String[] lat = new String[pixelCounter];
        String lonText = "lon=";
        String empty = "";
        String help = "";
        String help2 = "";
        String[][] longlat = new String[pixelCounter][2];

        //Schleife zur Anpassung der Pixel Aufgrung ungleicher Größe WebView ImageView
        //Bearbeiten String Koordinaten für einzeichnen Polygon in Map
        for (int count = 0; count < pixelCounter; count++) {
            //*********************************************
            //für -10 die Variable aus Höhe/Breite zentralger Pixel Image - Höhe/Breite WebView ersetzen
            //***********************************************
            int x = pixelArray[count][0] - 9;
            int y = pixelArray[count][1] - 10;
            Object[] coordinate = new Object[pixelCounter];
            coordinate[count] = webEngineTest.executeScript("getCoordinate(" + x + ", " + y + ")");
            test = coordinate[count].toString();
            help = test.replaceAll(lonText, empty);
            help2 = help.replaceAll("lat=", empty);
            longlat[count] = help2.split(Pattern.quote(","));
            lon[count] = longlat[count][0];
            lat[count] = longlat[count][1];
        }

        //Erstellt Array's für Längen- und Breiten-Koordinaten in JavaScript (index.html)
        webEngineTest.executeScript("createArrayLonLat(" + pixelCounter + ")");

        //Hilfsvariablen
        Object helpLongtitude;
        Object helpLatitude;

        //Speichern der Koordinaten für Polygone in JavaScript(index.html)
        for (int count = 0; count < pixelCounter; count++) {
            helpLongtitude = lon[count];
            helpLatitude = lat[count];
            helpLatitude = webEngineTest.executeScript("setLonLatArrays(" + helpLongtitude + "," + helpLatitude + "," + count + ")");
        }
        //Ausgabe Polygon auf Map
        webEngineTest.executeScript("pintarZonas()");

    }

    @FXML
    ImageView imageViewShowChangeColor;

    @FXML
    public void showChangeColor() throws IOException {

        Image image;
        image = WebViewMap.snapshot(null, null);

        // Obtain PixelReader
        PixelReader pixelReader = image.getPixelReader();
        //System.out.println("Image Width: " + image.getWidth());
        //System.out.println("Image Height: " + image.getHeight());
        //System.out.println("Pixel Format: " + pixelReader.getPixelFormat());

        // Create WritableImage
        WritableImage wImage = new WritableImage(
                (int) image.getWidth(),
                (int) image.getHeight());
        PixelWriter pixelWriter = wImage.getPixelWriter();

        for (int readY = 0; readY < image.getHeight(); readY++) {
            for (int readX = 0; readX < image.getWidth(); readX++) {
                Color color = pixelReader.getColor(readX, readY);

                Color red = pixelReader.getColor(readX, readY);
                red = Color.RED;
                //System.out.println("Farbe Rot:"+red.toString());

                Color transparent = Color.TRANSPARENT;
                if (color.toString().equals("0xc0b0aeff")
                        || color.toString().equals("0xbeadadff")
                        || color.toString().equals("0xd6d1c8ff")
                        || color.toString().equals("0xd5d1c8ff")
                        || color.toString().equals("0xc1b0afff")
                        || color.toString().equals("0xd5d0c8ff")
                        || color.toString().equals("0xc1b0adff")) {
                    pixelWriter.setColor(readX, readY, red);
                    //System.out.println("Red");

                } else {
                    pixelWriter.setColor(readX, readY, transparent);
                    //System.out.println("Transparent");
                }
            }
        }

        double middleY = wImage.getHeight() / 2;
        double middleX = wImage.getWidth() / 2;
        double[] zentralPixel = {middleX, middleY};
        int[] nearestPixel = {0, 0};
        double distance = 100000;
        double helpDistance;
        double[] helpPixel;

        PixelReader wPixelReader = wImage.getPixelReader();
        for (int y = 0; y < wImage.getHeight(); y++) {
            for (int x = 0; x < wImage.getWidth(); x++) {
                Color color = wPixelReader.getColor(x, y);
                // System.out.println(color.toString());
                if (color.toString().equals("0xff0000ff")) {
                    helpDistance = Math.sqrt(Math.pow(Math.abs(zentralPixel[0] - y), 2) + Math.pow(Math.abs(zentralPixel[1] - x), 2));
                    //   System.out.println("Berechnung Distance");
                    if (helpDistance < distance) {
                        distance = helpDistance;
                        nearestPixel[0] = x;
                        nearestPixel[1] = y;
                    }
                }
            }

            //WebEngine webEngine = webViewShowChangeColor.getEngine();
            //URL url = getClass().getResource("showChangeColor.html");
            //webEngine.load(url.toExternalForm());
        }
        //System.out.println(nearestPixel[0] + " " + nearestPixel[1]);

        /*int lengthX;
         int lengthY;
         double lengthXY;
         int getY;
         int getX;
         int getNextX;
         int getNextY;
         double helpGetNextX;
         double helpGetNextY;

         for(getY = 0; getY<wImage.getHeight();getY++){
         for(getX = 0; getX<wImage.getWidth(); getX++){
         System.out.println("x="+getX+", y= "+getY);
             
         Color color = pixelReader.getColor(getX,getY);
         System.out.println(color.toString());
                
         //Color green = pixelReader.getColor(getX,getY);
         Color green = Color.GREEN;
         System.out.println("Zeile360");
         if(color.toString().equals("0xbeadadff")){
         System.out.println("Zeile 362");
         lengthX = nearestPixel[0] - getX;
         lengthY = nearestPixel[1] - getY;
         Color colorNextpixel;
         if(lengthX>0 && lengthY>0){
         int counter = 0;
         if(lengthX>lengthY){
         lengthXY = lengthX / lengthY;
         for(int i = 1; i<=lengthY; i++){
                                
         helpGetNextX = (i*lengthXY)+getX;
         getNextX = (int) Math.round(helpGetNextX);
         getNextY = i+getY;
         colorNextpixel = pixelReader.getColor(getNextX,getNextY);
                                
         if(!colorNextpixel.toString().equals("0xbeadadff")){
         counter = counter+1;
         if(counter>10){
                                        
         pixelWriter.setColor(getX, getY, green);
                                        
         }
                                    
         }
                                
         }
                                   
         }
         else{
         System.out.println("Zeile 391");
         lengthXY = lengthY / lengthX;
         for (int i = 1; i <= lengthX; i++){
                                
         helpGetNextY = (i*lengthXY)+getY;
         getNextY = (int) Math.round(helpGetNextY);
         getNextX = i+getX;
         colorNextpixel = pixelReader.getColor(getNextX, getNextY);
                                
         if(!colorNextpixel.toString().equals("0xbeadadff")){
         counter=counter+1;
         if(counter>10){ 
         pixelWriter.setColor(getX, getY, green);  
         }  
         } 
         }
         }
         }   
         } 
         }
         }

         */
        imageViewShowChangeColor.setImage(wImage);

        //int pictureCounter = 0;
        //pictureCounter =pictureCounter+1;
        /*BufferedImage image1 = SwingFXUtils.fromFXImage(wImage, null); // Get buffered image.
         BufferedImage imageRGB = new BufferedImage(image1.getWidth(), image1.getHeight(), BufferedImage.OPAQUE); // Remove alpha-channel from buffered image.
         Graphics2D graphics = imageRGB.createGraphics();
         graphics.drawImage(image1, 0, 0, null);
         ImageIO.write(imageRGB, "png", new File("C:/Users/Tino/Documents/osm/"+pictureCounter+".png"));
         graphics.dispose(); */
    }

    @FXML
    TextField lonText;

    @FXML
    TextField latText;

    @FXML
    TextField textFieldZoom;

    public void goToCoordinate() {
        String lon = lonText.textProperty().get();
        String lat = latText.textProperty().get();
        String zoom = textFieldZoom.textProperty().get();
        WebEngine webEngineGoToCoordinate = WebViewMap.getEngine();
        webEngineGoToCoordinate.executeScript("goTo(" + lon + "," + lat + "," + zoom + ")");

    }

}
