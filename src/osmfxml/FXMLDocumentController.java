package osmfxml;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.regex.Pattern;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.*;
import javafx.scene.paint.Color;

/**
 *
 * @author Tino
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label label;

    @FXML
    Button nextCoordi;

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
    int zählen = 0;

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
            //Aufruf der Methode readKml und abspeichern der Ergebnisse in den String koordinaten
            String[] koordinaten = menu.Menu.readKML();
            int counter = koordinaten.length;
            //erste Koordinate = Längengrad und zweite Koordinate = Breitengrad
            String[] AllLon = new String[counter]; //alle Längengrad Angaben
            String[] AllLat = new String[counter];  //alle Breitengrad Angaben
            String firstLon = null; //Längengrad
            String firstLat = null; //Breitengrad
            
            //hier werden aus dem String koordinaten die Längen- und Breitengraden ausgelesen
            for (int i = 0; i < koordinaten.length; i++) {
                if (i == 0) {
                    //In der Schleife werden die ersten Koordinaten in firstLon und firstLat
                    //sowie in AllLon und AllLat gespeichert.
                    //firstLon und firstLat werden später verwendet um die angezeigte Karte zu zentrieren
                    String[] firstLine = koordinaten[i].split(",");
                    firstLon = firstLine[0];
                    firstLat = firstLine[1];
                    AllLon[i] = firstLon;
                    AllLat[i] = firstLat;
                }
                String[] helpLine = koordinaten[i].split(",");
                AllLon[i] = helpLine[0];
                AllLat[i] = helpLine[1];
            }

            //Ausführen der JS-Methode um dort ein Array der Koordinaten zu erzeugen.
            webEngineTest.executeScript("createArrayLonLat(" + (counter) + ")");

            Object helpLongtitude = null;
            Object helpLatitude = null;
            Object temp = null;
            for (int count = 0; count < counter; count++) {
                helpLongtitude = AllLon[count];
                helpLatitude = AllLat[count];
                //Übergabe der Arrays mit den separten Koordinaten der Längen und Breitengrade
                temp = webEngineTest.executeScript("setLonLatArrays(" + helpLongtitude + "," + helpLatitude + "," + count + ")");
            }
            //Zentrieren der Karte auf die entsprechende Koordinaten mithilfe der JS Methode
            webEngineTest.executeScript("goTo(" + firstLon + "," + firstLat + ")");
            //Zeichnen des Polygons auf der Karte mithilfe der JS-Methode
            webEngineTest.executeScript("pintarZonas()");

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Datei könnte nicht gelesen werden. " + e);
        }
    }

    String[][] ergSURGlobal;

    @FXML
    public void readAllSUR() throws FileNotFoundException, IOException, InterruptedException {
        String[][] ergSUR;
        ergSUR = menu.Menu.readAllSUR();
        ergSURGlobal = new String[ergSUR.length][4];
        for (int i = 0; i < ergSUR.length; i++) {
            for (int j = 0; j < 4; j++) {
                ergSURGlobal[i][j] = ergSUR[i][j];
            }
        }
        if (zählen >= ergSUR.length) {
            zählen = 0;
            nextCoordi.setDisable(false);
        }
        int countSURS = ergSUR.length;
        zahlKML = ergSUR[0][0];

        selectTypeByRule(ergSUR[0]);
    }

    @FXML
    Tab copyMapID;
    @FXML
    Tab selectColorID;
    @FXML
    Tab showChangeColorID;
    @FXML
    Tab changeColorID;
    
    
    public void refresh() throws IOException{
        if(copyMapID.isSelected()==true){
            showImage();
        }else if(selectColorID.isSelected()==true){
            selectColor();
        }else if(showChangeColorID.isSelected()==true){
            showChangeColor();
        }else{
            
        }
    }
    
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

    public class selectColorTable {

        SimpleStringProperty x = new SimpleStringProperty();
        SimpleStringProperty y = new SimpleStringProperty();
        
        public String getX() {
            return x.get();
        }

        public String getY() {
            return y.get();
        }
    }

    @FXML
    ImageView imageViewSelectColor;
    //Gibt die Farbwerte auf der Konsole für jeden Pixel aus, VORSICHT!!! Dauert lang
    @FXML
    TableColumn pixelXTable;

    @FXML
    TableColumn pixelYTable;

    @FXML
    TableView<selectColorTable> table;

    @FXML
    public void selectColor() throws IOException {

        int counter = 0;

        Integer helpReadX;
        String xString;

        Integer helpReadY;
        String yString;

        Image image;
        image = WebViewMap.snapshot(null, null);

        // Obtain PixelReader
        PixelReader pixelReader = image.getPixelReader();
        ObservableList<selectColorTable> olX = FXCollections.observableArrayList();

        pixelXTable.setCellValueFactory(new PropertyValueFactory<selectColorTable, String>("x"));
        pixelYTable.setCellValueFactory(new PropertyValueFactory<selectColorTable, String>("y"));
        
        String helpColorTable;
        String helpPixelNo;
        Integer xPixel;
        Integer yPixel;
        
        table.setItems(olX);
        // Determine the color of each pixel in the image
        for (int readY = 0; readY < image.getHeight(); readY++) {
            for (int readX = 0; readX < image.getWidth(); readX++) {
                selectColorTable sct = new selectColorTable();
                Color color = pixelReader.getColor(readX, readY);
                helpColorTable = color.toString();
                xPixel = readX;
                yPixel = readY;
                helpPixelNo = xPixel.toString()+"; "+yPixel.toString();
                
                sct.x.set(helpColorTable);
                sct.y.set(helpPixelNo);
                
                olX.add(sct);
                
            }
        }
            table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    

    

    public void nextCoordinate() throws IOException, InterruptedException {

        zählen++;
        if (zählen >= ergSURGlobal.length) {
            nextCoordi.setDisable(true);
        } else {
            selectTypeByRule(ergSURGlobal[zählen]);
        }
    }
    String type = "";

    public void selectTypeByRule(String[] sur) throws IOException, InterruptedException {

        if (sur[3].contains("swimming") || sur[3].contains("fishing")) {
            type = "lake";
        } else if (sur[3].contains("dog_waste") || sur[3].contains("littering") || sur[3].contains("animal_feeding")) {
            type = "green";
        } else if (sur[3].contains("parking") || sur[3].contains("open_fire")) {
            type = "area";
        } else {
            type = "building";
        }
        goToCoordinateInMap(sur, type);
    }

    public void goToCoordinateInMap(String[] sur, String type) throws IOException, InterruptedException {
        WebEngine webEngineGoToCoordinate = WebViewMap.getEngine();
        String latSUR = sur[1];
        latSUR = latSUR.substring(1);
        String lonSUR = sur[2];
        lonSUR = lonSUR.substring(1);
        String zoom = "18";

        switch (type) {
            case "lake":
                zoom = "18";
                break;
            case "green":
                zoom = "16";
                break;
            case "area":
                zoom = "16";
                break;
            case "building":
                zoom = "18";
                break;
        }
        webEngineGoToCoordinate.executeScript("goTo(" + lonSUR + "," + latSUR + "," + zoom + ")");
    }

    @FXML
    public void selectStategy() throws IOException, FileNotFoundException, InterruptedException {
        switch (type) {
            case "lake":
                findLake();
                break;
            case "green":
                findForestAndGreenField();
                break;
            case "area":
                findAreaArroundBuilding();
                break;
            case "building":
                findBuilding();
                break;
        }
    }

    public void findLake() throws FileNotFoundException, IOException, InterruptedException {
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

                if (color.toString().equals(Color.RED.toString())) {
                    helpDistance = Math.sqrt(Math.pow(Math.abs(zentralPixel[0] - y), 2) + Math.pow(Math.abs(zentralPixel[1] - x), 2));
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
        //imageViewChangeColor.setImage(wImage);

        //Bestimmt den zentralen Pixel der index.html
        WebEngine webEngineTest = WebViewMap.getEngine();
        Object centerWebView;
        centerWebView = webEngineTest.executeScript("test()");

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

        //Speichert Polygon als KML
        menu.Menu.saveKML(lonLatForSave);
        //Erstellt Array's für Längen- und Breiten-Koordinaten in JavaScript (index.html)
        webEngineTest.executeScript("createArrayLonLat(" + pixelCounter + ")");

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

                if (color.toString().equals(Color.RED.toString())) {
                    helpDistance = Math.sqrt(Math.pow(Math.abs(zentralPixel[0] - y), 2) + Math.pow(Math.abs(zentralPixel[1] - x), 2));
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
        //imageViewChangeColor.setImage(wImage);

        //Bestimmt den zentralen Pixel der index.html
        WebEngine webEngineTest = WebViewMap.getEngine();
        Object centerWebView;
        centerWebView = webEngineTest.executeScript("test()");

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
            lonLatForSave[count] = lon[count] + "," + lat[count];
        }

        //Speichert Polygon als KML
        menu.Menu.saveKML(lonLatForSave);
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

                if (color.toString().equals(Color.RED.toString())) {
                    helpDistance = Math.sqrt(Math.pow(Math.abs(zentralPixel[0] - y), 2) + Math.pow(Math.abs(zentralPixel[1] - x), 2));
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
        //imageViewChangeColor.setImage(wImage);

        //Bestimmt den zentralen Pixel der index.html
        WebEngine webEngineTest = WebViewMap.getEngine();
        Object centerWebView;
        centerWebView = webEngineTest.executeScript("test()");

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
            lonLatForSave[count] = lon[count] + "," + lat[count];

        }

        //Erstellt Array's für Längen- und Breiten-Koordinaten in JavaScript (index.html)
        webEngineTest.executeScript("createArrayLonLat(" + pixelCounter + ")");
        menu.Menu.saveKML(lonLatForSave);
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
                        || color.toString().equals("0xd9d0c9ff")
                        || color.toString().equals("0xd8d0c9ff")
                        || color.toString().equals("0xc5b8aaff")
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

                if (color.toString().equals(Color.RED.toString())) {
                    helpDistance = Math.sqrt(Math.pow(Math.abs(zentralPixel[0] - y), 2) + Math.pow(Math.abs(zentralPixel[1] - x), 2));
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
        //imageViewChangeColor.setImage(wImage);

        //Bestimmt den zentralen Pixel der index.html
        WebEngine webEngineTest = WebViewMap.getEngine();
        Object centerWebView;
        centerWebView = webEngineTest.executeScript("test()");

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
            lonLatForSave[count] = lon[count] + "," + lat[count];

        }
        menu.Menu.saveKML(lonLatForSave);
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

        PixelReader pixelReader = image.getPixelReader();
       
        WritableImage wImage = new WritableImage(
                (int) image.getWidth(),
                (int) image.getHeight());
        PixelWriter pixelWriter = wImage.getPixelWriter();

        for (int readY = 0; readY < image.getHeight(); readY++) {
            for (int readX = 0; readX < image.getWidth(); readX++) {
                Color color = pixelReader.getColor(readX, readY);

                Color red = pixelReader.getColor(readX, readY);
                red = Color.RED;
                
                Color transparent = Color.TRANSPARENT;
                if (color.toString().equals("0xc0b0aeff")
                        || color.toString().equals("0xbeadadff")
                        || color.toString().equals("0xd6d1c8ff")
                        || color.toString().equals("0xd5d1c8ff")
                        || color.toString().equals("0xc1b0afff")
                        || color.toString().equals("0xd8d0c9ff")
                        || color.toString().equals("0xd9d0c9ff")
                        || color.toString().equals("0xd5d0c8ff")
                        || color.toString().equals("0xc1b0adff")) {
                    pixelWriter.setColor(readX, readY, red);
                    

                } else {
                    pixelWriter.setColor(readX, readY, transparent);
                   
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
                
                if (color.toString().equals("0xff0000ff")) {
                    helpDistance = Math.sqrt(Math.pow(Math.abs(zentralPixel[0] - y), 2) + Math.pow(Math.abs(zentralPixel[1] - x), 2));
                    
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
       
        //Bestimmt den zentralen Pixel der index.html
        WebEngine webEngineTest = WebViewMap.getEngine();
        Object centerWebView;
        centerWebView = webEngineTest.executeScript("test()");

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
            lonLatForSave[count] = lon[count] + "," + lat[count];

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

    
        
        imageViewShowChangeColor.setImage(wImage);

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
