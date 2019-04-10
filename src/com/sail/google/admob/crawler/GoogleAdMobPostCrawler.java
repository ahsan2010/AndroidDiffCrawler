package com.sail.google.admob.crawler;



import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.csvreader.CsvReader;

public class GoogleAdMobPostCrawler {
	
	WebDriver driver = null;
	public String ROOT = "/home/ahsan/Documents/SAILLabResearch/Ahsan_Research_Project/GoogleAdMobDiscussion";
	ArrayList<String> postUrlList = new ArrayList<String>();
	List<String> monthNameList = Arrays.asList("January","February","March","April","May","June","July","August","September","October","November","December");
	int PostId = 0;
	
	public void setDriverProperty() {
		System.setProperty("webdriver.gecko.driver",
				ROOT + "/geckodriver");
	}	
	
	public void readPostUrList() throws Exception{
		CsvReader reader = new CsvReader(ROOT + "/Data/postUrl.csv");
		reader.readHeaders();		
		while(reader.readRecord()){
			postUrlList.add(reader.get("Post_Url"));
		}
		
		reader.close();
	}
	public String getMonthNumber(String month){
		String monthNumber = "";
		for(int i = 0; i < monthNameList.size() ; i ++){
			if(monthNameList.get(i).equals(month)){
				if((i + 1) < 10){
					monthNumber += "0"+ (i + 1);
				}else{
					monthNumber = Integer.toString(i + 1);
				}
				return monthNumber;				
			}
		}
		return monthNumber;
	}
	public String getDayNumber(String dayString){
		int day = Integer.parseInt(dayString.trim());
		String dayNumber = "";
		if(day < 10){
			dayNumber += "0"+day;
		}else{
			dayNumber = Integer.toString(day);
		}
		return dayNumber;
	}
	
	public String formatCreationDate(String creationDate){
		String formattedDate = "";
		String words[] = creationDate.split(" ");
		String monthNumber = getMonthNumber(words[2]);
		String dayNumber = getDayNumber(words[1].trim());
		formattedDate += words[3].trim() + "-" + monthNumber + "-" + dayNumber + "T" + words[4].trim();
		return formattedDate;
	}
	
	public ArrayList<GoogleAdMobPostInfo> crawlASinglePostInformation(String url) throws Exception{
		ArrayList<GoogleAdMobPostInfo> postThreadList = new ArrayList<GoogleAdMobPostInfo>();
		String title = "";		
		driver.get(url);
		Thread.sleep(3000);
		String htmlContent = driver.getPageSource();
		Document doc = Jsoup.parse(htmlContent);
		List<Element> docElements = doc.select("div.F0XO1GC-nb-x");
		List<Element> docElementForTitle = doc.select("div.F0XO1GC-mb-z");
		
		title = docElementForTitle.get(0).text();
		
		for(int i = 1 ; i < docElements.size() ; i ++ ){
			Element p = docElements.get(i);
			Elements el = p.select("blockquote");
			if(el != null){
				el.remove();
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
			            String formattedDate = formatCreationDate(timeString);
			            //System.out.println(formattedDate);
			            GoogleAdMobPostInfo threadInfo = new GoogleAdMobPostInfo(title, element.text(), formattedDate, posterName);
			            postThreadList.add(threadInfo);
			            previous = element.text();
			        }
			        
			    }
			}
		}
		
		/*if(postThreadList.size() > 0){
			postThreadList.sort(new Comparator<GoogleAdMobPostInfo>() {

				@Override
				public int compare(GoogleAdMobPostInfo o1, GoogleAdMobPostInfo o2) {
					// TODO Auto-generated method stub
					
					return 0;
				}
				
			});
		}*/
		
		return postThreadList;
	}
	
	public void crawlPostData() throws Exception{
		setDriverProperty();
		FirefoxOptions  options = new FirefoxOptions();
		options.addArguments("--headless");
		driver = new FirefoxDriver(options);
		
		for(int i = 0 ; i < 100 ; i ++ ){
			String postUrl = postUrlList.get(i);
			ArrayList<GoogleAdMobPostInfo> postThreadList = crawlASinglePostInformation(postUrl);
			writeDataInFile(postThreadList,i);
			System.out.println("Finish Post: ["+(i+1)+"]");
		}
	}
	public void writeDataInFile(ArrayList<GoogleAdMobPostInfo> postThreadList,int number) throws Exception{
		if(postThreadList.size() == 0){
			return;
		}
		
		String path = ROOT +"/Data/CrawlData/Thread_"+ postThreadList.get(0).getPostCreationDate() + "_" + (number) + ".xml";
		BufferedWriter bw = new BufferedWriter(new FileWriter(path));
		StringWriter stringWriter = new StringWriter();
		XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
		XMLStreamWriter xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(stringWriter);
		
		GoogleAdMobPostInfo questionThread = postThreadList.get(0);
		
		xMLStreamWriter.writeStartDocument();
		xMLStreamWriter.writeStartElement("GoogleAdMob");
		
		//xMLStreamWriter.writeStartElement("postThread");
		
		xMLStreamWriter.writeStartElement("Question");
		
		xMLStreamWriter.writeStartElement("Title");
		xMLStreamWriter.writeCharacters(questionThread.getPostTitle());
		xMLStreamWriter.writeEndElement();
		
		xMLStreamWriter.writeStartElement("Body");
		xMLStreamWriter.writeCharacters(questionThread.getPostBody());
		xMLStreamWriter.writeEndElement();
		
		xMLStreamWriter.writeStartElement("CreationDate");
		xMLStreamWriter.writeCharacters(questionThread.getPostCreationDate());
		xMLStreamWriter.writeEndElement();
		
		xMLStreamWriter.writeStartElement("Creator");
		xMLStreamWriter.writeCharacters(questionThread.getPostCreatorName());
		xMLStreamWriter.writeEndElement();
		
		xMLStreamWriter.writeEndElement();
		
		if(postThreadList.size() > 1){
			for(int i = 1; i < postThreadList.size() ; i ++ ){
				xMLStreamWriter.writeStartElement("Answer");
				GoogleAdMobPostInfo answerThread = postThreadList.get(i);
				xMLStreamWriter.writeStartElement("Body");
				xMLStreamWriter.writeCharacters(answerThread.getPostBody());
				xMLStreamWriter.writeEndElement();
				
				xMLStreamWriter.writeStartElement("CreationDate");
				xMLStreamWriter.writeCharacters(answerThread.getPostCreationDate());
				xMLStreamWriter.writeEndElement();
				
				xMLStreamWriter.writeStartElement("Creator");
				xMLStreamWriter.writeCharacters(answerThread.getPostCreatorName());
				xMLStreamWriter.writeEndElement();
				xMLStreamWriter.writeEndElement();
			}
			
		}
		
		xMLStreamWriter.writeEndElement();

		xMLStreamWriter.writeEndDocument();
		xMLStreamWriter.flush();
		xMLStreamWriter.close();
		
		String xmlString = stringWriter.getBuffer().toString();
		stringWriter.close();

		bw.write(xmlString);
		bw.newLine();
		bw.close();
	}
	
	public static void main(String[] args) throws Exception{
		GoogleAdMobPostCrawler ob = new GoogleAdMobPostCrawler();
		ob.readPostUrList();
		ob.crawlPostData();
		System.out.println("Program finishes successfully");
	}
}
