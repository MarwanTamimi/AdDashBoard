package com.example.addashboardcw;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ZoomedGraphController implements Initializable {

    private static final Logger logger = LogManager.getLogger(ComparePageController.class);
    @FXML
    VBox container;
    ArrayList<String[]> impressionArray;
    ArrayList<String[]> clickArray;
    ArrayList<String[]> serverArray;

    ArrayList<BarChart> barCharts;
    ArrayList<LineChart> lineCharts;

    BarChart barChart;

    String prevScene;
    Chart chart;

    LineChart lineChart;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        prevScene = "compare"; //default
    }

    public void setPrevScene(String scene){
        prevScene = scene;
    }

    public void displayChart(){
        if(chart != null){

            Chart displayChart = new Chart() {
                @Override
                protected void layoutChartChildren(double top, double left, double width, double height) {

                }
            };

            displayChart = chart;
            container.getChildren().add(displayChart);
        }
    }

    public void setImpData(ArrayList<String[]> array){
        impressionArray = array;
        if(impressionArray != null){
            logger.info("Impression Array size {}",impressionArray.size());
        }
    }
    /**
     * sets click data from a previous screen to save it to the controller
     * @param array
     */
    public void setClickData(ArrayList<String[]> array){
        clickArray = array;
        if(clickArray != null){
            logger.info("Click Array size {}",clickArray.size());
        }
    }
    /**
     * sets server data from a previous screen to save it to the controller
     * @param array
     */
    public void setServerData(ArrayList<String[]> array){
        serverArray= array;
        if(serverArray != null){
            logger.info("Server Array size {}",serverArray.size());

        }
    }

    public void setBarGraph(BarChart barChart){
        this.barChart = barChart;
        chart = barChart;
        displayChart();
    }

    public void setLineGraph(LineChart lineChart){
        this.lineChart = lineChart;
        chart = lineChart;
        displayChart();

    }

    public void setLineCharts(ArrayList<LineChart> array){
        lineCharts = array;

    }
    public void setBarCharts(ArrayList<BarChart> array){
        barCharts = array;

    }

    public void setChart(Chart chart){
        this.chart = chart;
        displayChart();
    }

    public void screenShot(ActionEvent e) throws IOException{
        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
        Scene scene = window.getScene();
        WritableImage image = scene.snapshot(null);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        File file = new File("graph.png");
        try{
            ImageIO.write(bufferedImage, "png", file);
        }catch (Exception exception){
            exception.printStackTrace();
        }

    }

    public void goBack(ActionEvent e) throws IOException {
        if(prevScene.equals("compare")){
            logger.info("GO to compare page");
            var fxmlLoader= new FXMLLoader(getClass().getResource("comparePage.fxml"));
            Parent menuRoot = fxmlLoader.load();
            ComparePageController comparePageController = fxmlLoader.getController();
            //pass impression data over to next chart
            comparePageController.setImpData(impressionArray);
            comparePageController.setClickData(clickArray);
            comparePageController.setServerData(serverArray);

            comparePageController.setBarCharts(barCharts);
            comparePageController.setLineCharts(lineCharts);

            var menuScene = new Scene(menuRoot);
            //gets the current stage from the button event
            Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
            window.setScene(menuScene);
            window.show();
        } else if (prevScene.equals("barchart")) {
            logger.info("GO to linechart page");
            var fxmlLoader= new FXMLLoader(getClass().getResource("barGraphPage.fxml"));
            Parent graphRoot = fxmlLoader.load();

            BarGraphController barGraphController = fxmlLoader.getController();
            //pass impression data over to next chart
            barGraphController.setImpData(impressionArray);
            barGraphController.setClickData(clickArray);
            barGraphController.setServerData(serverArray);
            barGraphController.setBarCharts(barCharts);
            barGraphController.setCurrentChart((BarChart) chart);
            barGraphController.setLineCharts(lineCharts);
            var graphScene = new Scene (graphRoot);


            //gets the current stage from the button event
            Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
            window.setScene(graphScene);
            window.show();
        }else if (prevScene.equals("linechart")) {
            logger.info("GO to linechart page");
            var fxmlLoader= new FXMLLoader(getClass().getResource("lineGraphPage.fxml"));
            Parent graphRoot = fxmlLoader.load();

            LineGraphController lineGraphController = fxmlLoader.getController();
            //pass impression data over to next chart
            lineGraphController.setImpData(impressionArray);
            lineGraphController.setClickData(clickArray);
            lineGraphController.setServerData(serverArray);
            lineGraphController.setBarCharts(barCharts);
            lineGraphController.setCurrentChart((LineChart) chart);
            lineGraphController.setLineCharts(lineCharts);
            var graphScene = new Scene (graphRoot);


            //gets the current stage from the button event
            Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
            window.setScene(graphScene);
            window.show();
        }else if (prevScene.equals("histogram")) {
            logger.info("GO to histogram page");
            var fxmlLoader= new FXMLLoader(getClass().getResource("histogramPage.fxml"));
            Parent graphRoot = fxmlLoader.load();

            HistogramController histogramController = fxmlLoader.getController();

            //pass impression data over to next chart
            histogramController.setImpData(impressionArray);
            histogramController.setClickData(clickArray);
            histogramController.setServerData(serverArray);
            histogramController.setBarCharts(barCharts);
            histogramController.setCurrentChart((BarChart) chart);
            histogramController.setLineCharts(lineCharts);
            var graphScene = new Scene (graphRoot);


            //gets the current stage from the button event
            Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
            window.setScene(graphScene);
            window.show();
        }

    }
}
