package com.example.addashboardcw;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


public class FilterHelper {
    private static final Logger logger = LogManager.getLogger(FilterHelper.class);
    public ArrayList<String[]> filterByDateRange(ArrayList<String[]> logArray, LocalDate startDate, LocalDate endDate){
        ArrayList<String[]> filteredArray = new ArrayList<String[]>();

        //loop through each record
        for(String[] record : logArray) {
            String date = record[0].substring(0, 10); //this is gonna look like 2015-01-01
            LocalDate localDate = LocalDate.parse(date);
            //logger.info(localDate.toString());

            //now compare this localdate with the start and end dates to check if its in the range between them
            if(!localDate.isBefore(startDate) && !localDate.isAfter(endDate))
            {
                //logger.info("Is in range");
                filteredArray.add(record); //is included if in range
            }else{
                //logger.info("Not in range");
            }

        }
        logger.info("Size {}",filteredArray.size());
        return filteredArray;
    }
    public ArrayList<String[]> filterByContext(ArrayList<String[]> logArray,String selectedContext){
        ArrayList<String[]> filteredArray = new ArrayList<String[]>();

        for (String[] record : logArray){
            String recordContext = record[5];

            if(recordContext.equals(selectedContext)){
                filteredArray.add(record);
            }
        }
        //logger.info("Size {}",filteredArray.size());
        return filteredArray;
    }

    public ArrayList<String[]> filterByAudienceSegment(ArrayList<String[]> logArray,String ageRange, String gender, String income){

        ArrayList<String[]> filteredArray = new ArrayList<String[]>();
        ArrayList<String[]> filteredArray2 = new ArrayList<String[]>();
        ArrayList<String[]> filteredArray3 = new ArrayList<String[]>();



        //filters age range
        if(ageRange.equals("None")){
            System.out.println("No age range filter picked");
            filteredArray = logArray;
        }else{
            for (String[] record : logArray){
                String recordContext = record[3];

                if(recordContext.equals(ageRange)){
                    filteredArray.add(record);
                }
            }
        }
        //gender
        if(gender.equals("None")){
            System.out.println("No gender filter picked");
            filteredArray2 = filteredArray;
        }else{
            System.out.println("Filtering");
            for (String[] record : filteredArray){
                String recordContext = record[2];
                //System.out.println("Line: " + recordContext + ", chosen = " + gender);
                if(recordContext.equals(gender)){

                    filteredArray2.add(record);
                }
            }
        }
        //income
        if(income.equals("None")){
            System.out.println("No income filter picked");
            filteredArray3 = filteredArray2;
        }else{
            for (String[] record : filteredArray2){
                String recordContext = record[4];
                //System.out.println("Line: " + recordContext + ", chosen = " + income);
                if(recordContext.equals(income)){
                    filteredArray3.add(record);
                }
            }
        }


        logger.info("Size {}",filteredArray3.size());
        return filteredArray3;
    }
}
