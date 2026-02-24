package com.seleniumspring.epramaan.staging;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.demo.seleniumspring.util.AppConstant;

import javax.swing.JOptionPane;
import java.time.Duration;

public class ForgotPasswordPan {

    @Test
    public void ForgotPasswordPanTest() {
        System.setProperty(AppConstant.EPRAMAAN_staging_Driver, AppConstant.EPRAMAAN_DPATH_staging);
        System.out.println("Running Selenium Forgot Password");

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            driver.manage().window().maximize();
            driver.get(AppConstant.EPPRAMAAN_ForgotPassword_staging);

            // Page Load
            Assert.assertTrue(driver.getTitle().contains("Forgot Password"), "Forgot Password page not loaded");

            // Select Type Dropdown
            WebElement selectTypeDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("selectType")));
            Assert.assertNotNull(selectTypeDropdown, "Select Type dropdown not found");
            new Select(selectTypeDropdown).selectByValue("pan");

            // PAN Field
            WebElement idField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user.otherTypeNumber")));
            Assert.assertNotNull(idField, "PAN input field not found");
            idField.sendKeys(AppConstant.PanNo_staging);

            // DOB Span
            WebElement dobSpan = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Date of Birth ']")));
            Assert.assertTrue(dobSpan.isDisplayed(), "DOB span not clickable");
            dobSpan.click();

            // Username Dropdown
            WebElement selectUserDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("selectUser")));
            Assert.assertNotNull(selectUserDropdown, "Select User dropdown not found");
            wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("selectUser"), AppConstant.Username_staging));
            new Select(selectUserDropdown).selectByValue(AppConstant.Username_staging);

            // CAPTCHA
            if (driver.findElements(By.id("j_captcha_response")).size() > 0) {
                String captcha = JOptionPane.showInputDialog("Enter CAPTCHA shown on the screen:");
                Assert.assertNotNull(captcha, "Captcha input missing");
                driver.findElement(By.id("j_captcha_response")).sendKeys(captcha);
            }

            // Submit Button
            WebElement submitButton = driver.findElement(By.id("submitButton"));
            Assert.assertTrue(submitButton.isDisplayed(), "Submit button not found");
            submitButton.click();

            // OTP
            if (driver.findElements(By.id("otp")).size() > 0) {
                String otp = JOptionPane.showInputDialog("Enter OTP");
                Assert.assertNotNull(otp, "OTP input missing");
                driver.findElement(By.id("otp")).sendKeys(otp);
            }

            // Verify Button
            WebElement verifyButton = driver.findElement(By.xpath("//input[@type='submit' and @value='Verify']"));
            Assert.assertTrue(verifyButton.isDisplayed(), "Verify button not found");
            verifyButton.click();

            // Password Fields
            WebElement passwordField = driver.findElement(By.id("password"));
            Assert.assertNotNull(passwordField, "Password field not found");
            passwordField.sendKeys("Admin@123");

            WebElement confirmPasswordField = driver.findElement(By.id("confirmPassword"));
            Assert.assertNotNull(confirmPasswordField, "Confirm Password field not found");
            confirmPasswordField.sendKeys("Admin@123");

            // Second CAPTCHA if present
            if (driver.findElements(By.id("j_captcha_response")).size() > 0) {
                String captcha2 = JOptionPane.showInputDialog("Enter CAPTCHA shown on the screen:");
                Assert.assertNotNull(captcha2, "Second CAPTCHA input missing");
                driver.findElement(By.id("j_captcha_response")).sendKeys(captcha2);
            }

            // Final Submit
            WebElement submitButton1 = driver.findElement(By.xpath("//input[@type='submit' and @value='Submit']"));
            Assert.assertTrue(submitButton1.isDisplayed(), "Final Submit button not found");
            submitButton1.click();

            // Success Message Validation
            WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("successMessage")));
            Assert.assertTrue(successMsg.getText().contains("Password reset successfully"),
                    "Password reset failed or success message not displayed");

        } catch (Exception e) {
            Assert.fail("Test failed due to exception: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }
}
