package com.seleniumspring.epramaan.staging;

import java.time.Duration;
import javax.swing.JOptionPane;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.demo.seleniumspring.util.AppConstant;

public class UserAadharKYC_EnteringIncorrectCaptcha {

    @Test
    public void UserAadharKYCTest() {
        System.setProperty(AppConstant.EPRAMAAN_staging_Driver, AppConstant.EPRAMAAN_DPATH_staging);
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            driver.manage().window().maximize();
            driver.get(AppConstant.URL_Staging);

            // Login
            try {
                WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user.username")));
                usernameField.clear();
                usernameField.sendKeys(AppConstant.Username_staging);
            } catch (Exception e) {
                Assert.fail("Failed to enter username", e);
            }
            
           

            try {
                WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user.password")));
                passwordField.clear();
                passwordField.sendKeys(AppConstant.Password_staging);
            } catch (Exception e) {
                Assert.fail("Failed to enter password", e);
            }

            try {
                WebElement consentCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.id("ssoConsent1")));
                consentCheckbox.click();
            } catch (Exception e) {
                Assert.fail("Failed to click consent checkbox", e);
            }

            try {
                WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("submit")));
                signInButton.click();
                System.out.println("Login submitted successfully.");
            } catch (Exception e) {
                Assert.fail("Failed to click Sign In button", e);
            }

            Thread.sleep(1000);

            // Manage Profile
            try {
                WebElement manageProfile = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//a[contains(@class,'dropdown-toggle') and contains(text(),'Manage Profile')]")));
                manageProfile.click();
            } catch (Exception e) {
                Assert.fail("Failed to click 'Manage Profile'", e);
            }

            
            Thread.sleep(1000);
            
            try {
                WebElement editProfile = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//*[@id=\"navbarSupportedContent\"]/ul[1]/li[3]/ul/li[1]/a")));
                editProfile.click();
            } catch (Exception e) {
                Assert.fail("Failed to click 'Edit Profile'", e);
            }

            // OTP for login
            String otp;
            try {
                otp = OtpFetcher.fetchLatestOtp();
                System.out.println("Fetched OTP: " + otp);
                Assert.assertNotNull(otp, "OTP not fetched for login");
            } catch (Exception e) {
                Assert.fail("Failed to fetch OTP for login", e);
                return;
            }

            try {
                WebElement otpFieldLogin = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("otp")));
                otpFieldLogin.clear();
                otpFieldLogin.sendKeys(otp);
            } catch (Exception e) {
                Assert.fail("Failed to enter login OTP", e);
            }

            try {
                WebElement verifyBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("verifyOtpBtn")));
                verifyBtn.click();
            } catch (Exception e) {
                Assert.fail("Failed to click 'Verify OTP' button", e);
            }

            Thread.sleep(3000);

            // Aadhaar KYC
            try {
                WebElement aadharButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("Aadhaar")));
                js.executeScript("arguments[0].click();", aadharButton);
            } catch (Exception e) {
                Assert.fail("Failed to click 'Aadhaar KYC' button", e);
            }

            Thread.sleep(3000);

            // Select OTP medium
            try {
                WebElement otpRadioButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("otpMedium1")));
                if (!otpRadioButton.isSelected()) {
                    otpRadioButton.click();
                }
            } catch (Exception e) {
                Assert.fail("Failed to select OTP medium", e);
            }

            Thread.sleep(1000);

            // Consent radio
            WebElement radioButton;
            try {
                radioButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("ekycConsent1")));
                js.executeScript("arguments[0].scrollIntoView(true);", radioButton);
                Thread.sleep(500);
                js.executeScript("arguments[0].click();", radioButton);
            } catch (Exception e) {
                Assert.fail("Failed to click eKYC consent radio", e);
                return;
            }

            Thread.sleep(500);

            try {
                Boolean isSelected = (Boolean) js.executeScript("return arguments[0].checked;", radioButton);
                Assert.assertTrue(isSelected, "eKYC consent radio not selected");
                System.out.println("eKYC consent radio selected.");
            } catch (Exception e) {
                Assert.fail("Error verifying eKYC consent radio selection", e);
            }

            // Captcha (entered incorrectly on purpose)
            String captchaInput = JOptionPane.showInputDialog(null, "Enter Incorrect Captcha:");
            Assert.assertNotNull(captchaInput, "Captcha input cancelled by user");

            try {
                WebElement captchaField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("j_captcha_response")));
                js.executeScript("arguments[0].scrollIntoView(true);", captchaField);
                captchaField.clear();
                captchaField.sendKeys(captchaInput);
            } catch (Exception e) {
                Assert.fail("Failed to enter captcha", e);
            }

            try {
                WebElement verifyButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("aadhaarOTPRequest")));
                js.executeScript("arguments[0].click();", verifyButton);
            } catch (Exception e) {
                Assert.fail("Failed to click Aadhaar OTP Request button", e);
            }

            Thread.sleep(9000);  // You might see failure here due to incorrect captcha

            // Aadhaar OTP
            try {
                otp = OtpFetcher.fetchLatestOtp();
                System.out.println("Fetched Aadhaar OTP: " + otp);
                Assert.assertNotNull(otp, "Aadhaar OTP not fetched");
            } catch (Exception e) {
                Assert.fail("Exception fetching Aadhaar OTP", e);
                return;
            }

            WebElement otpField;
            try {
                otpField = driver.findElement(By.id("aadhaarOTPForKYC"));
            } catch (NoSuchElementException e1) {
                try {
                    otpField = driver.findElement(By.id("otp"));
                } catch (NoSuchElementException e2) {
                    Assert.fail("OTP field for Aadhaar KYC not found", e2);
                    return;
                }
            }

            try {
                otpField.clear();
                otpField.sendKeys(otp);
            } catch (Exception e) {
                Assert.fail("Failed to enter Aadhaar OTP", e);
            }

            try {
                WebElement verifyOtp = wait.until(ExpectedConditions.elementToBeClickable(By.id("aadhaarVerifyOTP")));
                js.executeScript("arguments[0].scrollIntoView(true);", verifyOtp);
                js.executeScript("arguments[0].click();", verifyOtp);
                System.out.println("✅ Aadhaar KYC OTP submitted successfully.");
            } catch (Exception e) {
                Assert.fail("Failed to click 'Verify Aadhaar OTP' button", e);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Test failed due to unhandled exception: " + e.getMessage());
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
