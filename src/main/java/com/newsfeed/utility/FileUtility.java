package com.newsfeed.utility;

import java.io.File;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.newsfeed.entities.Item;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;

public class FileUtility {

	private static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");//EEE, dd MMM yyyy HH:mm:ss z
	public static synchronized String getDirectoryPath(String paseDirectory, SyndEntry entry) {
		File fileDirectory = new File(paseDirectory + dateFormatter.format(entry.getPublishedDate()));
		if (!fileDirectory.exists()) {
			fileDirectory.mkdir();
		}
		fileDirectory = new File(paseDirectory + dateFormatter.format(entry.getPublishedDate()) + "\\" +entry.getCategories().get(0).getName());
		if (!fileDirectory.exists()) {
			fileDirectory.mkdir();
		}
		return fileDirectory.getAbsolutePath();
	}
	
	public static String convertObjectToXML(SyndEntry entry) throws JAXBException {
		Item item = new Item(entry);
		
        JAXBContext jaxbContext = JAXBContext.newInstance(Item.class);
         
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        StringWriter sw = new StringWriter();
         
        jaxbMarshaller.marshal(item, sw);
         
        return sw.toString();
        
		
	}
}
