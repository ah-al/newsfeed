package com.newsfeed.thread;

import static com.newsfeed.utility.FileUtility.convertObjectToXML;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;

import com.newsfeed.utility.FileUtility;
import com.rometools.rome.feed.synd.SyndEntry;
/**
 * NO LONGER NEEDED CLASS.
 * @author aali
 *
 */
@PropertySource("classpath:thread.properties")
public class SaveEntryToFileThread implements Runnable {
	
	private String filesPath;
	
    private static final Logger LOGGER = LoggerFactory.getLogger(SaveEntryToFileThread.class);
    
    private List<SyndEntry> entry;
    int minIndex;
    int maxIndex;
    
    public void setEntry(int minIndex, int maxIndex, List<SyndEntry> list, String filesPath) {
    	this.minIndex = minIndex;
    	this.maxIndex = maxIndex;
    	this.entry = list;
    	this.filesPath = filesPath;
    }
    
    @Override
    public void run() {
    	for (int i = minIndex; i < maxIndex; i++) {
	        String directoryPath = FileUtility.getDirectoryPath(filesPath, entry.get(i));
	        Writer writer;
			try {
				writer = new FileWriter(directoryPath +"\\" + entry.get(i).getUri() +".xml");
				
				String xml = convertObjectToXML(entry.get(i));
				PrintWriter printWriter = new PrintWriter(writer);
				printWriter.print(xml);
				printWriter.close();

		        writer.close();
		        LOGGER.info(entry.get(i).getLink());
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}

    }
}