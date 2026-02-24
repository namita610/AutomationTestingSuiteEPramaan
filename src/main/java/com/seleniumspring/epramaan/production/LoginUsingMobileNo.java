package com.seleniumspring.epramaan.production;

import java.time.Duration;
import java.util.List;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import com.demo.seleniumspring.util.AppConstant;

public class LoginUsingMobileNo {

    @Test
    public void LoginUsingMobileNoTest() {
        System.setProperty(AppConstant.EPRAMAAN_DPATH, AppConstant.EPRAMAAN_DPATH);
        System.out.println("Running Selenium mobile login test...");

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            driver.manage().window().maximize();
            driver.manage().deleteAllCookies();
            driver.get(AppConstant.URL);

            // Click on "Mobile" tab
            WebElement mobileTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("mobilebtn")));
            mobileTab.click();
            
            Thread.sleep(1000);

            // Enter Mobile Number
            WebElement mobileField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user.mobileNumber")));
            mobileField.clear();
            mobileField.sendKeys(AppConstant.MobileNo);
            
            Thread.sleep(1000);
            

            // Click Consent Checkbox
            WebElement consentCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.id("ssoConsent1")));
            if (!consentCheckbox.isSelected()) {
                consentCheckbox.click();
            }

            Thread.sleep(1000);
            
            // ✅ Fetch OTP using Appium
			/*
			 * String otp = OtpFetcher.fetchLatestOtp(); System.out.println("Fetched OTP: "
			 * + otp);
			 */
            
            
            Thread.sleep(2000);

            // Enter OTP only if it's valid
			/*
			 * if (!otp.equalsIgnoreCase("no otp")) { WebElement otpField =
			 * wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user.otp")));
			 * otpField.clear(); otpField.sendKeys(otp); } else { throw new
			 * RuntimeException("❌ OTP not received or extracted. Check device or regex.");
			 * }
			 */

            String otp = JOptionPane.showInputDialog("Enter the OTP received:");
            driver.findElement(By.id("user.otp")).sendKeys(otp);
            //driver.findElement(By.id("verifyOtpButton")).click();
            
            
            Thread.sleep(3000);
            
            // Enter Password
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user.password")));
            passwordField.clear();
            passwordField.sendKeys(AppConstant.Password);
            
            
           Thread.sleep(1000);

            // Wait for username dropdown to populate
            wait.until(driver1 -> {
                List<WebElement> options = driver1.findElements(By.cssSelector("#selectUser option"));
                return options.size() > 1;
            });

            
            Thread.sleep(1000);
            // Select first available username
            Select usernameDropdown = new Select(driver.findElement(By.id("selectUser")));
            usernameDropdown.selectByVisibleText("namita610");

            Thread.sleep(1000);
            
            // Consent
            WebElement consentCheckbox1 = wait.until(ExpectedConditions.elementToBeClickable(By.id("ssoConsent1")));
            if (!consentCheckbox1.isSelected()) {
                js.executeScript("arguments[0].click();", consentCheckbox);
            }

            Thread.sleep(1000);
            
            WebElement signInButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("submit")));
            js.executeScript("arguments[0].scrollIntoView(true);", signInButton);

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
         
            
            Thread.sleep(2000);
            
           

        } catch (Exception e) {
            System.out.println("❌ Exception occurred during login:");
            e.printStackTrace();
        } finally {
            try {
                Thread.sleep(3000); // Optional: allow UI to settle before quit
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver.quit();
        }
    }
}
