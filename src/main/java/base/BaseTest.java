package base;

import com.demo.seleniumspring.util.AppConstant;
import com.demo.seleniumspring.util.ExtentManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseTest {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return driver.get();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");

        driver.set(new ChromeDriver(options));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        ExtentManager.getTest().info("Starting Chrome browser");
        getDriver().get(AppConstant.URL);
        ExtentManager.getTest().info("Navigated to URL: " + AppConstant.URL);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            ExtentManager.getTest().info("Browser closed");
            driver.remove();
        }
    }

    // ✅ Screenshot method for TestListener
    public String takeScreenshot(String methodName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String screenshotPath = System.getProperty("user.dir") +
                "/reports/screenshots/" + methodName + "_" + timestamp + ".png";

        File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);

        try {
            FileUtils.copyFile(srcFile, new File(screenshotPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return screenshotPath;
    }
}
