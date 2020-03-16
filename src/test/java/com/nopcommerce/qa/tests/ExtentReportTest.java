package com.nopcommerce.qa.tests;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportTest {
	WebDriver driver;
	public ExtentHtmlReporter htmlReporter;
	public ExtentReports extent;
	public ExtentTest test;
	
	@BeforeTest
	public void setExtent() {
		htmlReporter=new ExtentHtmlReporter(System.getProperty("user.dir")+"/Reports/myReport.html");
		htmlReporter.config().setDocumentTitle("Automation Report");
		htmlReporter.config().setReportName("Functional Report");
		htmlReporter.config().setTheme(Theme.DARK);
		
		extent=new ExtentReports();
		extent.attachReporter(htmlReporter);
		
		extent.setSystemInfo("Hostname", "LocalHost");
		extent.setSystemInfo("OS", "Windows10");
		extent.setSystemInfo("Tester name", "Sohag");
		extent.setSystemInfo("Browser", "chrome");
		
		}
	
	@AfterTest
	public void endReport() {
		extent.flush();
	}
	
	@BeforeMethod
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\nafas\\Desktop\\New_Workspace_Eclipse\\com.nopcommerce\\Browser\\chromedriver.exe");
		driver=new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		driver.get("https://www.nopcommerce.com/demo");	}
	
	@Test
	public void titleTest() {
		test=extent.createTest("titleTest");
		String title=driver.getTitle();
		Assert.assertEquals(title, "Store Demo - nopCommerc");}
	@Test
	public void logoTest() {
		test=extent.createTest("logoTest");
		boolean status =driver.findElement(By.xpath("//img[@alt='nopCommerce']")).isDisplayed();
		Assert.assertTrue(status);}
	@Test
	public void loginTest() {
		test=extent.createTest("loginTest");
		Assert.assertTrue(true);}
	
	
	
	@AfterMethod
	public void resultProduce(ITestResult result) throws IOException {
		if(result.getStatus()==ITestResult.FAILURE) {
			test.log(Status.FAIL,"Name of The Test is:"+result.getName());
			test.log(Status.FAIL,"Reason is:"+result.getThrowable());
			
			//call the method which take screen shot ->creating path and pass the path for adding screen shot method in test (obj)
			String screenshotpath=getScreenShot(driver, result.getName());
			test.addScreenCaptureFromPath(screenshotpath);
			
		}else if(result.getStatus()==ITestResult.SKIP) {
			test.log(Status.SKIP, "Name of the Test is:"+result.getName());
		}else if(result.getStatus()==ITestResult.SUCCESS) {
			test.log(Status.PASS, "Name of the Test is:"+result.getName());
		}
		
		driver.quit();
	}
	
	//method to take screenshot: to call incase of test case fails
	public static String getScreenShot(WebDriver driver, String screenShotName) throws IOException {
		String dateName=new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		TakesScreenshot ts=(TakesScreenshot)driver;
		File source=ts.getScreenshotAs(OutputType.FILE);
		//taking a variable where it will be stored
		String destination=System.getProperty("user.dir")+"/Screenshot/"+screenShotName+dateName+".png";
		//now need to send it to extent report
		File finalDestinaiton=new File(destination);
		FileUtils.copyFile(source, finalDestinaiton);
		
		return destination ;
	}
	
	


}
