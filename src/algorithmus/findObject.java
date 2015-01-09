/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmus;

import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 *
 * @author Nora
 */
public class findObject {
    
    @FXML
    public static WebView WebViewMap;
    
    @FXML
    public static ImageView imageViewChangeColor;
    
    public void findSea(){
    
    }
    
    public void findWoodAndGreenfield(){
    
}
    public void findUrbanArea(){
        
    } 
    
    public static Image findBuilding(){
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
                        || color.toString().equals("0xd6d1c8ff")
                        || color.toString().equals("0xd5d1c8ff")
                        || color.toString().equals("0xc1b0afff")
                        || color.toString().equals("0xd5d0c8ff")
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

        System.out.println(nearestPixel[0] + " " + nearestPixel[1]);
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

        //setzt hilfsvariable auf den nächsten Punkt
        int checkX = nearestPixel[0];
        int checkY = nearestPixel[1];
        int helpX = 0;
        int helpY = 0;
        boolean redpixel = true;
        int feld = 0;

        int zähler = 0;
        Color blue = Color.BLUE;
        int[][] pixelArray = new int[1000][2];

        int pixelCounter = 0;
        

        //while schleife die solange offen bleibt, bis der boolean redpixel auf false ist, dieser wird false, wenn kein RAndpixel mehr gefunden wird
        while (redpixel) {
            //zähler für momentane prüfung, damit while schleife abbricht, da code für blau nicht gefunden
            zähler++;
            //System.out.println(checkX + " " + checkY);
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
                    System.out.println("2");

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
                    System.out.println("3");
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
                    System.out.println("4");

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
                    System.out.println("5");

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
                    System.out.println("6");

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
                    System.out.println("7");

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
                    System.out.println("8");

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
            if (zähler == 20000) {
                break;
            }

            if (zähler < 2 && redpixel == false) {
                while (wPixelReader.getColor(checkX, checkY).toString().equals("0xff0000ff")) {
                    checkX++;
                }
                checkX = checkX - 1;
                redpixel = true;
            }
            int xhelp = 0;
            int yhelp = 0;
            int zähler1 = 0;
            if (redpixel == false) {
                System.out.println("if wegen außenpixel");
                if (wPixelReader.getColor(checkX + 1, checkY).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    System.out.println("1");
                    xhelp = xhelp + 1;
                    System.out.println("xhelp: "+xhelp+"; yhelp: "+yhelp);

                }
                if (wPixelReader.getColor(checkX + 1, checkY + 1).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    System.out.println("2");
                    xhelp = xhelp + 1;
                    yhelp = yhelp + 1;
                     System.out.println("xhelp: "+xhelp+"; yhelp: "+yhelp);

                }
                if (wPixelReader.getColor(checkX + 0, checkY + 1).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    System.out.println("3");
                    yhelp=yhelp+1;
                     System.out.println("xhelp: "+xhelp+"; yhelp: "+yhelp);

                }
                if (wPixelReader.getColor(checkX - 1, checkY + 1).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    System.out.println("4");
                    xhelp = xhelp-1;
                    yhelp=yhelp+1;
                     System.out.println("xhelp: "+xhelp+"; yhelp: "+yhelp);

                }
                if (wPixelReader.getColor(checkX - 1, checkY + 0).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    System.out.println("5");
                    xhelp = xhelp -1;
                     System.out.println("xhelp: "+xhelp+"; yhelp: "+yhelp);
                    

                }
                if (wPixelReader.getColor(checkX - 1, checkY - 1).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    System.out.println("6");
                    xhelp=xhelp-1;
                    yhelp = yhelp-1;
                     System.out.println("xhelp: "+xhelp+"; yhelp: "+yhelp);

                }
                if (wPixelReader.getColor(checkX + 0, checkY - 1).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    System.out.println("7");
                    yhelp = yhelp-1;
                     System.out.println("xhelp: "+xhelp+"; yhelp: "+yhelp);

                }
                if (wPixelReader.getColor(checkX + 1, checkY - 1).toString().equals("0x0000ffff")) {
                    zähler1 = zähler1 + 1;
                    System.out.println("8");
                    xhelp = xhelp +1;
                    yhelp = yhelp -1;
                     System.out.println("xhelp: "+xhelp+"; yhelp: "+yhelp);
                }
                if (zähler1 == 1) {
                    System.out.println("letzte if");
                    pixelWriter.setColor(checkX, checkY, Color.TRANSPARENT);
                    System.out.println(checkX +" "+ checkY);
                    checkX = checkX+xhelp;
                    System.out.println(xhelp+" "+yhelp);
                    checkY = checkY+yhelp;
                    System.out.println(checkX +" "+checkY);
                    redpixel = true;
                }
            }
        }
        //System.out.println("Farbe Blau:" + blue.toString());
        imageViewChangeColor.setImage(wImage);

      
        //Bestimmt den zentralen Pixel der index.html
        WebEngine webEngineTest = WebViewMap.getEngine();
        Object centerWebView;
        centerWebView = webEngineTest.executeScript("test()");
        //System.out.println(centerWebView);
        String test;
        String[] lon = new String[pixelCounter];
        String[] lat = new String[pixelCounter];
        String lonText = "lon=";
        String empty = "";
        String help = "";
        String help2 = "";
        String[][] longlat = new String[pixelCounter][2];
        //gibt die Koordinaten aus
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
            //System.out.println(help2);
            longlat[count] = help2.split(Pattern.quote(","));
            //System.out.println(longlat[count][0]);
            //System.out.println(longlat[count][1]);
            lon[count] = longlat[count][0];
            lat[count] = longlat[count][1];
        }

        webEngineTest.executeScript("createArrayLonLat(" + pixelCounter + ")");
        Object helpLongtitude;
        Object helpLatitude;
        for (int count = 0; count < pixelCounter; count++) {
            helpLongtitude = lon[count];
            helpLatitude = lat[count];
            helpLatitude = webEngineTest.executeScript("setLonLatArrays(" + helpLongtitude + "," + helpLatitude + "," + count + ")");
            //System.out.println(helpLatitude.toString());

        }
        webEngineTest.executeScript("pintarZonas()");

       return wImage;
    }
    
}
