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
import java.util.Arrays;


public class LineGraphController implements Initializable {

    static final int TEXT_FIElD_MAX_INPUT = 4;

    private static final Logger logger = LogManager.getLogger(BarGraphController.class);

    LineChart lineChart;
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

    ArrayList<LineChart> lineCharts = new ArrayList<LineChart>();
    ArrayList<BarChart> barCharts = new ArrayList<BarChart>();
    String timeLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pageError.setText("");
        timeError.setText("");
        //set combo box for bounce
        bounceBox.getItems().removeAll(bounceBox.getItems());
        bounceBox.getItems().addAll("NumPages", "TimeSpent");
        bounceBox.getSelectionModel().select("NumPages");

        //set combo box for filters
        contextBox.getItems().removeAll(contextBox.getItems());
        contextBox.getItems().addAll("None", "News", "Shopping", "Social Media", "Blog", "Hobbies", "Travel");
        contextBox.getSelectionModel().select("None");

        timeBox.getItems().removeAll(timeBox.getItems());
        timeBox.getItems().addAll("Day", "Week", "Hour");
        timeBox.getSelectionModel().select("Day");

        ageRangeBox.getItems().removeAll(ageRangeBox.getItems());
        ageRangeBox.getItems().addAll("None", "<25", "25-34", "35-44", "45-54", ">54");
        ageRangeBox.getSelectionModel().select("None");

        genderBox.getItems().removeAll(genderBox.getItems());
        genderBox.getItems().addAll("None", "Male", "Female");
        genderBox.getSelectionModel().select("None");

        incomeBox.getItems().removeAll(incomeBox.getItems());
        incomeBox.getItems().addAll("None", "Low", "Medium", "High");
        incomeBox.getSelectionModel().select("None");

        LocalDate minDate = LocalDate.of(2015, Month.JANUARY, 1);
        LocalDate maxDate = LocalDate.of(2015, Month.APRIL, 30);
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

        endDatePicker.setDayCellFactory((p) -> new DateCell() {
            @Override
            public void updateItem(LocalDate ld, boolean bln) {
                super.updateItem(ld, bln);
                setDisable(ld.isBefore(minDate) || ld.isAfter(maxDate));
            }
        });
        Platform.runLater(() -> {
            endDatePicker.getEditor().clear();
        });
    }


    public void setCurrentChart(LineChart chart){
        lineChart = chart;

        if(lineChart != null){
            graphContainer.getChildren().clear();
            graphContainer.getChildren().add(lineChart);
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

    public void handleApplyFilters(ActionEvent event) {
        // Assume origImpressionArray, origClickArray, origServerArray are initialized properly
        impressionArray = origImpressionArray != null ? new ArrayList<>(origImpressionArray) : new ArrayList<>();
        clickArray = origClickArray != null ? new ArrayList<>(origClickArray) : new ArrayList<>();
        serverArray = origServerArray != null ? new ArrayList<>(origServerArray) : new ArrayList<>();

        // Apply date range filter if both dates are not null
        if (startDate != null && endDate != null) {
            impressionArray = filterHelper.filterByDateRange(impressionArray, startDate, endDate);
            clickArray = filterHelper.filterByDateRange(clickArray, startDate, endDate);
            serverArray = filterHelper.filterByDateRange(serverArray, startDate, endDate);
        }

        // Check and cast contextBox value properly before using it
        if (contextBox.getValue() != null && !contextBox.getValue().equals("None")) {
            String contextValue = (String) contextBox.getValue();  // Explicitly cast to String
            logger.info("Context selected: {}", contextValue);
            impressionArray = filterHelper.filterByContext(impressionArray, contextValue);
        }

        // Apply audience segment filters, ensuring proper casting
        if (ageRangeBox.getValue() != null && genderBox.getValue() != null && incomeBox.getValue() != null) {
            String ageRangeValue = (String) ageRangeBox.getValue(); // Cast to String
            String genderValue = (String) genderBox.getValue(); // Cast to String
            String incomeValue = (String) incomeBox.getValue(); // Cast to String
            impressionArray = filterHelper.filterByAudienceSegment(impressionArray, ageRangeValue, genderValue, incomeValue);
        }

        // Check if remaining data is zero after filtering
        if (impressionArray.isEmpty() || clickArray.isEmpty() || serverArray.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Filter error: No data left - Please try different filters");
            alert.showAndWait();

            // Revert to original data
            impressionArray = origImpressionArray != null ? new ArrayList<>(origImpressionArray) : new ArrayList<>();
            clickArray = origClickArray != null ? new ArrayList<>(origClickArray) : new ArrayList<>();
            serverArray = origServerArray != null ? new ArrayList<>(origServerArray) : new ArrayList<>();
            return;
        }

        logger.info("Impression array filtered is now size {}", impressionArray.size());

        // Call generate button on the end
        handleShowLineChart(event);
    }
    public void handleResetFilters(ActionEvent event) {
        // Assuming origImpressionArray, origClickArray, origServerArray are defined as List<String[]>
        // That means each is a List of String arrays, not a List of ArrayLists

        if (origImpressionArray != null) {
            // Create a new ArrayList of String arrays, copying each String array from the original list
            impressionArray = new ArrayList<>(origImpressionArray.size());
            for (String[] array : origImpressionArray) {
                impressionArray.add(array.clone());  // Cloning each String[] to ensure a deep copy if modification is needed
            }
        } else {
            impressionArray = new ArrayList<>();  // Initialize to empty if original is null
            logger.warn("origImpressionArray is null");
        }

        if (origClickArray != null) {
            clickArray = new ArrayList<>(origClickArray.size());
            for (String[] array : origClickArray) {
                clickArray.add(array.clone());  // Cloning each String[] to ensure a deep copy if modification is needed
            }
        } else {
            clickArray = new ArrayList<>();  // Initialize to empty if original is null
            logger.warn("origClickArray is null");
        }

        if (origServerArray != null) {
            serverArray = new ArrayList<>(origServerArray.size());
            for (String[] array : origServerArray) {
                serverArray.add(array.clone());  // Cloning each String[] to ensure a deep copy if modification is needed
            }
        } else {
            serverArray = new ArrayList<>();  // Initialize to empty if original is null
            logger.warn("origServerArray is null");
        }

        logger.info("Reset completed. Sizes - Impressions: {}, Clicks: {}, Servers: {}",
            impressionArray.size(), clickArray.size(), serverArray.size());

        // Resetting selection in combo boxes
        resetComboBoxes();
    }

    private void resetComboBoxes() {
        if (contextBox != null && ageRangeBox != null && genderBox != null && incomeBox != null) {
            contextBox.getSelectionModel().select("None");
            ageRangeBox.getSelectionModel().select("None");
            genderBox.getSelectionModel().select("None");
            incomeBox.getSelectionModel().select("None");
        } else {
            logger.warn("One or more ComboBoxes are null");
        }
    }


//    public void handleResetFilters(ActionEvent event){
//        //resets arrays back to original data and empties filter options
//        impressionArray = new ArrayList<String[]>(origImpressionArray);
//        clickArray = new ArrayList<String[]>(origClickArray);
//        serverArray = new ArrayList<String[]>(origServerArray);
//        logger.info("Now reset size is {}", impressionArray.size());
//
//        //set anything back to normal
//        contextBox.getSelectionModel().select("None");
//        ageRangeBox.getSelectionModel().select("None");
//        genderBox.getSelectionModel().select("None");
//        incomeBox.getSelectionModel().select("None");
//    }
    /**
     * Activates after generate button is pressed, checks the radio buttons and decides which metric method to use for
     * the graph
     * @param event
     */
    public void handleShowLineChart(ActionEvent event){

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

        if(lineChart != null){
            graphContainer.getChildren().clear();
            graphContainer.getChildren().add(lineChart);
            lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
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

        lineChart = new LineChart(xAxis, yAxis);


        lineChart.setTitle("Number of Impressions");
        lineChart.lookup(".chart-title").setStyle("-fx-text-fill: Black;");
        lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
        var metrics = new XYChart.Series();

        metrics.setName("Metrics");

        int day = 1; //start day
        //loop through and compute metric for each data group
        for(ArrayList<String[]> dataGroup : groupImpressionArray){
            int numImp = fileReader.getNumImpressions(dataGroup);
            metrics.getData().add(new XYChart.Data<>(Integer.toString(day), numImp));
            day += 1;
        }

        lineChart.getData().add(metrics);
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

        lineChart = new LineChart(xAxis, yAxis);

        lineChart.setTitle("Number of Clicks");
        var metrics = new XYChart.Series();
        lineChart.lookup(".chart-title").setStyle("-fx-text-fill: Black;");
        lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
        metrics.setName("Metrics");


        int day = 1;
        for(ArrayList<String[]> dataGroup : groupClickArray){
            int numClicks = fileReader.getNumClicks(dataGroup);

            metrics.getData().add(new XYChart.Data<>(Integer.toString(day), numClicks));
            day += 1;
        }


        lineChart.getData().add(metrics);

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

        lineChart = new LineChart(xAxis, yAxis);


        lineChart.setTitle("Number of Uniques");
        var metrics = new XYChart.Series();
        lineChart.lookup(".chart-title").setStyle("-fx-text-fill: Black;");
        lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
        metrics.setName("Metrics");

        int day = 1;
        for(ArrayList<String[]> dataGroup : groupImpressionArray){
            int numUni = fileReader.getNumUniques(dataGroup);
            metrics.getData().add(new XYChart.Data<>(Integer.toString(day), numUni));
            day += 1;
        }
        lineChart.getData().add(metrics);
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

        lineChart = new LineChart(xAxis, yAxis);

        lineChart.setTitle("Number of Conversion");
        var metrics = new XYChart.Series();
        lineChart.lookup(".chart-title").setStyle("-fx-text-fill: Black;");
        lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
        metrics.setName("Metrics");

        int day = 1;
        for(ArrayList<String[]> dataGroup : groupServerArray){
            int numConversions = fileReader.getNumConversions(dataGroup);
            logger.info("Number of conveersions {}", numConversions);
            metrics.getData().add(new XYChart.Data<>(Integer.toString(day), numConversions));
            day += 1;
        }
        lineChart.getData().add(metrics);
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

        lineChart = new LineChart(xAxis, yAxis);

        lineChart.setTitle("Total cost");
        var metrics = new XYChart.Series();
        lineChart.lookup(".chart-title").setStyle("-fx-text-fill: Black;");
        lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
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
        lineChart.getData().add(metrics);
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

        lineChart = new LineChart(xAxis, yAxis);

        lineChart.setTitle("Number of bounces");
        lineChart.lookup(".chart-title").setStyle("-fx-text-fill: Black;");
        lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
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
        lineChart.getData().add(metrics);
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

        lineChart = new LineChart(xAxis, yAxis);

        lineChart.setTitle("BouncesRate");
        lineChart.lookup(".chart-title").setStyle("-fx-text-fill: Black;");
        lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
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

        lineChart.getData().add(metrics);
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

        lineChart = new LineChart(xAxis, yAxis);

        lineChart.setTitle("Click-Through-Rate (CTR)");
        lineChart.lookup(".chart-title").setStyle("-fx-text-fill: Black;");
        lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
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

        lineChart.getData().add(metrics);
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

        lineChart = new LineChart(xAxis, yAxis);

        lineChart.setTitle("Cost-Per-Acquisition (CPA)");
        lineChart.lookup(".chart-title").setStyle("-fx-text-fill: Black;");
        lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
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

        lineChart.getData().add(metrics);
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

        lineChart = new LineChart(xAxis, yAxis);

        lineChart.setTitle("Cost-Per-Click (CPC)");
        lineChart.lookup(".chart-title").setStyle("-fx-text-fill: Black;");
        lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
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

        lineChart.getData().add(metrics);

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

        lineChart = new LineChart(xAxis, yAxis);

        lineChart.setTitle("Cost-Per-Thounds Impression (CPM)");
        lineChart.lookup(".chart-title").setStyle("-fx-text-fill: Black;");
        lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
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
        lineChart.getData().add(metrics);
    }

    public void windowGraph1(ActionEvent e) throws IOException{

        logger.info("Go to zoom");
        var fxmlLoader= new FXMLLoader(getClass().getResource("zoomedGraph.fxml"));
        Parent menuRoot = fxmlLoader.load();
        ZoomedGraphController zoomedGraphController = fxmlLoader.getController();
        //check if box 1 has a graph in it
        try{
            zoomedGraphController.setChart(lineChart);
        }catch(Exception exception){
            logger.info("No graph");
            return;
        }
        //pass impression data over to next chart
        zoomedGraphController.setImpData(impressionArray);
        zoomedGraphController.setClickData(clickArray);
        zoomedGraphController.setServerData(serverArray);
        zoomedGraphController.setPrevScene("linechart");
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
        if(lineChart == null){
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
        //add linecharts
        lineCharts.add(lineChart);
        comparePageController.setLineCharts(lineCharts);
        comparePageController.setBarCharts(barCharts);
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
    public void setLineCharts(ArrayList<LineChart> lineCharts) {
        this.lineCharts = lineCharts;
    }

    public void setBarCharts(ArrayList<BarChart> barCharts) {
        this.barCharts = barCharts;
    }
}
