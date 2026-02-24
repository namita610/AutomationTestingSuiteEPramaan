package com.seleniumspring.epramaan.staging;

import java.time.Duration;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.Test;

import com.demo.seleniumspring.util.AppConstant;


public class LoginUsingPan {

    @Test
    public void LoginWithPanTest() {
        System.setProperty(AppConstant.EPRAMAAN_Driver, AppConstant.EPRAMAAN_DPATH);
        System.out.println("▶ Starting Pan login test...");

        WebDriver driver = new ChromeDriver();
        //WebDriverWait wait = new WebDriverWait(driver, 15);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            driver.manage().window().maximize();
            driver.get(AppConstant.URL_Staging); // Use actual URL

            // Click on Aadhaar/PAN/.. tab
            WebElement othersBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("othersbtn")));
            othersBtn.click();
            Thread.sleep(1000);

            // Select DL from dropdown
            WebElement typeDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("selectType")));
            new Select(typeDropdown).selectByValue("panNumber");
            Thread.sleep(1000);

            // Enter DL number
            WebElement idNumberField = driver.findElement(By.id("user.otherTypeNumber"));
            idNumberField.clear();
            idNumberField.sendKeys(AppConstant.PanNo_staging);
            idNumberField.sendKeys(Keys.TAB); // Trigger blur / AJAX event
            Thread.sleep(2000); // Allow time for AJAX to fetch usernames

			/*
			 * // Wait for the username dropdown to be populated with actual options
			 * wait.until(driver1 -> { List<WebElement> options =
			 * driver1.findElements(By.cssSelector("#selectUser option")); return
			 * options.size() > 1; // Assuming "Select a Username" + actual options });
			 * 
			 * // Select the first available username (excluding default option) Select
			 * usernameDropdown = new Select(driver.findElement(By.id("selectUser")));
			 * List<WebElement> usernames = usernameDropdown.getOptions();
			 * 
			 * for (WebElement option : usernames) { String val =
			 * option.getAttribute("value"); if (!val.equals("NONE")) {
			 * usernameDropdown.selectByVisibleText(option.getText());
			 * System.out.println("✅ Username selected: " + option.getText()); break; } }
			 */

            
            
            
            
            // Password
            WebElement passwordField = driver.findElement(By.id("user.password"));
            passwordField.sendKeys(AppConstant.Password_staging);
            Thread.sleep(1000);

            // Consent
            WebElement consentCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.id("ssoConsent1")));
            if (!consentCheckbox.isSelected()) {
                js.executeScript("arguments[0].click();", consentCheckbox);
            }

            // Submit
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("submit")));
            js.executeScript("arguments[0].click();", signInButton);
            System.out.println("✅ Login submitted.");

        } catch (Exception e) {
            System.out.println("❌ Exception occurred:");
            e.printStackTrace();
        } finally {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver.quit();
        }
    }
}
