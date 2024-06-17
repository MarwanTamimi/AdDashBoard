package com.example.addashboardcw;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

public class HistogramController implements Initializable {

    private static final Logger logger = LogManager.getLogger(BarGraphController.class);

    BarChart histogram;
    @FXML
    VBox graphContainer;
    @FXML
    DatePicker startDatePicker;
    @FXML
    DatePicker endDatePicker;

    //for date range filter
    LocalDate startDate;
    LocalDate endDate;


    @FXML
    ComboBox contextBox;

    ArrayList<String[]> impressionArray;
    ArrayList<String[]> clickArray;
    ArrayList<String[]> serverArray;

    ArrayList<String[]> origImpressionArray;

    ArrayList<String[]> origClickArray;

    ArrayList<String[]> origServerArray;

    ArrayList<ArrayList<String[]>> groupImpressionArray;

    ArrayList<ArrayList<String[]>> groupClickArray;

    ArrayList<ArrayList<String[]>> groupServerArray;

    FileReaderHelper fileReader = new FileReaderHelper();

    FilterHelper filterHelper = new FilterHelper();

    ArrayList<BarChart> barCharts = new ArrayList<BarChart>();
    ArrayList<LineChart> lineCharts = new ArrayList<LineChart>();

    String timeLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    //sets previously made chart
    public void setCurrentChart(BarChart chart){
        histogram = chart;

        if(histogram != null){
            graphContainer.getChildren().clear();
            graphContainer.getChildren().add(histogram);
            //barChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
        }

    }

    //for date range pickers
    public void onPickStartDate(ActionEvent e){
        try{
            startDate = startDatePicker.getValue();
            logger.info(startDate.toString());
        }catch(Exception exception){
            logger.info("No filter");
            startDate = null;
        }

    }

    public void onPickEndDate(ActionEvent e){
        try{
            endDate = endDatePicker.getValue();
            logger.info(endDate.toString());
        }catch(Exception exception){
            logger.info("No filter");
            endDate = null;
        }

    }



    public void handleApplyFilters(ActionEvent event){
        //checks filters in use and applies them
        impressionArray = new ArrayList<String[]>(origImpressionArray);
        clickArray = new ArrayList<String[]>(origClickArray);
        serverArray = new ArrayList<String[]>(origServerArray);
        if(startDate != null && endDate != null){
            clickArray = filterHelper.filterByDateRange(clickArray,startDate,endDate);
        }
        //check if remaining data is zero
        if(clickArray.size() == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Filter error: No data left - Please try different filters");
            alert.showAndWait();
            impressionArray = new ArrayList<String[]>(origImpressionArray);
            clickArray = new ArrayList<String[]>(origClickArray);
            serverArray = new ArrayList<String[]>(origServerArray);
            return;
        }
        logger.info("Impression array filtered is now size {}", impressionArray.size());

        //call generate button on the end
        handleShowHistogram(event);
    }

    public void handleResetFilters(ActionEvent event){
        //resets arrays back to original data and empties filter options
        impressionArray = new ArrayList<String[]>(origImpressionArray);
        clickArray = new ArrayList<String[]>(origClickArray);
        serverArray = new ArrayList<String[]>(origServerArray);
        logger.info("Now reset size is {}", impressionArray.size());

        //set anything back to normal
    }
    /**
     * Activates after generate button is pressed, checks the radio buttons and decides which metric method to use for
     * the graph
     * @param event
     */
    public void handleShowHistogram(ActionEvent event){
        ArrayList<Integer> points = new ArrayList<Integer>();
        if(clickArray != null){
            //logger.info(impressionArray.size());
            //get values from filereader
            points = fileReader.getHistogramData(clickArray);

        }else{
            logger.info("click array IS NULL");
        }

        var xAxis = new CategoryAxis();
        xAxis.setLabel("Click Cost");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Frequency");
        //make histogram
        histogram = new BarChart<>(xAxis, yAxis);
        histogram.setCategoryGap(0);
        histogram.setBarGap(0);
        histogram.setTitle("Click-Cost Histogram");
        histogram.lookup(".chart-title").setStyle("-fx-text-fill: Black;");
        //histogram.lookup(".data0.chart-bar").setStyle("-fx-bar-fill: red");
        histogram.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");

        //remove bar spacing

        var metrics = new XYChart.Series();

        metrics.setName("Metrics");
        metrics.getData().add(new XYChart.Data<>("0-2",points.get(0)));

        metrics.getData().add(new XYChart.Data<>("2-4", points.get(1)));

        metrics.getData().add(new XYChart.Data<>( "4-6",points.get(2)));

        metrics.getData().add(new XYChart.Data<>("6-8",points.get(3)));

        metrics.getData().add(new XYChart.Data<>("8-10", points.get(4)));

        metrics.getData().add(new XYChart.Data<>( "10-12",points.get(5)));

        metrics.getData().add(new XYChart.Data<>("12-14",points.get(6)));

        metrics.getData().add(new XYChart.Data<>("14-16", points.get(7)));

        metrics.getData().add(new XYChart.Data<>( "16-18",points.get(8)));

        metrics.getData().add(new XYChart.Data<>("18-20",points.get(9)));

        metrics.getData().add(new XYChart.Data<>(">20", points.get(10)));


        histogram.getData().add(metrics);

        if(histogram!= null){
            graphContainer.getChildren().clear();
            graphContainer.getChildren().add(histogram);
            histogram.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
        }

    }

    /**
     * sets impression data from a previous screen to save it to the controller
     * @param array
     */
    public void setImpData(ArrayList<String[]> array){
        impressionArray = array;

        if(impressionArray != null){
            logger.info("Impression Array size {}",impressionArray.size());
            logger.info("Number of uniques {}", fileReader.getNumUniques(impressionArray));
            origImpressionArray = new ArrayList<String[]>(impressionArray); //holds original data (all records)

            String date = impressionArray.get(0)[0].substring(0, 10); //this is gonna look like 2015-01-01
            LocalDate minDate = LocalDate.parse(date);
            String date2 = impressionArray.get(impressionArray.size()-1)[0].substring(0, 10); //this is gonna look like 2015-01-01
            LocalDate maxDate = LocalDate.parse(date2);

            //LocalDate minDate = LocalDate.of(2015, Month.JANUARY, 1);
            //LocalDate maxDate = LocalDate.of(2015, Month.APRIL, 30);
            startDatePicker.setDayCellFactory((p) -> new DateCell() {
                @Override
                public void updateItem(LocalDate ld, boolean bln) {
                    super.updateItem(ld, bln);
                    setDisable(ld.isBefore(minDate) || ld.isAfter(maxDate));
                }
            });
            Platform.runLater(() -> {
                endDatePicker.getEditor().clear();
            });
            startDatePicker.setValue(minDate);
            endDatePicker.setValue(maxDate);
            endDatePicker.setDayCellFactory((p) -> new DateCell() {
                @Override
                public void updateItem(LocalDate ld, boolean bln) {
                    super.updateItem(ld, bln);
                    setDisable(ld.isBefore(minDate) || ld.isAfter(maxDate));
                }
            });
            Platform.runLater(() -> {
                startDatePicker.getEditor().clear();
                endDatePicker.getEditor().clear();
            });
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
            origClickArray = new ArrayList<String[]>(clickArray); //holds original data (all records)
        }

    }
    /**
     * sets server data from a previous screen to save it to the controller
     * @param array
     */
    public void setServerData(ArrayList<String[]> array){
        serverArray = array;
        if(serverArray != null){
            logger.info("Server Array size {}",serverArray.size());
            origServerArray = new ArrayList<String[]>(serverArray); //holds original data (all records)
        }
    }

    public void windowGraph1(ActionEvent e) throws IOException{

        logger.info("Go to zoom");
        var fxmlLoader= new FXMLLoader(getClass().getResource("zoomedGraph.fxml"));
        Parent menuRoot = fxmlLoader.load();
        ZoomedGraphController zoomedGraphController = fxmlLoader.getController();
        //check if box 1 has a graph in it
        try{
            zoomedGraphController.setChart(histogram);
        }catch(Exception exception){
            logger.info("No graph");
            return;
        }
        //pass impression data over to next chart
        zoomedGraphController.setImpData(impressionArray);
        zoomedGraphController.setClickData(clickArray);
        zoomedGraphController.setServerData(serverArray);
        zoomedGraphController.setPrevScene("histogram");
        zoomedGraphController.setBarCharts(barCharts);
        zoomedGraphController.setLineCharts(lineCharts);



        var menuScene = new Scene(menuRoot);
        //gets the current stage from the button event
        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
        window.setScene(menuScene);
        window.show();

        //controller.setBarGraph(barCharts.get(0));


    }


    public void uploadClicked(ActionEvent e) throws IOException {
        logger.info("GO to upload page");
        var fxmlLoader= new FXMLLoader(getClass().getResource("uploadPage.fxml"));
        Parent menuRoot = fxmlLoader.load();
        UploadPageController uploadPageController = fxmlLoader.getController();
        //pass impression data over to next chart
        uploadPageController.setImpData(origImpressionArray);
        uploadPageController.setClickData(origClickArray);
        uploadPageController.setServerData(origServerArray);
        var menuScene = new Scene(menuRoot);
        //gets the current stage from the button event
        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
        window.setScene(menuScene);
        window.show();
    }

    public void lineClicked(ActionEvent e) throws IOException{
        logger.info("GO to linechart page");
        var fxmlLoader= new FXMLLoader(getClass().getResource("lineGraphPage.fxml"));
        Parent graphRoot = fxmlLoader.load();

        LineGraphController lineGraphController = fxmlLoader.getController();
        //pass impression data over to next chart
        lineGraphController.setImpData(origImpressionArray);
        lineGraphController.setClickData(origClickArray);
        lineGraphController.setServerData(origServerArray);
        lineGraphController.setBarCharts(barCharts);
        lineGraphController.setLineCharts(lineCharts);
        var graphScene = new Scene (graphRoot);


        //gets the current stage from the button event
        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
        window.setScene(graphScene);
        window.show();
    }

    public void barClicked(ActionEvent e) throws IOException{
        logger.info("GO to linechart page");
        var fxmlLoader= new FXMLLoader(getClass().getResource("barGraphPage.fxml"));
        Parent graphRoot = fxmlLoader.load();

        BarGraphController barGraphController = fxmlLoader.getController();
        //pass impression data over to next chart
        barGraphController.setImpData(origImpressionArray);
        barGraphController.setClickData(origClickArray);
        barGraphController.setServerData(origServerArray);
        barGraphController.setBarCharts(barCharts);
        barGraphController.setLineCharts(lineCharts);
        var graphScene = new Scene (graphRoot);


        //gets the current stage from the button event
        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
        window.setScene(graphScene);
        window.show();
    }

    public void compareClicked(ActionEvent e) throws IOException{
        logger.info("GO to compare page");
        if(histogram == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Cannot add empty graph to the compare page");
            alert.showAndWait();
            return;
        }
        var fxmlLoader= new FXMLLoader(getClass().getResource("comparePage.fxml"));
        Parent menuRoot = fxmlLoader.load();
        ComparePageController comparePageController = fxmlLoader.getController();
        //pass impression data over to next chart
        comparePageController.setImpData(origImpressionArray);
        comparePageController.setClickData(origClickArray);
        comparePageController.setServerData(origServerArray);
        //add barcharts
        barCharts.add(histogram);
        comparePageController.setBarCharts(barCharts);
        comparePageController.setLineCharts(lineCharts);
        var menuScene = new Scene(menuRoot);
        //gets the current stage from the button event
        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
        window.setScene(menuScene);
        window.show();
    }

    /**
     * back button to navigate back to the upload page
     * @param event
     * @throws IOException
     */
    public void goBack(ActionEvent event) throws IOException {
        var fxmlLoader= new FXMLLoader(getClass().getResource("uploadPage.fxml"));
        Parent menuRoot = fxmlLoader.load();

        UploadPageController uploadPageController = fxmlLoader.getController();
        //pass impression data over to next chart
        uploadPageController.setImpData(origImpressionArray);
        uploadPageController.setClickData(origClickArray);
        uploadPageController.setServerData(origServerArray);

        var menuScene = new Scene(menuRoot);
        //gets the current stage from the button event
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menuScene);
        window.show();
    }

    //saves barchart array from compare screen
    public void setBarCharts(ArrayList<BarChart> barCharts) {
        this.barCharts = barCharts;
    }

    public void setLineCharts(ArrayList<LineChart> lineCharts) {
        this.lineCharts = lineCharts;
    }
}
