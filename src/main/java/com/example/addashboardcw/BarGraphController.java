package com.example.addashboardcw;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BarGraphController implements Initializable {

    static final int TEXT_FIElD_MAX_INPUT = 4;

    private static final Logger logger = LogManager.getLogger(BarGraphController.class);

    BarChart barChart;
    @FXML
    VBox graphContainer;
    @FXML
    RadioButton impressionSelect;
    @FXML
    RadioButton clicksSelect;
    @FXML
    RadioButton uniqueSelect;
    @FXML
    RadioButton bouncesSelect;
    @FXML
    RadioButton conversionSelect;
    @FXML
    RadioButton totalCostSelect;
    @FXML
    RadioButton CTRSelect;
    @FXML
    RadioButton CPASelect;
    @FXML
    RadioButton CPCSelect;
    @FXML
    RadioButton CPMSelect;
    @FXML
    RadioButton bounceRate;
    @FXML
    TextField pagesTF;
    @FXML
    TextField timeSpentTF;
    @FXML
    DatePicker startDatePicker;
    @FXML
    DatePicker endDatePicker;

    //for date range filter
    LocalDate startDate;
    LocalDate endDate;

    @FXML
    Text pageError;
    @FXML
    Text timeError;

    @FXML
    ComboBox bounceBox;
    @FXML
    ComboBox contextBox;
    @FXML
    ComboBox ageRangeBox;
    @FXML
    ComboBox genderBox;
    @FXML
    ComboBox incomeBox;
    @FXML
    ComboBox timeBox;
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
        pageError.setText("");
        timeError.setText("");
        //set combo box for bounce
        bounceBox.getItems().removeAll(bounceBox.getItems());
        bounceBox.getItems().addAll("NumPages","TimeSpent");
        bounceBox.getSelectionModel().select("NumPages");

        //set combo box for filters
        contextBox.getItems().removeAll(contextBox.getItems());
        contextBox.getItems().addAll("None","News","Shopping","Social Media", "Blog", "Hobbies", "Travel");
        contextBox.getSelectionModel().select("None");

        timeBox.getItems().removeAll(timeBox.getItems());
        timeBox.getItems().addAll("Day", "Week", "Hour");
        timeBox.getSelectionModel().select("Day");

        ageRangeBox.getItems().removeAll(ageRangeBox.getItems());
        ageRangeBox.getItems().addAll("None","<25","25-34","35-44", "45-54", ">54");
        ageRangeBox.getSelectionModel().select("None");

        genderBox.getItems().removeAll(genderBox.getItems());
        genderBox.getItems().addAll("None", "Male", "Female");
        genderBox.getSelectionModel().select("None");

        incomeBox.getItems().removeAll(incomeBox.getItems());
        incomeBox.getItems().addAll("None", "Low", "Medium", "High");
        incomeBox.getSelectionModel().select("None");



    }

    //sets previously made chart
    public void setCurrentChart(BarChart chart){
        barChart = chart;

        if(barChart != null){
            graphContainer.getChildren().clear();
            graphContainer.getChildren().add(barChart);
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

    public void validatePagesField(KeyEvent e){
        //check if text is integer or not
        String input = pagesTF.getText();
        if (!input.matches("[0-9]+") ) {
            pageError.setText("Error: Invalid Input");
        }else{
            pageError.setText("");
        }
        if (input.length() > 3) {
            pagesTF.setText(input.substring(0,input.length()-1));
            //e.getCharacter().charAt(0);
        }
        //try and make input number
    }

    public void validateTimeField(KeyEvent e){
        //check if text is integer or not
        String input = timeSpentTF.getText();
        if (!input.matches("[0-9]+") ) {
            timeError.setText("Error: Invalid Input");
        }else{
            timeError.setText("");
        }
        if (input.length() > 10) {
            timeSpentTF.setText(input.substring(0,input.length()-1));
            //e.getCharacter().charAt(0);
        }
        //try and make input number
    }

    public void handleApplyFilters(ActionEvent event){
        //checks filters in use and applies them
        impressionArray = new ArrayList<String[]>(origImpressionArray);
        clickArray = new ArrayList<String[]>(origClickArray);
        serverArray = new ArrayList<String[]>(origServerArray);
        if(startDate != null && endDate != null){
            impressionArray = filterHelper.filterByDateRange(impressionArray,startDate,endDate);
            System.out.println(startDate.toString());
            clickArray = filterHelper.filterByDateRange(clickArray,startDate,endDate);
            serverArray = filterHelper.filterByDateRange(origServerArray,startDate,endDate);
        }
        if(!contextBox.getValue().equals("None")){
            logger.info("Context selected");
            impressionArray = filterHelper.filterByContext(impressionArray,(String)contextBox.getValue());
        }

        impressionArray = filterHelper.filterByAudienceSegment(impressionArray, (String)ageRangeBox.getValue(),(String)genderBox.getValue(),(String)incomeBox.getValue());
        //check if remaining data is zero
        if(impressionArray.size() == 0 || clickArray.size() == 0 | serverArray.size() == 0){
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
        handleShowBarChart(event);
    }

    public void handleResetFilters(ActionEvent event){
        //resets arrays back to original data and empties filter options
        impressionArray = new ArrayList<String[]>(origImpressionArray);
        clickArray = new ArrayList<String[]>(origClickArray);
        serverArray = new ArrayList<String[]>(origServerArray);
        logger.info("Now reset size is {}", impressionArray.size());

        //set anything back to normal
        contextBox.getSelectionModel().select("None");
        ageRangeBox.getSelectionModel().select("None");
        genderBox.getSelectionModel().select("None");
        incomeBox.getSelectionModel().select("None");
    }
    /**
     * Activates after generate button is pressed, checks the radio buttons and decides which metric method to use for
     * the graph
     * @param event
     */
    public void handleShowBarChart(ActionEvent event){

        if(impressionArray != null){
            //logger.info(impressionArray.size());
            logger.info(timeBox.getValue());
            switch((String) timeBox.getValue()) {
                case "Day":
                    groupImpressionArray = fileReader.splitByDay(impressionArray);
                    timeLabel = "Day";
                    break;
                case "Hour":
                    groupImpressionArray = fileReader.splitByHour(impressionArray);
                    timeLabel = "Hour";
                    break;
                case "Week":
                    groupImpressionArray = fileReader.splitByWeek(impressionArray);
                    timeLabel = "Week";
                    break;
                    default:
                    groupImpressionArray = fileReader.splitByDay(impressionArray);
                    timeLabel = "Day";
                    break;
            }

        }else{
            logger.info("Impression array IS NULL");
        }

        if(clickArray != null){
            //logger.info(impressionArray.size());
            logger.info(timeBox.getValue());
            switch((String) timeBox.getValue()) {
                case "Day":
                    groupClickArray = fileReader.splitByDay(clickArray);
                    timeLabel = "Day";
                    break;
                case "Hour":
                    groupClickArray = fileReader.splitByHour(clickArray);
                    timeLabel = "Hour";
                    break;
                case "Week":
                    groupClickArray = fileReader.splitByWeek(clickArray);
                    timeLabel = "Week";
                    break;
                default:
                    groupClickArray = fileReader.splitByDay(clickArray);
                    timeLabel = "Day";
                    break;
            }
        }else{
            logger.info("click array IS NULL");
        }

        if(serverArray != null){
            //logger.info(impressionArray.size());
            logger.info(timeBox.getValue());
            switch((String) timeBox.getValue()) {
                case "Day":
                    groupServerArray = fileReader.splitByDay(serverArray);
                    timeLabel = "Day";
                    break;
                case "Hour":
                    groupServerArray = fileReader.splitByHour(serverArray);
                    timeLabel = "Hour";
                    break;
                case "Week":
                    groupServerArray = fileReader.splitByWeek(serverArray);
                    timeLabel = "Week";
                    break;
                default:
                    groupServerArray = fileReader.splitByDay(serverArray);
                    timeLabel = "Day";
                    break;
            }
        }else{
            logger.info("server array IS NULL");
        }


        //group data first



        //chooses a graph to display depending on which radio button is selecte
        if(impressionSelect.isSelected()){
            displayNumImpressions();
        }else if(clicksSelect.isSelected()){
            displayNumClicks();
        }else if(uniqueSelect.isSelected()){
            displayNumUniques();
        }else if (conversionSelect.isSelected()){
            displayNumConversion();
        }else if(bouncesSelect.isSelected()) {
            displayBounces();
        }else if(totalCostSelect.isSelected()) {
            displayTotalCost();
        }else if(CTRSelect.isSelected()) {
            displayCTR();
        }else if(CPASelect.isSelected()) {
            displayCPA();
        }else if(CPCSelect.isSelected()){
            displayCPC();
        }else if(CPMSelect.isSelected()){
            displayCPM();
        }else if(bounceRate.isSelected()){
            displayBouncesRate();
        }else {
            logger.info("No buttons selected SELECT ONE");
        }

        if(barChart != null){
            graphContainer.getChildren().clear();
            graphContainer.getChildren().add(barChart);
            barChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
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


    //displays a graph with number of impressions over time

    /**
     * calculates the number of impressions for each time point and adds those to a graph
     */
    public void displayNumImpressions(){
        if(impressionArray == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Missing impressions log files - Go back and reupload");
            alert.showAndWait();
            return;
        }
        //splits the impression array, which holds each record, by each day


        //set up bar graph
        var xAxis = new CategoryAxis();
        xAxis.setLabel(timeLabel);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Num Impressions");

        barChart = new BarChart(xAxis, yAxis);


        barChart.setTitle("Number of Impressions");
        barChart.lookup(".chart-title").setStyle("-fx-text-fill: Black;");
        barChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
        var metrics = new XYChart.Series();

        metrics.setName("Metrics");

        int day = 1; //start day
        //loop through and compute metric for each data group
        for(ArrayList<String[]> dataGroup : groupImpressionArray){
            int numImp = fileReader.getNumImpressions(dataGroup);
            metrics.getData().add(new XYChart.Data<>(Integer.toString(day), numImp));
            day += 1;
        }

        barChart.getData().add(metrics);
        //barChart.getStyleClass().add("chart-bar");

    }

    /**
     * calculates the number of clicks for each time point and adds those to a graph
     */
    public void displayNumClicks(){
        if(clickArray == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Missing log file - Go back and reupload");
            alert.showAndWait();
            return;
        }



        var xAxis = new CategoryAxis();
        xAxis.setLabel(timeLabel);

        xAxis.setStyle("-fx-text-fill: Black;");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Num Clicks");
        yAxis.setStyle("-fx-text-fill: Black;");

        barChart = new BarChart(xAxis, yAxis);

        barChart.setTitle("Number of Clicks");
        var metrics = new XYChart.Series();
        barChart.lookup(".chart-title").setStyle("-fx-text-fill: Black;");
        barChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
        metrics.setName("Metrics");


        int day = 1;
        for(ArrayList<String[]> dataGroup : groupClickArray){
            int numClicks = fileReader.getNumClicks(dataGroup);

            metrics.getData().add(new XYChart.Data<>(Integer.toString(day), numClicks));
            day += 1;
        }


        barChart.getData().add(metrics);

    }
    //methods to display each metric on the bar chart
    /**
     * calculates the number of uniques for each time point and adds those to a graph
     */
    public void displayNumUniques(){
        if(impressionArray == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Missing log files - Go back and reupload");
            alert.showAndWait();
            return;
        }


        var xAxis = new CategoryAxis();
        xAxis.setLabel(timeLabel);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Num Uniques");

        barChart = new BarChart(xAxis, yAxis);


        barChart.setTitle("Number of Uniques");
        var metrics = new XYChart.Series();
        barChart.lookup(".chart-title").setStyle("-fx-text-fill: Black;");
        barChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
        metrics.setName("Metrics");

        int day = 1;
        for(ArrayList<String[]> dataGroup : groupImpressionArray){
            int numUni = fileReader.getNumUniques(dataGroup);
            metrics.getData().add(new XYChart.Data<>(Integer.toString(day), numUni));
            day += 1;
        }
        barChart.getData().add(metrics);
    }
    /**
     * calculates the number of conversions for each time point and adds those to a graph
     */
    public void displayNumConversion(){
        if(serverArray == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Missing log files - Go back and reupload");
            alert.showAndWait();
            return;
        }


        var xAxis = new CategoryAxis();
        xAxis.setLabel(timeLabel);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Num Conversion");

        barChart = new BarChart(xAxis, yAxis);

        barChart.setTitle("Number of Conversion");
        var metrics = new XYChart.Series();
        barChart.lookup(".chart-title").setStyle("-fx-text-fill: Black;");
        barChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
        metrics.setName("Metrics");

        int day = 1;
        for(ArrayList<String[]> dataGroup : groupServerArray){
            int numConversions = fileReader.getNumConversions(dataGroup);
            logger.info("Number of conveersions {}", numConversions);
            metrics.getData().add(new XYChart.Data<>(Integer.toString(day), numConversions));
            day += 1;
        }
        barChart.getData().add(metrics);
    }

    /**
     * calculates the total cost for each time point and adds those to a graph
     */
    public void displayTotalCost() {
        if(impressionArray == null || clickArray == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Missing log files - Go back and reupload");
            alert.showAndWait();
            return;
        }


        //get iterators
        Iterator<ArrayList<String[]>> itImpression = groupImpressionArray.iterator();
        Iterator<ArrayList<String[]>> itClick = groupClickArray.iterator();

        //prepare graph
        var xAxis = new CategoryAxis();
        xAxis.setLabel(timeLabel);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Total Costs");

        barChart = new BarChart(xAxis, yAxis);

        barChart.setTitle("Total cost");
        var metrics = new XYChart.Series();
        barChart.lookup(".chart-title").setStyle("-fx-text-fill: Black;");
        barChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
        metrics.setName("Metrics");

        int day = 1;

        while (itImpression.hasNext() && itClick.hasNext()){
            var impressionGroup = itImpression.next();
            var clickGroup = itClick.next();

            //compute total cost, and add to graph
            double totalCost = fileReader.getTotalCost(impressionGroup,clickGroup);
            Math.round(totalCost);
            logger.info(totalCost);
            metrics.getData().add(new XYChart.Data<>(Integer.toString(day), totalCost));
            day += 1;
        }
        barChart.getData().add(metrics);
    }
    /**
     * calculates the number of bounces for each time point and adds those to a graph
     */
    public void displayBounces(){
        if(serverArray == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Missing log files - Go back and reupload");
            alert.showAndWait();
            return;
        } else if (!pagesTF.getText().matches("[0-9]+") && !timeSpentTF.getText().matches("[0-9]+")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Missing data - invalid bounce");
            alert.setContentText("Go below and change the bounce limit ");
            alert.showAndWait();
            return;
        }


        var xAxis = new CategoryAxis();
        xAxis.setLabel(timeLabel);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Num bounces");

        barChart = new BarChart(xAxis, yAxis);

        barChart.setTitle("Number of bounces");
        barChart.lookup(".chart-title").setStyle("-fx-text-fill: Black;");
        barChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
        var metrics = new XYChart.Series();

        metrics.setName("Metrics");

        int day = 1;

        //get bounce limit
        for(ArrayList<String[]> dataGroup : groupServerArray){
            int numBounce = 0;
            if(bounceBox.getValue().toString() == "NumPages" && !pagesTF.getText().isEmpty()){
                logger.info("Bounce chosen by number of pages");
                int limit = Integer.parseInt(pagesTF.getText());
                numBounce = fileReader.getBounce(dataGroup,limit);
            }else if(bounceBox.getValue().toString() == "TimeSpent" && !timeSpentTF.getText().isEmpty()){
                logger.info("Bounce chosen by time spent in seconds");
                int limit = Integer.parseInt(timeSpentTF.getText());
                numBounce = fileReader.getTimeBounce(dataGroup,limit);
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Missing values - Go back and reupload");
                alert.showAndWait();
                return;
            }

            metrics.getData().add(new XYChart.Data<>(Integer.toString(day), numBounce));
            day += 1;
        }
        barChart.getData().add(metrics);
    }
    /**
     * calculates the bounce rate for each time point and adds those to a graph
     */
    public void displayBouncesRate(){
        if(serverArray == null || clickArray == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Missing log files - Go back and reupload");
            alert.showAndWait();
            return;
        } else if (!pagesTF.getText().matches("[0-9]+") && !timeSpentTF.getText().matches("[0-9]+")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Missing data - invalid bounce");
            alert.setContentText("Go below and change the bounce limit ");
            alert.showAndWait();
            return;
        }

       Iterator<ArrayList<String[]>> iterator1 =groupServerArray.iterator();
       Iterator<ArrayList<String[]>> iterator2 =groupClickArray.iterator();

        //prepare graph
        var xAxis = new CategoryAxis();
        xAxis.setLabel(timeLabel);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Bounces Rate");

        barChart = new BarChart(xAxis, yAxis);

        barChart.setTitle("BouncesRate");
        barChart.lookup(".chart-title").setStyle("-fx-text-fill: Black;");
        barChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
        var metrics = new XYChart.Series();

        metrics.setName("Metrics");

        int day = 1;

        while (iterator1.hasNext() && iterator2.hasNext()){
            var server = iterator1.next();
            var click = iterator2.next();
            double bounceRate = 0;
            //compute bounces, and add to graph
            if(bounceBox.getValue().toString() == "NumPages" && !pagesTF.getText().isEmpty()){
                logger.info("Bounce chosen by number of pages");
                int limit = Integer.parseInt(pagesTF.getText());
                bounceRate = fileReader.getBounceRate(server,click,limit);
            }else if(bounceBox.getValue().toString() == "TimeSpent" && !timeSpentTF.getText().isEmpty()){
                logger.info("Bounce chosen by time spent in seconds");
                int limit = Integer.parseInt(timeSpentTF.getText());
                bounceRate = fileReader.getBounceRateTimeSpent(server,click,limit);
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Missing values - Go back and reupload");
                alert.showAndWait();
                return;
            }

            logger.info(bounceRate);
            metrics.getData().add(new XYChart.Data<>(Integer.toString(day), bounceRate));
            day += 1;
        }

        barChart.getData().add(metrics);
    }

    /**
     * calculates the click-through-rate for each time point and adds those to a graph
     */
    public void displayCTR(){
        if(impressionArray == null || clickArray == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Missing log files - Go back and reupload ");
            alert.showAndWait();
            return;
        }


        Iterator<ArrayList<String[]>> iterator1 =groupImpressionArray.iterator();
        Iterator<ArrayList<String[]>> iterator2 =groupClickArray.iterator();

        //prepare graph
        var xAxis = new CategoryAxis();
        xAxis.setLabel(timeLabel);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("CTR");

        barChart = new BarChart(xAxis, yAxis);

        barChart.setTitle("Click-Through-Rate (CTR)");
        barChart.lookup(".chart-title").setStyle("-fx-text-fill: Black;");
        barChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
        var metrics = new XYChart.Series();

        metrics.setName("Metrics");

        int day = 1;

        while (iterator1.hasNext() && iterator2.hasNext()){
            var imp = iterator1.next();
            var click = iterator2.next();

            //compute click-through rate, and add to graph
            double ctr = fileReader.clickThroughRate(click,imp);
            logger.info(ctr);
            metrics.getData().add(new XYChart.Data<>(Integer.toString(day), ctr));
            day += 1;
        }

        barChart.getData().add(metrics);
    }

    /**
     * calculates the cost per acquisition for each time point and adds those to a graph
     */
    public void displayCPA(){
        if(impressionArray == null || clickArray == null || serverArray == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Missing log files - Go back and reupload");
            alert.showAndWait();
            return;
        }

        Iterator<ArrayList<String[]>> iterator1 = groupImpressionArray.iterator();
        Iterator<ArrayList<String[]>> iterator2 = groupClickArray.iterator();
        Iterator<ArrayList<String[]>> iterator3 = groupServerArray.iterator();


        //prepare graph
        var xAxis = new CategoryAxis();
        xAxis.setLabel(timeLabel);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("CPA");

        barChart = new BarChart(xAxis, yAxis);

        barChart.setTitle("Cost-Per-Acquisition (CPA)");
        barChart.lookup(".chart-title").setStyle("-fx-text-fill: Black;");
        barChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
        var metrics = new XYChart.Series();

        metrics.setName("Metrics");

        int day = 1;

        while (iterator1.hasNext() && iterator2.hasNext() && iterator3.hasNext()){
            var imp = iterator1.next();
            var click = iterator2.next();
            var server = iterator3.next();
            //compute click-through rate, and add to graph
            double cpa = fileReader.costPerAcquisition(server,click,imp);
            logger.info(cpa);
            metrics.getData().add(new XYChart.Data<>(Integer.toString(day), cpa));
            day += 1;
        }

        barChart.getData().add(metrics);
    }

    /**
     * calculates the cost per click for each time point and adds those to a graph
     */
    public void displayCPC(){
        if(impressionArray == null || clickArray == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Missing log files - Go back and reupload ");
            alert.showAndWait();
            return;
        }


        Iterator<ArrayList<String[]>> iterator1 = groupImpressionArray.iterator();
        Iterator<ArrayList<String[]>> iterator2 = groupClickArray.iterator();

        var xAxis = new CategoryAxis();
        xAxis.setLabel(timeLabel);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("CPC");

        barChart = new BarChart(xAxis, yAxis);

        barChart.setTitle("Cost-Per-Click (CPC)");
        barChart.lookup(".chart-title").setStyle("-fx-text-fill: Black;");
        barChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
        var metrics = new XYChart.Series();

        metrics.setName("Metrics");

        int day = 1;
        while (iterator1.hasNext() && iterator2.hasNext()){
            var imp = iterator1.next();
            var click = iterator2.next();
            //compute click-through rate, and add to graph
            double cpc = fileReader.costPerClick(click,imp);
            logger.info(cpc);
            metrics.getData().add(new XYChart.Data<>(Integer.toString(day), cpc));
            day += 1;
        }

        barChart.getData().add(metrics);

    }
    /**
     * calculates the cost per 1000 impressions for each time point and adds those to a graph
     */
    public void displayCPM(){
        if(impressionArray == null || clickArray == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Missing log files - Go back and reupload" );
            alert.showAndWait();
            return;
        }

        Iterator<ArrayList<String[]>> iterator1 = groupImpressionArray.iterator();
        Iterator<ArrayList<String[]>> iterator2 = groupClickArray.iterator();

        var xAxis = new CategoryAxis();
        xAxis.setLabel(timeLabel);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("CPM");

        barChart = new BarChart(xAxis, yAxis);

        barChart.setTitle("Cost-Per-Thounds Impression (CPM)");
        barChart.lookup(".chart-title").setStyle("-fx-text-fill: Black;");
        barChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
        var metrics = new XYChart.Series();

        metrics.setName("Metrics");

        int day = 1;
        while (iterator1.hasNext() && iterator2.hasNext()) {
            var imp = iterator1.next();
            var click = iterator2.next();
            //compute click-through rate, and add to graph
            double cpm = fileReader.costPerThousandsImpressions(click, imp);
            logger.info(cpm);
            metrics.getData().add(new XYChart.Data<>(Integer.toString(day), cpm));
            day += 1;
        }
        barChart.getData().add(metrics);
    }

    public void windowGraph1(ActionEvent e) throws IOException{

        logger.info("Go to zoom");
        var fxmlLoader= new FXMLLoader(getClass().getResource("zoomedGraph.fxml"));
        Parent menuRoot = fxmlLoader.load();
        ZoomedGraphController zoomedGraphController = fxmlLoader.getController();
        //check if box 1 has a graph in it
        try{
            zoomedGraphController.setChart(barChart);
        }catch(Exception exception){
            logger.info("No graph");
            return;
        }
        //pass impression data over to next chart
        zoomedGraphController.setImpData(impressionArray);
        zoomedGraphController.setClickData(clickArray);
        zoomedGraphController.setServerData(serverArray);
        zoomedGraphController.setPrevScene("barchart");
        zoomedGraphController.setBarCharts(barCharts);
        zoomedGraphController.setLineCharts(lineCharts);



        var menuScene = new Scene(menuRoot);
        //gets the current stage from the button event
        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
        window.setScene(menuScene);
        window.show();

        //controller.setBarGraph(barCharts.get(0));


    }


    public void uploadClicked(ActionEvent e) throws IOException{
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

    public void histoClicked(ActionEvent e) throws IOException{
        logger.info("GO to linechart page");
        var fxmlLoader= new FXMLLoader(getClass().getResource("histogramPage.fxml"));
        Parent graphRoot = fxmlLoader.load();

        HistogramController histogramController = fxmlLoader.getController();

        //pass impression data over to next chart
        histogramController.setImpData(origImpressionArray);
        histogramController.setClickData(origClickArray);
        histogramController.setServerData(origServerArray);
        histogramController.setBarCharts(barCharts);
        histogramController.setLineCharts(lineCharts);
        var graphScene = new Scene (graphRoot);


        //gets the current stage from the button event
        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
        window.setScene(graphScene);
        window.show();
    }

    public void compareClicked(ActionEvent e) throws IOException{
        logger.info("GO to compare page");
        if(barChart == null){
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
        barCharts.add(barChart);
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
