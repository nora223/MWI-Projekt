/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package osmfxml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import javax.swing.JFileChooser;

/**
 *
 * @author CaReich
 */
public class Menu2 {

    private static String[][] daten = new String[10][4];

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
                        System.out.println("test " + lon + lat);
                        System.out.println("test " + helpLon + helpLat);

                        if (lon.equals(helpLon) && lat.equals(helpLat)) {
                            System.out.println("läuft er rein?");
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

    public String[] getCoordinates() {
        String[] coordinates = new String[2];

        coordinates[0] = daten[0][1];
        coordinates[0] = daten[0][2];

        return coordinates;
    }
    
    public String getLongitude(){
        //String lon = daten[0][1];
        String lon = "test";
        return lon;
    }
}  

