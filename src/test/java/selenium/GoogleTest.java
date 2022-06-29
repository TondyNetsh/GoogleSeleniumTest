package selenium;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class GoogleTest {

    public WebDriver driver;
    public static Properties properties;
    public static ArrayList<NamesDto> names;

    @BeforeClass
    public static void beforeTest() {
        properties = getEnvironmentVariables("src/test/resources/application.properties");
        System.out.println(properties);

        names = ReadCSV.readNamesFromCSV("src/test/resources/names.csv");

        System.out.println(names);
    }

    @Test
    public void googleTest() throws InterruptedException {

        for(int i = 1; i < names.size(); i++) {
            System.setProperty("webdriver.chrome.driver","src/test/resources/chromedriver.exe");
            driver = new ChromeDriver();

            // Maximize google chrome window
            driver.manage().window().maximize();

            // Navigate to Google search web page
            driver.navigate().to(properties.getProperty("url"));

            // Enter search word in the Google search text area
            driver.findElement(By.xpath(properties.getProperty("searchBox"))).sendKeys(names.get(i).getFirstName() + " " + names.get(i).getLastName());

            // Click on the html body to clear auto suggestions
            driver.findElement(By.xpath(properties.getProperty("htmlBody"))).click();
            Thread.sleep(3000);

            // Click on the search button
            driver.findElement(By.xpath(properties.getProperty("searchBtn"))).click();
            //Thread.sleep(3000);
        }

        // Close google window when test is complete
        Thread.sleep(5000);
        driver.close();
    }

    @After
    public void afterTest() {
        System.out.println("After.....");
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
