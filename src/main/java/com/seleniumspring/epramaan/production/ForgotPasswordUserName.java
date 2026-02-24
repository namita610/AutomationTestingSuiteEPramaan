package com.seleniumspring.epramaan.production;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import com.demo.seleniumspring.util.AppConstant;

import javax.swing.JOptionPane;
import java.time.Duration;

public class ForgotPasswordUserName {

    @Test
    public void ForgotPasswordUserNameTest() {
        System.setProperty(AppConstant.EPRAMAAN_Driver, AppConstant.EPRAMAAN_DPATH);
        System.out.println("Running Selenium Forgot Password");

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        //WebDriverWait wait = new WebDriverWait(driver, 10); // For Selenium 3

        try {
            driver.manage().window().maximize();
            driver.get(AppConstant.EPPRAMAAN_ForgotPassword); // fixed typo

            
            WebElement selectTypeDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("selectType")));
            new Select(selectTypeDropdown).selectByValue(AppConstant.Username);

      
            WebElement idField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user.otherTypeNumber")));
            idField.sendKeys(AppConstant.Username);

	
            
            driver.findElement(By.id("date")).sendKeys("06");
            Thread.sleep(1000);
            driver.findElement(By.id("month")).sendKeys("October");
            Thread.sleep(1000);
            driver.findElement(By.id("year")).sendKeys("1997");
            Thread.sleep(1000);
            
            

        
            if (driver.findElements(By.id("j_captcha_response")).size() > 0) {
                String captcha = JOptionPane.showInputDialog("Enter CAPTCHA shown on the screen:");
                driver.findElement(By.id("j_captcha_response")).sendKeys(captcha);
            }

            
            Thread.sleep(1000);
          
            WebElement submitButton = driver.findElement(By.id("submitButton"));
            submitButton.click();
            
            Thread.sleep(8000);
            
            
            
            Thread.sleep(2000);  // Replace with explicit wait in production


            if (driver.findElements(By.id("otp")).size() > 0) {
                String captcha = JOptionPane.showInputDialog("Enter OTP");
                driver.findElement(By.id("otp")).sendKeys(captcha);
            }

            
            Thread.sleep(5000);;
			/*
			 * // Locate and click the Verify button WebElement verifyButton =
			 * driver.findElement(By.xpath("//input[@type='submit' and @value='Verify']"));
			 * verifyButton.click();
			 */
            
            WebElement verifyButton = driver.findElement(By.xpath("//input[@type='submit' and @value='Verify']"));
            verifyButton.click();
            
            Thread.sleep(3000);
            
            
            WebElement passwordField = driver.findElement(By.id("password"));
            passwordField.sendKeys("Admin@1234");  // Replace with your password

            // Fill in Confirm Password
            WebElement confirmPasswordField = driver.findElement(By.id("confirmPassword"));
            confirmPasswordField.sendKeys("Admin@1234");  // Must match password


            if (driver.findElements(By.id("j_captcha_response")).size() > 0) {
                String captcha = JOptionPane.showInputDialog("Enter CAPTCHA shown on the screen:");
                driver.findElement(By.id("j_captcha_response")).sendKeys(captcha);
            }


            // Click Submit button
            WebElement submitButton1 = driver.findElement(By.xpath("//input[@type='submit' and @value='Submit']"));
            submitButton1.click();
            
            
            Thread.sleep(6000);
            
            

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                Thread.sleep(3000); // Optional pause to observe result
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver.quit();
        }
    }
}
