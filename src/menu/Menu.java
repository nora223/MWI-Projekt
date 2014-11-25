package menu;

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
public class Menu {

    public static void readKML() throws FileNotFoundException {
        File f = null;
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
//            filename = f.;
                System.out.println("test " + f);
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
//                    System.out.println(help);
                }
                System.out.println(name);
                System.out.println(pic);
                System.out.println(koordinaten);
            }

        } catch (Exception e) {
            System.out.println("fehler");
        }

    }

    public static void readSUR() throws FileNotFoundException {
        File s = null;
        String txtname = null;
        String inhalt = null;
        int intAnzahl = 0;
        String anzahlSUR = null;
        List<String> Koordinaten = new ArrayList<String>();

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
                    //System.out.println(intAnzahl);
                }

                //System.out.println("______________________");
                for (int j = 1; j <= intAnzahl; j++) {
                    zeile = in.readLine();
                    String [][] daten = cutSnip(zeile, j);
                    //System.out.println("______________________");
                    //Koordinaten.add(zeile);

                    for (int i =0; i<4; i++){
                    System.out.println("Daten: " + daten[j-1][i]);
                    }
                    //System.out.println(zeile);
                }

                // Test Array List Inhaltausgaben
                //               System.out.println("______________________");
                //               for(String current: Koordinaten){
                //                   System.out.println(current);
                //               }
            }

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Datei könnte nicht gelesen werden. " + e);
            System.out.println("fehler");
        }
    }

    private static boolean containsString(String s, String subString) {
        return s.indexOf(subString) > -1 ? true : false;
    }

    public static String[][] cutSnip(String zeile, int i) {
        String [][] erg = new String [i][4];
        String komma = ",";
        StringTokenizer st = new StringTokenizer(zeile, komma);
        
                int temp = -1;

                while(st.hasMoreTokens()){
                    temp++;
                    String help = st.nextToken();
                    erg[i-1][temp] = help;
                    
            }
        return erg;
    }
}
