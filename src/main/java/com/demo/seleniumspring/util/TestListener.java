package com.demo.seleniumspring.util;

import com.aventstack.extentreports.Status;
import base.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    // Thread-safe storage for ExtentTest per test thread
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    // Getter for current test instance
    public static ExtentTest getTest() {
        return test.get();
    }

    @Override
    public void onStart(ITestContext context) {
        ExtentManager.getExtent(); // Initialize ExtentReports instance
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.getExtent().flush(); // Finalize and write report
    }

    @Override
    public void onTestStart(ITestResult result) {
        String name = result.getMethod().getMethodName();
        String desc = result.getMethod().getDescription() != null ? result.getMethod().getDescription() : "";

        ExtentTest extentTest = ExtentManager.createTest(name, desc);

        // Assign TestNG groups as categories
        for (String group : result.getMethod().getGroups()) {
            extentTest.assignCategory(group);
        }

        extentTest.log(Status.INFO, "Test started: " + name);

        // Store test instance for thread-safe access
        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        getTest().log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        getTest().fail(result.getThrowable());

        try {
            String screenshotPath = ((BaseTest) result.getInstance())
                    .takeScreenshot(result.getMethod().getMethodName());
            getTest().addScreenCaptureFromPath(screenshotPath);
            getTest().log(Status.INFO, "Screenshot attached: " + screenshotPath);
        } catch (Exception e) {
            getTest().warning(" Failed to capture screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        getTest().skip("Test skipped: " +
                (result.getThrowable() != null ? result.getThrowable().getMessage() : ""));
    }
}
