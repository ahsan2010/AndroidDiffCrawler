package com.sail.android.diff.cralwer;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class MyAndroidDiffCrawler {

	public void crawlingData() throws Exception{
		System.setProperty("webdriver.gecko.driver", "/home/ahsan/Documents/ResearchBD/PC_Research/RecommendPost/IssueCrawler/resources/geckodriver");
		WebDriver driver = new FirefoxDriver();
		driver.get("https://developer.android.com/sdk/api_diff/p-dp1/changes");
		
		String title = driver.getTitle();
		System.out.println("Title: " + title);
		
		Thread.sleep(1000);
		
//		WebElement element = driver.findElement(By.xpath("/html/body/div[2]/a[17]"));
		
		driver.switchTo().frame("bottomleftframe");
		
		List<WebElement> list = driver.findElements(By.xpath("//div[@style='line-height:1.5em;color:black']//a[@class='hiddenlink']"));
		WebElement element = list.get(0);
		String hrefValue = element.getAttribute("href");
		driver.get(hrefValue);
		
		System.out.println(hrefValue);
		
//		WebDriverWait wait = new WebDriverWait(driver, 20);
//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/a[17]")));
//			
		
		System.out.println("Data: " + list.size());
		
//		driver.close();

	}
	
	public static void main(String[] args) throws Exception{
		MyAndroidDiffCrawler ob = new MyAndroidDiffCrawler();
		ob.crawlingData();
		System.out.println("Program finishes successfully");
	}
}


