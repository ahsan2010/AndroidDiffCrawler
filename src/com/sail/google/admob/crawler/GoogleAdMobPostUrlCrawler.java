package com.sail.google.admob.crawler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.csvreader.CsvWriter;

public class GoogleAdMobPostUrlCrawler {
	WebDriver driver = null;
	Set<String> visiblePostUrls = new HashSet<String>();
	String ROOT = "";
	
	public void setDriverProperty() {
		System.setProperty("webdriver.gecko.driver",
				ROOT + "/geckodriver");
	}

	public int identifyTotalVisibleThreads() throws Exception {

		Thread.sleep(3000);
		String title = driver.getTitle();
		System.out.println("Title: " + title);
		List<WebElement> visibleThreadElements = driver.findElements(By.className("F0XO1GC-q-R"));
		for(WebElement element : visibleThreadElements){
			visiblePostUrls.add(element.getAttribute("href"));
		}
		return visibleThreadElements.size();
	}
	
	public void crawlingData() throws Exception {
		setDriverProperty();
		FirefoxOptions  options = new FirefoxOptions();
		options.addArguments("--headless");
		driver = new FirefoxDriver(options);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		driver.get("https://groups.google.com/forum/#!categories/google-admob-ads-sdk");
		Thread.sleep(3000);
		WebElement scrollbarElement = driver.findElement(By.xpath("/html/body/div[4]/div[5]/div[3]/div/div/div/div[2]/div"));
		Long previousScrollHeight = 0L;
		
		for(int i = 0 ; i <= 100000 ; i ++){
			int threadNo = identifyTotalVisibleThreads();
			js.executeScript("arguments[0].scrollBy(0,arguments[0].scrollHeight);", scrollbarElement);			
			Long presentScrollHeight = (Long)js.executeScript("return arguments[0].scrollHeight",scrollbarElement);
			if(presentScrollHeight == previousScrollHeight){
				System.out.println("FINISH CRAWLING");
				break;
			}
			previousScrollHeight = presentScrollHeight;			
			Thread.sleep(5000);
			System.out.println("Total Threads " + threadNo);	
		}				
		writeUrlInformation();
	}
	
	public void writeUrlInformation() throws Exception{
		System.out.println("Total visible post urls ["+visiblePostUrls.size()+"]");
		CsvWriter writer = new CsvWriter(ROOT + "/Results/postUrl.csv");
		writer.write("Post_Url");
		writer.endRecord();
		for(String postLink : visiblePostUrls){
			writer.write(postLink);
			writer.endRecord();
		}
		writer.close();		
	}
	
	public static void main(String[] args) throws Exception{
		GoogleAdMobPostUrlCrawler ob = new GoogleAdMobPostUrlCrawler();
		ob.ROOT = args[0];
		//ob.ROOT = "/home/ahsan/Documents/Sail_Research/AndroidDiffCrawler";
		ob.crawlingData();
		System.out.println("Program finishes successfully");
	}
}
