package com.seleniumspring.epramaan.production;

import java.time.Duration;
import javax.swing.JOptionPane;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.demo.seleniumspring.util.AppConstant;

public class UserAadharKYC {

    @Test
    public void UserAadharKYCTest() {
        System.setProperty(AppConstant.EPRAMAAN_Driver, AppConstant.EPRAMAAN_DPATH);
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            driver.manage().window().maximize();
            driver.get(AppConstant.URL);

            // Username
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user.username")));
            usernameField.clear();
            usernameField.sendKeys(AppConstant.Username);

            // Password
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user.password")));
            passwordField.clear();
            passwordField.sendKeys(AppConstant.Password);

            // Consent checkbox
            WebElement consentCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.id("ssoConsent1")));
            consentCheckbox.click();

            // Click Login Button
            WebElement signInButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("submit")));
            try {
                wait.until(ExpectedConditions.elementToBeClickable(signInButton)).click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", signInButton);
            }
            System.out.println("✅ Login submitted successfully.");

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

            // Aadhaar KYC
            WebElement aadharButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("Aadhaar")));
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", aadharButton);
            Thread.sleep(500);
            js.executeScript("arguments[0].click();", aadharButton);

            WebElement otpRadioButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("otpMedium1")));
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", otpRadioButton);
            Thread.sleep(500);
            if (!otpRadioButton.isSelected()) {
                otpRadioButton.click();
            }
            Assert.assertTrue(otpRadioButton.isSelected(), "OTP medium radio not selected!");

            Thread.sleep(1000);
            
            // Explicit scroll to consent radio after otpRadioButton is selected
            WebElement consentRadio = wait.until(ExpectedConditions.elementToBeClickable(By.id("ekycConsentAgree")));
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", consentRadio);
            Thread.sleep(500);
            js.executeScript("arguments[0].click();", consentRadio);

            Boolean isSelected = (Boolean) js.executeScript("return arguments[0].checked;", consentRadio);
            Assert.assertTrue(isSelected, "Consent radio not selected!");
            System.out.println("eKYC consent radio selected.");

            String captchaInput = JOptionPane.showInputDialog(null, "Enter Captcha:");
            Assert.assertNotNull(captchaInput, "Captcha input cancelled!");

            Thread.sleep(1000);
            
            WebElement captchaField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("j_captcha_response")));
            captchaField.clear();
            captchaField.sendKeys(captchaInput);
            Assert.assertEquals(captchaField.getAttribute("value"), captchaInput, "Captcha not entered correctly!");

            WebElement verifyCaptchaButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("aadhaarOTPRequest")));
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", verifyCaptchaButton);
            Thread.sleep(500);
            js.executeScript("arguments[0].click();", verifyCaptchaButton);

			/*
			 * otp = OtpFetcher.fetchLatestOtp(); Assert.assertNotNull(otp,
			 * "Aadhaar OTP not fetched!"); System.out.println("Fetched Aadhaar OTP: " +
			 * otp);
			 * 
			 * WebElement otpField; try { otpField =
			 * driver.findElement(By.id("aadhaarOTPForKYC")); } catch
			 * (NoSuchElementException e) { otpField = driver.findElement(By.id("otp")); }
			 * otpField.clear(); otpField.sendKeys(otp);
			 * Assert.assertEquals(otpField.getAttribute("value"), otp,
			 * "Aadhaar OTP not entered correctly!");
			 */

            String otpforkyc = JOptionPane.showInputDialog("Enter the OTP received:");
            driver.findElement(By.id("aadhaarOTPForKYC")).sendKeys(otpforkyc);
            Thread.sleep(2000);
            
			/*
			 * WebElement verifyOtp =
			 * wait.until(ExpectedConditions.elementToBeClickable(By.id("aadhaarVerifyOTP"))
			 * ); js.executeScript("arguments[0].scrollIntoView({block: 'center'});",
			 * verifyOtp); Thread.sleep(2000);
			 * 
			 * js.executeScript("arguments[0].click();", verifyOtp);
			 */
            
            
           

            WebElement verifyOtp = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("aadhaarVerifyOTP"))
            );

            verifyOtp.click();

           Thread.sleep(2000);


            System.out.println("Aadhaar KYC OTP submitted successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Test failed due to exception: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }
}
