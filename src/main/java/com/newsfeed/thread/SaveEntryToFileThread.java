package com.newsfeed.thread;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.newsfeed.utility.FileUtility;
import com.rometools.rome.feed.synd.SyndEntry;

@Component
@Scope("prototype")
@PropertySource("classpath:thread.properties")
public class SaveEntryToFileThread implements Runnable {
	
	@Autowired
    Environment env;
	
    private static final Logger LOGGER = LoggerFactory.getLogger(SaveEntryToFileThread.class);
    
    private SyndEntry entry;
    
    public void setEntry(SyndEntry entry) {
    	this.entry = entry;
    }
    
    @Override
    public void run() {
        LOGGER.info("Called from thread:" + entry.getLink());
        String directoryPath = FileUtility.getDirectoryPath(env.getProperty("file.path"), entry);
        Writer writer;
		try {
			writer = new FileWriter(directoryPath +"\\" + entry.getUri() +".xml");

//	        SyndFeedOutput output = new SyndFeedOutput();
//	        output.output(entry,writer);
	        writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

    }
}