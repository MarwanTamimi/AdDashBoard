package com.example.addashboardcw;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ComparePageController implements Initializable {
    private static final Logger logger = LogManager.getLogger(ComparePageController.class);
    ArrayList<String[]> impressionData;
    ArrayList<String[]> clickData;
    ArrayList<String[]> serverData;

    ArrayList<BarChart> barCharts;
    ArrayList<LineChart> lineCharts;

    @FXML
    HBox box1;
    @FXML
    HBox box2;
    @FXML
    HBox box3;
    @FXML
    HBox box4;

    @FXML
    GridPane chartGrid;


    int counter = 1;

    public void setLineCharts(ArrayList<LineChart> array){
        lineCharts = array;
        if(lineCharts != null){
            for( var lineChart : lineCharts) {
                addLineChart(lineChart, counter);
                counter++;
            }
        }
    }
    public void setBarCharts(ArrayList<BarChart> array){
        barCharts = array;
        if(barCharts != null){
            for(var barChart : barCharts){
                addBarChart(barChart,counter);
                counter++;
            }

        }
    }

    public void resetClicked(ActionEvent event){
        barCharts = new ArrayList<BarChart>();
        lineCharts = new ArrayList<LineChart>();
        chartGrid.getChildren().clear();
        logger.info("Reset");
    }
    //adds graph with respect to which box it needs to go in
    public void addBarChart(BarChart barchart,int counter){

        switch(counter) {
            case 1:
                // put box one
                box1.getChildren().clear();
                box1.getChildren().add(barchart);
                break;
            case 2:
                // put box2
                box2.getChildren().clear();
                box2.getChildren().add(barchart);
                break;
            case 3:
                // put box3
                box3.getChildren().clear();
                box3.getChildren().add(barchart);
                break;
            case 4:
                // put box4
                box4.getChildren().clear();
                box4.getChildren().add(barchart);
                break;
            default:
                break;
        }
    }
    public void addLineChart(LineChart lineChart, int counter){
        switch(counter) {
            case 1:
                // put box one
                box1.getChildren().clear();
                box1.getChildren().add(lineChart);
                break;
            case 2:
                // put box two
                box2.getChildren().clear();
                box2.getChildren().add(lineChart);
                break;
            case 3:
                // put box three
                box3.getChildren().clear();
                box3.getChildren().add(lineChart);
                break;
            case 4:
                // put box four
                box4.getChildren().clear();
                box4.getChildren().add(lineChart);
                break;
            default:

                break;
        }
    }



    public void windowGraph1(ActionEvent e) throws IOException{

        logger.info("GO to compare page");
        var fxmlLoader= new FXMLLoader(getClass().getResource("zoomedGraph.fxml"));
        Parent menuRoot = fxmlLoader.load();
        ZoomedGraphController zoomedGraphController = fxmlLoader.getController();
        //check if box 1 has a graph in it
        try{
            Chart graph = (Chart)box1.getChildren().get(0);
            zoomedGraphController.setChart(graph);
        }catch(Exception exception){
            logger.info("No graph");
            return;
        }
        //pass impression data over to next chart
        zoomedGraphController.setImpData(impressionData);
        zoomedGraphController.setClickData(clickData);
        zoomedGraphController.setServerData(serverData);
        zoomedGraphController.setPrevScene("compare");

        zoomedGraphController.setBarCharts(barCharts);
        zoomedGraphController.setLineCharts(lineCharts);



        var menuScene = new Scene(menuRoot);
        //gets the current stage from the button event
        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
        window.setScene(menuScene);
        window.show();

        //controller.setBarGraph(barCharts.get(0));


    }

    public void windowGraph2(ActionEvent e) throws IOException{

        logger.info("GO to compare page");
        var fxmlLoader= new FXMLLoader(getClass().getResource("zoomedGraph.fxml"));
        Parent menuRoot = fxmlLoader.load();
        ZoomedGraphController zoomedGraphController = fxmlLoader.getController();
        //check if box 1 has a graph in it
        try{
            Chart graph = (Chart)box2.getChildren().get(0);
            zoomedGraphController.setChart(graph);
        }catch(Exception exception){
            logger.info("No graph");
            return;
        }
        //pass impression data over to next chart
        zoomedGraphController.setImpData(impressionData);
        zoomedGraphController.setClickData(clickData);
        zoomedGraphController.setServerData(serverData);
        zoomedGraphController.setPrevScene("compare");

        zoomedGraphController.setBarCharts(barCharts);
        zoomedGraphController.setLineCharts(lineCharts);



        var menuScene = new Scene(menuRoot);
        //gets the current stage from the button event
        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
        window.setScene(menuScene);
        window.show();

        //controller.setBarGraph(barCharts.get(0));

    }

    public void windowGraph3(ActionEvent e) throws IOException{

        logger.info("GO to compare page");
        var fxmlLoader= new FXMLLoader(getClass().getResource("zoomedGraph.fxml"));
        Parent menuRoot = fxmlLoader.load();
        ZoomedGraphController zoomedGraphController = fxmlLoader.getController();
        //check if box 1 has a graph in it
        try{
            Chart graph = (Chart)box3.getChildren().get(0);
            zoomedGraphController.setChart(graph);
        }catch(Exception exception){
            logger.info("No graph");
            return;
        }
        //pass impression data over to next chart
        zoomedGraphController.setImpData(impressionData);
        zoomedGraphController.setClickData(clickData);
        zoomedGraphController.setServerData(serverData);
        zoomedGraphController.setPrevScene("compare");

        zoomedGraphController.setBarCharts(barCharts);
        zoomedGraphController.setLineCharts(lineCharts);



        var menuScene = new Scene(menuRoot);
        //gets the current stage from the button event
        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
        window.setScene(menuScene);
        window.show();

        //controller.setBarGraph(barCharts.get(0));


    }

    public void windowGraph4(ActionEvent e) throws IOException{
        logger.info("GO to compare page");
        var fxmlLoader= new FXMLLoader(getClass().getResource("zoomedGraph.fxml"));
        Parent menuRoot = fxmlLoader.load();
        ZoomedGraphController zoomedGraphController = fxmlLoader.getController();
        //check if box 1 has a graph in it
        try{
            Chart graph = (Chart)box4.getChildren().get(0);
            zoomedGraphController.setChart(graph);
        }catch(Exception exception){
            logger.info("No graph");
            return;
        }
        //pass impression data over to next chart
        zoomedGraphController.setImpData(impressionData);
        zoomedGraphController.setClickData(clickData);
        zoomedGraphController.setServerData(serverData);
        zoomedGraphController.setPrevScene("compare");
        zoomedGraphController.setBarCharts(barCharts);
        zoomedGraphController.setLineCharts(lineCharts);



        var menuScene = new Scene(menuRoot);
        //gets the current stage from the button event
        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
        window.setScene(menuScene);
        window.show();

        //controller.setBarGraph(barCharts.get(0));
    }

    public void setImpData(ArrayList<String[]> array){
        impressionData = array;
        if(impressionData != null){
            logger.info("Impression Array size {}",impressionData.size());
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

        }
    }
    public void goBack(ActionEvent event) throws IOException {
        var fxmlLoader = new FXMLLoader(getClass().getResource("barGraphPage.fxml"));
        Parent graphRoot = fxmlLoader.load();
        var graphScene = new Scene(graphRoot);

        BarGraphController barGraphController = fxmlLoader.getController();
        barGraphController.setImpData(impressionData);
        barGraphController.setClickData(clickData);
        barGraphController.setServerData(serverData);
        barGraphController.setBarCharts(barCharts);
        barGraphController.setLineCharts(lineCharts);

        //gets the current stage from the button event
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(graphScene);
        window.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
