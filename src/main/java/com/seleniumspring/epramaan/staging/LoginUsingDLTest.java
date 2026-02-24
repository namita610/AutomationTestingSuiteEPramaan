package com.seleniumspring.epramaan.staging;

import java.time.Duration;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;

import com.demo.seleniumspring.util.AppConstant;

public class LoginUsingDLTest {

    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;
    Actions actions;

    @BeforeMethod
    public void setUp() {
        System.setProperty(AppConstant.EPRAMAAN_Driver, AppConstant.EPRAMAAN_DPATH);
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // ✅ corrected: removed local variable
        js = (JavascriptExecutor) driver;
        actions = new Actions(driver);
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
    }

    @Test
    public void LoginUsingDL() {
        try {
            driver.get(AppConstant.URL_Staging);
            System.out.println("▶ Starting DL login test...");

            wait.until(ExpectedConditions.elementToBeClickable(By.id("othersbtn"))).click();
            Thread.sleep(1000);

            WebElement typeDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("selectType")));
            new Select(typeDropdown).selectByValue("dlNumber");
            Thread.sleep(1000);

            WebElement idNumberField = driver.findElement(By.id("user.otherTypeNumber"));
            idNumberField.clear();
            idNumberField.sendKeys(AppConstant.DLNo_staging);
            idNumberField.sendKeys(Keys.TAB);
            Thread.sleep(2000);

            // Enter Password
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user.password")));
            passwordField.clear();
            passwordField.sendKeys(AppConstant.Password_staging);

			/*
			 * // Consent Checkbox WebElement consentCheckbox =
			 * wait.until(ExpectedConditions.elementToBeClickable(By.id("ssoConsent1"))); if
			 * (!consentCheckbox.isSelected()) { consentCheckbox.click(); }
			 */

         // Click Consent Checkbox
            WebElement consentCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.id("ssoConsent1")));
            if (!consentCheckbox.isSelected()) {
                consentCheckbox.click();
            }

            // Click Sign In
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("submit")));
            signInButton.click();

            System.out.println("Login with mobile and OTP submitted successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                Thread.sleep(10000); // Optional pause to see result before closing
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver.quit();
        }
    }
}
