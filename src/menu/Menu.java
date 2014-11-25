package menu;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
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
                    }else if (containsString(help, pic)) {
                        pic = help;
                    }else if(containsString(help, koordinaten)) {
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

private static boolean containsString(String s, String subString) {
 return s.indexOf(subString) > -1 ? true : false;
 }
}