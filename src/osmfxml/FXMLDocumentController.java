/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package osmfxml;

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
        public void showMap(){
          Stage primaryStage = new Stage();
                  
          //WebView browser = new WebView();
          WebEngine webEngine = WebViewMap.getEngine();
          URL url = getClass().getResource("index.html");
          webEngine.load(url.toExternalForm());
            
           Scene scene = new Scene(WebViewMap);
           primaryStage.setTitle("Test");
            
           primaryStage.setScene(scene);
           primaryStage.show();
        
    }
}
