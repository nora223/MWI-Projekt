package osmfxml;

import com.sun.org.apache.bcel.internal.generic.TargetLostException;
import com.sun.prism.BufferedImageTools;
import com.sun.webpane.platform.ContextMenu;
import java.awt.Graphics2D;
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
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.MenuItem;
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
    public void alg1copy() {
        showAlgMap();
    }

    @FXML
    public void readKML() throws FileNotFoundException {
        try {
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

    public void saveKML() {
        menu.Menu.saveKML();
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

    public Image changeColor() throws IOException {
        Image image;
        image = WebViewMap.snapshot(null, null);
// Obtain PixelReader
        PixelReader pixelReader = image.getPixelReader();
        System.out.println("Image Width: " + image.getWidth());
        System.out.println("Image Height: " + image.getHeight());
        System.out.println("Pixel Format: " + pixelReader.getPixelFormat());
// Create WritableImage
        WritableImage wImage = new WritableImage(
                (int) image.getWidth(),
                (int) image.getHeight());
        PixelWriter pixelWriter = wImage.getPixelWriter();
        for (int readY = 0; readY < image.getHeight(); readY++) {
            for (int readX = 0; readX < image.getWidth(); readX++) {
                Color color = pixelReader.getColor(readX, readY);
                
                Color red = Color.RED;
                //System.out.println("Color red: "+red.toString());
                Color transparent = Color.TRANSPARENT;
                if (color.toString().equals("0xc0b0aeff")
                        || color.toString().equals("0xbeadadff")
                        || color.toString().equals("0xc1b0afff")
                        ||color.toString().equals("0xd5d0c8ff")
                        || color.toString().equals("0xc1b0adff")) {
                    pixelWriter.setColor(readX, readY, red);
                    //System.out.println("Rot");
                } else {
                    pixelWriter.setColor(readX, readY, transparent);
                    //System.out.println("Transparent");
                }
            }
        }
        //System.out.println("Farbe Rot: "+Color.RED.toString());
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
            
//WebEngine webEngine = webViewShowChangeColor.getEngine();
//URL url = getClass().getResource("showChangeColor.html");
//webEngine.load(url.toExternalForm());
        }
        
        
        
         System.out.println(nearestPixel[0]+" "+nearestPixel[1]);
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
         }*/

     

        int startX = nearestPixel[0];
        int startY = nearestPixel[1];
        int zählerMinus = 1;
        int zählerPlus = 1;
        //setzt hilfsvariable auf den nächsten Punkt
        int checkX = nearestPixel[0];
        int checkY = nearestPixel[1];
        boolean redpixel = true;
        int checkGreyX = 0;
        int checkGreyY = 0;
        int feld = 0;

        int zähler = 0;
        Color blue = Color.BLUE;
        int[][] pixelArray = new int[1000][2];
        
        int pixelCounter = 0;
        
        //while schleife die solange offen bleibt, bis der boolean redpixel auf false ist, dieser wird false, wenn kein RAndpixel mehr gefunden wird
        while (redpixel) {
            //zähler für momentane prüfung, damit while schleife abbricht, da code für blau nicht gefunden
            zähler++;
            System.out.println(checkX + " " + checkY);
            //setze redpixel auf false damit nur weiter durch dei schleife gelaufen wird wenn in einer if schleife gegangen wird
            redpixel = false;
            //erste if schleife prüft, ob der nächste pixel rot oder blau ist (blau noch nciht implementiert da farbcode nicht vorhanden, momentaner farbcode leider nicht richtig)
            if (!(wPixelReader.getColor(checkX + 1, checkY).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX + 1, checkY).toString().equals("0x0000ffff"))) {
                //Prüfung ob danach ein Roter Pixel kommt, weil dann ist dieser Pixel ein RandPixel
                if (wPixelReader.getColor(checkX + 1, checkY + 1).toString().equals("0xff0000ff")) {
                    //wie ebenbeschrieben wird boolean auf true gesetzt
                    redpixel = true;
                    feld = 1;
                    System.out.println("1");
                    //Randpixel wird blau gemacht
                    pixelWriter.setColor(checkX + 1, checkY + 1, blue);
                    //neuer Randpunkt wird als neuer Startpunkt gewählt
                    checkX = checkX + 1;
                    checkY = checkY + 1;
                    pixelArray[pixelCounter][0]=checkX;
                    pixelArray[pixelCounter][1]=checkY;
                    pixelCounter = pixelCounter + 1;
                    

                }
            }
                //diese Schleifen wieder holen sich 8 mal wo immer ein Pixel weiter gegangen wird //Start punkt für den Rundgang um den Pixel ist der 3 Uhr Pixel
            if (!(wPixelReader.getColor(checkX + 1, checkY + 1).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX + 1, checkY + 1).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX, checkY + 1).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 2;
                    System.out.println("2");

                    pixelWriter.setColor(checkX, checkY + 1, blue);

                    checkY = checkY + 1;
                    pixelArray[pixelCounter][0]=checkX;
                    pixelArray[pixelCounter][1]=checkY;
                    pixelCounter = pixelCounter + 1;

                }
            }
            if (!(wPixelReader.getColor(checkX, checkY + 1).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX, checkY + 1).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX - 1, checkY + 1).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 3;
                    System.out.println("3");
                    pixelWriter.setColor(checkX - 1, checkY + 1, blue);
                    checkX = checkX - 1;
                    checkY = checkY + 1;
                    
                    pixelArray[pixelCounter][0]=checkX;
                    pixelArray[pixelCounter][1]=checkY;
                    pixelCounter = pixelCounter + 1;
                }
            }
            if (!(wPixelReader.getColor(checkX - 1, checkY + 1).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX - 1, checkY + 1).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX - 1, checkY).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 4;
                    System.out.println("4");

                    pixelWriter.setColor(checkX - 1, checkY, blue);
                    checkX = checkX - 1;
                    
                    pixelArray[pixelCounter][0]=checkX;
                    pixelArray[pixelCounter][1]=checkY;
                    pixelCounter = pixelCounter + 1;

                }
            }
            if (!(wPixelReader.getColor(checkX - 1, checkY).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX - 1, checkY).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX - 1, checkY - 1).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 5;
                    System.out.println("5");

                    pixelWriter.setColor(checkX - 1, checkY - 1, blue);
                    checkX = checkX - 1;
                    checkY = checkY - 1;
                    
                    pixelArray[pixelCounter][0]=checkX;
                    pixelArray[pixelCounter][1]=checkY;
                    pixelCounter = pixelCounter + 1;
                }
            }

            if (!(wPixelReader.getColor(checkX - 1, checkY - 1).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX - 1, checkY - 1).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX, checkY - 1).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 6;
                    System.out.println("6");

                    pixelWriter.setColor(checkX, checkY - 1, blue);
                    checkY = checkY - 1;
                    
                    pixelArray[pixelCounter][0]=checkX;
                    pixelArray[pixelCounter][1]=checkY;
                    pixelCounter = pixelCounter + 1;
                }

            }

            if (!(wPixelReader.getColor(checkX, checkY - 1).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX, checkY - 1).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX + 1, checkY - 1).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 7;
                    System.out.println("7");

                    pixelWriter.setColor(checkX + 1, checkY - 1, blue);
                    checkX = checkX + 1;
                    checkY = checkY - 1;
                    
                    pixelArray[pixelCounter][0]=checkX;
                    pixelArray[pixelCounter][1]=checkY;
                    pixelCounter = pixelCounter + 1;
                }
            }
            if (!(wPixelReader.getColor(checkX + 1, checkY - 1).toString().equals("0xff0000ff")) && !(wPixelReader.getColor(checkX + 1, checkY - 1).toString().equals("0x0000ffff"))) {
                if (wPixelReader.getColor(checkX + 1, checkY).toString().equals("0xff0000ff")) {

                    redpixel = true;
                    feld = 8;
                    System.out.println("8");

                    pixelWriter.setColor(checkX + 1, checkY, blue);

                    checkX = checkX + 1;
                    
                    pixelArray[pixelCounter][0]=checkX;
                    pixelArray[pixelCounter][1]=checkY;
                    pixelCounter = pixelCounter + 1;

                }
            }

            
            //Wenn eine Runde um das Gebäude geangen wurde sind die hilfsvariablen wieder auf dem Anfangs punkt, hier wird dann die schleife beendet
            if (checkX == nearestPixel[0] && checkY == nearestPixel[1]) {
                redpixel = false;
            }
            // Zähler damit momentan beendet wird, da farbcode für blau nicht vorhanden, so wird nach 10000 Pixeln abgebrochen
            if (zähler == 10000) {
                break;
            }

        }
        System.out.println("Farbe Blau:"+blue.toString());
        imageViewChangeColor.setImage(wImage);
        
        for(int i = 0; i<pixelCounter; i++){
            System.out.println("Pixel"+i+": "+pixelArray[i][0]+"; "+pixelArray[i][1]);
        }
        WebEngine webEngineTest = WebViewMap.getEngine();
        Object i;
        i = webEngineTest.executeScript("test(234, 285)");
        System.out.println(i);
        
        
        return wImage;
    }
    @FXML
    ImageView imageViewShowChangeColor;

    @FXML
    public void showChangeColor() throws IOException {
        
       
        
        Image image;  
        image = WebViewMap.snapshot(null, null);
        
        
        // Obtain PixelReader
        PixelReader pixelReader = image.getPixelReader();
        System.out.println("Image Width: "+image.getWidth());
        System.out.println("Image Height: "+image.getHeight());
        System.out.println("Pixel Format: "+pixelReader.getPixelFormat());
        
        // Create WritableImage
         WritableImage wImage = new WritableImage(
                 (int)image.getWidth(),
                 (int)image.getHeight());
         PixelWriter pixelWriter = wImage.getPixelWriter();
         
         for(int readY=0;readY<image.getHeight();readY++){
            for(int readX=0; readX<image.getWidth();readX++){
                Color color = pixelReader.getColor(readX,readY);
                
                Color red = pixelReader.getColor(readX,readY);
                red = Color.RED;
                //System.out.println("Farbe Rot:"+red.toString());
                
                Color transparent = Color.TRANSPARENT;
                if(color.toString().equals("0xc0b0aeff") || 
                        color.toString().equals("0xbeadadff")|| 
                        color.toString().equals("0xc1b0afff")||
                        color.toString().equals("0xd5d0c8ff")||
                        color.toString().equals("0xc1b0adff")){
                    pixelWriter.setColor(readX, readY, red);
                    //System.out.println("Red");
                    
                }
                else{
                    pixelWriter.setColor(readX, readY, transparent);
                    //System.out.println("Transparent");
                }
                
            }
         }
        
        double middleY = wImage.getHeight()/2;
        double middleX = wImage.getWidth()/2;
        double[] zentralPixel = {middleX, middleY};
        int[] nearestPixel= {0,0};
        double distance = 100000;
        double helpDistance;
        double[] helpPixel;
        
        
        PixelReader wPixelReader = wImage.getPixelReader();
        for(int y = 0; y<wImage.getHeight();y++){
            for(int x = 0; x<wImage.getWidth(); x++){
                Color color = wPixelReader.getColor(x,y);
                System.out.println(color.toString());
                if(color.toString().equals("0xff0000ff")){
                helpDistance = Math.sqrt(Math.pow(Math.abs(zentralPixel[0]-y),2)+ Math.pow(Math.abs(zentralPixel[1]-x),2));
                System.out.println("Berechnung Distance");
                if(helpDistance<distance){
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
        System.out.println(nearestPixel[0]+" "+nearestPixel[1]);
        
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
    
}
