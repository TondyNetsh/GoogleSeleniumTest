package selenium;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import static org.junit.Assert.fail;

public class GoogleTest {

    public WebDriver driver;
    public static Properties props;
    public static ArrayList<NamesDto> names;
    public static ExtentReports extent;
    public static boolean passed = true;

    @BeforeClass
    public static void beforeTest() {
        props = getEnvironmentVariables("src/test/resources/application.properties");
        //System.out.println(properties);

        names = ReadCSV.readNamesFromCSV("src/test/resources/names.csv");
        //System.out.println(names);

        extent  = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("C:/Reports/Spark.html");
        extent.attachReporter(spark);
    }


    @Test
    public void googleTest() throws Exception {
        System.setProperty("webdriver.chrome.driver","src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();

        // Maximize google chrome window
        driver.manage().window().maximize();

        for(int i = 1; i < names.size(); i++) {
            ExtentTest test = extent.createTest(names.get(i).getId() + " " + names.get(i).getDescription() + " " + names.get(i).getFirstName() + " " + names.get(i).getLastName());
            try{
                // Navigate to Google search web page
                driver.navigate().to(props.getProperty("url"));
                test.log(Status.PASS,"Navigate to Google search page ",
                        MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot(driver, props.getProperty("screenshotPath")+"Img"+System.nanoTime()+".png")).build());


                // Enter search word in the Google search text area
                Thread.sleep(2000);
                driver.findElement(By.xpath(props.getProperty("searchBox"))).sendKeys(names.get(i).getFirstName() + " " + names.get(i).getLastName() + "\t");
                test.log(Status.PASS,"Enter first name and last name in the search box: " + " " + names.get(i).getFirstName() + " " + names.get(i).getLastName(),
                        MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot(driver, props.getProperty("screenshotPath")+"Img"+System.nanoTime()+".png")).build());

                // Click on the search button
                Thread.sleep(2000);
                driver.findElement(By.xpath(props.getProperty("searchBtn"))).click();
                // test.log(Status.PASS,"Click on th search button " + " " + names.get(i).getFirstName() + " " + names.get(i).getLastName(),
                //        MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot(driver, props.getProperty("screenshotPath")+"Img"+System.nanoTime()+".png")).build());

                //String about = driver.findElement(By.id("result-stats")).getText();
                String body = driver.findElement(By.tagName("body")).getText();

                if (body.contains("About")) {
                    // Pass
                    test.log(Status.PASS,"Search result found for " + names.get(i).getFirstName() + " " + names.get(i).getLastName(),
                            MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot(driver, props.getProperty("screenshotPath")+"Img"+System.nanoTime()+".png")).build());
                } else {
                    // Fail
                    test.log(Status.FAIL,"No search result found",
                            MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot(driver, props.getProperty("screenshotPath")+"img"+System.nanoTime()+".png")).build());
                }

                //Assert.assertEquals(about.substring(0,5),"About 1");
                //if()
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Close google window when test is complete
        Thread.sleep(5000);

        driver.close();
    }

    @AfterClass
    public static void afterTest() {
        System.out.println("After.....");
        extent.flush();
        if (!passed) {
            fail("One or more test failed...");
        }
    }

    public static String takeScreenshot(WebDriver drive, String str) {
        try {
            TakesScreenshot screenshot = ((TakesScreenshot) drive);
            File imgPath = screenshot.getScreenshotAs(OutputType.FILE);

            // Move image file to new destination
            File srcImg = new File(str);
            FileUtils.copyFile(imgPath, srcImg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static Properties getEnvironmentVariables(String path) {
        Properties properties = new Properties();
        try {
            //load a properties file from class path, inside static method
            properties.load(GoogleTest.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        return properties;
    }
}