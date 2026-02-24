package com.seleniumspring.epramaan.production;

import java.time.Duration;
import javax.swing.JOptionPane;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.demo.seleniumspring.util.AppConstant;

public class UserAadharKYC_ForIncorrectOtp {

    @Test
    public void UserAadharKYCTest() {
        System.setProperty(AppConstant.EPRAMAAN_Driver, AppConstant.EPRAMAAN_DPATH);
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            driver.manage().window().maximize();
            driver.get(AppConstant.URL);

            // Login
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user.username")));
            usernameField.clear();
            usernameField.sendKeys(AppConstant.Username);

            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user.password")));
            passwordField.clear();
            passwordField.sendKeys(AppConstant.Password);

            WebElement consentCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.id("ssoConsent1")));
            consentCheckbox.click();

            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("submit")));
            signInButton.click();
            System.out.println("Login submitted successfully.");
            
            Thread.sleep(1000);

            // Manage Profile
            WebElement manageProfile = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(@class,'dropdown-toggle') and contains(text(),'Manage Profile')]")));
            manageProfile.click();

            WebElement editProfile = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id=\"navbarSupportedContent\"]/ul[1]/li[3]/ul/li[1]/a")));
            editProfile.click();

            // OTP for login
            String otp = OtpFetcher.fetchLatestOtp();
            System.out.println("Fetched OTP: " + otp);
           // Assert.assertNotNull(otp, "❌ OTP not fetched for login");

            WebElement otpFieldLogin = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("otp")));
            otpFieldLogin.clear();
            otpFieldLogin.sendKeys(otp);

            WebElement verifyBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("verifyOtpBtn")));
            verifyBtn.click();
            Thread.sleep(3000);

            // Aadhaar KYC
            WebElement aadharButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("Aadhaar")));
            js.executeScript("arguments[0].click();", aadharButton);
            Thread.sleep(3000);

            // Select OTP medium
            WebElement otpRadioButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("otpMedium1")));
            if (!otpRadioButton.isSelected()) {
                otpRadioButton.click();
            }
            Thread.sleep(1000);

            // Consent radio
            WebElement radioButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("ekycConsent1")));
            js.executeScript("arguments[0].scrollIntoView(true);", radioButton);
            Thread.sleep(500);
            try {
                js.executeScript("arguments[0].click();", radioButton);
            } catch (Exception e) {
                Assert.fail("Failed to click eKYC consent radio", e);
            }

            Thread.sleep(500);
            Boolean isSelected = (Boolean) js.executeScript("return arguments[0].checked;", radioButton);
            Assert.assertTrue(isSelected, "Failed to select eKYC consent radio");
            System.out.println("eKYC consent radio selected.");

            // Captcha
            String captchaInput = JOptionPane.showInputDialog(null, "Enter Captcha:");
            Assert.assertNotNull(captchaInput, "Captcha input cancelled");

            WebElement captchaField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("j_captcha_response")));
            js.executeScript("arguments[0].scrollIntoView(true);", captchaField);
            Thread.sleep(500);
            captchaField.clear();
            captchaField.sendKeys(captchaInput);

            WebElement verifyButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("aadhaarOTPRequest")));
            js.executeScript("arguments[0].scrollIntoView(true);", verifyButton);
            Thread.sleep(500);
            js.executeScript("arguments[0].click();", verifyButton);
            Thread.sleep(9000);

            // Aadhaar OTP
            otp = OtpFetcher.fetchLatestOtp();
            System.out.println("Fetched Aadhaar OTP: " + otp);
            Assert.assertNotNull(otp, "Aadhaar OTP not fetched");

            WebElement otpField;
            try {
                otpField = driver.findElement(By.id("aadhaarOTPForKYC"));
            } catch (NoSuchElementException e) {
                otpField = driver.findElement(By.id("otp"));
            }

            otpField.clear();
            otpField.sendKeys(otp);

            WebElement verifyOtp = wait.until(ExpectedConditions.elementToBeClickable(By.id("aadhaarVerifyOTP")));
            js.executeScript("arguments[0].scrollIntoView(true);", verifyOtp);
            Thread.sleep(500);
            js.executeScript("arguments[0].click();", verifyOtp);

            Thread.sleep(1000);
            System.out.println("Aadhaar KYC OTP submitted successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Test failed due to exception: " + e.getMessage());
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
