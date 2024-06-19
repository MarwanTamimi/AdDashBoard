# Ad Auction Dashboard

![Ad Auction Dashboard Compare Screen](/Users/marwanmousa/Documents/AdDashboard/Screen Shot.png)

## Description
The Ad Auction Dashboard is designed to manage and analyze advertising data effectively. It provides a user-friendly interface for visualizing different aspects of ad campaigns, such as impressions, clicks, and server interactions. This tool is essential for advertisers looking to optimize their campaigns based on detailed analytics. For a complete outline of project features and goals, please refer to the `SEG_Ad_Auction_Dashboard_specification.pdf` included in the project files.

## Prerequisites
- **Java Version**: This system requires Java 17, which supports newer features and optimizations not available in earlier versions. Download and install the latest version of Java from [Oracle's official Java download page](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html).

- **Log Files**: Ensure that the Impression Log, Click Log, and Server Log for each campaign are available as `.csv` files. It is recommended to store these files in a designated folder for each campaign to maintain organization and ease of access.

# Uploading Campaign Data

Follow these steps to upload your campaign data:

### Step 1: Open the Upload CSVs Interface
- Launch the Ad Auction Dashboard and navigate to the 'Upload' section on the sidebar.

### Step 2: Upload Each CSV File
- For each log type (Impression, Click, Server), click the corresponding 'Open CSV...' button. Select the appropriate CSV file and confirm the upload. A success message will appear once each file is uploaded.

### Step 3: Handling Errors
- If an upload fails, an error message will display. Verify that the CSV files are in the correct format and properly correspond to their respective log types. Correct any format inconsistencies or missing data to ensure successful uploads.

# Filtering Campaign Data

After uploading your campaign data, you have the flexibility to filter and analyze specific metrics using various types of charts on the dashboard. Here’s how:

### Step 1: Access Chart Interfaces
- From the main sidebar, select the type of chart you wish to use: 'Linechart', 'Barchart', or 'Histogram' to open the corresponding filtering interface.

### Step 2: Select Metrics and Date Range
- **Select Metrics**: Check the boxes for the metrics you wish to analyze, such as Number of Impressions, Clicks, Uniques, etc.
- **Set Date Range**: Use the 'Start Date' and 'End Date' dropdowns to define the time frame for the data you want to view.

### Step 3: Apply Filters and Generate Graph
- After selecting your metrics and setting the dates, click the 'Apply' button to filter the data.
- Click 'Generate Graph' to visualize the data based on your selected filters. You can also add the graph to a comparison page by clicking 'Add to Compare Page'.

### Step 4: Adjust Additional Filters
- For more specific analyses, such as Bounce Rate, input the necessary parameters like number of bounce pages or time spent before bounce.

### Taking Screenshots and Comparing Graphs
- **Accessing Screenshot Functionality**: To take a screenshot of any graph, first click the '+' sign located at the top right corner of the screen. This will open a menu where you can select the 'Screenshot' option.
- **Comparing Multiple Graphs**: The dashboard allows you to compare up to four graphs simultaneously. This can be done by generating graphs from different chart types (such as line charts, bar charts, and histograms) and adding them to the comparison page. 


# Frequently Asked Questions (FAQs)

### Q: In addition to date ranges can I filter charts by time intervals, such as hours, days, and weeks?
**A:** Yes. There is an option for this for all types of charts in their respective creation screens, it can be found as a drop-down menu and is the bottom left button beneath the chart. By default, the time interval is set to be days.

### Q: Would it cause complications if someone uploads a CSV file with the wrong data?
**A:** The application has precautions for user errors like this, displaying an alert message if data is not present within the CSV file. The system will also reject files that are not CSV files and will also inform the user of this mistake.

### Q: How can I differentiate between the filters I have applied when comparing multiple charts?
**A:** On the compare page charts can be given a title. This title can include the date ranges for the chart and whichever filters the user.

### Q: Can I add multiple metrics to one chart?
**A:** This is not possible as the data values range drastically from metric to metric with some data being >10,000 and others being <100. This would mean displaying both metrics on one chart would greatly hurt the readability of the created charts.

### Q: Can I see the created charts in more detail?
**A:** Once a chart has been added to the compare screen the user can use the zoom feature to full screen the chart and improve their readability.

### Q: Is it possible to save charts to view them again at a later point? Or will I need to recreate this chart every time I load the application?
**A:** Charts can be saved locally on your device as an image from the compare page, by pressing the “Screenshot” button. You cannot save charts and store them within the application itself thus cannot be edited after. To ensure a chart is final when saving them as an image. All charts and images will be saved in the current directory as “Chart.png”.

### Q: I am having issues where I am only able to select a limited number of metrics for my Chart. What could be causing this?
**A:** All three CSV files need to be uploaded successfully on the upload page before navigating to any of the chart screens to ensure there is data for all the available metrics.

### Q: How many charts can be compared at once?
**A:** Up to four charts can be generated on the compare screen and be analysed for comparison at one time. If the user wishes to compare more Charts, they can save these charts as images and then use the reset button to reset the compare page and generate more charts. Using this method, A user can compare as many charts as they can generate.

### Q: What do the metrics stand for/mean?
**A:** The metrics and their definitions include:
- **Impressions**: The number of times an ad is shown to a user.
- **Clicks**: The number of times a user clicks on an ad that is shown to them.
- **CTR (Click-Through-Rate)**: The average number of clicks per impression.
- **Bounces**: The number of times a user clicks on an ad but then fails to interact with the website. You can define this yourself.
- **CPC (Cost-Per-Click)**: The average amount of money spent on an advertising campaign for each click.
- **Uniques**: The number of unique users that click on an ad during a campaign.
- **CPM (Cost-Per-Thousand Impressions)**: The average amount of money spent on an advertising campaign for every thousand impressions.
- **Bounce Rate**: The average number of bounces per click.
- **CPA (Cost-Per-Acquisition)**: The average amount of money spent on an advertising campaign for each time a user clicks and then acts on an ad.




