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
                + "        <script type=\"text/javascript\" src=\"http://www.openlayers.org/api/OpenLayers.js\"></script>\n"
                + "        <script type=\"text/javascript\" src=\"http://www.openstreetmap.org/openlayers/OpenStreetMap.js\"></script>\n"
                + "        <script type=\"text/javascript\" src=\"tom.js\"></script>\n"
                + "        <script type=\"text/javascript\" src=\"menu/showCoordinate.js\"></script>\n"
                + "        <script type=\"text/javascript\">\n"
                + "\n"
                + "            var map;\n"
                + "            var layer_mapnik;\n"
                + "            var layer_tah;\n"
                + "            var layer_polygon;\n"
                + "\n"
                + "            function drawmap() {\n"
                + "                // Popup und Popuptext mit evtl. Grafik\n"
                + "                var popuptext = \"<font color=\\\"black\\\"><b>Woop Karlsruhe</b><p><img src=\\\"dhbw.jpg\\\" width=\\\"180\\\" height=\\\"113\\\"></p></font>\";\n"
                + "\n"
                + "                OpenLayers.Lang.setCode('de');\n"
                + "\n"
                + "                // Position und Zoomstufe der Karte\n"
                + "                var lon = 5.34109800 ;\n"
                + "                var lat = 50.93274000 ;\n"
                + "                var zoom = 18;\n"
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
                + "                Layer_polygon = new OpenLayers.Layer.OSM.Mapnik(\"draw Polygon\");\n"
                + "\n"
                + "                jumpTo(5.34109800, 50.93274000, zoom);\n"
                + "                // Position des Polygons\n"
                + "                var point1 = new OpenLayers.LonLat(5.34109800, 50.93274000);\n"             
                + "                var point2 = new OpenLayers.LonLat(5.34114700, 50.93278900);\n"
                + "                var point3 = new OpenLayers.LonLat(5.34116100, 50.93278200);\n"
                + "                var  polygon = new OpenLayers.Geometry.Polygon(point1, point2, point3);\n"
                + "                polygon.getGeodesicArea();\n"
                + "\n"
                + "                var a=  OpenLayers.Geometry.Polygon.createRegularPolygon(point1,50000,40,0);\n"
                + "                var aPoint = new OpenLayers.Geometry.Point(point1);\n"
                + "                var aCirclePoint = new OpenLayers.Geometry.Collection([a, aPoint]);\n"
                + "                var b = new OpenLayers.Feature.Vector(aCirclePoint);\n"               
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
