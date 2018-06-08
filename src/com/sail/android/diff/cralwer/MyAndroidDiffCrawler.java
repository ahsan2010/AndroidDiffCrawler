package com.sail.android.diff.cralwer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class MyAndroidDiffCrawler {

	
	List<String> changedTypeList = Arrays.asList("Changed Methods","Added Methods","Removed Methods","Added Classes and Interfaces","Changed Classes and Interfaces","Changed classes","Added Constructors","Removed Constructors");
	
	public void setDriverProperty(){
		System.setProperty("webdriver.gecko.driver", "/home/ahsan/Documents/ResearchBD/PC_Research/RecommendPost/IssueCrawler/resources/geckodriver");
	}
	
	public List<WebElement> getChangeTableInfo(WebDriver driver, String type){
		List<WebElement> elements = null;
		try{
			WebElement element = driver.findElement(By.xpath("//div[@id='mainBodyFluid']//code"));
			elements = driver.findElements(By.xpath("//table[@summary='"+type+"']//tr"));
			return elements;
		}catch(Exception e){
			return null;
		}
		
	}
	
	
	public Map<String,ArrayList<ClassChangeInfo>> extractChangeInfo(WebElement changeTypeWebElement, WebDriver driver){
		Map<String,ArrayList<ClassChangeInfo>> changeInfo = new HashMap<String,ArrayList<ClassChangeInfo>>();
		
		for(String changeType : changedTypeList){
			changeInfo.put(changeType, new ArrayList<ClassChangeInfo>());
		}
			
			for(int i = 0 ; i < changedTypeList.size() ; i ++){
				String changeType = changedTypeList.get(i);
				try{
					List<WebElement> listTableElements = driver
							.findElements(By.xpath("//table[@summary='"+changeType+"']//tr"));
					for(int j = 0 ; j < listTableElements.size() ; j ++){
						WebElement tableElement = listTableElements.get(j);
						try{
							List<WebElement> listTd = tableElement.findElements(By.xpath(".//td[@valign='TOP']"));
							List<WebElement> codes = listTd.get(0).findElements(By.xpath("..//nobr//code"));
							
							
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
			
			
			
			
			
		
		
		
		return changeInfo;
	}
	
	
	public void crawlingData() throws Exception{
		
		WebDriver driver = new FirefoxDriver();
		driver.get("https://developer.android.com/sdk/api_diff/23/changes/classes_index_all");
		
		String title = driver.getTitle();
		System.out.println("Title: " + title);
		
		List<WebElement> list = driver.findElements(By.xpath("//div[@style='line-height:1.5em;color:black']//a[@class='hiddenlink']"));
		
		WebElement element = list.get(1);
		String hrefValue = element.getAttribute("href");
		driver.get(hrefValue);
		WebElement tableElementChangeMethod = null;
		WebElement tableElementAddedMethod = null;
		WebElement tableElementRemoveMethod = null;
		
		try {
			WebElement codeElement = driver.findElement(By.xpath("//*[@id='mainBodyFluid']"));
			List<WebElement> listTableElements = driver
					.findElements(By.xpath("//table[@summary='Changed Fields']//tr"));
			/*for (WebElement element2 : listTableElements) {

				System.out.println("TR: " + element2.getAttribute("innerHTML"));

			}*/
			
			WebElement secondElement = listTableElements.get(1);
			System.out.println(secondElement.getAttribute("innerHTML"));
			List<WebElement> listTd = secondElement.findElements(By.xpath(".//td[@valign='TOP']//a"));
			
			for(WebElement element2 : listTd){
				System.out.println(element2.getAttribute("href"));
				System.out.println("-------------------");
			}
			

		} catch (Exception e) {
			System.err.println("Could not find the changed elements");
		}
		
		System.out.println("Data: " + list.size());
		
	}
	
	public static void main(String[] args) throws Exception{
		MyAndroidDiffCrawler ob = new MyAndroidDiffCrawler();
		ob.setDriverProperty();
		ob.crawlingData();
		System.out.println("Program finishes successfully");
	}
}


