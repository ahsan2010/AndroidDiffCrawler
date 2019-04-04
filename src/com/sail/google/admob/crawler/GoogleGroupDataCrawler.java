package com.sail.google.admob.crawler;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class GoogleGroupDataCrawler {

	WebDriver driver = null;
	WebDriver pageDriver = null;
	
	public void setDriverProperty() {
		//System.setProperty("webdriver.gecko.driver",
		//		"/home/ahsan/Documents/ResearchBD/PC_Research/RecommendPost/IssueCrawler/resources/geckodriver");
		//System.setProperty("webdriver.chrome.driver", "/home/ahsan/Downloads/chromedriver");
		System.setProperty("webdriver.gecko.driver", "/home/ahsan/Downloads/Lib/geckodriver");
	}

	public List<WebElement> getChangeTableInfo(WebDriver driver, String type) {
		List<WebElement> elements = null;
		try {
			WebElement element = driver.findElement(By.xpath("//div[@id='mainBodyFluid']//code"));
			elements = driver.findElements(By.xpath("//table[@summary='" + type + "']//tr"));
			return elements;
		} catch (Exception e) {
			return null;
		}

	}

	public int identifyTotalVisibleThreads() throws Exception{
		
		Thread.sleep(3000);
		String title = driver.getTitle();
		System.out.println("Title: " + title);
		//List<WebElement> visibleThreadElements = driver.findElements(By.xpath("//a[contains(@class, 'F0XO1GC-q-R F0XO1GC-Jb-g')]/@href"));
		List<WebElement> visibleThreadElements = driver.findElements(By.className("F0XO1GC-q-R"));
		System.out.println("Visible Threads : " + visibleThreadElements.size());
		return visibleThreadElements.size();
		////*[@id="l_topic_title_8QX41Bdjb1U"]
		//System.out.println("E: " + visibleThreadElements.getText());
	}
	///html/body/div[4]/div[5]/div[3]/div/div/div/div[2]/div
	
	public void getPageInformation(String url) throws Exception{
		pageDriver.get(url);
		Thread.sleep(3000);
		List<WebElement> pageThreads 	= pageDriver.findElements(By.xpath("//div[contains(@class, 'F0XO1GC-nb-W F0XO1GC-nb-Y'))"));
		//List<WebElement> pageThreads 	= pageDriver.findElements(By.className("F0XO1GC-nb-W F0XO1GC-nb-Y F0XO1GC-b-Gb F0XO1GC-nb-X"));
		
		System.out.println(pageThreads.size());
	}
	
	
	public void crawlingData() throws Exception {
		setDriverProperty();
		pageDriver = new FirefoxDriver();
		driver = new FirefoxDriver();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		driver.get("https://groups.google.com/forum/#!categories/google-admob-ads-sdk");
		Thread.sleep(1000);
		WebElement scrollbarElement = driver.findElement(By.xpath("/html/body/div[4]/div[5]/div[3]/div/div/div/div[2]/div"));
		Long previousScrollHeight = 0L;
		for(int i = 0 ; i <= 1 ; i ++){
			int threadNo = identifyTotalVisibleThreads();
			//js.executeAsyncScript(arg0, arg1)
			js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
			//js.executeScript("alert('PROBLEM PROBEM')");
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
		
	}
	
	public void testPageCrawling() throws Exception{
		setDriverProperty();
		String url = "https://groups.google.com/forum/#!category-topic/google-admob-ads-sdk/0QDKcvK_nEk";
		pageDriver = new FirefoxDriver();
		getPageInformation(url);
	}
	
	public static void main(String[] args) throws Exception{
		GoogleGroupDataCrawler ob = new GoogleGroupDataCrawler();
		//ob.crawlingData();
		ob.testPageCrawling();
		
		System.out.println("Program finishes successfully");
	}
}
