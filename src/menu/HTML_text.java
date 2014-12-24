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

        String Polygon
                = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\n"
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
                +"         <script type=\"text/javascript\" src=\"//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js\"></script>\n" 
                +"         <script type=\"text/javascript\" src=\"//maps.google.com/maps/api/js?sensor=true\"></script>\n" 
                +"         <script type=\"text/javascript\" src=\"../gmaps.js\"></script>"
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
                + "                var lon = 5.33674460 ;\n"
                + "                var lat = 50.93041450 ;\n"
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
                + "                visibility: true, displayInLayerSwitcher: false});\n"
                + "\n"
                + "                map.addLayers([layer_mapnik, layer_markers]);\n"
                + "                jumpTo(lon, lat, zoom);\n"
                + "                // Position des Markers\n"
                + "                addMarker(layer_markers, lon, lat, popuptext);\n"
                + "                 var path =  [[5.33674460,50.93041450], [5.33656140,50.93043350], [5.33658170,50.93050940]];"
                + "                 polygon = map.drawPolygon({\n"
                + "                 paths: path, \n"
                + "                 strokeColor: '#BBD8E9',\n"
                + "                 strokeOpacity: 1,\n"
                + "                 strokeWeight: 3,\n"
                + "                 fillColor: '#BBD8E9',\n"
                + "                 fillOpacity: 0.6\n"
                + "                 });"
                + "\n"
                + "            }\n"
                + "\n"
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

        return Polygon;

    }
}
