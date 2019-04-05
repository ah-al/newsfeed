package com.newsfeed.utility;

import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.newsfeed.entities.Item;
import com.rometools.rome.feed.synd.SyndEntry;

public class FileUtility {

	
	public static String convertObjectToXML(SyndEntry entry) {
		Item item = new Item(entry);
		
        JAXBContext jaxbContext;
        StringWriter sw = new StringWriter();

		try {
			jaxbContext = JAXBContext.newInstance(Item.class);
	        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	        jaxbMarshaller.marshal(item, sw);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return sw.toString();
        
		
	}
	
	public static String getDateFolderName(String message) {
    	SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");//yyyy-MM-dd
    	String name = getElementFromMessage(message, "pubDate");
    	try {
			Date date = dateFormatter.parse(name);
			dateFormatter = new SimpleDateFormat("yyyy-MM-dd");//
			name = dateFormatter.format(date);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	
    	return name;
    }
    
	public static String getElementFromMessage(String message, String elementId) {
    	String name = "missing" + elementId;
    	Pattern pattern = Pattern.compile("<" + elementId + ">(.*?)</" + elementId + ">");
    	Matcher matcher = pattern.matcher(message);
    	if (matcher.find())
    	{
    		name = matcher.group(1);
    	}
    	return name;
    }
	
}
