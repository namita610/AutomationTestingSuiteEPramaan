package com.seleniumspring.epramaan.staging;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.demo.seleniumspring.util.AppConstant;

import java.time.Duration;

public class TestLoginUsingAadhar {

    @Test
    public void LoginUsingAadharTest() {
        System.setProperty(AppConstant.EPRAMAAN_staging_Driver, AppConstant.EPRAMAAN_DPATH_staging);
        System.out.println("Running Selenium Aadhaar login test...");

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();

        try {
            // Step 1: Load Page
            driver.get(AppConstant.URL_Staging);
            System.out.println("Opened staging URL.");

            // Step 2: Click Aadhaar/PAN/DL tab
            WebElement othersTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("othersbtn")));
            othersTab.click();
            System.out.println("Clicked Aadhaar/PAN/DL tab.");

            // Step 3: Select Aadhaar from dropdown
            WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("selectType")));
            new Select(dropdown).selectByValue("aadhaarNumber");
            System.out.println("Selected Aadhaar in dropdown.");

            // Step 4: Enter Aadhaar number
            WebElement idNumberField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user.otherTypeNumber")));
            idNumberField.clear();
            idNumberField.sendKeys(AppConstant.AadharNo_staging);
            System.out.println("Aadhaar number entered.");

            // Step 5: Enter password
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user.password")));
            passwordField.clear();
            passwordField.sendKeys(AppConstant.Password_staging);
            System.out.println("Password entered.");

            // Step 6: Click Consent Checkbox (force click if blocked)
            WebElement consentCheckbox = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ssoConsent1")));
            js.executeScript("arguments[0].scrollIntoView(true);", consentCheckbox);
            try {
                if (!consentCheckbox.isSelected()) {
                    consentCheckbox.click();
                }
                System.out.println("Consent checkbox clicked.");
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", consentCheckbox);
                System.out.println("Consent checkbox clicked using JS fallback.");
            }

            // Step 7: Click Submit Button (force enable + click)
            WebElement signInButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("submit")));
            js.executeScript("arguments[0].scrollIntoView(true);", signInButton);

            // If disabled, remove attribute
            if (!signInButton.isEnabled()) {
                js.executeScript("arguments[0].removeAttribute('disabled');", signInButton);
            }

            try {
                signInButton.click();
                System.out.println("Login submitted with normal click.");
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", signInButton);
                System.out.println("Login submitted with JS click fallback.");
            }

            // Step 8: Post-click assertion (confirm login page opened)
            try {
                WebElement profileIcon = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//li[contains(@class,'dropdown') and contains(text(),'namita')]")));
                Assert.assertTrue(profileIcon.isDisplayed(), "Login failed - user profile not visible.");
                System.out.println("Login successful.");
            } catch (Exception e) {
                Assert.fail("Login failed - username/profile not visible.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Test crashed: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }
}
