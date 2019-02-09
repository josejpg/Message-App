/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message.app.stages;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Jose J. Pardines Garcia
 */
public class DashboardStage {
    Stage stage;
    Scene scene;
    
    public DashboardStage() throws IOException {
        stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        Parent root = FXMLLoader.load(getClass().getResource("/message/app/templates/dashboardTemplate.fxml"));      
        scene = new Scene(root, 671, 420);
        stage.setScene(scene);
        stage.setTitle("Dashboard");
        stage.setResizable(false);
    }
    
    public Scene getScene(){ return this.scene; }
    public Stage getStage(){ return this.stage; }
    public void showStage(){ stage.show(); }
}
