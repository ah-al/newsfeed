package com.newsfeed;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.newsfeed.thread.SaveEntryToFileThread;
import com.rometools.rome.feed.synd.SyndEntry;

@SpringBootApplication
public class NewsFeedApplication implements CommandLineRunner {

	private static Logger logger = LoggerFactory.getLogger(NewsFeedApplication.class);
	
	@Value("${file.path}")
	private String filePath;
	
	@Autowired
	private NewsFeedSourceService newsFeedSource;

	@Autowired
    private TaskExecutor taskExecutor;
	
    public void executeAsynchronously() {
    	List<SyndEntry> feed = newsFeedSource.getFeed();
    	int increment = feed.size() / 5;
    	int min = 0;
    	int max = increment;
    	int unEvenCount = feed.size() - increment * 5;
    	for(int i = 0; i < 5; i++) {
	    	SaveEntryToFileThread myThread = new SaveEntryToFileThread();
	    	myThread.setEntry(min, max, feed, filePath);
	    	
	    	min = max;
	    	if (unEvenCount == 0) {
	    		max += increment;
	    	} else {
	    		max += increment + 1;
	    		unEvenCount--;
	    	}
	        taskExecutor.execute(myThread);
    	}
        ((ThreadPoolTaskExecutor)taskExecutor).shutdown();//In order to stop waiting for more threads when all threads are done.
    }
    
	public static void main(String[] args) {
		SpringApplication.run(NewsFeedApplication.class, args);
		
	}

	@Override
	public void run(String... args) throws Exception {
		File baseDirectory  = new File(filePath);
		if (!baseDirectory.exists()) {
			baseDirectory.mkdirs();
		}
       	executeAsynchronously();
	}
	
}
