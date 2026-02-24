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
import org.testng.Assert;
import org.testng.annotations.Test;

import com.demo.seleniumspring.util.AppConstant;

public class UserPanKYC {

    @Test
    public void UserPanKYCTest() {
        System.setProperty(AppConstant.EPRAMAAN_staging_Driver, AppConstant.EPRAMAAN_DPATH_staging);
        System.out.println("Running Selenium login test...");

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            driver.manage().window().maximize();
            driver.get(AppConstant.URL_Staging);

            // ✅ Enter Username
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user.username")));
            Assert.assertTrue(usernameField.isDisplayed(), "Username field not visible!");
            usernameField.clear();
            usernameField.sendKeys(AppConstant.Username_staging);

            // ✅ Enter Password
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user.password")));
            Assert.assertTrue(passwordField.isDisplayed(), "Password field not visible!");
            passwordField.clear();
            passwordField.sendKeys(AppConstant.Password_staging);

            // ✅ Click consent checkbox
            WebElement consentCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.id("ssoConsent1")));
            consentCheckbox.click();
            Assert.assertTrue(consentCheckbox.isSelected(), "Consent checkbox not selected!");

            // ✅ Submit login
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("submit")));
            signInButton.click();
            System.out.println("Login submitted successfully.");

            Thread.sleep(1000);;
            
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
            
            Thread.sleep(1000);
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
            
            Thread.sleep(2000);

            // ✅ Click PAN button
            WebElement panButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("PAN")));
            js.executeScript("arguments[0].click();", panButton);
            Assert.assertTrue(panButton.isDisplayed(), "PAN button not clickable!");

            // ✅ Enter PAN Number
            WebElement panNumberField = wait.until(ExpectedConditions.elementToBeClickable(By.id("panIdProof.idValue")));
            panNumberField.clear();
            panNumberField.sendKeys(AppConstant.PanNo_staging);
            Assert.assertEquals(panNumberField.getAttribute("value"), AppConstant.PanNo_staging, "PAN number not entered correctly!");

            // ✅ Enter Name as in PAN
            WebElement nameField = wait.until(ExpectedConditions.elementToBeClickable(By.id("name_as_in_pan")));
            nameField.clear();
            nameField.sendKeys(AppConstant.Name_as_in_Pan_staging);
            Assert.assertEquals(nameField.getAttribute("value"), AppConstant.Name_as_in_Pan_staging, "Name not entered correctly!");

            // ✅ Submit for verification
            WebElement verifyButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("panReqest")));
            verifyButton.click();
            System.out.println("PAN verification submitted successfully.");

            // You can add one more assert to verify success message if available
            // Example: 
            // WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("successMessageId")));
            // Assert.assertTrue(successMsg.isDisplayed(), "PAN verification failed!");

        } catch (Exception e) {
            Assert.fail("Test failed due to exception: " + e.getMessage(), e);
        } finally {
            driver.quit();
        }
    }
}
