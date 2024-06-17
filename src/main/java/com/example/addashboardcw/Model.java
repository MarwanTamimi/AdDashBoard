package com.example.addashboardcw;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Model {
    private static final Logger logger = LogManager.getLogger(Model.class);
    private FileReaderHelper rHelper; //for reading in csv files

    //hold data from the files
    private ArrayList<String[]> impressionArray;
    private ArrayList<String[]> clicksArray;
    private ArrayList<String[]> serverArray;

    public static void main (String[] args){
        var model = new Model();
        model.getMetrics();
    }

    public void getMetrics(){
        rHelper = new FileReaderHelper();
        impressionArray = rHelper.readCSV("src/main/java/impression_log.csv");
        clicksArray = rHelper.readCSV("src/main/java/click_log.csv");
        serverArray = rHelper.readCSV("src/main/java/server_log.csv");

        logger.info(rHelper.getNumImpressions(impressionArray));
        logger.info(rHelper.getNumClicks(clicksArray));
        logger.info(rHelper.getNumUniques(impressionArray));
        logger.info(rHelper.getNumConversions(serverArray));
    }
}
