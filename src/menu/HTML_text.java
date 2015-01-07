/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menu;

/**
 *
 * @author CaReich
 */
public class HTML_text {

    public static String generatekmlHTML(String x[]) {

        String[] koordinaten = x;
        int zaehler = 0;
        //erste Koordinate = Längengrad und zweite Koordinate = Breitengrad
        String[] AllLon = new String[100]; //alle Längengrad Angaben
        String[] AllLat = new String[100];  //alle Breitengrad Angaben
        String firstLon = null; //Längengrad
        String firstLat = null; //Breitengrad
        
        for (int i = 1; i<x.length; i++){
            //Zeile 0 des Array koordinaten ist immer leer
            //System.out.println("zeile " + i + ":" + koordinaten[i]);
            
            if (i==1){
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
            System.out.println(helpLine[0] + "     " + helpLine[1]);
            
            zaehler++;
            AllLon[i] = helpLine[0] + ", ";
            AllLat[i] = helpLine[1] + ", ";
            //System.out.println("Längengrad: " + AllLon[i] + " Breitengrad: " + AllLat[i]);
            
        }
      
        

        String Polygon
                = " <!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\n"
                + "<html>\n"
                + "<head>\n"
                + "    <title></title>\n"
                + "<script type=\"text/javascript\"  src=\"http://openlayers.org/api/OpenLayers.js\"></script>\n"
                + "<script src=\"http://www.openstreetmap.org/openlayers/OpenStreetMap.js\"></script>\n"
                + "<script type=\"text/javascript\">    \n"
                + "\n"
                + "var map;\n"
                + "var lineLayer;\n"
                + "var points;\n"
                + "var style;\n"
                + "\n"
                + "var polygonFeature;\n"
                + "\n"
                + "function pintarZonas(){ \n"
                + "\n" 
                + "var zonaALng = [-3.841867446899414, -3.838176727294922, -3.838348388671875,    -3.843669891357422];\n"
                + "var zonaALat = [];\n"
                + "var zonaBLng = [];\n"
                + "var zonaBLat = [];\n"
                + "\n"
                + "//for (var i = 0; i <= AllLon.length; i++){"
                + "//     zonaALat = [AllLon[i], AllLon [i+1]]"
                + "//}"
                + "\n"
                + "     zonaALat = [ AllLat.join() ];"
                + "\n"
                + "     zonaBLng = [AllLon.join() ];"
                + "\n"
                + "     zonaBLat = [AllLat.join() ];"
                + "\n"
                + "\n"
                + "var vectorZonas = [zonaALng, zonaALat, zonaBLng, zonaBLat];\n"
                + "\n"
                + "\n"
                + "  lineLayerZona1 = new OpenLayers.Layer.Vector(\"zona1\");\n"
                + "  style1 = { strokeColor: '#0000ff',\n"
                + "     strokeOpacity: 1,\n"
                + "     strokeWidth: 5\n"
                + "  };\n"
                + "\n"
                + "  lineLayerZona2 = new OpenLayers.Layer.Vector(\"zo na2\");\n"
                + "  style2 = { strokeColor: '#ff0000',\n"
                + "     strokeOpacity: 1,\n"
                + "     fillColor: '#ff0000',\n"
                + "     strokeWidth: 2,\n"
                + "     fillOpacity: 0.1\n"
                + "\n"
                + "  };\n"
                + "\n"
                + "var vectorStyle = [style1, style2];  \n"
                + "var vectorLineLayer = [lineLayerZona1, lineLayerZona2];\n"
                + "\n"
                + "pointZona1 = new Array(); \n"
                + "pointZona2 = new Array();\n"
                + "vectorPoint = [pointZona1, pointZona2];\n"
                + "//Here I keep all points to both areas\n"
                + "var vectorCapas = [lineLayerZona1, lineLayerZona2];  \n"
                + "var i = 0;\n"
                + "var x = 0;\n"
                + "var zonaLat, zonaLng;\n"
                + "    var ww = 0;\n"
                + "for(i = 0; i < vectorZonas.length/2; i ++){\n"
                + "    zonaLng = vectorZonas[ww];\n"
                + "    zonaLat = vectorZonas[ww+1];\n"
                + "    point = vectorPoint[i];\n"
                + "    ww = i;\n"
                + "    ww +=2;\n"
                + "    for(x = 0; x < zonaLng.length; x ++){\n"
                + "        point[x] =new OpenLayers.LonLat(zonaLng[x],zonaLat[x] ).transform(new OpenLayers.Projection(\"EPSG:4326\"), map.getProjectionObject());\n"
                + "\n"
                + "        point[x] = new OpenLayers.Geometry.Point(point[x].lon, point[x].lat);   \n"
                + "    }\n"
                + "    //No hace falta añadir un punto de final porque se cierra solo el poligono\n"
                + "}\n"
                + "\n"
                + "\n"
                + " //Añadimos las capas al mapa\n"
                + " for(z = 0; z < vectorPoint.length; z++){\n"
                + "    linear_ring = new OpenLayers.Geometry.LinearRing(vectorPoint[z]);\n"
                + "    polygonFeature = new OpenLayers.Feature.Vector(\n"
                + "    new OpenLayers.Geometry.Polygon([linear_ring]), null, vectorStyle[z]);\n"
                + "\n"
                + "    vectorLineLayer[z].addFeatures([polygonFeature]);\n"
                + "    map.addLayer(vectorLineLayer[z]); \n"
                + "\n"
                + "}\n"
                + "\n"
                + "\n"
                + "}\n"
                + "\n"
                + "function initialize() \n"
                + "{    \n"
                + "  map = new OpenLayers.Map (\"map_canvas\", {\n"
                + "        controls:[\n"
                + "            new OpenLayers.Control.Navigation(),\n"
                + "            new OpenLayers.Control.PanZoomBar(),\n"
                + "            new OpenLayers.Control.LayerSwitcher(),\n"
                + "            new OpenLayers.Control.Attribution()],\n"
                + "        maxExtent: new     OpenLayers.Bounds(-20037508.34,-20037508.34,20037508.34,20037508.34),\n"
                + "        maxResolution: 156543.0399,\n"
                + "        numZoomLevels: 19,\n"
                + "        units: 'm',\n"
                + "        projection: new OpenLayers.Projection(\"EPSG:900913\"),\n"
                + "        displayProjection: new OpenLayers.Projection(\"EPSG:4326\")\n"
                + "      });\n"
                + "\n"
                + "    // Define the map layer\n"
                + "    // Here we use a predefined layer that will be kept up to date with URL changes\n"
                + "    layerMapnik = new OpenLayers.Layer.OSM.Mapnik(\"MapaCiudad\");\n"
                + "    map.addLayer(layerMapnik);\n"
                + "    //Angabe eines Längen- und Breitengrades\n"
                + "    var lonLat = new OpenLayers.LonLat(" + firstLon + "," + firstLat + ").transform(new OpenLayers.Projection(\"EPSG:4326\"), map.getProjectionObject());\n"
                + "    map.zoomTo(15);\n"
                + "    map.setCenter(lonLat, 19);  \n"
                + "\n"
                + " pintarZonas();\n"
                + "}\n"
                + "\n"
                + "</script>\n"
                + "</head>\n"
                + "\n"
                + "<body onload=\"initialize()\" >\n"
                + "\n"
                + "<div id=\"map_canvas\" style=\"width: 828px; height: 698px\"></div>  \n"
                + "</body>\n"
                + "\n" 
                + "</html>";


        return Polygon;

    }
}
