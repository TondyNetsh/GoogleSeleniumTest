package selenium;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.util.Properties;

public class GoogleTest {

    public WebDriver driver;
    public static Properties properties;

    @BeforeClass
    public static void beforeTest() {
        properties = getEnvirnmentVariables("src/test/resources/application.properties");
        System.out.println(properties);
    }

    @Test
    public void googleTest() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver","src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();

        // Navigate to Google search web page
        driver.navigate().to(properties.getProperty("url"));

        // Maximize google chrome window
        driver.manage().window().maximize();

        // Enter search word in the google search text area
        driver.findElement(By.xpath("/html/body/div[1]/div[3]/form/div[1]/div[1]/div[1]/div/div[2]/input")).sendKeys(properties.getProperty("name"));
        driver.findElement(By.xpath("/html/body/div[1]/div[3]/form/div[1]/div[1]/div[1]/div/div[2]/input")).sendKeys(properties.getProperty("surname"));

        // Click on the search button
        driver.findElement(By.xpath("/html/body/div[1]/div[3]/form/div[1]/div[1]/div[3]/center/input[1]")).click();
        Thread.sleep(3000);

        // Close google window when test is complete
        driver.close();

    }

    public static Properties getEnvirnmentVariables(String path) {
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
