package com.sail.google.admob.crawler;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class GoogleGroupDataCrawler {

	WebDriver driver = null;
	WebDriver pageDriver = null;
	
	public void setDriverProperty() {
		//System.setProperty("webdriver.gecko.driver", "/home/ahsan/Documents/Sail_Research/AndroidDiffCrawler/geckodriver");
		System.setProperty("webdriver.gecko.driver", "/home/ahsan/Sail_Workspace/CrawlingAndroidSdkDifference/geckodriver");
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
		JavascriptExecutor js = (JavascriptExecutor) pageDriver;
		//js.executeScript("return document.getElementsByClassName('F0XO1GC-Db-b').remove();");"F0XO1GC-nb-W F0XO1GC-mb-p"
		List<WebElement> pageThreads 	= pageDriver.findElements(By.xpath("//div[contains(@class,'F0XO1GC-nb-W F0XO1GC-mb-p')]"));
		//List<WebElement> pageThreads 	= pageDriver.findElements(By.className("F0XO1GC-nb-P"));
		System.out.println("SIZE: " + pageThreads.size());
		List<WebElement> elements = pageThreads.get(1).findElements(By.className("F0XO1GC-nb-P"));
		WebElement element = pageThreads.get(1);
		//System.out.println(element.getAttribute("innerHTML"));
		
		for(WebElement el : elements){
			System.out.println(el.getAttribute("innerHTML"));
		}
		
		/*for(WebElement element : pageThreads){			
			//WebElement dateElement = element.findElement(By.xpath("//span[contains(@class, 'F0XO1GC-nb-Q F0XO1GC-b-Fb')]"));
			System.out.println(element.getAttribute("innerHTML"));
		//	System.out.println(element.getText());
			//System.out.println(dateElement.getText());
			System.out.println("---------------------------------------");
			System.out.println("---------------------------------------");
			
		}*/
		
	}
	
	
	
	public void getPageInformationII(String url) throws Exception{
		pageDriver.get(url);
		Thread.sleep(3000);
		String htmlContent = pageDriver.getPageSource();
		//System.out.println(htmlContent);
		Document doc = Jsoup.parse(htmlContent);
		List<Element> docElements = doc.select("div.F0XO1GC-nb-x");
		for(int i = 1 ; i < docElements.size() ; i ++ ){
			Element p = docElements.get(i);
			Elements el = p.select("blockquote");
			if(el != null){
				el.remove();
			}
			String text = p.text();
			//System.out.println(text.indexOf(":"));
			text = text.substring(text.indexOf(":") + 1);
			
			//System.out.println(text);
			//System.out.println("------------------------");
			
			for(Element filteredText : p.select("div[dir='ltr']")){
				System.out.println("V:  " + filteredText.text());
				System.out.println("----------------------");
			}
			
			String previous = "";
			String timeString = "";
			String posterName = "";
			for( Element element : p.getAllElements() )
			{
			    for( Attribute attribute : element.attributes() )
			    {
			    	if(attribute.getValue().equals("F0XO1GC-F-a")){
			    		posterName = element.text();
			    	}
			    	if(attribute.getValue().equals("F0XO1GC-nb-Q F0XO1GC-b-Fb")){
			    		timeString = element.attr("title");
			    		
			    	}
			        if( attribute.getValue().equalsIgnoreCase("ltr") )
			        {			            
			            if(previous.equals(element.text())){
			            	continue;
			            }
			            System.out.println( timeString + " " + posterName +  " V "+ element.text());
			            System.out.println("----------------------");
			            previous = element.text();
			        }
			    }
			}
		}
	}
	
	public void crawlingData() throws Exception {
		setDriverProperty();
		FirefoxOptions  options = new FirefoxOptions();
		options.addArguments("headless");
		pageDriver = new FirefoxDriver(options);
		driver = new FirefoxDriver(options);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		driver.get("https://groups.google.com/forum/#!categories/google-admob-ads-sdk");
		Thread.sleep(5000);
		WebElement scrollbarElement = driver.findElement(By.xpath("/html/body/div[4]/div[5]/div[3]/div/div/div/div[2]/div"));
		Long previousScrollHeight = 0L;
		int heightChagneDifference = 0;
		for(int i = 0 ; i <= 15000 ; i ++){
			//int threadNo = identifyTotalVisibleThreads();
			//js.executeAsyncScript(arg0, arg1)
			js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
			//js.executeScript("alert('PROBLEM PROBEM')");
			js.executeScript("arguments[0].scrollBy(0,arguments[0].scrollHeight);", scrollbarElement);			
			Long presentScrollHeight = (Long)js.executeScript("return arguments[0].scrollHeight",scrollbarElement);
			
			if(presentScrollHeight != previousScrollHeight){
				++heightChagneDifference;
			}
			
			System.out.println("Height: " + presentScrollHeight +" Find difference: " + heightChagneDifference + " " + (heightChagneDifference*30));
			
			if(presentScrollHeight == previousScrollHeight){
				System.out.println("FINISH CRAWLING");
				break;
			}
			previousScrollHeight = presentScrollHeight;			
			Thread.sleep(5000);
			//System.out.println("Total Threads " + threadNo);
			
		}
		
	}
	
	public void testPageCrawling() throws Exception{
		setDriverProperty();
		String url = "https://groups.google.com/forum/#!category-topic/google-admob-ads-sdk/0QDKcvK_nEk";
		//String url = "https://groups.google.com/forum/#!category-topic/google-admob-ads-sdk/t2M4n92F0t8";
		FirefoxOptions  options = new FirefoxOptions();
		options.addArguments("--headless");
		pageDriver = new FirefoxDriver(options);
		getPageInformationII(url);
		pageDriver.quit();
	}
	
	public static void main(String[] args) throws Exception{
		GoogleGroupDataCrawler ob = new GoogleGroupDataCrawler();
		ob.crawlingData();
		//ob.testPageCrawling();
		
		System.out.println("Program finishes successfully");
	}
}
