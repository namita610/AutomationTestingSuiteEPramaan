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
import org.testng.annotations.Test;

import com.demo.seleniumspring.util.AppConstant;

public class ResetPassword {

  
	@Test
    public void UserResetPasswordTest() {
        // Set path to ChromeDriver if not in system PATH
       // System.setProperty("webdriver.chrome.driver", AppConstants.URL);
   	 System.setProperty(AppConstant.EPRAMAAN_staging_Driver,AppConstant.EPRAMAAN_DPATH_staging);
        System.out.println("Running Selenium login test...");

        WebDriver driver = new ChromeDriver();
        //WebDriverWait wait = new WebDriverWait(driver, 10); // For Selenium 3
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().deleteAllCookies();
        
        
        try {
            driver.manage().window().maximize();
            driver.get(AppConstant.URL_Staging);
         
            // Wait for and enter Username
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user.username")));
            usernameField.clear(); // Clear any pre-filled value
            usernameField.sendKeys(AppConstant.Username_staging);
            //usernameField.sendKeys(null);
            Thread.sleep(1000); 
            // Wait for and enter Password
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user.password")));
            passwordField.clear();
            passwordField.sendKeys(AppConstant.Password_staging);
            Thread.sleep(1000); 
            // Wait for and click the checkbox
            WebElement consentCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.id("ssoConsent1")));
            consentCheckbox.click();

            // Wait for and click Submit
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("submit")));
            signInButton.click();

            System.out.println("Login submitted successfully.");

			
            Thread.sleep(1000);
          
           
            
            // Step 1: Click "Password Settings" dropdown
            WebElement passwordSettingsDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@class, 'dropdown-toggle') and contains(text(), 'Password Settings')]")
            ));
            passwordSettingsDropdown.click();

            Thread.sleep(1000);            
            
            // Step 2: Click "Reset Password"
            WebElement resetPasswordLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@class='nav-link' and contains(text(), 'Reset Password')]")
            ));
            resetPasswordLink.click();
            
            Thread.sleep(1000);   

            // Step 3: Fill Old, New, and Confirm Password fields
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("oldPassword"))).sendKeys(AppConstant.Password_staging);
            driver.findElement(By.id("newPassword")).sendKeys(AppConstant.NewPasswordForReset_staging);
            driver.findElement(By.id("confirmPassword")).sendKeys(AppConstant.ConfirmPasswordForReset_staging);
            
            Thread.sleep(1000);   

            // Step 4: Click the Update button
            WebElement updateButton = driver.findElement(By.xpath("//input[@type='submit' and @value='Update']"));
            updateButton.click();
      
            
            Thread.sleep(5000);   
            
            
           
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











