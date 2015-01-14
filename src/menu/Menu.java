package menu;


import de.micromata.opengis.kml.v_2_2_0.Boundary;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LinearRing;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Polygon;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
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
import javax.naming.spi.ObjectFactory;
import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import javax.xml.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 *
 * @author CaReich & MaLeupolz
 */
public class Menu {

    private static String[][] daten = new String[70][4];
    private static int NumberSUR = 0;
    private static int countIDKML = 0; //damit jede abgespeicherte KML-Datei eine neue Bezeichnung erhält

    public static String[] readKML() throws FileNotFoundException {
        File f = null;
        String help = null;
        String name = "name";
        String pic = "src";
        String koordinaten = "coordinates";
        String filename = null;
        String[][] KmlKoordinaten = new String[1000][4];
        String ListOfCoordinates = null;
        String komma = ",";
        int temp = -1;
        int i = -1;
        String space = " ";
        String[] strings = null;
        String[] shortstrings = null;

        try {
            //POP-UP Fenster zur Auswahl der zu lesenden Datei
            JFileChooser fc = new JFileChooser();
            int chooseResult = fc.showDialog(null, "Bitte Datei auswählen");
            if (chooseResult == JFileChooser.APPROVE_OPTION) {
                f = fc.getSelectedFile();
                filename = f.getPath();
            }
            if (f != null) {
                //Auslesen der Koordinaten des Polygons aus der KML-Datei
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(new File(filename));
                doc.getDocumentElement().normalize();
                //System.out.println ("Root element of the doc is " + doc.getDocumentElement().getNodeName());
                NodeList Koordinaten = doc.getElementsByTagName("LinearRing");
                Node FirstKoordinate = Koordinaten.item(0);
                if (FirstKoordinate.getNodeType() == Node.ELEMENT_NODE) {
                    Element firstElement = (Element) FirstKoordinate;
                    //Explizit wird hier das KML Element coordinates aus der kml-Datei gezogen um die darin enthaltenen Koordinaten zu selektieren
                    NodeList firstNameList = firstElement.getElementsByTagName("coordinates");
                    Element firstNameElement = (Element) firstNameList.item(0);
                    NodeList textFNList = firstNameElement.getChildNodes();

                    ListOfCoordinates = textFNList.item(0).getNodeValue();
                    //System.out.println("LOC " + ListOfCoordinates);
                    //Splitten der Koordinaten in ein zweidimensionales Array
                    //Zunächst wird geprüft in welcher Form sich die Koordinaten innerhalb der KML gespeichert sind
                    if (ListOfCoordinates.contains(space)) {//Bei einzeiliger Koordinaten Auflistung
                        // Splitten nach Leerzeichen
                        shortstrings = ListOfCoordinates.split(" ");
                        strings = new String[shortstrings.length - 1];
                        shortstrings[0] = shortstrings[0].trim();
                        //System.out.println("Strings an der STelle 0: " + shortstrings[0]);
                        for (int j = 0; j < shortstrings.length - 1; j++) {
                            //System.out.println("Strings an der STelle j: " + strings[j]);
                            strings[j] = shortstrings[j].replaceAll(",0", "");
                            //System.out.println("neu: " +j + " " + strings[j]);
                        }
                    } else {
                        //-----------------------------------------------------------------------------
                        //Strings beinhaltet die gesamten Koordinaten aus der KML Datei 
                        //Jede zeile des STrings Arrays beinhaltet einen Längen- und Breitengrad
                        // Andererseit kommen die Koordinaten auch in der Form vor, das diese untereinander weggeschrieben sind, 
                        //und sich somit Längen und Breitengrad immer in einer Zeile befinden.
                        //Split nach Returns
                        shortstrings = ListOfCoordinates.split("\n");
                        strings = new String[shortstrings.length - 1];
                        for (int y = 0; y < shortstrings.length - 1; ++y) {
                            strings[y] = shortstrings[y + 1];
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("fehler " + e);
        }
        return strings;
    }

    public static String[][] readAllSUR() throws FileNotFoundException {
        String[][] ergSUR = null;
        int anzahl;
        String[][] allSUR;
        String SURName;
        String[] vorherigeZeile = new String[4];
        int counterSame = 0;
        File s = null;
        String txtname = null;
        String inhalt = null;
        int intAnzahlSUR = 0;
        String anzahlSUR = null;
        List<String> Koordinaten = new ArrayList<String>();
        String lon = null; //longitude = Längengrad
        String lat = null; //latitude = Breitengrad
        String helpLon = null;
        String helpLat = null;
        int count = 0;                           

        try {
            //POP-UP zur Auswahl der einzulesenden Datei
            JFileChooser sc = new JFileChooser();
            int chooseResult = sc.showDialog(null, "Bitte Datei auswählen");
            if (chooseResult == JFileChooser.APPROVE_OPTION) {
                s = sc.getSelectedFile();
                txtname = s.getName();
                BufferedReader in = new BufferedReader(new FileReader(s));
                String zeile = null;

                //in intAnzahlSUR ist die Anzahl an allen SUR's die in dieser Datei gespeichert sind --> Auslesen der ersten Zeile der .txt Datei
                anzahlSUR = in.readLine();
                intAnzahlSUR = Integer.parseInt(anzahlSUR);
                NumberSUR = intAnzahlSUR;
                anzahl = intAnzahlSUR;
                //Array das alle Orte aus der SUR beinhaltet, die selben Orte sind in einer Zeile dieses Arrays gespeichert. 
                allSUR = new String[intAnzahlSUR][4];

                for (int j = 0; j < intAnzahlSUR; j++) {
                    //Innerhalb dieser beiden Schleifen werden die unterschiedlichen Regeln zu einem Objekt in eine Array Spalte gemeinsam geschrieben
                    zeile = in.readLine();
                    String[] zeileSplit = zeile.split(",");

                    for (int y = 0; y < 4; y++) {
                        if (vorherigeZeile[0] != null) {
                            //und innerhalb dieser Schleife in einen String gespeichert.
                            if (vorherigeZeile[0].equals(zeileSplit[0])) {
                                counterSame++;
                                anzahl--;
                                String help1 = allSUR[j - counterSame][3];
                                String help2 = zeileSplit[3];
                                String help3 = "";
                                help3 = new StringBuffer(help1).append("; " + help2).toString();
                                allSUR[j-counterSame][3] = help3;
                                break;
                            } else {
                                counterSame = 0;
                                allSUR[j][y] = zeileSplit[y];
                            }

                        } else {
                            for (int f = 0; f < 4; f++) {
                            }
                            allSUR[j][y] = zeileSplit[y];
                        }
                    }
                    //Letzter Eintrag
                    for (int t = 0; t < 4; t++) {
                        vorherigeZeile[t] = zeileSplit[t];
                    }
                }
                
                //ErgebnisSUR speichern ohne null Elemente
                ergSUR = new String [anzahl][4];
                int temp = 0;
                for (int h = 0; h < intAnzahlSUR; h++) {
                    for (int l = 0; l < 4; l++) {
                          temp = 0;
                        if (allSUR[h][l] != null) {                           
                           temp = 1;
                           ergSUR[count][l] = allSUR[h][l];
                        }                        
                    }
                    count = count + temp;
                }
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Datei könnte nicht gelesen werden. " + e);
        }
        return ergSUR;

    }

    /*private static boolean containsString(String s, String subString) {
        return s.indexOf(subString) > -1 ? true : false;
    }*/

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

    public static void saveKML(String[] koordinaten) throws FileNotFoundException {
        System.out.println("Datei wird gespeichert");
                
        //KML-Datei erzeugen
        final Kml kml = new Kml();
        //da wir oben das normale Document importiert haben, müssen wir hier das micromata Document importieren
        final de.micromata.opengis.kml.v_2_2_0.Document document = new de.micromata.opengis.kml.v_2_2_0.Document();
	kml.setFeature(document);

        //Placemark erzeugen mit dem Namen LinearRing.kml
	final Placemark placemark = new Placemark();
	document.getFeature().add(placemark);

        //Polygon erzeugen
	final Polygon polygon = new Polygon();
	placemark.setGeometry(polygon);
	final Boundary boundary = new Boundary();
	polygon.setOuterBoundaryIs(boundary);

        //Linear Ring erzuegen
	final LinearRing linearring = new LinearRing();
	boundary.setLinearRing(linearring);

        //Koordinaten hinzufügen
	List<Coordinate> coord = new ArrayList<Coordinate>();
	linearring.setCoordinates(coord);
	for (int i = 0; i < koordinaten.length; i++){
            String help = koordinaten[i];
            coord.add(new Coordinate(help));
        }
        
        //Datei speichern
        kml.marshal(new File(countIDKML + ".computed.kml"));
        countIDKML++; //countIDKML um eins erhöhen, da eine Datei erzeugt wurde
        System.out.println("Datei wurde erfolgreich gespeichert");
    }

    //Methode dient zur Rückgabe der Anzahl von SUR
    public static int getAnzahlSUR() {
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

    public String getLongitude() {
        String lon = daten[0][1];
        return lon;
    }

}
