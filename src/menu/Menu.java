package menu;

import java.applet.*;
import java.io.BufferedReader;
import java.io.File;
import static java.io.FileDescriptor.in;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

/**
 *
 * @author CaReich
 */
public class Menu{

    private static String[][] daten = new String[70][4];
    private static int NumberSUR = 0;

    public static String[] readKML() throws FileNotFoundException {
        /*File f = null;
        String help = null;
        String name = "name";
        String pic = "src";
        String koordinaten = "coordinates";
        String filename = null;

        try {
            JFileChooser fc = new JFileChooser();
            int chooseResult = fc.showDialog(null, "Bitte Datei auswählen");
            if (chooseResult == JFileChooser.APPROVE_OPTION) {
                f = fc.getSelectedFile();
                filename = f.getName();
            }

            if (f != null) {
                Scanner scan = new Scanner(f);
                while (scan.hasNextLine()) {
                    help = scan.nextLine();

                    if (containsString(help, name)) {
                        name = help;
                    } else if (containsString(help, pic)) {
                        pic = help;
                    } else if (containsString(help, koordinaten)) {
                        koordinaten = help;
                    }
                }
            }*/
        File f = null;
        String help = null;
        String name = "name";
        String pic = "src";
        String koordinaten = "coordinates";
        String filename = null;
        String[][] KmlKoordinaten = new String[70][4];
        String ListOfCoordinates = null;
        String komma = ",";
        int temp = -1;
        int i = -1;
        String[] strings = null;

        try {
            JFileChooser fc = new JFileChooser();
            int chooseResult = fc.showDialog(null, "Bitte Datei auswählen");
            if (chooseResult == JFileChooser.APPROVE_OPTION) {
                f = fc.getSelectedFile();
                filename = f.getPath();
            }
            if (f != null) {
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(new File(filename));
                doc.getDocumentElement().normalize();
                //System.out.println ("Root element of the doc is " + doc.getDocumentElement().getNodeName());
                NodeList Koordinaten = doc.getElementsByTagName("LinearRing");
                Node FirstKoordinate = Koordinaten.item(0);
                if (FirstKoordinate.getNodeType() == Node.ELEMENT_NODE) {
                    Element firstElement = (Element) FirstKoordinate;
                    NodeList firstNameList = firstElement.getElementsByTagName("coordinates");
                    Element firstNameElement = (Element) firstNameList.item(0);
                    NodeList textFNList = firstNameElement.getChildNodes();

                    ListOfCoordinates = textFNList.item(0).getNodeValue();
                    
                    //-----------------------------------------------------------------------------
                    //Strings beinhaltet die gesamten Koordinaten aus der KML Datei 
                    //Jede zeile des STrings Arrays beinhaltet einen Längen- und Breitengrad
                    
                    strings = ListOfCoordinates.split("\n");
                    for (int x = 0; x < strings.length; x++){
                        temp = -1;
                        StringTokenizer st = new StringTokenizer(strings[x], komma);
                        i++;
                        while (st.hasMoreTokens()) {
                        temp++;
                        help = st.nextToken();
                        KmlKoordinaten[i][temp] = help;
                        }
                    }
                                        
                    //Ausgabe KmlKoordinaten
                    /*for (int z = 0; z < 70; z++){
                        for (int y = 0; y < 2; y++){
                            System.out.println("Koordinate - " + z + y + "    " + KmlKoordinaten[z][y]);
                        }
                    }*/
                    //Ausgabe der Strings aus der KML
                    /*for(int j = 0; j < 62; j++){
                     System.out.println("Zeile " + j+ ": " + strings[j]);
                     }*/
                    
                }
            }            
        } catch (Exception e) {
            System.out.println("fehler" + e);
        }
        return strings; 
    }

  
    public static String[][] readSUR() throws FileNotFoundException {
        File s = null;
        String txtname = null;
        String inhalt = null;
        int intAnzahl = 0;
        String anzahlSUR = null;
        List<String> Koordinaten = new ArrayList<String>();
        String lon = null; //longitude = Längengrad
        String lat = null; //latitude = Breitengrad
        String helpLon = null;
        String helpLat = null;

        try {
            JFileChooser sc = new JFileChooser();
            int chooseResult = sc.showDialog(null, "Bitte Datei auswählen");
            if (chooseResult == JFileChooser.APPROVE_OPTION) {
                s = sc.getSelectedFile();
                txtname = s.getName();
                //System.out.println(txtname); Ausgabe des Textnamens/s beinhaltet stattdessen den kompletten Dateipfad
                BufferedReader in = new BufferedReader(new FileReader(s));
                String zeile = null;
                for (int i = 1; i <= 1; i++) {
                    anzahlSUR = in.readLine();
                    intAnzahl = Integer.parseInt(anzahlSUR);
                    NumberSUR = intAnzahl;
                    System.out.println(intAnzahl);
                }
                for (int j = 1; j <= intAnzahl; j++) {
                    zeile = in.readLine();
                    cutSnip(zeile, j);
                }
                //Überprüfung ob alle Koordinaten in den Feldern gleich sind
                for (int i = 0; i < intAnzahl; i++) {
                    lon = daten[i][1];
                    lat = daten[i][2];
                    
                    if (helpLon != null && helpLat != null) {
                        //System.out.println("test " + lon + lat);
                        //System.out.println("test " + helpLon + helpLat);

                        if (lon.equals(helpLon) && lat.equals(helpLat)) {
                            //System.out.println("läuft er rein?");
                            helpLon = lon;
                            helpLat = lat;
                            continue;
                        } else {
                            javax.swing.JOptionPane.showMessageDialog(null, "Die angegebenen Koordinaten sind inkompatibel!");
                            break;
                        }
                    } else {
                        helpLon = lon;
                        helpLat = lat;
                    }
                }
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Datei könnte nicht gelesen werden. " + e);
        }
        return daten;
    }

    private static boolean containsString(String s, String subString) {
        return s.indexOf(subString) > -1 ? true : false;
    }

    public static void cutSnip(String zeile, int i) {
        //System.out.println(zeile);
        String komma = ",";
        StringTokenizer st = new StringTokenizer(zeile, komma);
        int temp = -1;

        while (st.hasMoreTokens()) {
            temp++;
            String help = st.nextToken();
            daten[i - 1][temp] = help;
        }
    }

    public static void saveKML() {

    }

    //Methode dient zur Rückgabe der Anzahl von SUR
    public static int getAnzahlSUR(){
        int Anzahl = 0;
        
        Anzahl = NumberSUR;
        //System.out.println("NumberSUR: " + Anzahl);
        return Anzahl;
    }

 
    
    public String[] getCoordinates() {
        String[] coordinates = new String[2];

        coordinates[0] = daten[0][1];
        coordinates[0] = daten[0][2];

        return coordinates;
    }
    
    public String getLongitude(){
        String lon = daten[0][1];
        return lon;
    }
    
    public static String generateHTML(){
        String Ausgabe = 
                    "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\n"
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
                    + "\n"
                    + "//            function timedRefresh(timeoutPeriod) {\n"
                    + "//                setTimeout(\"location.reload(true);\", timeoutPeriod);\n"
                    + "//            }\n"
                    + "            function drawmap() {\n"
                    + "                // Popup und Popuptext mit evtl. Grafik\n"
                    + "                var popuptext = \"<font color=\\\"black\\\"><b>Woop Karlsruhe</b><p><img src=\\\"dhbw.jpg\\\" width=\\\"180\\\" height=\\\"113\\\"></p></font>\";\n"
                    + "\n"
                    + "                OpenLayers.Lang.setCode('de');\n"
                    + "\n"
                    + "                // Position und Zoomstufe der Karte\n"
                    + "                var lon = "+ daten[0][1] + ";\n"
                    + "                var lat = "+ daten[0][2] + ";\n"
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
                    + "                map.addLayers([layer_mapnik, layer_markers]);\n"
                    + "                jumpTo(lon, lat, zoom);\n"
                    + "                // Position des Markers\n"
                    + "                addMarker(layer_markers, lon, lat, popuptext);\n"
                    + "\n"
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
                
        System.out.println("Test Koordinaten" + daten[0][1] + "dasten "+ daten[0][2]);
        
        return Ausgabe;
        
    }
}
