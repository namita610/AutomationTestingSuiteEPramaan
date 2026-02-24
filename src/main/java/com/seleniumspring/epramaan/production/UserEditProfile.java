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

public class UserEditProfile {

    @SuppressWarnings("resource")
	@Test
    public void UserEditProfileTest() {
        // Set path to ChromeDriver if not in system PATH
       // System.setProperty("webdriver.chrome.driver", AppConstants.URL);
   	 System.setProperty(AppConstant.EPRAMAAN_Driver,AppConstant.EPRAMAAN_DPATH);
        System.out.println("Running Selenium login test..");

        WebDriver driver = new ChromeDriver();
        //WebDriverWait wait = new WebDriverWait(driver, 10); // For Selenium 3
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            driver.manage().window().maximize();
            driver.get(AppConstant.URL);
         
            // Wait for and enter Username
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user.username")));
            usernameField.clear(); // Clear any pre-filled value
            usernameField.sendKeys(AppConstant.Username);
            //usernameField.sendKeys(null);
            Thread.sleep(1000); 
            // Wait for and enter Password
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user.password")));
            passwordField.clear();
            passwordField.sendKeys(AppConstant.Password);
            Thread.sleep(1000); 
            // Wait for and click the checkbox
            WebElement consentCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.id("ssoConsent1")));
            consentCheckbox.click();

            // Wait for and click Submit
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("submit")));
            signInButton.click();

            System.out.println("Login submitted successfully.");

			
            Thread.sleep(1000);
         // Click "Manage Profile" (has multiple classes, use XPath)
            WebElement manageProfile = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@class,'dropdown-toggle') and contains(text(),'Manage Profile')]")));
            manageProfile.click();

            
            Thread.sleep(1000);
            // Click "Edit Profile"
            WebElement editProfile = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@class,'nav-link') and contains(text(),'Edit Profile')]")));
            editProfile.click();
            Thread.sleep(1000);
            
			
            driver.findElement(By.id("password")).sendKeys(AppConstant.Password);
            Thread.sleep(1000);
            
            
            
         // Show input dialog to user for captcha entry
            String captcha = JOptionPane.showInputDialog(null, "Please enter the CAPTCHA:");

            // Enter captcha on the webpage
            driver.findElement(By.id("j_captcha_response")).sendKeys(captcha);
            
            Thread.sleep(1000);
            
		       
            
            WebElement submitButton = driver.findElement(By.xpath("//button[@type='submit' and @formaction='/profile/edit/verifyUser.do']"));

            // Click the Submit button
            submitButton.click();
            
            Thread.sleep(1000);
            
            driver.findElement(By.id("email")).sendKeys(AppConstant.Email);
            Thread.sleep(1000);
            
            
            
            
         // Show input dialog to user for OTP entry
            String otp = JOptionPane.showInputDialog(null, "Please enter the OTP received:");

            // Enter OTP on the webpage
            driver.findElement(By.id("user.emailOtp")).sendKeys(otp);
            Thread.sleep(1000);
            
            WebElement verifyBtn = driver.findElement(By.id("verifyEmailOTP"));
            verifyBtn.click();
            
         
            Thread.sleep(10000);
            
            
            WebElement button = driver.findElement(By.xpath("//*[@id='userForm']/div/div[1]/div/div[4]/div/div[2]/button"));
            button.click();
            
			
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                Thread.sleep(3000); // Optional pause before closing
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver.quit();
    }
}

}











