package com.web.utility;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

/**
 * Description : Class is used to maintain all extent report listeners to generate extent reports for each and evey test case
 * @author sushma.p
 */
public class ExtentReportsListners  implements ITestListener {

	ExtentReports extent ;//= extentReprter(); 
	ExtentTest test ;
	private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<ExtentTest>();

	/**
	 * Method is to update method name on extent report when execution started
	 */
	public void onTestStart(ITestResult result) {
		test = extent.createTest(result.getMethod().getMethodName());
		extentTest.set(test);
	}

	/**
	 * Method is to update the test success result on extent report
	 */
	public void onTestSuccess(ITestResult result) {
		extentTest.get().log(Status.PASS, "success");
	}

	/**
	 * Method is to update test failure result on extent report
	 */
	public void onTestFailure(ITestResult result) {
		// TODO Auto-generated method stub
	}

	/**
	 * Method is to update test skip result on extent report
	 */
	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
	}

	/**
	 * Method is to update test failure with success percentage on extent report
	 */
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
	}

	/**
	 * Method is to start the extent report
	 */
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
	}

	/**
	 * Method is to finish the extent report
	 */
	public void onFinish(ITestContext context) {
		extent.flush();
	}

	/**
	 * Method is to generate the extent report by gathering all required information
	 * @return
	 */
	public static ExtentTest getReporter() {
		return extentTest.get();
	}
}
