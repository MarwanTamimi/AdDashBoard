package com.example.addashboardcw;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileReaderHelperTest {
    //testdata
    String[] row1 = {"2015-01-01 12:00:02","4620864431353617408","Male","25-34","High","Blog","1"};
    String[] row2 = {"2015-01-02 12:00:04","4620864431353617408","Female","35-44","Medium","News","2"};
    String[] row3 = {"2015-01-02 12:00:05","5239785226806161408","Female",">54","Medium","Shopping","3"};
    String[] row4 = {"2015-01-02 13:00:06","8530398223564990464","Female","<25","Medium","Social Media","4"};
    String[] row5 = {"2015-01-03 14:00:10","399593948382193664","Male","<25","Low","Social Media","6"};

    ArrayList<String[]> impressionArray = new ArrayList<String[]>(List.of(row1,row2,row3,row4,row5));

    //2015-01-01 12:01:21,8895519749317550080,11.794442
    //2015-01-01 12:01:33,6487139546184780800,11.718663
    //2015-01-01 12:02:25,1277480883056123904,0.000000
    //2015-01-01 12:02:57,3777890599251549184,0.000000
    //2015-01-01 12:04:02,6202006224593186816,9.340521
    String[] click1 = {"2015-01-01 12:01:21","8895519749317550080","1"};
    String[] click2 = {"2015-01-01 12:01:21","8895519749317550080","2.5"};
    String[] click3 = {"2015-01-01 12:01:21","8895519749317550080","0.000000"};
    String[] click4 = {"2015-01-01 12:01:21","8895519749317550080","0.000000"};
    String[] click5 = {"2015-01-01 12:01:21","8895519749317550080","0.5"};
    ArrayList<String[]> clickArray = new ArrayList<String[]>(List.of(click1,click2,click3,click4,click5));

    String[] server1 = {"2015-01-01 12:01:21","8895519749317550080","2015-01-01 12:05:13","7","No"};
    String[] server2 = {"2015-01-01 12:01:34","6487139546184780800","2015-01-01 12:02:01","1","No"};
    String[] server3 = {"2015-01-01 12:02:26","1277480883056123904","2015-01-01 12:05:19","10","No"};
    String[] server4 = {"2015-01-01 12:04:02","6202006224593186816","2015-01-01 12:04:03","1","No"};
    String[] server5 = {"2015-01-01 12:04:16","7375754838836782080","2015-01-01 12:05:48","4","No"};
    ArrayList<String[]> serverArray = new ArrayList<String[]>(List.of(server1,server2,server3,server4,server5));


    FileReaderHelper fileReader = new FileReaderHelper();
    @Test
    void getNumImpressions() {

        int impressions = fileReader.getNumImpressions(impressionArray);
        assertEquals(5,impressions,"Incorrect number of impressions read from data");
    }

    @Test
    void readCSV() {
        File file = new File("src/test/test.csv");
        ArrayList<String[]> arrayList = fileReader.readCSV(file);
        assertEquals(6,arrayList.size(),"The csv file should only have six lines and the header");

    }

    @Test
    void getBounce() {
        int bounces = fileReader.getBounce(serverArray,1);
        assertEquals(2,bounces,"incorrect number of bounces");
    }

    @Test
    void splitByDay() {
        ArrayList<ArrayList<String[]>> splitArray = fileReader.splitByDay(impressionArray);
        assertEquals(3,splitArray.size(),"Days split incorrectly");
    }

    @Test
    void splitByWeek() {
        ArrayList<ArrayList<String[]>> splitArray = fileReader.splitByWeek(impressionArray);
        assertEquals(1,splitArray.size(),"Weeks split incorrectly");
    }@Test
    void getNumClicks() {
        int clicks  = fileReader.getNumClicks(clickArray);
        assertEquals(5,clicks, "Incorrect number of clicks");
    }

    @Test
    void getNumUniques() {
        int uniques = fileReader.getNumUniques(impressionArray);
        assertEquals(4,uniques,"Incorrect number of uniques");
    }

    @Test
    void getNumConversions() {
        int conversions = fileReader.getNumConversions(serverArray);
        assertEquals(0,conversions, "Incorrect number of conversions");
    }

    @Test
    void getTotalCost() {
        double totalcost = fileReader.getTotalCost(impressionArray,clickArray);
        //total cost is 16 + 4 = 20
        assertEquals(20.0, totalcost, "Incorrect total cost");
    }

    @Test
    void getTimeBounce() {
        int timeBounces = fileReader.getTimeBounce(serverArray ,60);
        assertEquals(2,timeBounces,"Incorrect number of time bounces ");
    }

    @Test
    void getBounceRate() {
        double bounceRate = fileReader.getBounceRate(serverArray,clickArray,1);
        //2 divided by 5 = 0.4
        assertEquals(0.4,bounceRate,"Incorrect number of bounce rate");

    }

    @Test
    void getBounceRateTimeSpent() {
        double bounceRateTimeSpent = fileReader.getBounceRateTimeSpent(serverArray,clickArray,2);
        assertEquals(0.2,bounceRateTimeSpent,"Incorrect number of bounce rate time spent");
        //1/5 = 0.2

    }

    @Test
    void clickThroughRate() {
        double ctr = fileReader.clickThroughRate(clickArray,impressionArray);
        //5/5
        assertEquals(1,ctr, "Incorrect click through rate");
    }

    @Test
    void costPerAcquisition() {
        double acquisition = fileReader.costPerAcquisition(serverArray,clickArray,impressionArray);
        assertEquals(0,acquisition,"Incorrect number of acquisition");
    }

    @Test
    void costPerClick() {
        double costPerClick = fileReader.costPerClick(clickArray,impressionArray);
        assertEquals(4,costPerClick,"Incorrect number of per clicks");
        //20 / 5
    }

    @Test
    void costPerThousandsImpressions() {
        double perThousandsImpressions = fileReader.costPerThousandsImpressions(clickArray,impressionArray);
        assertEquals(4000,perThousandsImpressions,"Incorrect Cost per thousand Impressions");
        //4000
    }

    @Test
    void splitByHour() {
        ArrayList<ArrayList<String[]>> splitArray = fileReader.splitByHour(impressionArray);
        assertEquals(3,splitArray.size(),"hour split incorrectly");
    }

    @Test
    void getHistogramData() {
        ArrayList<Integer> histogramArray = fileReader.getHistogramData(clickArray);
        assertEquals(4,histogramArray.get(0),"Incorrect frequency");
    }
}