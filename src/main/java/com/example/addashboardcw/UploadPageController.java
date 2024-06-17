package com.example.addashboardcw;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

public class UploadPageController implements Initializable {
    private static final Logger logger = LogManager.getLogger(UploadPageController.class);

    public FileChooser fC = new FileChooser();
    @FXML
    Text UploadIText;
    @FXML
    Text UploadCText;
    @FXML
    Text UploadSText;

    ArrayList<String[]> impressionData;
    ArrayList<String[]> clickData;
    ArrayList<String[]> serverData;

    public File selectFile(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");

        // Set initial directory to user's desktop or other relevant location
        File defaultDirectory = new File(System.getProperty("user.home"));
        fileChooser.setInitialDirectory(defaultDirectory);

        // Set extension filter for CSV files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        return selectedFile;
    }

    /**
     * allows upload of an impression log. Checks if the file has the right file format
     * @param event
     */
    //when upload impression log csv button is selected
    public void onUploadILClick(ActionEvent event) {
        logger.info("Upload IL");
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        File file = selectFile(stage); // Select the file using FileChooser

        if (file != null && file.exists()) {
            // Check both the file name and extension
            String fileName = file.getName();
            int i = fileName.lastIndexOf('.');
            String extension = "";

            if (i > 0) {
                extension = fileName.substring(i + 1);
            }

            if ("csv".equalsIgnoreCase(extension) && "impression_log.csv".equals(fileName)) {
                logger.info("Successful");
                UploadIText.setText("Upload Impression Log: Success");
                var fileReader = new FileReaderHelper();
                impressionData = fileReader.readCSV(file);
                logger.info(impressionData.size());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                if (!"csv".equalsIgnoreCase(extension)) {
                    alert.setContentText("Wrong file type!");
                    UploadIText.setText("Upload Impression Log: Failed");
                } else {
                    alert.setContentText("Wrong file name!");
                    UploadIText.setText("Upload Impression Log: Failed");
                }
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("File not found!");
            UploadIText.setText("Upload Impression Log: Failed due to file not found!");
            alert.showAndWait();
        }
    }


    /**
     * allows upload of an click log. Checks if the file has the right file format
     * @param event
     */
    //when upload click log csv button is selected
    public void onUploadCLClick(ActionEvent event) {
        logger.info("Upload CL");
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        File file = selectFile(stage);

        if(file != null && file.exists()){
            String fileName = file.getName();
            int i = fileName.lastIndexOf('.');
            String extension = "";
            if (i > 0) {
                extension = fileName.substring(i+1);
            }
            if ("csv".equalsIgnoreCase(extension) && "click_log.csv".equals(fileName)) {
                logger.info("Successful");
                UploadCText.setText("Upload Click Log: Success");
                var fileReader = new FileReaderHelper();
                clickData = fileReader.readCSV(file);
                logger.info(clickData.size());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                if (!"csv".equalsIgnoreCase(extension)) {
                    alert.setContentText("Wrong file type!");
                } else {
                    alert.setContentText("Wrong file name!");
                }
                UploadCText.setText("Upload Click Log: Failed!");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("File not found!");
            UploadCText.setText("Upload Click Log: Failed!");
            alert.showAndWait();
        }
    }



    /**
     * allows upload of an server log. Checks if the file has the right file format
     * @param event
     */
    //when upload server log csv button is selected
    public void onUploadSLClick(ActionEvent event){
        logger.info("Upload SL");
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        File file = selectFile(stage);

        if(file != null && file.exists()){
            String fileName = file.getName();
            int i = fileName.lastIndexOf('.');
            String extension = "";
            if (i > 0) {
                extension = fileName.substring(i+1);
            }
            if ("csv".equalsIgnoreCase(extension) && "server_log.csv".equals(fileName)) {
                logger.info("Successful");
                UploadSText.setText("Upload Server Log: Success");
                var fileReader = new FileReaderHelper();
                serverData = fileReader.readCSV(file);
                logger.info(serverData.size());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                if (!"csv".equalsIgnoreCase(extension)) {
                    alert.setContentText("Wrong file type!");
                } else {
                    alert.setContentText("Wrong file name!");
                }
                UploadSText.setText("Upload Server Log: Failed!");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("File not found!");
            UploadSText.setText("Upload Server Log: Failed!");
            alert.showAndWait();
        }
    }

    /**
     * button event that loads in the bar graph scene and passes the record data to that scene, and loads it
     * @param event
     * @throws IOException
     */
    public void onGotoBarChart(ActionEvent event) throws IOException{
        var fxmlLoader= new FXMLLoader(getClass().getResource("barGraphPage.fxml"));
        Parent graphRoot = fxmlLoader.load();
        var graphScene = new Scene (graphRoot);

        BarGraphController barGraphController = fxmlLoader.getController();
        barGraphController.setImpData(impressionData);
        barGraphController.setClickData(clickData);
        barGraphController.setServerData(serverData);

        //gets the current stage from the button event
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(graphScene);
        window.show();
    }
    /**
     * button event that loads in the line graph scene and passes the record data to that scene, and loads it
     * @param event
     * @throws IOException
     */
    public void onGotoLineChart(ActionEvent event) throws IOException{
        var fxmlLoader= new FXMLLoader(getClass().getResource("lineGraphPage.fxml"));
        Parent graphRoot = fxmlLoader.load();

        LineGraphController lineGraphController = fxmlLoader.getController();
        //pass impression data over to next chart
        lineGraphController.setImpData(impressionData);
        lineGraphController.setClickData(clickData);
        lineGraphController.setServerData(serverData);
        var graphScene = new Scene (graphRoot);


        //gets the current stage from the button event
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(graphScene);
        window.show();
    }

    public void onGotoHistogram(ActionEvent event) throws IOException{
        var fxmlLoader= new FXMLLoader(getClass().getResource("histogramPage.fxml"));
        Parent graphRoot = fxmlLoader.load();

        HistogramController histogramController = fxmlLoader.getController();
        //pass impression data over to next chart
        histogramController.setImpData(impressionData);
        histogramController.setClickData(clickData);
        histogramController.setServerData(serverData);
        var graphScene = new Scene (graphRoot);


        //gets the current stage from the button event
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(graphScene);
        window.show();
    }

    /**
     * back button logic, goes back to the main page
     * @param event
     * @throws IOException
     */
    public void goBack(ActionEvent event) throws IOException{
        var fxmlLoader= new FXMLLoader(getClass().getResource("mainPage.fxml"));
        Parent menuRoot = fxmlLoader.load();

        var menuScene = new Scene (menuRoot);
        //gets the current stage from the button event
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menuScene);
        window.show();
    }

    /**
     * Sets where the filechooser opens up in
     * @param url
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resourceBundle
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fC.setInitialDirectory(new File("C:"));

    }

    /**
     * sets impression data from a previous screen to save it to the controller
     * @param array
     */
    public void setImpData(ArrayList<String[]> array){
        impressionData = array;
        if(impressionData != null){
            logger.info("Impression Array size {}",impressionData.size());
            UploadIText.setText("Upload Impression Log: Uploaded");
        }
    }

    /**
     * sets click data from a previous screen to save it to the controller
     * @param array
     */
    public void setClickData(ArrayList<String[]> array){
        clickData = array;
        if(clickData != null){
            logger.info("Click Array size {}",clickData.size());
            UploadCText.setText("Upload Click Log: Uploaded");
        }

    }
    /**
     * sets server data from a previous screen to save it to the controller
     * @param array
     */
    public void setServerData(ArrayList<String[]> array){
        serverData = array;
        if(serverData != null){
            logger.info("Server Array size {}",serverData.size());
            UploadSText.setText("Upload Server Log: Uploaded");
        }
    }
//    @Override
//    public void actionPerformed(ActionEvent e)
//    {
//        if(e.equals(ReFresh))
//        {
//            repaint();
//        }
//    }
}
