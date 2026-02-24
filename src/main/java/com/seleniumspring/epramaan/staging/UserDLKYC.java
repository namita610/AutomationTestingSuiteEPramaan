package com.seleniumspring.epramaan.staging;

import java.time.Duration;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import com.demo.seleniumspring.util.AppConstant;

public class UserDLKYC {

    @Test
    public void UserDLKYCTest() {
        // Set up ChromeDriver
        System.setProperty(AppConstant.EPRAMAAN_staging_Driver, AppConstant.EPRAMAAN_DPATH_staging);
        System.out.println("Running Selenium login test...");

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            driver.manage().window().maximize();
            driver.get(AppConstant.URL_Staging);

            // Enter Username
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user.username")));
            usernameField.clear();
            usernameField.sendKeys(AppConstant.Username_staging);

            // Enter Password
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user.password")));
            passwordField.clear();
            passwordField.sendKeys(AppConstant.Password_staging);

            // Click consent checkbox
            WebElement consentCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.id("ssoConsent1")));
            consentCheckbox.click();

            // Submit login
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("submit")));
            signInButton.click();
            System.out.println("Login submitted successfully.");

         // Navigate to Manage Profile ➜ View Profile
            WebElement manageProfile = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(@class,'dropdown-toggle') and contains(text(),'Manage Profile')]")));
            manageProfile.click();

            WebElement viewProfile = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id=\"navbarSupportedContent\"]/ul[1]/li[3]/ul/li[1]/a")));
            viewProfile.click();

            // ✅ Check OTP flooding block message
            try {
                WebElement warningMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[contains(text(),'OTP flooding detected')]")));

                if (warningMsg.isDisplayed()) {
                    System.out.println("⚠ OTP flooding message displayed. Waiting for 60 seconds...");
                    Thread.sleep(60000);

                    // Click regenerate OTP
                    WebElement regenBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("regenerateOtpBtn")));
                    regenBtn.click();
                    System.out.println("🔄 Regenerate OTP clicked.");
                }

            } catch (Exception ex) {
                System.out.println("✅ No OTP flooding message. Proceeding...");
            }

            // ✅ Prompt to enter OTP
            String otp = JOptionPane.showInputDialog("Enter the OTP received:");
            WebElement otpField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("otp")));
            otpField.clear();
            otpField.sendKeys(otp);
            System.out.println("✅ OTP entered.");

			/*
			 * // ✅ Click Verify OTP WebElement verifyBtn =
			 * wait.until(ExpectedConditions.elementToBeClickable(By.id("verifyOtpBtn")));
			 * verifyBtn.click(); System.out.println("✅ OTP submitted successfully.");
			 * 
			 * Thread.sleep(5000);
			 */

            Thread.sleep(3000);
            
            WebElement verifyBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("verifyOtpBtn")));
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", verifyBtn);
            Thread.sleep(500);
            verifyBtn.click();
            
            // Click DL button using JS (if needed)
            WebElement dlButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("DrivingLicence")));
            js.executeScript("arguments[0].click();", dlButton);

            // Optional: Wait for modal/popup to load, or skip this if not required
            Thread.sleep(3000);
            

            // Enter DL number
            WebElement dlNumberField = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("drivingLicenceIdProof.idValue")));
            dlNumberField.clear();
            dlNumberField.sendKeys(AppConstant.DLNo_staging);

            Thread.sleep(1000);

            // Click Verify button
            WebElement verifyButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("drivingLicenceReqest")));
            verifyButton.click();

            Thread.sleep(1000); // Wait for result (add assertion or verification here if needed)

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                Thread.sleep(3000); // Final wait before closing
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver.quit(); // Always quit the browser
        }
    }
}
