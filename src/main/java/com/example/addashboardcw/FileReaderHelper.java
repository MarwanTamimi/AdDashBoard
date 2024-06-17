package com.example.addashboardcw;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class FileReaderHelper {

    private static final Logger logger = LogManager.getLogger(FileReaderHelper.class);
    public static void main (String args[]){
        /**
         *

        logger.info("Test");
        System.out.print("I said test");
        ArrayList<String[]> readData= readCSV("src/main/java/click_log.csv");
        logger.info(readData.get(0)[1]);
        logger.info(readData.size());
        ArrayList<String[]> readData2 = readCSV("src/main/java/impression_log.csv");
        logger.info(readData2.size());
        ArrayList<String[]> readData3 = readCSV("src/main/java/server_log.csv");
        logger.info(readData3.size());
        logger.info(readData.get(1).length);
         */
    }

    //function for reading in a CSV

    /**
     * Reads in a csv to an array. Each record is split into a string list and added to the array
     * @param filePath
     * @return arraylist containing record data
     */
    public ArrayList<String[]> readCSV (String filePath){
        var array = new ArrayList<String[]>();
        try{

            var reader = new BufferedReader(new FileReader(filePath)); //create buffered reader
            logger.info(reader.readLine()); //reads headers

            //goes through each line until there are no more lines and splits into lists
            for(String line; (line = reader.readLine()) != null;){
                {
                    String[] data = line.split(","); //now split
                    logger.info(data[0]);
                    array.add(data);
                }
            }
            logger.info("Read file");

        }catch (Exception exception){
            exception.printStackTrace();
        }
        return array;
    }

    //for file uploading
    /**
     * Reads in a csv to an array. Each record is split into a string list and added to the array
     * @param file the name of the file
     * @return arraylist containing record data
     */
    public ArrayList<String[]> readCSV (File file){
        var array = new ArrayList<String[]>(); //
        try{

            var reader = new BufferedReader(new FileReader(file)); //create buffered reader
            logger.info(reader.readLine()); //reads headers

            //goes through each line until there are no more lines and splits into lists
            for(String line; (line = reader.readLine()) != null;){
                {
                    String[] data = line.split(","); //now split, e.g "Date,Id,Gender" would become a list ["March,"23","Male"]
                    //logger.info(data[0]);
                    array.add(data); //add to main array
                }
            }
            logger.info("Read file");

        }catch (Exception exception){
            exception.printStackTrace();
        }
        return array;
    }

    //gets number of impressions

    /**
     * gets the number of impressions, which is how many records there are in the impression log
     * @param impressArray array containing record data
     * @return
     */
    public int getNumImpressions(ArrayList<String[]> impressArray){
        return impressArray.size();
    }
    //gets number of clicks

    /**
     * gets number of clicks
     * @param clickArray array containing record data
     * @return
     */
    public int getNumClicks(ArrayList<String[]> clickArray){
        return clickArray.size();
    }

    /**
     * gets number of records with unique ids. Doesn't count duplicates
     * @param array contains record data
     * @return
     */
    //gets number of unique users from ids
    public int getNumUniques(ArrayList<String[]> array){
        //get list of ids
        Iterator<String[]> it = array.iterator();
        var uniques = new HashSet<String>();
        while (it.hasNext()){
           String[] record = it.next();
            uniques.add(record[1]);
        }

        return uniques.size();
    }

    /**
     * gets number of records with conversion column containing a "Yes" value
     * @param serverArray contains server record data
     * @return
     */
    public int getNumConversions(ArrayList<String[]> serverArray){
        //index 4
        int count = 0;
        for(String[] record : serverArray){
            String conversion = record[4];
            conversion = conversion.toLowerCase();
            if(conversion.equals("yes")){
                count++;
            }
        }
        return count;
    }

    /**
     * gets the total cost by summing up the cost columns in the impression and click records
     * @param impressionArray
     * @param clickArray
     * @return
     */
    //sums cost fields from impression and click logs
    public double getTotalCost(ArrayList<String[]> impressionArray, ArrayList<String[]> clickArray){
        double totalCost = 0;
        double totalClickCost = 0;
        double totalImpressionCost = 0;
        for(int i = 0; i<impressionArray.size(); i++){
            String[] impressionData = impressionArray.get(i);

            double impressionCost = Double.parseDouble(impressionData[6]);

            totalImpressionCost += impressionCost;
        }
        for(int i = 0; i<clickArray.size(); i++){

            String[] clickData = clickArray.get(i);

            double clickCost = Double.parseDouble(clickData[2]);
            totalClickCost += clickCost;
        }
        return totalImpressionCost + totalClickCost;



        //int sumClicks = 0;
        //int sumImpressions = 0;
        //for(String[] total : impressionArray){
          //  int clicks = Integer.parseInt(total[2]);
            //int impressions = Integer.parseInt(total[6]);
            //sumClicks += clicks;
            //sumImpressions += impressions;
        //}
            //iterate through the arrays
            //keep total variable
            //for each line take index 6 so line[6] for the impression log data and line line[2]
            //sum them up and return them
        //return sumClicks/ sumImpressions;
    }

    /**
     * gets a bounce, currently determined by only viewing a single page
     * @param serverArray
     * @return
     */
    public int getBounce (ArrayList<String[]> serverArray ){
        int BounceNumber =0;
        for (int i =0;i< serverArray.size();i++){
            int view = Integer.parseInt(serverArray.get(i)[3]);
            if (view==1){
                BounceNumber++;
            }
        }
        return BounceNumber;
    }

    public int getBounce (ArrayList<String[]> serverArray, int maxPages){
        int BounceNumber =0;
        for (int i =0;i< serverArray.size();i++){
            int view = Integer.parseInt(serverArray.get(i)[3]);
            //checks if view pages is low enough to be considered bounce
            if (view <= maxPages){
                BounceNumber++;
            }
        }
        return BounceNumber;
    }

    public int getTimeBounce (ArrayList<String[]> serverArray, int seconds){
        int BounceNumber =0;
        for (int i =0;i< serverArray.size();i++){
            //get start date from server record
            String startTime = serverArray.get(i)[0];
            String[] token = startTime.split(" ");
            String[] token1 = token[1].split(":");

            //get leave date from server record
            String endTime = serverArray.get(i)[2];
            if(endTime.equals("n/a")){
                logger.info("invalid");
            }else{
                String[] token3 = endTime.split(" ");
                String[] token4 =token3[1].split(":");

                int totalEnd = ((Integer.parseInt(token4[0])*3600))+((Integer.parseInt(token4[1])*60))+(Integer.parseInt(token4[2]));
                int totalStart = ((Integer.parseInt(token1[0])*3600))+((Integer.parseInt(token1[1])*60))+(Integer.parseInt(token1[2]));
                int total = totalEnd-totalStart;
                logger.info("Total time spent {}, bounce limit {}",total,seconds);
                //checks if total seconds on website is less than time limit is low enough to be considered bounce
                if (total <= seconds){
                    BounceNumber++;
                }
            }

        }
        return BounceNumber;
    }
    //2015-01-20 16:12:47 - it looks like taht
    //n/a

    /**
     * gets how many bounces per number of clicks
     * @param serverArray
     * @param clickArray
     * @return
     */


    public double getBounceRate(ArrayList<String[]> serverArray,ArrayList<String[]> clickArray, int maxPages){
        logger.info(getBounce(serverArray,maxPages));
        logger.info(getNumClicks(clickArray));
        double bounceRate = (double)getBounce(serverArray,maxPages)/(double)getNumClicks(clickArray);
        return bounceRate;
    }

    //bounce rate method for getting bounces in timespent
    public double getBounceRateTimeSpent(ArrayList<String[]> serverArray,ArrayList<String[]> clickArray, int timeSpent){
        logger.info(getBounce(serverArray,timeSpent));
        logger.info(getNumClicks(clickArray));
        double bounceRate = (double)getTimeBounce(serverArray,timeSpent)/(double)getNumClicks(clickArray);
        return bounceRate;
    }

    /**
     * gets the number of clicks per the number of impressions
     * @param clickArray
     * @param impressionArray
     * @return
     */
    public double clickThroughRate(ArrayList<String[]> clickArray,ArrayList<String[]> impressionArray){
        double ctr = (double)getNumClicks(clickArray)/(double)getNumImpressions(impressionArray);
        return ctr;
    }

    /**
     * gets the total cost per the number of conversions
     * @param serverLog
     * @param clickLog
     * @param impressionLog
     * @return
     */
     public double costPerAcquisition(ArrayList<String[]> serverLog,ArrayList<String[]> clickLog,ArrayList<String[]> impressionLog){
        double costPerAcqusition = getTotalCost(impressionLog,clickLog)/(double)getNumConversions(serverLog);
        if(costPerAcqusition == Double.POSITIVE_INFINITY){
            logger.info("ITS INFINITY");
            costPerAcqusition = 0;
        }
        return costPerAcqusition;
     }

    /**
     * gets the total cost per number of clicks
     * @param clickLog
     * @param impressionLog
     * @return
     */
     public double costPerClick(ArrayList<String[]> clickLog,ArrayList<String[]> impressionLog){
        double costPerclick= getTotalCost(impressionLog,clickLog)/(double)getNumClicks(clickLog);
        return costPerclick;
     }

    /**
     * gets the cost per each 1000 impressions
     * @param clicklog
     * @param impressionlog
     * @return
     */
    public double costPerThousandsImpressions(ArrayList<String[]> clicklog,ArrayList<String[]> impressionlog){
        double costPerT = getTotalCost(impressionlog,clicklog)/((double)getNumImpressions(impressionlog)/1000);
        return costPerT;
    }

    /**
     * groups the record data from a file into groups based on a day timeframe. All records in the same day are grouped
     * @param arrayList
     * @return
     */
    public ArrayList<ArrayList<String[]>> splitByDay (ArrayList<String[]> arrayList){

        var groupedArrayList = new ArrayList<ArrayList<String[]>>(); //split the file into multiple lists based on the days

        var temp = new ArrayList<String[]>(); //for grouping
        var date = arrayList.get(0)[0].substring(0,10); //holds the starting date
        //for loop gets date, adds until date changes
        Iterator<String[]> it = arrayList.iterator();

        logger.info("Initial date is {}", date);

        while(it.hasNext()){
            String[] record = it.next();
            if(record[0].substring(0,10).equals(date)){
                temp.add(record);
            }else{
                logger.info("Elements in this day is {}",temp.size());
                groupedArrayList.add(temp); //add to the grouped arraylist
                temp = new ArrayList<String[]>(); //reset for next group
                date = record[0].substring(0,10); //set new date
                logger.info("Change date to {}", date);
                temp.add(record); //adds start for next record
            }
            //logger.info(date);

        }
        logger.info("Elements in this day is {}",temp.size());
        groupedArrayList.add(temp);
        logger.info(groupedArrayList.size());

        int sum = 0;
        for (ArrayList<String[]> array: groupedArrayList){
            //logger.info(array.size());
            sum += array.size();
        }
        logger.info(sum);


        return groupedArrayList;
    }
    public ArrayList<ArrayList<String[]>> splitByHour(ArrayList<String[]> arrayList){
        var groupedArrayList = new ArrayList<ArrayList<String[]>>(); //split the file into multiple lists based on the hours
        var temp = new ArrayList<String[]>(); //for holding record of the grouping
        var time = arrayList.get(0)[0].substring(11,13); //
        logger.info("DATE is : {}", time);
        for(String[] record : arrayList){
            if(record[0].substring(11,13).equals(time)){
                temp.add(record);
            }else{
                groupedArrayList.add(temp);
                temp = new ArrayList<>();
                time = record[0].substring(11,13);
                logger.info("Hour is now : {}", time);
                temp.add(record);
            }
        }
        groupedArrayList.add(temp);
        //checking if the array list size is same as before
        int sum = 0;
        for (ArrayList<String[]> array: groupedArrayList){
            //logger.info(array.size());
            sum += array.size();
        }
        logger.info(sum);
        return groupedArrayList;
    }

    //  01/01/2015 12:00:02 format of date

    public ArrayList<ArrayList<String[]>> splitByWeek(ArrayList<String[]> arrayList){
        var groupedArrayList = new ArrayList<ArrayList<String[]>>(); //split the file into multiple lists based on the weeks
        var temp = new ArrayList<String[]>(); //for grouping
        var currentWeek = arrayList.get(0)[0].substring(0,10); //holds the starting date
        //can use LocalDate
        LocalDate startDate = LocalDate.parse(currentWeek);
        DayOfWeek startDay = startDate.getDayOfWeek();

        logger.info(startDay.name());
        //for loop gets date, adds until date changes

        for(String[] record : arrayList){
            LocalDate recordDate = LocalDate.parse(record[0].substring(0,10)); //gets localdate of record
            DayOfWeek recordDay = recordDate.getDayOfWeek();
            //LocalDate recordForWeek = recordDate.with(DayOfWeek.MONDAY);
            //check if not on same day and if day is start day aagin
            if(!recordDate.isEqual(startDate) && recordDay.equals(startDay)) {
                //logger.info("{} again, restarting",recordDay.name());
                groupedArrayList.add(temp);
                temp = new ArrayList<String[]>();
                //reset variables
                currentWeek = record[0].substring(0,10);
                startDate = LocalDate.parse(currentWeek);
                startDay = startDate.getDayOfWeek();
                temp.add(record);
            }else{
                temp.add(record);
            }
        }
        groupedArrayList.add(temp);

        int sum = 0;
        for (ArrayList<String[]> array: groupedArrayList){
            //logger.info(array.size());
            sum += array.size();
        }
        logger.info(sum);
        return groupedArrayList;
    }


    //histogram method

    //x=0
    //0<x<10
    // 10<x<15
    //above 15        x >15

    //0-2
    //2-4
    //4-6
    //6-8
    //8-10
    //10-12
    //12-14
    //14-16
    //16-18
    //18-20
    //>20

    public ArrayList<Integer> getHistogramData(ArrayList<String[]> clickArray){

        int frequency1 = 0;
        int frequency2 = 0;
        int frequency3 = 0;
        int frequency4 = 0;
        int frequency5 = 0;
        int frequency6 = 0;
        int frequency7 = 0;
        int frequency8 = 0;
        int frequency9 = 0;
        int frequency10 = 0;
        int frequency11 = 0;
        for(int i = 0; i<clickArray.size(); i++) {

            String[] clickData = clickArray.get(i);

            double clickCost = Double.parseDouble(clickData[2]);

            //find which category it goes in

            if (clickCost >= 0 && clickCost <= 2) {
                frequency1++;
            }else if (clickCost > 2 && clickCost <= 4){
                frequency2++;
            } else if (clickCost > 4 && clickCost <= 6) {
                frequency3++;
            } else if (clickCost > 6 && clickCost <= 8) {
                frequency4++;
            }else if (clickCost > 8 && clickCost <= 10) {
                frequency5++;
            }else if (clickCost > 10 && clickCost <= 12) {
                frequency6++;
            }else if (clickCost > 12 && clickCost <= 14) {
                frequency7++;
            }else if (clickCost > 14 && clickCost <= 16) {
                frequency8++;
            }else if (clickCost > 16 && clickCost <= 18) {
                frequency9++;
            }else if (clickCost > 18 && clickCost <= 20) {
                frequency10++;
            }else if (clickCost > 20) {
                frequency11++;
            }
        }
        ArrayList<Integer> frequencyArray = new ArrayList<Integer>();
        frequencyArray.add(frequency1);
        frequencyArray.add(frequency2);
        frequencyArray.add(frequency3);
        frequencyArray.add(frequency4);
        frequencyArray.add(frequency5);
        frequencyArray.add(frequency6);
        frequencyArray.add(frequency7);
        frequencyArray.add(frequency8);
        frequencyArray.add(frequency9);
        frequencyArray.add(frequency10);
        frequencyArray.add(frequency11);
        System.out.println();
        for(int frequency : frequencyArray){
            System.out.print(frequency + " ");
        }
        return frequencyArray;
    }


}
