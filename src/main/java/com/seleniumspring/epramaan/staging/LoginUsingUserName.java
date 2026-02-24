package com.seleniumspring.epramaan.staging;

import java.time.Duration;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.demo.seleniumspring.util.AppConstant;

public class LoginUsingUserName {

    @DataProvider(name = "userCredentials")
    public Object[][] getUserCredentials() {
        return new Object[][] {
            {AppConstant.Username_staging, AppConstant.Password_staging}, // Correct credentials
            //{"rohini1974", "Admin@1234"}, // Wrong credentials
            //{"Namita", "Admin@1234"}      // Wrong credentials
        };
    }

    @Test(dataProvider = "userCredentials")
    public void LoginUsingUserNameTest(String username, String password) {
        System.setProperty(AppConstant.EPRAMAAN_staging_Driver, AppConstant.EPRAMAAN_DPATH_staging);
        System.out.println("Starting login test for user: " + username);

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            driver.manage().window().maximize();
            driver.get(AppConstant.URL_Staging);

            // Enter username
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user.username")));
            
            usernameField.clear();
            usernameField.sendKeys(username);
            Assert.assertEquals(usernameField.getAttribute("value"), username, " Username not entered correctly!");
            
            Thread.sleep(3000);

            // Enter password
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user.password")));
            passwordField.clear();
            passwordField.sendKeys(password);
            Assert.assertEquals(passwordField.getAttribute("value"), password, "Password not entered correctly!");
            
            Thread.sleep(3000);

            // Tick consent checkbox
            WebElement consentCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.id("ssoConsent1")));
            if (!consentCheckbox.isSelected()) {
                js.executeScript("arguments[0].click();", consentCheckbox);
            }
            // Re-locate after JS click to avoid stale state
            WebElement consentCheckboxAfter = wait.until(ExpectedConditions.elementToBeClickable(By.id("ssoConsent1")));
            Assert.assertTrue(consentCheckboxAfter.isSelected(), "Consent checkbox was not selected!");

            // Click Sign In
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("submit")));
            js.executeScript("arguments[0].click();", signInButton);
            System.out.println(" Login submitted for user: " + username);

            // Check if error message is displayed
            boolean loginFailed = false;
            try {
                WebElement errorMsg = wait.withTimeout(Duration.ofSeconds(3))
                        .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error, .alert-danger")));
                System.out.println("Login failed for user: " + username + " | Error: " + errorMsg.getText());
                loginFailed = true;
                Assert.assertTrue(errorMsg.isDisplayed(), "Error message should be visible but is not!");
            } catch (TimeoutException te) {
                System.out.println("Login successful for user: " + username);
                // Assert that some post-login element is visible (like user menu)
                WebElement userMenuButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//*[@id='navbarSupportedContent']/ul[2]/li/button/i")));
                Assert.assertTrue(userMenuButton.isDisplayed(), "User menu button not visible after login!");
            }

            // Logout only if login succeeded
            if (!loginFailed) {
                WebElement userMenuButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//*[@id='navbarSupportedContent']/ul[2]/li/button/i")));
                userMenuButton.click();

                WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[@type='submit' and @formaction='/logout']")));
                logoutButton.click();

				/*
				 * // Verify logout successful → login page visible again WebElement
				 * loginPageUsername =
				 * wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(
				 * "user.username"))); Assert.assertTrue(loginPageUsername.isDisplayed(),
				 * " Logout failed – login page not displayed again!");
				 * System.out.println(" Logout completed for user: " + username);
				 */
            }

        } catch (Exception e) {
            System.err.println(" Unexpected error for user: " + username + " → " + e.getMessage());
            Assert.fail("Test failed due to unexpected exception: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }
}
