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
import org.testng.Assert;
import org.testng.annotations.Test;

import com.demo.seleniumspring.util.AppConstant;

public class ViewProfile {

    @Test
    public void ViewProfileTest() {
        System.setProperty(AppConstant.EPRAMAAN_Driver, AppConstant.EPRAMAAN_DPATH);
        System.out.println("Running Selenium login test...");

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
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

            // ✅ Click Verify OTP
            WebElement verifyBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("verifyOtpBtn")));
            verifyBtn.click();
            System.out.println("✅ OTP submitted successfully.");

            Thread.sleep(5000);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("❌ Test failed due to unexpected exception: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }
}
