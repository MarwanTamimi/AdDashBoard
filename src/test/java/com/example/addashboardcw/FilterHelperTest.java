package com.example.addashboardcw;

import javafx.fxml.JavaFXBuilderFactory;
import org.apache.logging.log4j.core.appender.mom.JmsManager;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.chrono.JapaneseChronology;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilterHelperTest {

    String[] row1 = {"2015-01-01 12:00:02","4620864431353617408","Male","25-34","High","Blog","0.001713"};
    String[] row2 = {"2015-01-02 15:00:04","3365479180556158976","Female","35-44","Medium","News","0.002762"};
    String[] row3 = {"2015-01-02 12:00:05","5239785226806161408","Female",">54","Medium","Shopping","0.001632"};
    String[] row4 = {"2015-01-02 12:00:06","8530398223564990464","Female","<25","Medium","Social Media","0.000000"};
    String[] row5 = {"2015-01-03 12:00:10","399593948382193664","Male","<25","Low","Social Media","0.002064"};
    ArrayList<String[]> testArray = new ArrayList<String[]>(List.of(row1,row2,row3,row4,row5));

    String[] testData1 = {"2015-01-01 12:01:21","8895519749317550080","2015-01-01 12:05:13","7","No"};
    String[] testData2 = {"2015-01-02 12:01:34","6487139546184780800","2015-01-01 12:02:01","1","No"};
    String[] testData3 = {"2015-01-02 12:02:26","1277480883056123904","2015-01-01 12:05:19","10","No"};
    String[] testData4 = {"2015-01-03 12:04:02","6202006224593186816","2015-01-01 12:04:03","1","No"};
    String[] testData5 = {"2015-01-03 12:04:16","7375754838836782080","2015-01-01 12:05:48","4","No"};
    ArrayList<String[]> testArray2 = new ArrayList<String[]>(List.of(testData1,testData2,testData3,testData4,testData5));

    LocalDate startDate = LocalDate.parse("2015-01-01");
    LocalDate endDate = LocalDate.parse("2015-01-02");
    FilterHelper filterHelper = new FilterHelper();


    @Test
    void filterByDateRange() {
        ArrayList<String[]> filteredData =  filterHelper.filterByDateRange(testArray, startDate, endDate);
        assertEquals(4,filteredData.size(), "Incorrect filtering");
    }
    @Test
    void filterByContext() {
        ArrayList<String[]> filteredData = filterHelper.filterByContext(testArray, "Social Media");
        assertEquals (2,filteredData.size(),"Incorrect Number of social media records");
    }

    @Test
    void filterByAudienceSegments() {
        ArrayList<String[]> filteredData = filterHelper.filterByAudienceSegment(testArray, "<25", "Female", "Medium");
        assertEquals (1,filteredData.size(),"Incorrect Number of social media records");
    }


}