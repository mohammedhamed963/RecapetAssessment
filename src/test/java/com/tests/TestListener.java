package com.tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.utils.ScreenshotUtil;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    private static final ExtentReports extent = ExtentManager.getInstance();
    private static final ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = extent.createTest(result.getMethod().getMethodName());
        testThread.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        testThread.get().pass("Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Object inst = result.getInstance();
        String path = null;
        if (inst instanceof BaseTest) {
            BaseTest base = (BaseTest) inst;
            if (base.driver != null) {
                path = ScreenshotUtil.takeScreenshot(base.driver, result.getMethod().getMethodName());
            }
        }
        try {
            if (path != null) {
                testThread.get().fail(result.getThrowable(),
                        MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            } else {
                testThread.get().fail(result.getThrowable());
            }
        } catch (Exception e) {
            testThread.get().fail("Failed to attach screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        testThread.get().skip("Test skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }

    // unused
    @Override public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}
    @Override public void onStart(ITestContext context) {}
    @Override public void onTestFailedWithTimeout(ITestResult result) {}
}
