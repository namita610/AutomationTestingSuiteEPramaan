package com.seleniumspring.epramaan.production;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import com.demo.seleniumspring.util.AppConstant;

public class ViewTransactionHistory {

    @Test
    public void ViewTransactionHistoryTest() {
        // Set path to ChromeDriver if not in system PATH
       // System.setProperty("webdriver.chrome.driver", AppConstants.URL);
   	 System.setProperty(AppConstant.EPRAMAAN_Driver,AppConstant.EPRAMAAN_DPATH);
        System.out.println("Running Selenium login test...");

        WebDriver driver = new ChromeDriver();
        //WebDriverWait wait = new WebDriverWait(driver, 10); // For Selenium 3
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            driver.manage().window().maximize();
            driver.get(AppConstant.URL);
            
			/*
			 * // Click "Username" button WebElement usernameBtn =
			 * wait.until(ExpectedConditions.elementToBeClickable(By.id("userNamebtn")));
			 * usernameBtn.click();
			 * 
			 * // Wait for the personal assurance message
			 * wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("personalmsg")
			 * ));
			 */

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
            Thread.sleep(3000); 
          
            Thread.sleep(1000);
            // Click "Manage Profile" (has multiple classes, use XPath)
               WebElement manageProfile = wait.until(ExpectedConditions.elementToBeClickable(
                   By.xpath("//a[contains(@class,'dropdown-toggle') and contains(text(),'Manage Profile')]")));
               manageProfile.click();

               
               Thread.sleep(5000);
               
               
               WebElement viewTxnLink = driver.findElement(By.xpath("//a[text()='View Transaction History']"));
               viewTxnLink.click();

             

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











