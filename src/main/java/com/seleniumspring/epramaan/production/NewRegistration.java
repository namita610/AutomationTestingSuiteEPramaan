package com.seleniumspring.epramaan.production;

import java.time.Duration;

import javax.swing.JOptionPane;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.demo.seleniumspring.util.AppConstant;

public class NewRegistration {

    @Test
    public void NewRegistrationTest() {
        // Setup WebDriver and WebDriverWait
        System.setProperty(AppConstant.EPRAMAAN_staging_Driver, AppConstant.EPRAMAAN_DPATH_staging);
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            // Step 1: Open the registration page and maximize window
            driver.manage().window().maximize();
            driver.get(AppConstant.EPRAMAAN_NewRegistrationURL_staging);

            // Step 2: Enter mobile number
            WebElement mobileField = wait.until(ExpectedConditions.elementToBeClickable(By.id("verifiedMobileNumber")));
            Assert.assertTrue(mobileField.isDisplayed(), "Mobile number input not visible.");
            mobileField.sendKeys(AppConstant.MobileNo_staging);
            
            Thread.sleep(1000);
            // Step 3: Click on Generate OTP
            WebElement generateOtpBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("generateOtp")));
            js.executeScript("arguments[0].scrollIntoView(true);", generateOtpBtn);
            generateOtpBtn.click();
            System.out.println("Clicked on Generate OTP.");
            
            Thread.sleep(1000);  
            
            // Step 4: Wait for OTP (using WebDriverWait for better synchronization)
			/*
			 * WebElement otpField =
			 * wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user.otp")));
			 * String otp = OtpFetcher.fetchLatestOtp(); Assert.assertNotNull(otp,
			 * "OTP not fetched."); System.out.println("Fetched OTP: " + otp);
			 */
            String otp = JOptionPane.showInputDialog("Enter the OTP received:");

         // Locate OTP field
         WebElement otpField = driver.findElement(By.id("user.otp"));

         try {
             otpField.clear(); 
         } catch (InvalidElementStateException e) {
             js.executeScript("arguments[0].value = '';", otpField);
         }

         otpField.sendKeys(otp);

            Thread.sleep(3000);
            
            WebElement verifyOtpBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("verifyOTP")));
            js.executeScript("arguments[0].scrollIntoView(true);", verifyOtpBtn);
            verifyOtpBtn.click();
            System.out.println("OTP verification triggered.");
            Thread.sleep(3000);
            // Step 6: Wait for form fields to become editable after OTP verification
            wait.until(ExpectedConditions.not(ExpectedConditions.attributeContains(By.id("givenName"), "readonly", "readonly")));

            // Step 7: Fill out the registration form
            driver.findElement(By.id("givenName")).sendKeys("Namita Jikamade");
            Thread.sleep(1000);
            new Select(driver.findElement(By.id("gender"))).selectByValue("F");
            Thread.sleep(1000);
            driver.findElement(By.id("date")).sendKeys("06");
            Thread.sleep(1000);
            driver.findElement(By.id("month")).sendKeys("October");
            Thread.sleep(1000);
            driver.findElement(By.id("year")).sendKeys("1997");
            Thread.sleep(1000);
            driver.findElement(By.id("personalMessage")).sendKeys("Hello Namita");
            Thread.sleep(1000);
            driver.findElement(By.id("username")).sendKeys("namita_staging5");
            Thread.sleep(1000);
            driver.findElement(By.id("password")).sendKeys("Test@12345");
            Thread.sleep(1000);
            driver.findElement(By.id("confirmPassword")).sendKeys("Test@12345");
            Thread.sleep(1000);

            // Step 8: Accept terms and conditions, then submit
            WebElement termsCheckbox = driver.findElement(By.id("agreeTC1"));
            if (!termsCheckbox.isSelected()) {
                js.executeScript("arguments[0].click();", termsCheckbox);
            }
            Thread.sleep(1000);
            WebElement signUpBtn = driver.findElement(By.id("submitBtn"));
            js.executeScript("arguments[0].removeAttribute('disabled');", signUpBtn); // Enable the button if disabled
            js.executeScript("arguments[0].click();", signUpBtn);
            System.out.println("Submitted registration form.");

            // Optional: Validate success message or page navigation
            // wait.until(ExpectedConditions.urlContains("success"));
            // Assert.assertTrue(driver.getPageSource().contains("Registration successful"));

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Test failed due to exception: " + e.getMessage());
        } finally {
            try {
                Thread.sleep(5000);  // Optional: Pause to observe result
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            driver.quit();
        }
    }
}
