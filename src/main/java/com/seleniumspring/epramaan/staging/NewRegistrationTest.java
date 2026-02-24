package com.seleniumspring.epramaan.staging;

import java.time.Duration;

import javax.swing.JOptionPane;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.demo.seleniumspring.util.AppConstant;

public class NewRegistrationTest {

    @Test
    public void NewRegistration() {
        System.setProperty(AppConstant.EPRAMAAN_staging_Driver, AppConstant.EPRAMAAN_DPATH_staging);
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            driver.manage().window().maximize();
            driver.get(AppConstant.EPRAMAAN_NewRegistrationURL_staging);
            Assert.assertEquals(driver.getCurrentUrl(), AppConstant.EPRAMAAN_NewRegistrationURL_staging, "Incorrect URL loaded.");
            System.out.println("Navigated to New Registration URL");

            // Enter mobile number
            WebElement mobileField = wait.until(ExpectedConditions.elementToBeClickable(By.id("verifiedMobileNumber")));
            Assert.assertTrue(mobileField.isDisplayed(), "Mobile number field not displayed.");
            mobileField.clear();
            mobileField.sendKeys(AppConstant.MobileNo_staging);
            Assert.assertEquals(mobileField.getAttribute("value"), AppConstant.MobileNo_staging, "Mobile number not entered correctly.");
            System.out.println("Mobile number entered");

            // Click Generate OTP
            WebElement generateOtpBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("generateOtp")));
            Assert.assertTrue(generateOtpBtn.isDisplayed(), "Generate OTP button not visible.");
            js.executeScript("arguments[0].scrollIntoView(true);", generateOtpBtn);
            generateOtpBtn.click();
            System.out.println("OTP generated... fetching from service");
            
            Thread.sleep(1000);
            // Fetch and enter OTP
			/*
			 * String otp = OtpFetcher.fetchLatestOtp(); System.out.println("Fetched OTP: "
			 * + otp); Assert.assertNotNull(otp, "OTP not fetched for login");
			 */

			/*
			 * WebElement otpFieldLogin =
			 * wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user.otp")));
			 * otpFieldLogin.clear(); otpFieldLogin.sendKeys(otp);
			 * System.out.println("OTP entered successfully");
			 */
            
            
            String otp = JOptionPane.showInputDialog("Enter the OTP received:");
            driver.findElement(By.id("user.otp")).sendKeys(otp);
            //driver.findElement(By.id("verifyOtpButton")).click();
            
            // Click Verify OTP
            WebElement verifyOtpBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("verifyOTP")));
            Assert.assertTrue(verifyOtpBtn.isDisplayed(), "Verify OTP button not visible.");
            js.executeScript("arguments[0].scrollIntoView(true);", verifyOtpBtn);
            verifyOtpBtn.click();
            System.out.println("OTP verification attempted");

            // Wait for form fields to become editable
            wait.until(ExpectedConditions.not(
                ExpectedConditions.attributeContains(By.id("givenName"), "readonly", "readonly")
            ));
            WebElement givenNameField = driver.findElement(By.id("givenName"));
            Assert.assertTrue(givenNameField.isEnabled(), "Given name field is not editable.");
            System.out.println("OTP verified and form fields are editable");
            
            Thread.sleep(1000);

            // Fill registration form
            givenNameField.sendKeys("Namita Jikamade");
            Assert.assertEquals(givenNameField.getAttribute("value"), "Namita Jikamade", "Given name not entered properly.");
            Thread.sleep(1000);

            WebElement gender = driver.findElement(By.id("gender"));
            Assert.assertTrue(gender.isDisplayed(), "Gender dropdown not visible.");
            new Select(gender).selectByValue("F");
            Thread.sleep(1000);

            WebElement date = driver.findElement(By.id("date"));
            date.sendKeys("06");
            Assert.assertEquals(date.getAttribute("value"), "06", "Date not entered properly.");
            
            Thread.sleep(1000);

            
            driver.findElement(By.id("month")).sendKeys("October");
            Thread.sleep(1000);


            WebElement year = driver.findElement(By.id("year"));
            year.sendKeys("1997");
            Assert.assertEquals(year.getAttribute("value"), "1997", "Year not entered properly.");
            
            Thread.sleep(1000);


            WebElement personalMessage = driver.findElement(By.id("personalMessage"));
            personalMessage.sendKeys("Hello Namita");
            Assert.assertEquals(personalMessage.getAttribute("value"), "Hello Namita", "Personal message not entered properly.");
            
            Thread.sleep(1000);


            WebElement username = driver.findElement(By.id("username"));
            username.sendKeys("namita_staging_account6");
            Assert.assertEquals(username.getAttribute("value"), "namita_staging_account6", "Username not entered properly.");
            
            Thread.sleep(1000);


            WebElement password = driver.findElement(By.id("password"));
            password.sendKeys("Admin@123");
            Assert.assertEquals(password.getAttribute("value"), "Admin@123", "Password not entered properly.");

            Thread.sleep(1000);

            
            
            WebElement confirmPassword = driver.findElement(By.id("confirmPassword"));
            confirmPassword.sendKeys("Admin@123");
            Assert.assertEquals(confirmPassword.getAttribute("value"), "Admin@123", "Confirm password not entered properly.");
            Thread.sleep(1000);


            System.out.println("Registration form filled");

            // Accept terms and conditions
            WebElement termsCheckbox = driver.findElement(By.id("agreeTC1"));
            Assert.assertTrue(termsCheckbox.isDisplayed(), "Terms and Conditions checkbox not found.");
            if (!termsCheckbox.isSelected()) {
                js.executeScript("arguments[0].click();", termsCheckbox);
            }
            Assert.assertTrue(termsCheckbox.isSelected(), "Terms and Conditions checkbox not selected.");
            System.out.println("Terms and Conditions accepted");

            // Click Sign Up
            WebElement signUpBtn = driver.findElement(By.id("submitBtn"));
            Assert.assertTrue(signUpBtn.isDisplayed(), "Sign Up button not visible.");
            js.executeScript("arguments[0].removeAttribute('disabled');", signUpBtn);
            js.executeScript("arguments[0].click();", signUpBtn);
            System.out.println("Registration submitted");
            
            Thread.sleep(5000);

            // Verify successful registration
			/*
			 * String pageSource = driver.getPageSource(); Assert.assertTrue(
			 * pageSource.contains("Registration successful") ||
			 * pageSource.contains("Thank you"),
			 * "Registration failed or success message not found." );
			 * System.out.println("Registration completed successfully");
			 */

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Test failed due to exception: " + e.getMessage());
        } finally {
            driver.quit();
            System.out.println("Browser closed");
        }
    }
}
