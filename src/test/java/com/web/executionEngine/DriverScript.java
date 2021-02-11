package com.web.executionEngine;

import org.testng.annotations.Test;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.log4j.xml.DOMConfigurator;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.web.config.ActionKeywords;
import com.web.config.Constants;
import com.web.utility.ExcelUtils;
import com.web.utility.Log;
import com.web.utility.Utilities;


public class DriverScript {

	public static ActionKeywords actionKeywords;
	public static String sActionKeyword;
	public static String sPageObject;
	public static Method method[];

	public static int iTestStep;
	public static int iTestLastStep;
	public static String sTestCaseID;
	public static String sRunMode;
	public static String browser;
	public static String sData;
	public static boolean bResult;
	public static ExtentReports extent;
	public static ExtentTest test;
	public static Utilities utility;


	public static ExtentReports extentReporter()
	{
		ExtentSparkReporter reporter = new ExtentSparkReporter(System.getProperty("user.dir") + File.separator + "Reports" + File.separator + "ExtentReports.html");
		reporter.config().setReportName("Automation Results");
		reporter.config().setDocumentTitle("Test Results");
		extent = new ExtentReports();
		extent.attachReporter(reporter);
		extent.setSystemInfo("Tester", "Testing");
		return extent;
	}

	public DriverScript() throws NoSuchMethodException, SecurityException, IOException{
		utility = new Utilities();
		actionKeywords = new ActionKeywords();
		method = actionKeywords.getClass().getMethods();
	}

	@BeforeSuite
	public void setup() {
		utility.deleteFiles(new File(System.getProperty("user.dir")+ File.separator+"Reports"));
		extentReporter();

	}

	@Test
	public void Driver() throws Exception {
		ExcelUtils.setExcelFile(Constants.Path_TestData);
		DOMConfigurator.configure("log4j.xml");
		DriverScript startEngine = new DriverScript();
		startEngine.execute_TestCase();

	}

	@AfterSuite
	public void teardown() {
		extent.flush();	 
		ActionKeywords.driver.quit();
	}

	private void execute_TestCase() throws Exception {
		int iTotalTestCases = ExcelUtils.getRowCount(Constants.Sheet_TestCases);
		for(int iTestcase=1;iTestcase<iTotalTestCases;iTestcase++){
			bResult = true;
			sTestCaseID = ExcelUtils.getCellData(iTestcase, Constants.Col_TestCaseID, Constants.Sheet_TestCases); 
			sRunMode = ExcelUtils.getCellData(iTestcase, Constants.Col_RunMode,Constants.Sheet_TestCases);
			browser = ExcelUtils.getCellData(iTestcase, Constants.Col_Browser,Constants.Sheet_TestCases);
			if (sRunMode.equalsIgnoreCase("yes")){
				ActionKeywords.openBrowser("", browser);
				Log.startTestCase(sTestCaseID);
				test = extent.createTest(sTestCaseID);
				iTestStep = ExcelUtils.getRowContains(sTestCaseID, Constants.Col_TestCaseID, Constants.Sheet_TestSteps);
				iTestLastStep = ExcelUtils.getTestStepsCount(Constants.Sheet_TestSteps, sTestCaseID, iTestStep);
				bResult=true;
				for (;iTestStep<iTestLastStep;iTestStep++){
					sActionKeyword = ExcelUtils.getCellData(iTestStep, Constants.Col_ActionKeyword,Constants.Sheet_TestSteps);
					sPageObject = ExcelUtils.getCellData(iTestStep, Constants.Col_PageObject, Constants.Sheet_TestSteps);
					sData = ExcelUtils.getCellData(iTestStep, Constants.Col_DataSet, Constants.Sheet_TestSteps);
					execute_Actions();
					if(bResult==false){
						ExcelUtils.setCellData(Constants.KEYWORD_FAIL,iTestcase,Constants.Col_Result,Constants.Sheet_TestCases);
						Log.endTestCase(sTestCaseID);
						break;
					}						
					if(bResult==true){
						ExcelUtils.setCellData(Constants.KEYWORD_PASS,iTestcase,Constants.Col_Result,Constants.Sheet_TestCases);
						Log.endTestCase(sTestCaseID);
					}					
				}
			}
		}
	}	

	private static void execute_Actions() throws Exception {
		for(int i=0;i<method.length;i++){
			if(method[i].getName().equals(sActionKeyword)){
				method[i].invoke(actionKeywords, sPageObject, sData);
				if(bResult==true){
					ExcelUtils.setCellData(Constants.KEYWORD_PASS, iTestStep, Constants.Col_TestStepResult, Constants.Sheet_TestSteps);
					test.log(Status.PASS, sActionKeyword + " "+ sPageObject);
					break;
				}else{
					ExcelUtils.setCellData(Constants.KEYWORD_FAIL, iTestStep, Constants.Col_TestStepResult, Constants.Sheet_TestSteps);
					String imagePath = utility.takeScreenShot(sTestCaseID);
					test.log(Status.FAIL, sActionKeyword + " "+ sPageObject, MediaEntityBuilder.createScreenCaptureFromPath(imagePath).build());
					ActionKeywords.closeBrowser("","");
					break;
				}
			}
		}
	}
}