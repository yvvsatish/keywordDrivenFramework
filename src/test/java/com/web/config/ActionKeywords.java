package com.web.config;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.web.executionEngine.DriverScript;
import com.web.utility.ExcelUtils;
import com.web.utility.Log;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ActionKeywords {

	public static WebDriver driver;
	public static Properties OR;

	public static String objectRepo(String object) {
		try {
			int objectRow = ExcelUtils.getRowContains(object, Constants.Col_ObjectName, Constants.Sheet_Objects);
			return ExcelUtils.getCellData(objectRow, Constants.Col_ObjectValue, Constants.Sheet_Objects);
		}catch (Exception e) {
			Log.info("Not able to fetch object repository");
		}
		return null;
	}

	public static void openBrowser(String object,String data){		
		Log.info("Opening Browser");
		try{				
			if(data.equalsIgnoreCase("mozilla")){
				WebDriverManager.firefoxdriver().setup();
				driver=new FirefoxDriver();
				Log.info("Mozilla browser started");				
			}
			else if(data.equalsIgnoreCase("ie")){
				WebDriverManager.iedriver().setup();
				driver=new InternetExplorerDriver();
				Log.info("IE browser started");
			}
			else if(data.equalsIgnoreCase("edge")){
				WebDriverManager.edgedriver().setup();
				driver=new EdgeDriver();
				Log.info("IE browser started");
			}
			else if(data.equalsIgnoreCase("chrome")){
				WebDriverManager.chromedriver().setup();
				driver=new ChromeDriver();
				Log.info("Chrome browser started");
			}

			int implicitWaitTime=(10);
			driver.manage().timeouts().implicitlyWait(implicitWaitTime, TimeUnit.SECONDS);
			driver.manage().window().maximize();
		}catch (Exception e){
			Log.info("Not able to open the Browser --- " + e.getMessage());
			DriverScript.bResult = false;
		}
	}

	public static void navigate(String object, String data){
		try{
			Log.info("Navigating to URL");
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.get(data);
		}catch(Exception e){
			Log.info("Not able to navigate --- " + e.getMessage());
			DriverScript.bResult = false;
		}
	}

	public static void click(String object, String data){
		try{
			Log.info("Clicking on Webelement "+ object);
			waitForElement(By.xpath(objectRepo(object)));
			driver.findElement(By.xpath(objectRepo(object))).click();
		}catch(Exception e){
			Log.error("Not able to click --- " + e.getMessage());
			DriverScript.bResult = false;
		}
	}

	public static void input(String object, String data){
		try{
			Log.info("Entering the text in " + object);
			waitForElement(By.xpath(objectRepo(object)));
			driver.findElement(By.xpath(objectRepo(object))).sendKeys(data);
		}catch(Exception e){
			Log.error("Not able to Enter UserName --- " + e.getMessage());
			DriverScript.bResult = false;
		}
	}

	public static void moveToElement(String object, String data){
		try{
			Log.info("Move to element");
			Actions a = new Actions(driver);
			waitForElement(By.xpath(objectRepo(object)));
			a.moveToElement(driver.findElement(By.xpath(objectRepo(object)))).build().perform();
			Thread.sleep(1500);
			DriverScript.bResult = true;
		}catch(Exception e){
			Log.error("Not able to Enter UserName --- " + e.getMessage());
			DriverScript.bResult = false;
		}
	}

	public static void waitFor(String object, String data) throws Exception{
		try{
			Log.info("Wait for 5 seconds");
			Thread.sleep(5000);
		}catch(Exception e){
			Log.error("Not able to Wait --- " + e.getMessage());
			DriverScript.bResult = false;
		}
	}

	public static void closeBrowser(String object, String data){
		try{
			Log.info("Closing the browser");
			driver.quit();
		}catch(Exception e){
			Log.error("Not able to Close the Browser --- " + e.getMessage());
			DriverScript.bResult = false;
		}
	}

	public static void scrollToElement(String object, String data){
		try{
			Log.info("Scroll to Element");
			((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(objectRepo(object))));
			DriverScript.bResult = true;
		}catch(Exception e){
			Log.error("Not able to scroll element" + e.getMessage());
			DriverScript.bResult = false;
		}
	}

	public static void openNewPage(String object, String data) {
		try {
			String parent=driver.getWindowHandle();
			Set<String>s=driver.getWindowHandles();
			Iterator<String> I1= s.iterator();
			while(I1.hasNext())
			{
				String child_window=I1.next();
				if(!parent.equals(child_window))
				{
					driver.switchTo().window(child_window);
					System.out.println(driver.switchTo().window(child_window).getTitle());
				}
			}
			DriverScript.bResult = true;
		}catch(Exception e){
			Log.error("Not able to switch window" + e.getMessage());
			DriverScript.bResult = false;
		}
	}

	public static void waitForElement(By locator) {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.elementToBeClickable(locator));
	}
}