/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    
    @FXML
    ImageView imageView;
    
    @FXML
    public void showImage(){
      
      Image image;  
      image = WebViewMap.snapshot(null, null);
      imageView.setImage(image);
      
     
        
    }
    @FXML
    ImageView imageViewSelectColor;
    
    
    //Gibt die Farbwerte auf der Konsole für jeden Pixel aus, VORSICHT!!! Dauert lang
    @FXML
    public void selectColor(){
        
        Image image;  
        image = WebViewMap.snapshot(null, null);
        imageViewSelectColor.setImage(image);
        
        // Obtain PixelReader
        PixelReader pixelReader = image.getPixelReader();
        System.out.println("Image Width: "+image.getWidth());
        System.out.println("Image Height: "+image.getHeight());
        System.out.println("Pixel Format: "+pixelReader.getPixelFormat());
        
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
    
    public Image changeColor() throws IOException{
        
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
                Color transparent = Color.TRANSPARENT;
                if(color.toString().equals("0xc0b0aeff") || 
                        color.toString().equals("0xbeadadff")|| 
                        color.toString().equals("0xc1b0afff")|| 
                        color.toString().equals("0xc1b0adff")){
                    pixelWriter.setColor(readX, readY, red);
                    
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
        
        
        
        for(int y = 0; y<wImage.getHeight();y++){
            for(int x = 0; x<wImage.getWidth(); x++){
                Color color = pixelReader.getColor(x,y);
                //System.out.println(color.toString());
                if(color.toString().equals("0xbeadadff")){
                helpDistance = Math.sqrt(Math.pow(Math.abs(zentralPixel[0]-y),2)+ Math.pow(Math.abs(zentralPixel[1]-x),2));
                //System.out.println("Berechnung Distance");
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
        
        int lengthX;
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
        
        
        
        imageViewChangeColor.setImage(wImage);
        
        //int pictureCounter = 0;
        //pictureCounter =pictureCounter+1;
        
        /*BufferedImage image1 = SwingFXUtils.fromFXImage(wImage, null); // Get buffered image.
        BufferedImage imageRGB = new BufferedImage(image1.getWidth(), image1.getHeight(), BufferedImage.OPAQUE); // Remove alpha-channel from buffered image.
        Graphics2D graphics = imageRGB.createGraphics();
        graphics.drawImage(image1, 0, 0, null);
        ImageIO.write(imageRGB, "png", new File("C:/Users/Tino/Documents/osm/"+pictureCounter+".png"));
        graphics.dispose(); */
        
        return wImage;
    }
    
    
    @FXML
    WebView webViewShowChangeColor;
    
    @FXML
    public void showChangeColor(){
        
       
    }
}


