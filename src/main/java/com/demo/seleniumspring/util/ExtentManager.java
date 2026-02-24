package com.demo.seleniumspring.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.demo.seleniumspring.util.AppConstant;

public class ExtentManager {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static synchronized ExtentReports getExtent() {
        if (extent == null) {
            extent = createInstance();
        }
        return extent;
    }

    private static ExtentReports createInstance() {
        String baseDir = System.getProperty("report.dir", System.getProperty("user.dir") + "/reports");
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String reportPath = baseDir + "/ExtentReport_" + timestamp + ".html";

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        //spark.config().setTheme(Theme.DARK);
        spark.config().setTheme(Theme.STANDARD);
        spark.config().setDocumentTitle("Automation Execution Report");
        spark.config().setReportName("e-Pramaan Selenium Suite");

        ExtentReports ext = new ExtentReports();
        ext.attachReporter(spark);

        // Pull environment info dynamically from AppConstant
        ext.setSystemInfo("Environment", AppConstant.URL.contains("epstg") ? "Staging" : "Production");
        ext.setSystemInfo("Base URL", AppConstant.URL);
        ext.setSystemInfo("Username", AppConstant.Username);
        ext.setSystemInfo("OS", System.getProperty("os.name"));
        ext.setSystemInfo("Java Version", System.getProperty("java.version"));

        return ext;
    }

    public static synchronized ExtentTest createTest(String name, String description) {
        ExtentTest t = getExtent().createTest(name, description);
        test.set(t);
        return t;
    }

    public static synchronized ExtentTest getTest() {
        return test.get();
    }
}
