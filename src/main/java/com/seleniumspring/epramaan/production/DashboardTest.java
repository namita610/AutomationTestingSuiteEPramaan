package com.seleniumspring.epramaan.production;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.demo.seleniumspring.util.AppConstant;

public class DashboardTest {

    @Test
    public void loginTest() {
        // ✅ Setup ChromeDriver
        System.setProperty(AppConstant.EPRAMAAN_Driver, AppConstant.EPRAMAAN_DPATH);
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            driver.manage().window().maximize();
            driver.get(AppConstant.URL);
            System.out.println("Navigated to: " + AppConstant.URL);

         // ✅ Username input
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user.username")));
            usernameField.clear();
            usernameField.sendKeys(AppConstant.Username);
            Thread.sleep(500);
            Assert.assertEquals(usernameField.getAttribute("value"), AppConstant.Username, "Username input failed");

            // ✅ Password input
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user.password")));
            passwordField.clear();
            passwordField.sendKeys(AppConstant.Password);
            Thread.sleep(500);
            Assert.assertEquals(passwordField.getAttribute("value"), AppConstant.Password, "Password input failed");

            // ✅ Consent checkbox
            WebElement consentCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.id("ssoConsent1")));
            consentCheckbox.click();
            Thread.sleep(300);
            Assert.assertTrue(consentCheckbox.isSelected(), "Consent checkbox was not selected");

            // ✅ Submit login
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("submit")));
            signInButton.click();
            System.out.println("✅ Login submitted for user: " + AppConstant.Username);

            // Optionally verify login by checking for some dashboard element
            WebElement dashboardIndicator = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("someDashboardElement"))); // replace with actual ID
            Assert.assertTrue(dashboardIndicator.isDisplayed(), "Login may have failed - dashboard not visible");

            // ✅ Logout flow
            WebElement userMenuButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id='navbarSupportedContent']/ul[2]/li/button/i")));
            userMenuButton.click();
            Assert.assertTrue(userMenuButton.isDisplayed(), "Logout menu button was not clicked properly");


            WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='submit' and @formaction='/logout']")));
            logoutButton.click();

            System.out.println("Logout completed for user: " + AppConstant.Username);

        } catch (Exception e) {
            System.err.println("Error during login test: " + e.getMessage());
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
