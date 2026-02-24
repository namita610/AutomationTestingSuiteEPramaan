package com.seleniumspring.epramaan.production;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OtpFetcher {

    public static String fetchLatestOtp() {
        AndroidDriver driver = null;
        String otp = "no otp";

        try {
            DesiredCapabilities dc = new DesiredCapabilities();
            dc.setCapability("deviceName", "Redmi Note 10S");
            dc.setCapability("udid", "45GQA65TAAZH4TLF");
            dc.setCapability("platformName", "Android");
            dc.setCapability("platformVersion", "13");
            dc.setCapability("automationName", "UiAutomator2");
            dc.setCapability("appPackage", "com.android.mms");
            dc.setCapability("appActivity", "com.android.mms.ui.ConversationList");
            dc.setCapability("noReset", true);
            dc.setCapability("ignoreHiddenApiPolicyError", true);

            driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), dc);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

            // ✅ Step 1: Open Messaging App (if needed)
            try {
                WebElement messagingIcon = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc='Messaging']"));
                messagingIcon.click();
                System.out.println("✅ Messaging icon clicked.");
            } catch (Exception e) {
                System.out.println("ℹ️ Messaging icon not found, continuing...");
            }

            // ✅ Step 2: Click on the first message in the list
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//androidx.recyclerview.widget.RecyclerView[@resource-id='android:id/list']/android.view.ViewGroup[1]"))).click();
            System.out.println("✅ First message opened.");

            // ✅ Step 3: Click the 3rd action button (if needed)
            WebElement thirdButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("(//android.widget.Button[@resource-id='com.android.mms:id/action_btn'])[3]")));
            thirdButton.click();
            System.out.println("✅ 3rd action button clicked.");

            // ✅ Step 4: Click 4th arrow image (optional, for expanded view)
            List<WebElement> arrowList = driver.findElements(
                    By.xpath("(//android.widget.ImageView[@resource-id='com.android.mms:id/arrow'])[4]"));
            if (!arrowList.isEmpty()) {
                arrowList.get(0).click();
                System.out.println("✅ 4th arrow icon clicked.");
            }

            // ✅ Step 5: Read message text from actual TextView
            WebElement messageTextElement = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//android.widget.TextView[@resource-id='com.android.mms:id/message_body']")));

            String messageText = messageTextElement.getText();
            System.out.println("📩 Message content: " + messageText);

            // ✅ Step 6: Extract OTP using regex
            Pattern pattern = Pattern.compile("\\b\\d{6}\\b");
            Matcher matcher = pattern.matcher(messageText);
            if (matcher.find()) {
                otp = matcher.group();
                System.out.println("✅ OTP extracted: " + otp);
            } else {
                System.out.println("❌ No 6-digit OTP found.");
            }

        } catch (Exception e) {
            System.out.println("❌ Exception while fetching OTP: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
                System.out.println("🔒 Driver closed.");
            }
        }

        return otp;
    }
}
