package com.example.addashboardcw;

import com.example.addashboardcw.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {
    private Stage stage;

    @FXML
    private Button start;
    @FXML
    private Button exit;
    @FXML
    private VBox scenePane;


    /**
     * when the start button is pressed it loads the upload page and navigates to it
     *
     * @param e
     * @throws IOException
     */
    public void onMenuButtonPushed(ActionEvent e) throws IOException {
        var fxmlLoader = new FXMLLoader(getClass().getResource("uploadPage.fxml"));
        Parent uploadRoot = fxmlLoader.load();
        var uploadScene = new Scene(uploadRoot);

        //gets the current stage from the button event
        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
        window.setScene(uploadScene);
        window.show();


    }

    /**
     * exits the program, gives an alert to give a second chance before quitting
     *
     * @param event
     */
    public void exit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("exit");
        alert.setHeaderText("You're about to EXIT!");
        alert.setContentText("Do you want to save before Exiting?: ");

        if (alert.showAndWait().get() == ButtonType.OK) {
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            System.out.println("you Successfully logged out!");
            window.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


}
