package com.newsfeed.utility;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.newsfeed.entities.Item;
import com.rometools.rome.feed.synd.SyndEntry;

public class FileUtility {

	private static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");//EEE, dd MMM yyyy HH:mm:ss z
	public static synchronized String getDirectoryPath(String paseDirectory, SyndEntry entry) {
		if (entry.getPublishedDate() == null) {
			entry.setPublishedDate(new Date());//When date returned as null from source.
		}
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
	
	public static void saveEntryToFile (SyndEntry entry, String filesPath) {
		
		String directoryPath = FileUtility.getDirectoryPath(filesPath, entry);
        Writer writer;
		try {
			writer = new FileWriter(directoryPath +"\\" + entry.getUri() +".xml");
			
			String xml = convertObjectToXML(entry);
			PrintWriter printWriter = new PrintWriter(writer);
			printWriter.print(xml);
			printWriter.close();

	        writer.close();
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
