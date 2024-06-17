package com.example.addashboardcw;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class View extends Application {

    private static final Logger logger = LogManager.getLogger(View.class);

    /**
     * Starts the application
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        var fxmlLoader = new FXMLLoader(View.class.getResource("mainPage.fxml"));
        Parent root = fxmlLoader.load();
        //Controller controller = fxmlLoader.getController();
        var scene = new Scene(root, 960, 720);

//        scene.getWindow().setHeight(200);
//        scene.getWindow().setWidth(500);
        stage.setTitle("Ad Auction Dashboard");
        stage.setScene(scene);
        //Dashboard dashBoard =new Dashboard(stage,97,73);
        //var contorl =new Controller(dashboard);
        stage.setOnCloseRequest(event ->  {
            event.consume();
            exit(stage);
        });

        //controller.setDashboard(dashBoard);
        //logger.info(controller.getDashboard(dashBoard).getHeight());
        stage.show();
    }
    public void exit(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("exit");
        alert.setHeaderText("You're about to EXIT!");
        alert.setContentText("Do you want to save before Exiting?: ");

        if (alert.showAndWait().get() == ButtonType.OK) {
            System.out.println("You successfully logged out!");
            stage.close();
        }
    }



    public static void main(String[] args) {
        logger.info("Start");
        launch();
    }
}
