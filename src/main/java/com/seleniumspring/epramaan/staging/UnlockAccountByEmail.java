package com.seleniumspring.epramaan.staging;

import java.time.Duration;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import com.demo.seleniumspring.util.AppConstant;

//import java.time.Duration;

public class UnlockAccountByEmail{

    @Test
    public void UnlockAccountByEmailTest() {
        System.setProperty(AppConstant.EPRAMAAN_staging_Driver, AppConstant.EPRAMAAN_DPATH_staging);
        System.out.println("Running Selenium Unlock Account by Mobile test...");

        WebDriver driver = new ChromeDriver();
        //WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        //WebDriverWait wait = new WebDriverWait(driver, 10); // For Selenium 3
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        try {
            driver.manage().window().maximize();
            driver.get(AppConstant.URL_Staging);

            // STEP 1: Click "Unlock Your Account"
            WebElement unlockMenu = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(), 'Unlock Your Account')]")));
            unlockMenu.click();
              
            Thread.sleep(5000); 
            
            // STEP 2: Click "Using Email"
            WebElement usingMobileLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"navbarSupportedContent\"]/ul/li[3]/ul/li[1]/a")));
            usingMobileLink.click();
            
            Thread.sleep(5000); 

            // STEP 3: Enter Username
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
           // String username = JOptionPane.showInputDialog(null, "Enter your Username:");
            usernameField.sendKeys(AppConstant.Username_staging);
            
            Thread.sleep(3000); 

            // STEP 4: Prompt for CAPTCHA
            String captcha = JOptionPane.showInputDialog(null, "Enter CAPTCHA as shown on screen:");
            WebElement captchaField = driver.findElement(By.id("j_captcha_response"));
            captchaField.sendKeys(captcha);
            
            Thread.sleep(3000); 

            // STEP 5: Click Submit
            WebElement submitBtn = driver.findElement(By.xpath("//input[@type='submit' and @value='Submit']"));
            submitBtn.click();

            
            Thread.sleep(3000); 
            // Optional: Wait or assert based on what happens next

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
