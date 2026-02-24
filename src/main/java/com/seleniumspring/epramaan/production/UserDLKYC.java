package com.seleniumspring.epramaan.production;

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
    public void userDLKYCTest() {
        System.setProperty(AppConstant.EPRAMAAN_Driver, AppConstant.EPRAMAAN_DPATH);
        System.out.println("Running Selenium login test...");

        WebDriver driver = new ChromeDriver();
        //WebDriverWait wait = new WebDriverWait(driver, 20); // Increased wait time
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            driver.manage().window().maximize();
            driver.get(AppConstant.URL);

            // Enter Username
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user.username")));
            usernameField.clear();
            usernameField.sendKeys(AppConstant.Username);

            // Enter Password
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user.password")));
            passwordField.clear();
            passwordField.sendKeys(AppConstant.Password);

            // Click consent checkbox
            WebElement consentCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.id("ssoConsent1")));
            consentCheckbox.click();

            // Submit login
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("submit")));
            signInButton.click();
            System.out.println("Login submitted successfully.");

            // Click "Manage Profile"
            WebElement manageProfile = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@class,'dropdown-toggle') and contains(text(),'Manage Profile')]")));
            manageProfile.click();

            // Click "View Profile"
            WebElement editProfile = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"navbarSupportedContent\"]/ul[1]/li[3]/ul/li[1]/a")));
            editProfile.click();

            // Prompt user for OTP
            String otp = JOptionPane.showInputDialog(null, "Please enter the OTP received:");
            WebElement otpField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("otp")));
            otpField.sendKeys(otp);

            WebElement verifyBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("verifyOtpBtn")));
            verifyBtn.click();

            // Wait after OTP verification
            Thread.sleep(3000);

            // Click DL button using JS (if needed)
            WebElement dlButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("DrivingLicence")));
            js.executeScript("arguments[0].click();", dlButton);

            // Optional: Wait for modal/popup to load, or skip this if not required
            Thread.sleep(3000);

            WebElement panNumberField = wait.until(ExpectedConditions.elementToBeClickable(By.id("drivingLicenceIdProof.idValue")));
            panNumberField.clear(); // Add this line
            panNumberField.sendKeys(AppConstant.DLNo);

            Thread.sleep(1000);
            
      
            
          
            WebElement verifyButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("drivingLicenceReqest")));
            verifyButton.click();

            Thread.sleep(1000); // Wait for result (can add verification later)

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver.quit();
        }
    }
}
