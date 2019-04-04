package com.sail.android.diff.cralwer;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.csvreader.CsvReader;


public class AppUseNoAdInvestigation {
	WebDriver driver;
	public String FILE_APP_NO_ADS = "/home/ahsan/Documents/SAILLabResearch/Ahsan_Research_Project/Ads_Client_Study/result/App_No_Ads/App_No_Ads.csv";	
	Set<String> appUsingNoAds = new HashSet<String>();
	
	public void readAppUseNoAdData() throws Exception{
		CsvReader reader = new CsvReader(FILE_APP_NO_ADS);
		reader.readHeaders();
		while(reader.readRecord()){
			String appName = reader.get("Package_Name");
			appUsingNoAds.add(appName);
		}
	}
	
	public String searchInWebPage(String packageName) throws Exception{
		driver = (WebDriver) new FirefoxDriver();
		driver.get("https://play.google.com/store/apps/details?id="+packageName);
		WebElement offer_in_app = driver.findElement(By.xpath("/html/body/div[1]/div[4]/c-wiz/div/div[2]/div/div[1]/div/c-wiz[1]/c-wiz[1]/div/div[2]/div/div[1]/div[2]"));
		//WebElement offer_in_app = driver.findElement(By.xpath("/html/body/div[1]/div[4]/c-wiz/div/div[2]/div/div[1]/div/c-wiz[3]/div[1]/div[2]/div/div[5]/span/div/span"));
		System.out.println(packageName + " " + offer_in_app.getText());
		System.out.println("------------------------------------");
		return offer_in_app.getText();
	}
	
	public void getAppMonetizationInformation(){
		int appUseInAppPurchase = 0;
		int appContainsAds = 0;
		int appUseOtherMonetization = 0;
		int missingApp = 0;
		int finishedApps = 0;
		driver = (WebDriver) new FirefoxDriver();
		for(String appName : appUsingNoAds){
			try{
				String monetizationStrategy = searchInWebPage(appName);
				if(monetizationStrategy.contains("Offers in-app purchases")){
					appUseInAppPurchase++;
				}
				if(monetizationStrategy.contains("Contains Ads")){
					appContainsAds ++;
				}
				TimeUnit.SECONDS.sleep(1);
			}catch(Exception e){
				missingApp++;
				e.printStackTrace();
			}
			System.out.println("Finished ["+(++finishedApps)+"]");
		}
		System.out.println("Apps not using Ads ["+appUsingNoAds.size()+"]");
		System.out.println("Apps use in-app purchasing ["+appUseInAppPurchase+"]");
		System.out.println("Apps using ads ["+appContainsAds+"]");
		System.out.println("Missing Apps ["+missingApp+"]");
	}
	
	public static void main(String[] args) throws Exception{
		System.setProperty("webdriver.gecko.driver", "/home/ahsan/Downloads/Lib/geckodriver");
		AppUseNoAdInvestigation ob = new AppUseNoAdInvestigation();
		ob.readAppUseNoAdData();
		//ob.getAppMonetizationInformation();
		ob.searchInWebPage("com.facebook.katana");
		System.out.println("Program finishes successfully");
	}
}
