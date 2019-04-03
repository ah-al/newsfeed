package com.newsfeed;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.newsfeed.thread.SaveEntryToFileThread;
import com.rometools.rome.feed.synd.SyndEntry;

@SpringBootApplication
public class NewsFeedApplication implements CommandLineRunner {

	private static Logger logger = LoggerFactory.getLogger(NewsFeedApplication.class);
	
	@Autowired
	private NewsFeedSource newsFeedSource;

	@PostConstruct
	private void initialize() {
		System.out.println("@PostConstruct");
	}
	
	@Autowired
    private TaskExecutor taskExecutor;
	
    @Autowired
    private ApplicationContext applicationContext;
	
    public void executeAsynchronously() {
    	int increment = newsFeedSource.getFeed().size() / 5;
    	int min = 0;
    	int max = increment;
    	int unEvenCount = newsFeedSource.getFeed().size() - increment * 5;
    	for(int i = 0; i < 5; i++) {
	    	SaveEntryToFileThread myThread = applicationContext.getBean(SaveEntryToFileThread.class);
	    	myThread.setEntry(min, max, newsFeedSource.getFeed());
	    	
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
		
       	executeAsynchronously();
	}
	
}
